package com.ccaroni.kreasport.race;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.ccaroni.kreasport.BR;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRiddle;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.utils.Constants;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Master on 20/08/2017.
 */

public class RaceVM extends BaseObservable {

    private static final String TAG = RaceVM.class.getSimpleName();


    /**
     * Whether a timer is running
     */
    protected boolean raceActive;
    private int bottomSheetVisibility;
    private int passiveInfoVisibility;
    private int activeInfoVisibility;
    private int fabStartVisibility;


    private int fabMyLocationCornerVisibility;
    private int fabMyLocationAnchoredStartVisibility;
    private int fabMyLocationAnchoredBottomSheetVisibility;
    private List<CustomOverlayItem> overlayItems;


    private RaceViewComms raceViewComms;

    public RaceVM(Context context) {
        if (context instanceof RaceViewComms) {
            this.raceViewComms = (RaceViewComms) context;
        } else {
            throw new RuntimeException(context + " must implement " + RaceViewComms.class.getSimpleName());
        }

        raceActive = false;

        changeVisibilitiesOnRaceState();
    }

    /**
     * Updates the bindings for the whole bottom sheet visibilities and fab visibilities. NOT the data in the bottom sheet
     */
    protected void changeVisibilitiesOnRaceState() {
        passiveInfoVisibility = raceActive ? View.GONE : View.VISIBLE;
        activeInfoVisibility = raceActive ? View.VISIBLE : View.GONE;

        fabStartVisibility = !raceActive && RaceHolder.getInstance().isRaceSelected() ? View.VISIBLE : View.GONE;
        bottomSheetVisibility = RaceHolder.getInstance().isRaceSelected() ? View.VISIBLE : View.GONE;

        fabMyLocationAnchoredStartVisibility = fabStartVisibility == View.VISIBLE ? View.VISIBLE : View.GONE;
        fabMyLocationAnchoredBottomSheetVisibility = bottomSheetVisibility == View.VISIBLE && fabStartVisibility == View.GONE ? View.VISIBLE : View.GONE;
        fabMyLocationCornerVisibility = bottomSheetVisibility == View.GONE ? View.VISIBLE : View.GONE;


        notifyChange();
    }

    private void resumeRace() {
        raceViewComms.startChronometer(RaceHolder.getInstance().getTimeStart());
    }

