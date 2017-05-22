package com.ccaroni.kreasport.map.viewmodels.impl;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ccaroni.kreasport.BR;
import com.ccaroni.kreasport.data.RaceHelper;
import com.ccaroni.kreasport.data.dto.Race;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.utils.LocationUtils;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

import io.realm.RealmResults;

/**
 * Created by Master on 19/05/2017.
 */

public class RaceVMImpl extends BaseObservable {

    private static final String LOG = RaceVMImpl.class.getSimpleName();

    private RealmRace currentRace;
    private RealmCheckpoint currentCheckpoint;

    /*
     * Separate attrs because we can't just grab from currentRace or currentCheckpoint depending on raceActive?, progression and even if the user deliberately selects another
     * marker that is not related to his progression.
     */
    private String title;
    private String description;

    /**
     * The system time at {@link #onStartClicked()}
     */
    private long baseAtStart;

    /**
     * Whether this VM (not necessarily the race) is currently in an active state
     */
    private boolean raceActive;
    private int passiveInfoVisibility;
    private int activeInfoVisibility;
    private int fabVisibility;


    private RaceCommunication raceCommunication;
    private LocationUtils mLocationUtils;

    public RaceVMImpl(Activity activity, LocationUtils mLocationUtils) {
        RaceHelper.getInstance(activity).init(activity);
        if (activity instanceof RaceCommunication) {
            this.raceCommunication = (RaceCommunication) activity;
        } else {
            throw new RuntimeException(activity + " must implment " + RaceCommunication.class.getSimpleName());
        }
        this.mLocationUtils = mLocationUtils;
    }

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

    public boolean isRaceActive() {
        return raceActive;
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

    @Bindable
    public int getPassiveInfoVisibility() {
        return passiveInfoVisibility;
    }

    @Bindable
    public int getActiveInfoVisibility() {
        return activeInfoVisibility;
    }

    @Bindable
    public int getFabVisibility() {
        return fabVisibility;
    }

    public ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem> getIconGestureListener() {
        return new ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final CustomOverlayItem item) {
                Log.d(LOG, "on tap, is primary: " + item.isPrimary());

                updateCurrent(item);

                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final CustomOverlayItem item) {
                return false;
            }
        };
    }

    /**
     * Switch to update on a {@link CustomOverlayItem} selection. Calls the appropriate method for updating title & description according to the selectedItem.
     *
     * @param selectedItem
     */
    private void updateCurrent(CustomOverlayItem selectedItem) {
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

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getProgression() {
        if (currentRace == null) {
            Log.d(LOG, "No race active to get progression from");
            return null;
        }
        return currentRace.getProgressionAsString();
    }

    /**
     * Call to stop the current race. Used by passiveBottomSheet.
     */
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
    public void onStopClicked() {
        Log.d(LOG, "stop clicked");
        if (!raceActive) {
            throw new IllegalStateException("A race is already active");
        }

        stopRace();
    }

    public RealmCheckpoint getActiveCheckpoint() {
        return currentCheckpoint;
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
    public void saveOngoingBaseTime() {
        // TODO, save the base time in RealmRace to be persisted
    }

    /**
     * This interface permits communication to the Model in the MVVM.
     */
    public interface RaceCommunication {

        /**
         * Notification to start counting the time from newBase.
         *
         * @param newBase
         */
        void startChronometer(final long newBase);

        /**
         * Notificaiton to stop whatever is tracking the time.
         */
        void stopChronometer();

        /**
         * Display a message to the user, preferably through a {@link Toast}.
         *
         * @param message
         */
        void toast(String message);
    }
}
