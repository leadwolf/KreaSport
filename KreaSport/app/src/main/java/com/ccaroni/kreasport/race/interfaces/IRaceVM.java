package com.ccaroni.kreasport.race.interfaces;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Handler;
import android.util.Log;

import com.ccaroni.kreasport.BR;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRiddle;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.race.IRaceView;
import com.ccaroni.kreasport.race.RaceHolder;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Master on 28/10/2017.
 */

public abstract class IRaceVM extends BaseObservable {

    private static final String TAG = IRaceVM.class.getSimpleName();

    protected int bottomSheetVisibility;
    protected int passiveInfoVisibility;
    protected int activeInfoVisibility;
    protected int fabStartVisibility;


    protected int fabMyLocationCornerVisibility;
    protected int fabMyLocationAnchoredStartVisibility;
    protected int fabMyLocationAnchoredBottomSheetVisibility;

    protected IRaceView raceView;


    /**
     * @return a listener that will update the bottom sheet when a marker is clicked
     */
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
     * @param item
     */
    protected abstract void updateBottomSheetData(CustomOverlayItem item);


    /**
     * @return A List of {@link CustomOverlayItem} representing the current race (and its progression with this VM) OR a list of all the races.
     */
    public List<CustomOverlayItem> getOverlayItems() {
        List<CustomOverlayItem> items = new ArrayList<>();

        if (isRaceActive()) {
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
        if (isRaceActive()) {
            Log.d(TAG, "found an active raceRecord, will be resuming: " + RaceHolder.getInstance().getCurrentRaceRecordId());
            changeVisibilitiesOnRaceState();
            raceView.startChronometer(RaceHolder.getInstance().getTimeStart());

            raceView.focusOnRace(getOverlayItems());
            // no need to add geofence since the service should still be alive
        }
    }

    /**
     * Updates the visibilities for the whole bottom sheet and fab. NOT the data in the bottom sheet
     */
    protected abstract void changeVisibilitiesOnRaceState();

    /**
     * @return if a race is currently active
     */
    public boolean isRaceActive() {
        return RaceHolder.getInstance().isRaceActive();
    }

    /**
     * Call when the user confirmed to completely stop the race.
     * Stops the recording, updates visibilites with {@link #changeVisibilitiesOnRaceState()} and then calls {@link IRaceView#setDefaultMarkers()}
     */
    public void onStopConfirmation() {

        RaceHolder.getInstance().stopRecording();
        changeVisibilitiesOnRaceState(); // TODO lead to a new screen if finished race
        raceView.setDefaultMarkers();
    }

    /**
     * Removes visibility of all views related to the current race/checkpoint
     */
    public void onMapBackgroundTouch() {
        if (!isRaceActive()) {
            RaceHolder.getInstance().removeWholeSelection();
            changeVisibilitiesOnRaceState();
        }
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

        raceView.removeLastGeofence();

        RaceHolder.getInstance().onGeofenceTriggered();

        RealmRiddle targetingCheckpoint = RaceHolder.getInstance().getTargetingCheckpointRiddle();
        raceView.askRiddle(targetingCheckpoint.toDTO());
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
            raceView.stopChronometer();
            raceView.toast("Finished!");
        } else {
            // trigger next
            RealmCheckpoint targetingCheckpoint = RaceHolder.getInstance().getTargetingCheckpoint();
            triggerNextGeofence(targetingCheckpoint);
            revealNextCheckpoint(targetingCheckpoint);
        }
    }

    protected abstract void revealNextCheckpoint(RealmCheckpoint targetingCheckpoint);

    protected abstract void triggerNextGeofence(RealmCheckpoint targetingCheckpoint);


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

    @Bindable
    public String getProgression() {
        int progression = RaceHolder.getInstance().getCheckpointProgression();
        int total = RaceHolder.getInstance().getNumberCheckpoints();

        return "" + progression + "/" + total;
    }


    public void onStartClicked() {
        if (isRaceActive()) {
            throw new IllegalStateException("A race is already active");
        } else if (!RaceHolder.getInstance().isRaceSelected()) {
            throw new IllegalStateException("No race is currently selected");
        }

        if (raceView.userShouldVerifyLocationSettings()) {
            if (validateProximityToStart()) {

                GeoPoint startPoint = RaceHolder.getInstance().getCurrentRaceAsGeopoint();
                if (raceView.needToAnimateToPoint(startPoint)) {
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

    protected abstract void startRace();

    protected abstract boolean validateProximityToStart();


    public void setDescription(String description) {
        RaceHolder.getInstance().setDescription(description);
        notifyPropertyChanged(BR.description);
    }

    public void onMyLocationClicked() {
        raceView.onMyLocationClicked();
    }

    public void onStopClicked() {
        raceView.askStopConfirmation();
    }


}