    public ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem> getIconGestureListener() {
        return new ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final CustomOverlayItem item) {
                Log.d(TAG, "on tap, is primary: " + item.isPrimary());

                updateBottomSheetData(item);

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
    private void updateBottomSheetData(CustomOverlayItem selectedItem) {
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
        if (raceActive) {
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

    @Bindable
    public int getPassiveInfoVisibility() {
        return passiveInfoVisibility;
    }

    @Bindable
    public int getActiveInfoVisibility() {
        return activeInfoVisibility;
    }

    @Bindable
    public int getFabStartVisibility() {
        return fabStartVisibility;
    }

    @Bindable
    public int getFabMyLocationCornerVisibility() {
        return fabMyLocationCornerVisibility;
    }

    @Bindable
    public int getFabMyLocationAnchoredStartVisibility() {
        return fabMyLocationAnchoredStartVisibility;
    }

    @Bindable
    public int getFabMyLocationAnchoredBottomSheetVisibility() {
        return fabMyLocationAnchoredBottomSheetVisibility;
    }

    @Bindable
    public int getBottomSheetVisibility() {
        return bottomSheetVisibility;
    }

    @Bindable
    public String getTitle() {
        return RaceHolder.getInstance().getCurrentTitle();
    }

    public void setTitle(String title) {
        RaceHolder.getInstance().setTitle(title);
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDescription() {
        return RaceHolder.getInstance().getCurrentDescription();
    }

    public void setDescription(String description) {
        RaceHolder.getInstance().setDescription(description);
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getProgression() {
        int progression = RaceHolder.getInstance().getCheckpointProgression();
        int total = RaceHolder.getInstance().getNumberCheckpoints();

        return "" + progression + "/" + total;
    }

    public void onMapBackgroundTouch() {
        if (!raceActive) {
            RaceHolder.getInstance().removeWholeSelection();
            changeVisibilitiesOnRaceState();
        }
    }

    public void onMyLocationClicked() {
        raceViewComms.onMyLocationClicked();
    }

    public void onStartClicked() {
        if (raceActive) {
            throw new IllegalStateException("A race is already active");
        } else if (!RaceHolder.getInstance().isRaceSelected()) {
            throw new IllegalStateException("No race is currently selected");
        }

        if (raceViewComms.verifyLocationSettings()) {
            if (validateProximityToStart()) {

                GeoPoint startPoint = RaceHolder.getInstance().getCurrentRaceAsGeopoint();
                if (raceViewComms.needToAnimateToStart(startPoint)) {
                    Log.d(TAG, "waiting for animation to end to start race");
                    onMyLocationClicked(); // manually trigger animation to user's location

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startRace();
                        }
                    }, 1500);
                } else {
                    Log.d(TAG, "no animation, starting race");
                    startRace();
                }
            }
        }
    }

    private boolean validateProximityToStart() {
        boolean validStart = false;

        Location lastLocation = raceViewComms.getLastKnownLocation();
        Location raceLocation = RaceHolder.getInstance().getCurrentRaceLocation();

        float distance = lastLocation.distanceTo(raceLocation);

        if (distance > Constants.MINIMUM_DISTANCE_TO_START_RACE) {
            Log.d(TAG, "User was " + distance + "m away from start. Too far by " + (distance - Constants.MINIMUM_DISTANCE_TO_START_RACE) + "m");
            raceViewComms.toast("You are too far to start this race");
        } else {
            Log.d(TAG, "User was " + distance + "m away from start. Inside by " + (Constants.MINIMUM_DISTANCE_TO_START_RACE - distance) + "m");
            validStart = true;
        }

        return validStart;
    }

    private void startRace() {

        final long timeStart = SystemClock.elapsedRealtime();

        RaceHolder.getInstance().startRace(timeStart);

        raceActive = true;
        changeVisibilitiesOnRaceState();

        raceViewComms.focusOnRace(getOverlayItems());

        RealmCheckpoint targetingCheckpoint = RaceHolder.getInstance().getTargetingCheckpoint();
        triggerNextGeofence(targetingCheckpoint);

        raceViewComms.startChronometer(timeStart);

        raceViewComms.toast("Race started");
    }

    public void onStopClicked() {
        raceViewComms.askStopConfirmation();
    }

    public void onStopConfirmation() {
        this.raceActive = false;

        RaceHolder.getInstance().stopRecording();

        changeVisibilitiesOnRaceState(); // TODO lead to a new screen if finished race
    }

    /**
     * Interfaces with {@link RaceHolder} to verify progression, removes the geofence since we will now trigger the riddle for the checkpoint
     *
     * @param checkpointId
     */
    public void onGeofenceTriggered(String checkpointId) {

        if (!RaceHolder.getInstance().verifyGeofenceProgression()) {
            throw new IllegalStateException("Geofence was triggered but progressions are not synced");
        } // else continue

        raceViewComms.removeLastGeofence();

        RaceHolder.getInstance().onGeofenceTriggered();

        RealmRiddle targetingCheckpoint = RaceHolder.getInstance().getTargetingCheckpointRiddle();
        raceViewComms.askRiddle(targetingCheckpoint.toDTO());
    }

    /**
     * Interfaces with {@link RaceHolder} to increment targeting progression, triggeres the next checkpoint reveal and geofence
     *
     * @param answerIndex
     */
    public void onQuestionCorrectlyAnswered(int answerIndex) {
        RaceHolder.getInstance().onQuestionAnswered(answerIndex);

        if (RaceHolder.getInstance().isCurrentRaceFinished()) {
            // trigger end
            RaceHolder.getInstance().stopRecording();
            raceViewComms.stopChronometer();
            raceViewComms.toast("Finished!");
        } else {
            // trigger next
            RealmCheckpoint targetingCheckpoint = RaceHolder.getInstance().getTargetingCheckpoint();
            triggerNextGeofence(targetingCheckpoint);
            revealNextCheckpoint(targetingCheckpoint);
        }
    }

    /**
     * Calls the {@link #raceViewComms} to add a geofence for the targeting checkpoint
     */
    private void triggerNextGeofence(RealmCheckpoint targetingCheckpoint) {
        raceViewComms.addGeoFence(targetingCheckpoint);
    }

    private void revealNextCheckpoint(RealmCheckpoint targetingCheckpoint) {
        raceViewComms.revealNextCheckpoint(targetingCheckpoint.toCustomOverlayItem());
    }


    /**
     * @return A List of {@link CustomOverlayItem} representing the current race (and its progression with this VM) or a list of all the races.
     */
    public List<CustomOverlayItem> getOverlayItems() {
        List<CustomOverlayItem> items = new ArrayList<>();

        if (raceActive) {
            items.addAll(RaceHolder.getInstance().raceToCustomOverlay());
        } else {
            RealmResults<RealmRace> allRaces = RealmHelper.getInstance(null).getAllRaces(false);
            items.addAll(RealmRace.racesToOverlay(allRaces));
        }

        return items;
    }

    /**
     * Call this to trigger the VM to check for a previous race. If one is found, bound view will be updated accordingly
     */
    public void checkPreviousRace() {
        Log.d(TAG, "checking for a previous race");
        raceActive = RaceHolder.getInstance().isRaceActive();
        if (raceActive) {
            Log.d(TAG, "found an active raceRecord, will be resuming: " + RaceHolder.getInstance().getCurrentRaceRecordId());
            changeVisibilitiesOnRaceState();
            resumeRace();

            raceViewComms.focusOnRace(getOverlayItems());
            // no need to add geofence since the service should still be alive
        }
    }

    public boolean isRacing() {
        return RaceHolder.getInstance().isRaceActive();
    }
}
