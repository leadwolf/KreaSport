package com.ccaroni.kreasport.race;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRiddle;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.race.impl.RaceHolder;

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

    /*
    NOTE: the real "model" data is contained by RaceHolder because of how many attrs we need. This also makes it available to any background racing service
     */

    protected IRaceView raceView;

    public IRaceVM(IRaceView raceView, String userId) {
        this.raceView = raceView;
        RaceHolder.init(userId);

        changeVisibilitiesOnRaceState(true);
    }

    /* ABSTRACT METHODS */

    /**
     * Switch to update on a {@link CustomOverlayItem} selection. Calls the appropriate method for updating title & description according to the selectedItem.
     *
     * @param selectedItem the {@link CustomOverlayItem} that the user clicked
     */
    protected abstract void updateBottomSheetData(CustomOverlayItem selectedItem);

    /**
     * Updates the visibilities for the whole bottom sheet and fab. NOT the data in the bottom sheet
     *
     * @param notifyChange
     */
    protected abstract void changeVisibilitiesOnRaceState(boolean notifyChange);

    /**
     * Asks {@link #raceView} to reveal the next checkpoint
     *
     * @param targetingCheckpoint the checkpoint to reveal
     */
    protected abstract void revealNextCheckpoint(RealmCheckpoint targetingCheckpoint);

    /**
     * Asks {@link #raceView} to add the next geofence
     *
     * @param targetingCheckpoint the {@link RealmCheckpoint} corresponding to the new geofence
     */
    protected abstract void triggerNextGeofence(RealmCheckpoint targetingCheckpoint);

    /**
     * Update bottom sheet visiblities, asks {@link #raceView} to only display current race, triggers the checkpoint reveal and geofence, asks {@link #raceView} to start its
     * chronometer
     */
    protected abstract void startRace();

    /**
     * @return if the user's location is close enough to the start of the selected race
     */
    protected abstract boolean isUserLocationAtRaceStart();

    /* END ABSTRACT METHODS */

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
     * @return A List of {@link CustomOverlayItem} representing the current race (and its progression with this VM) OR a list of all the races.
     */
    public List<CustomOverlayItem> getOverlayItems() {
        List<CustomOverlayItem> items = new ArrayList<>();

        if (isRaceActive()) {
            items.addAll(RaceHolder.getInstance().currentRaceToCustomOverlay());
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
            changeVisibilitiesOnRaceState(true);
            raceView.startChronometer(RaceHolder.getInstance().getTimeStart());
            raceView.focusOnRace(getOverlayItems());
            // no need to add geofence since the service should still be alive
        }
    }

    /**
     * @return if a race is currently active
     */
    public boolean isRaceActive() {
        return RaceHolder.getInstance().isRaceActive();
    }

    /**
     * Call when the user confirmed to completely stop the race.
     * Stops the recording, updates visibilities with {@link #changeVisibilitiesOnRaceState(boolean)} and then calls {@link IRaceView#setDefaultMarkers()}
     */
    public void onStopConfirmation() {
        RaceHolder.getInstance().stopRecording();
        changeVisibilitiesOnRaceState(true); // TODO lead to a new screen if finished race
        raceView.setDefaultMarkers();
    }

    /**
     * Removes visibility of all views related to the current race/checkpoint
     */
    public void onMapBackgroundTouch() {
        if (!isRaceActive()) {
            RaceHolder.getInstance().resetSelection();
            changeVisibilitiesOnRaceState(true);
        }
    }


    /**
     * Interfaces with {@link RaceHolder} to verify progression, removes the geofence since we will now trigger the riddle for the checkpoint
     */
    public void onGeofenceTriggered() {
        if (!RaceHolder.getInstance().isGeofenceProgressionCorrect()) {
            throw new IllegalStateException("Geofence was triggered but progressions are not synced");
        }

        raceView.removeLastGeofence();
        RaceHolder.getInstance().incrementGeofenceProgression();

        RealmRiddle targetingCheckpoint = RaceHolder.getInstance().getTargetingCheckpointRiddle();
        raceView.askRiddle(targetingCheckpoint.toDTO());
    }

    /**
     * Interfaces with {@link RaceHolder} to increment targeting progression, triggers the next checkpoint reveal and geofence
     *
     * @param answerIndex the answerIndex that the user inputted
     */
    public void onQuestionCorrectlyAnswered(int answerIndex) {
        RaceHolder.getInstance().onQuestionAnswered(answerIndex);

        if (RaceHolder.getInstance().isCurrentRaceFinished()) {
            triggerRaceEnd();
        } else {
            triggerNextCheckpoint();
        }
    }

    /**
     * Stops the recording and chronometer
     */
    private void triggerRaceEnd() {
        RaceHolder.getInstance().stopRecording();
        raceView.stopChronometer();
        raceView.toast("Finished!");
    }

    /**
     * Triggers the next checkpoint reveal and geofence adding
     */
    private void triggerNextCheckpoint() {
        RealmCheckpoint targetingCheckpoint = RaceHolder.getInstance().getTargetingCheckpoint();
        triggerNextGeofence(targetingCheckpoint);
        revealNextCheckpoint(targetingCheckpoint);
    }

    /* METHODS FOR BINDINGS */

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
            Log.d(TAG, "cannot start race: user needs to resolve location settings");
            raceView.askResolveLocationSettings();
        } else {
            verifyProximityBeforeStart();
        }
    }

    /**
     * Verifies the user's proximity to the start, and starts the race if close enough
     */
    private void verifyProximityBeforeStart() {
        if (isUserLocationAtRaceStart()) {
            GeoPoint startPoint = RaceHolder.getInstance().getCurrentRaceAsGeoPoint();
            if (raceView.needToAnimateToPoint(startPoint)) {
                animateBeforeStart();
            } else {
                Log.d(TAG, "no animation, starting race");
                startRace();
            }
        }
    }

    /**
     * Animates to the user's location before starting the race by manually calling {@link #onMyLocationClicked()}
     */
    private void animateBeforeStart() {
        Log.d(TAG, "waiting for animation to end to start race");
        onMyLocationClicked(); // manually trigger animation to user's location

        // start the race after 1500ms, the time for onMyLocationClicked() to animate to the user's location
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startRace();
            }
        }, 1500);
    }

    public void onMyLocationClicked() {
        raceView.onMyLocationClicked();
    }

    public void onStopClicked() {
        raceView.askStopConfirmation();
    }

    /* END BINDINGS */

    public void saveLocation(Location location) {
        RaceHolder.getInstance().addLocationRecord(location);
    }

    public Location getLastRecordedLocation() {
        return RaceHolder.getInstance().getLastRecordedLocation();
    }
}
