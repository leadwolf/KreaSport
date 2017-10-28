package com.ccaroni.kreasport.race;

import android.content.Context;
import android.location.Location;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.race.interfaces.IRaceVM;
import com.ccaroni.kreasport.race.interfaces.IRaceView;
import com.ccaroni.kreasport.utils.Constants;

import java.util.List;

/**
 * Created by Master on 20/08/2017.
 */

public class RaceVM extends IRaceVM {

    private static final String TAG = RaceVM.class.getSimpleName();


    private List<CustomOverlayItem> overlayItems;


    public RaceVM(Context context) {
        if (context instanceof IRaceView) {
            this.raceView = (IRaceView) context;
        } else {
            throw new RuntimeException(context + " must implement " + IRaceView.class.getSimpleName());
        }

        changeVisibilitiesOnRaceState();
    }

    protected void changeVisibilitiesOnRaceState() {
        passiveInfoVisibility = isRaceActive() ? View.GONE : View.VISIBLE;
        activeInfoVisibility = isRaceActive() ? View.VISIBLE : View.GONE;

        fabStartVisibility = !isRaceActive() && RaceHolder.getInstance().isRaceSelected() ? View.VISIBLE : View.GONE;
        bottomSheetVisibility = RaceHolder.getInstance().isRaceSelected() ? View.VISIBLE : View.GONE;

        fabMyLocationAnchoredStartVisibility = fabStartVisibility == View.VISIBLE ? View.VISIBLE : View.GONE;
        fabMyLocationAnchoredBottomSheetVisibility = bottomSheetVisibility == View.VISIBLE && fabStartVisibility == View.GONE ? View.VISIBLE : View.GONE;
        fabMyLocationCornerVisibility = bottomSheetVisibility == View.GONE ? View.VISIBLE : View.GONE;


        notifyChange();
    }


    @Override
    protected void updateBottomSheetData(CustomOverlayItem selectedItem) {
        if (isRaceActive()) {
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
        if (!isRaceActive()) {
            throw new IllegalStateException("This method is only supposed to be called from a raceActive state");
        }

        if (selectedItem.isPrimary()) {
            Log.d(TAG, "selected the race marker of the ongoing race");
            setTitle(RaceHolder.getInstance().getCurrentRaceTitle());
            setDescription(RaceHolder.getInstance().getCurrentRaceDescription());
        } else {
            Log.d(TAG, "selected checkpoint of same race");
            RaceHolder.getInstance().updateCurrentCheckpoint(selectedItem.getId());
            setTitle(RaceHolder.getInstance().getCurrentCheckpointTitle());
            setDescription(RaceHolder.getInstance().getCurrentCheckpointDescription());
        }
    }

    /**
     * Called only when !raceActive
     *
     * @param selectedItem
     */
    private void updateFromInactiveState(CustomOverlayItem selectedItem) {
        if (isRaceActive()) {
            throw new IllegalStateException("This method is only supposed to be called from a !raceActive state");
        }

        if (RaceHolder.getInstance().getCurrentRaceId().equals(selectedItem.getRaceId())) {
            Log.d(TAG, "selected same race");
        } else {
            Log.d(TAG, "selected different race: " + selectedItem.getRaceId());

            RaceHolder.getInstance().updateCurrentRace(selectedItem.getRaceId());
            setTitle(RaceHolder.getInstance().getCurrentRaceTitle());
            setDescription(RaceHolder.getInstance().getCurrentRaceDescription());

            changeVisibilitiesOnRaceState(); // call this to restore the fab and bottom sheet if no item was previously selected
        }
    }

    protected boolean isUserLocationAtRaceStart() {
        boolean validStart = false;

        Location lastLocation = raceView.getLastKnownLocation();
        Location raceLocation = RaceHolder.getInstance().getCurrentRaceLocation();

        float distance = lastLocation.distanceTo(raceLocation);

        if (distance > Constants.MINIMUM_DISTANCE_TO_START_RACE) {
            Log.d(TAG, "User was " + distance + "m away from start. Too far by " + (distance - Constants.MINIMUM_DISTANCE_TO_START_RACE) + "m");
            raceView.toast("You are too far to start this race");
        } else {
            Log.d(TAG, "User was " + distance + "m away from start. Inside by " + (Constants.MINIMUM_DISTANCE_TO_START_RACE - distance) + "m");
            validStart = true;
        }

        return validStart;
    }

    protected void startRace() {
        final long timeStart = SystemClock.elapsedRealtime();
        RaceHolder.getInstance().startRace(timeStart);

        changeVisibilitiesOnRaceState();

        raceView.focusOnRace(getOverlayItems());

        RealmCheckpoint targetingCheckpoint = RaceHolder.getInstance().getTargetingCheckpoint();
        triggerNextGeofence(targetingCheckpoint);

        raceView.startChronometer(timeStart);

        raceView.toast("Race started");
    }

    protected void triggerNextGeofence(RealmCheckpoint targetingCheckpoint) {
        raceView.addGeoFence(targetingCheckpoint);
    }

    protected void revealNextCheckpoint(RealmCheckpoint targetingCheckpoint) {
        raceView.revealNextCheckpoint(targetingCheckpoint.toCustomOverlayItem());
    }

}
