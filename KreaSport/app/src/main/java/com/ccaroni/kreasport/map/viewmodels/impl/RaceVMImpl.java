package com.ccaroni.kreasport.map.viewmodels.impl;

import android.app.Activity;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.map.viewmodels.RaceVM;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.utils.LocationUtils;

import org.osmdroid.util.GeoPoint;
import org.threeten.bp.OffsetDateTime;

/**
 * Created by Master on 19/05/2017.
 */

public class RaceVMImpl extends RaceVM {

    private static final String LOG = RaceVMImpl.class.getSimpleName();

    /**
     * The system time as of {@link #onStartClicked()} minus the offset for any restored time
     */
    private long baseAtStart;

    public RaceVMImpl(Activity activity, LocationUtils mLocationUtils) {
        super(activity, mLocationUtils);
    }

    @Override
    protected void startRace() {
        if (currentRace == null) {
            throw new IllegalStateException("Cannot start race when no race is in use");
        }
        Log.d(LOG, "set race active");

        this.raceActive = true;

        this.currentCheckpoint = currentRace.getCheckpointByProgression(raceRecord.getGeofenceProgression());

        changeVisibilitiesOnRaceState(true);

        beginRecording();

        raceCommunication.toast("Started!");

        raceCommunication.revealNextCheckpoint(currentCheckpoint.toCustomOverlayItem());
        Log.d(LOG, "asking for geofence for checkpoint: " + currentCheckpoint.getId() + " " + currentCheckpoint.getTitle());
        raceCommunication.addGeoFence(currentCheckpoint);

        // if the user quit the app without stopping the race, we need to keep the old time elapsed.
        this.baseAtStart = SystemClock.elapsedRealtime() - raceRecord.getTimeExpired();
        raceCommunication.startChronometer(baseAtStart);
    }

    private void beginRecording() {
        RealmHelper.getInstance(null).beginTransaction();

        Log.d(LOG, "started recording for " + currentRace.getId() + " and record id: " + raceRecord.getId());
        raceRecord.setInProgress(true);
        raceRecord.setRaceId(currentRace.getId());

        RealmHelper.getInstance(null).commitTransaction();
    }

    /**
     * Stops the race recording, archives it and notifies the {@link android.content.Context} that whatever implementation of a chronometer should be stopped.
     */
    private void stopRace() {
        if (currentRace == null) {
            throw new IllegalStateException("Cannot stop race when no race is in use");
        }
        Log.d(LOG, "set race IN active");

        this.raceActive = false;

        changeVisibilitiesOnRaceState(false);

        archiveRaceRecord();

        raceCommunication.stopChronometer();
    }

    /**
     * Stops the recording in raceRecord (saved with Realm), creates a new one for next time the user starts.
     */
    private void archiveRaceRecord() {
        final long timeDifference = SystemClock.currentThreadTimeMillis() - baseAtStart;

        RealmHelper.getInstance(null).beginTransaction();

        raceRecord.setInProgress(false, false);
        raceRecord.setTimeExpired(timeDifference, false);
        raceRecord.setDateTime(OffsetDateTime.now().toString());

        RealmHelper.getInstance(null).commitTransaction();

        Log.d(LOG, "old raceRecord: " + raceRecord);
        initRaceRecord();
        Log.d(LOG, "new raceRecord: " + raceRecord);
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
            Log.d(LOG, "selected checkpoint of same race");
            currentCheckpoint = currentRace.getCheckpointById(selectedItem.getId());
            setTitle(currentCheckpoint.getTitle());
            setDescription(currentCheckpoint.getDescription());
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
            Log.d(LOG, "selected different race: " + selectedItem.getRaceId());

            currentRace = RealmHelper.getInstance(null).findRaceById(selectedItem.getRaceId());
            setTitle(currentRace.getTitle());
            setDescription(currentRace.getDescription());
            changeVisibilitiesOnRaceState(false); // call this to restore the fab and bottom sheet if no item was previously selected
        }
    }

    /**
     * Call to stop the current race. Used by passiveBottomSheet.<br>
     * Also calls {@link #onMyLocationClicked()}.
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

            GeoPoint startPoint = new GeoPoint(currentRace.getLatitude(), currentRace.getLongitude());
            if (raceCommunication.needToAnimateToStart(startPoint)) {
                Log.d(LOG, "waiting for animation to end to start race");
                onMyLocationClicked();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRace();
                    }
                }, 1500);
            } else {
                Log.d(LOG, "no animation, starting race");
                startRace();
            }
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
            throw new IllegalStateException("No race is active");
        }

        stopRace();
    }


    /**
     * Call when the user quits the activity but doesnt stop the activity
     */
    @Override
    public void saveOngoingBaseTime() {
        // TODO, save the base time in RealmRaceRecord to be persisted
    }
}
