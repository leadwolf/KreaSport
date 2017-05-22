package com.ccaroni.kreasport.map.viewmodels.impl;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.ccaroni.kreasport.BR;
import com.ccaroni.kreasport.data.RaceHelper;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.map.viewmodels.RaceCommunication;
import com.ccaroni.kreasport.map.viewmodels.RaceVM;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.utils.LocationUtils;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

import io.realm.RealmResults;

/**
 * Created by Master on 19/05/2017.
 */

public class RaceVMImpl extends RaceVM {

    private static final String LOG = RaceVMImpl.class.getSimpleName();

    /**
     * The system time at {@link #onStartClicked()}
     */
    private long baseAtStart;

    public RaceVMImpl(Activity activity, LocationUtils mLocationUtils) {
        super(activity, mLocationUtils);
    }

    @Override
    public void onStart() {
        Log.d(LOG, "onStart, UI is ready to be manipulated");

        // set this at the start because normally it has to be triggered by a change
        RealmResults<RealmRace> currentRaceResults = RaceHelper.getInstance(null).findCurrentRace();
        if (currentRaceResults.size() != 0) {
            Log.d(LOG, "found an ongoing race in db");

            currentRace = currentRaceResults.get(0);
            currentCheckpoint = currentRace.getCurrentCheckpoint();
            startRace();
        } else {
            Log.d(LOG, "no previous ongoing race, hiding bottom sheet");
            changeVisibilitiesOnRaceState(false);
        }
    }

    private void startRace() {
        if (currentRace == null) {
            throw new IllegalStateException("Cannot start race when no race is in use");
        }
        Log.d(LOG, "set race active");

        this.raceActive = true;

        changeVisibilitiesOnRaceState(true);

        currentRace.incrementAttempts();
        currentRace.setInProgress(true);

        // if the user quit the app without stopping the race, we need to keep the old time elapsed.
        this.baseAtStart = SystemClock.elapsedRealtime() - currentRace.getTimeExpired();
        raceCommunication.startChronometer(baseAtStart);
    }

    private void stopRace() {
        if (currentRace == null) {
            throw new IllegalStateException("Cannot stop race when no race is in use");
        }
        Log.d(LOG, "set race IN active");

        this.raceActive = false;

        changeVisibilitiesOnRaceState(false);

        // TODO archive time if user wants to save

        currentRace.setInProgress(false);
        currentRace.resetTimeExpired();

        raceCommunication.stopChronometer();
    }

    private void changeVisibilitiesOnRaceState(boolean raceActive) {
        passiveInfoVisibility = raceActive ? View.GONE : View.VISIBLE;
        fabVisibility = raceActive ? View.GONE : View.VISIBLE;
        activeInfoVisibility = raceActive ? View.VISIBLE : View.GONE;
        notifyChange();
    }

    /**
     * Switch to update on a {@link CustomOverlayItem} selection. Calls the appropriate method for updating title & description according to the selectedItem.
     *
     * @param selectedItem
     */
    @Override
    protected void updateCurrent(CustomOverlayItem selectedItem) {
        if (raceActive) {
            updateFromActiveState(selectedItem);
        } else {
            updateFromInactiveState(selectedItem);
        }
    }

    /**
     * Called only when raceActive
     *
     * @param selectedItem
     */
    private void updateFromActiveState(CustomOverlayItem selectedItem) {
        if (!raceActive) {
            throw new IllegalStateException("This method is only supposed to be called from a raceActive state");
        }
        if (selectedItem.isPrimary()) {
            Log.d(LOG, "selected the race marker of the ongoing race");
            setTitle(currentRace.getTitle());
            setDescription(currentRace.getDescription());
        } else {
            if (currentCheckpoint.getId().equals(selectedItem.getId())) {
                Log.d(LOG, "selected same race, same checkpoint");
            } else {
                Log.d(LOG, "selected same race, difference checkpoint");
                currentCheckpoint = currentRace.getCheckpointById(selectedItem.getId());
                setTitle(currentCheckpoint.getTitle());
                setDescription(currentCheckpoint.getDescription());
            }
        }
    }

    /**
     * Called only when !raceActive
     *
     * @param selectedItem
     */
    private void updateFromInactiveState(CustomOverlayItem selectedItem) {
        if (raceActive) {
            throw new IllegalStateException("This method is only supposed to be called from a !raceActive state");
        }
        if (currentRace != null && currentRace.getId().equals(selectedItem.getRaceId())) {
            Log.d(LOG, "selected same race");
        } else {
            Log.d(LOG, "selected different race");

            RaceHelper.getInstance(null).getAllRaces(false);

            currentRace = RaceHelper.getInstance(null).findRaceById(selectedItem.getRaceId());
            setTitle(currentRace.getTitle());
            setDescription(currentRace.getDescription());
        }
    }

    /**
     * Call to stop the current race. Used by passiveBottomSheet.
     */
    @Override
    public void onStartClicked() {
        Log.d(LOG, "start clicked");
        if (raceActive) {
            throw new IllegalStateException("A race is already active");
        } else if (currentRace == null) {
            throw new IllegalStateException("No race is currently selected");
        }

        if (validateProximityToStart()) {
            startRace();
        }

    }

    private boolean validateProximityToStart() {
        boolean validStart = false;

        Location lastLocation = mLocationUtils.getLastKnownLocation();
        Location raceLocation = currentRace.getLocation();

        float distance = lastLocation.distanceTo(raceLocation);

        if (distance > Constants.MINIMUM_DISTANCE_TO_START_RACE) {
            Log.d(LOG, "User was " + distance + "m away from start. Too far by " + (distance - Constants.MINIMUM_DISTANCE_TO_START_RACE) + "m");
            raceCommunication.toast("You are too far to start this race");
        } else {
            Log.d(LOG, "User was " + distance + "m away from start. Inside by " + (Constants.MINIMUM_DISTANCE_TO_START_RACE - distance) + "m");
            raceCommunication.toast("Started!");
            validStart = true;
        }

        return validStart;
    }

    /**
     * Call to stop the current race. Used by activeBottomSheet.
     */
    @Override
    public void onStopClicked() {
        Log.d(LOG, "stop clicked");
        if (!raceActive) {
            throw new IllegalStateException("A race is already active");
        }

        stopRace();
    }

    /**
     * Saves the time expired from {@link #baseAtStart} to the {@link RealmRace} (persisted with Realm).
     * // TODO archive istead of modifying one field
     */
    private void saveTimeToRace() {
        final long timeExpired = SystemClock.elapsedRealtime() - this.baseAtStart;
        Log.d(LOG, "saving time " + timeExpired);
        currentRace.setTimeExpired(timeExpired);
    }

    /**
     * Call when the user quits the activity but doesnt stop the activity
     */
    @Override
    public void saveOngoingBaseTime() {
        // TODO, save the base time in RealmRace to be persisted
    }
}
