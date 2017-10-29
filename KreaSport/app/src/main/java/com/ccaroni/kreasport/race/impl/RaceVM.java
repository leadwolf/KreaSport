package com.ccaroni.kreasport.race.impl;

import android.location.Location;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.dto.BaseItem;
import com.ccaroni.kreasport.data.dto.Checkpoint;
import com.ccaroni.kreasport.data.dto.Race;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.race.IRaceVM;
import com.ccaroni.kreasport.race.IRaceView;
import com.ccaroni.kreasport.utils.Constants;

/**
 * Created by Master on 20/08/2017.
 */

public class RaceVM extends IRaceVM {

    private static final String TAG = RaceVM.class.getSimpleName();


    public RaceVM(IRaceView raceView, String userId) {
        super(raceView, userId);
    }

    protected void changeVisibilitiesOnRaceState(boolean shouldNotifyBindings) {
        passiveInfoVisibility = isRaceActive() ? View.GONE : View.VISIBLE;
        activeInfoVisibility = isRaceActive() ? View.VISIBLE : View.GONE;

        fabStartVisibility = !isRaceActive() && RaceHolder.getInstance().isRaceSelected() ? View.VISIBLE : View.GONE;
        bottomSheetVisibility = RaceHolder.getInstance().isRaceSelected() ? View.VISIBLE : View.GONE;

        fabMyLocationAnchoredStartVisibility = fabStartVisibility == View.VISIBLE ? View.VISIBLE : View.GONE;
        fabMyLocationAnchoredBottomSheetVisibility = bottomSheetVisibility == View.VISIBLE && fabStartVisibility == View.GONE ? View.VISIBLE : View.GONE;
        fabMyLocationCornerVisibility = bottomSheetVisibility == View.GONE ? View.VISIBLE : View.GONE;


        if (shouldNotifyBindings) {
            notifyChange();
        }
    }

    @Override
    protected void updateBottomSheetData(CustomOverlayItem selectedItem) {
        Log.d(TAG, "selected: " + selectedItem.toString());

        boolean bottomSheetWasVisible = bottomSheetVisibility == View.VISIBLE;

        BaseItem baseItem = determineSpecificBaseItem(selectedItem);
        RaceHolder.getInstance().setSelectedItem(baseItem);

        if (!bottomSheetWasVisible) {
            Log.d(TAG, "will reveal bottom sheet");
            changeVisibilitiesOnRaceState(false);
        }
        notifyChange();
    }

    /**
     * The specific {@link BaseItem} associated to the selected item.
     *
     * @param selectedItem the selected item
     * @return the specific implementation that corresponds to the selected item. ({@link Race}/{@link Checkpoint})
     */
    private BaseItem determineSpecificBaseItem(CustomOverlayItem selectedItem) {
        BaseItem baseItem;
        RealmRace realmRace = getRealmRaceFromSelectedItem(selectedItem);

        if (selectedItem.isPrimary()) {
            baseItem = realmRace.toDTO();
        } else {
            baseItem = realmRace.getCheckpointById(selectedItem.getId()).toDTO();
        }
        return baseItem;
    }

    /**
     * Gets the {@link RealmRace} associated to the selected item.
     *
     * @param selectedItem the item selected
     * @return the {@link RealmRace} where {@link RealmRace#id} = {@link CustomOverlayItem#id}
     */
    private RealmRace getRealmRaceFromSelectedItem(CustomOverlayItem selectedItem) {
        RealmRace realmRace;
        if (selectedItem.isPrimary()) {
            realmRace = RealmHelper.getInstance(null).findRaceById(selectedItem.getId());
        } else {
            realmRace = RealmHelper.getInstance(null).findRaceById(selectedItem.getRaceId());
        }
        return realmRace;
    }

    protected boolean isUserLocationAtRaceStart() {
        boolean validStart = false;

        Location lastLocation = raceView.getLastKnownLocation();
        if (lastLocation == null) {
            Log.d(TAG, "cannot calculate dist to user location: no location available");
            return false;
        }

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
        RaceHolder.getInstance().setCurrentRaceToSelected();
        RaceHolder.getInstance().startRecording(timeStart);

        changeVisibilitiesOnRaceState(true);

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
