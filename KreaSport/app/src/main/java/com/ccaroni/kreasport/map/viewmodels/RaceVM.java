package com.ccaroni.kreasport.map.viewmodels;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ccaroni.kreasport.BR;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.utils.CredentialsManager;
import com.ccaroni.kreasport.utils.LocationUtils;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Master on 22/05/2017.
 */

public abstract class RaceVM extends BaseObservable {

    private static final String LOG = RaceVM.class.getSimpleName();

    /**
     * Whether this VM (not necessarily the race) is currently in an active state
     */
    protected boolean raceActive;
    private int bottomSheetVisibility;
    protected int passiveInfoVisibility;
    protected int activeInfoVisibility;
    protected int fabStartVisibility;

    /*
     * Separate attrs because we can't just grab from currentRace or currentCheckpoint depending on raceActive?, progression and even if the user deliberately selects another
     * marker that is not related to his progression.
     */
    private String title;
    private String description;

    protected RealmRace currentRace;
    protected RealmCheckpoint currentCheckpoint;

    protected RealmRaceRecord raceRecord;

    protected RaceCommunication raceCommunication;
    protected LocationUtils mLocationUtils;

    private String userId;
    private int fabMyLocationCornerVisibility;
    private int fabMyLocationAnchoredStartVisibility;
    private int fabMyLocationAnchoredBottomSheetVisibility;

    /**
     * Default constructor to use. Initializes Realm with activity and calls {@link #initRaceRecord()}.
     *
     * @param activity       the activity linked to this RaceVM. It must implement {@link RaceCommunication}.
     * @param mLocationUtils the instance of the location utility used by the activity.
     */
    public RaceVM(Activity activity, LocationUtils mLocationUtils) {
        RealmHelper.getInstance(activity).init(activity);
        this.userId = CredentialsManager.getUserId(activity);
        if (activity instanceof RaceCommunication) {
            this.raceCommunication = (RaceCommunication) activity;
        } else {
            throw new RuntimeException(activity + " must implment " + RaceCommunication.class.getSimpleName());
        }
        this.mLocationUtils = mLocationUtils;
        initRaceRecord();
    }

    /**
     * Creates a new {@link RealmRaceRecord} managed by Realm for the next recording. Sets the userId right away.
     */
    protected void initRaceRecord() {
        RealmHelper.getInstance(null).beginTransaction();

        raceRecord = RealmHelper.getInstance(null).createRealmRaceRecord();
        raceRecord.setUserId(userId);

        RealmHelper.getInstance(null).commitTransaction();
        Log.d(LOG, "prepared new record: " + raceRecord.getId());
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

        int progression = raceRecord.getGeofenceProgression();
        int total = currentRace.getNbCheckpoints();

        return "" + progression + "/" + total;
    }

    public RealmCheckpoint getActiveCheckpoint() {
        return currentCheckpoint;
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
     * @return A List of {@link CustomOverlayItem} representing the current race (and its progression with this VM) or a list of all the races.
     */
    public List<CustomOverlayItem> getOverlayItems() {
        List<CustomOverlayItem> items = new ArrayList<>();

        if (raceActive) {
            items.addAll(currentRace.toCustomOverlayWithCheckpoints(raceRecord.getGeofenceProgression()));
        } else {
            RealmResults<RealmRace> allRaces = RealmHelper.getInstance(null).getAllRaces(false);
            items.addAll(RealmRace.racesToOverlay(allRaces));
        }

        return items;
    }

    /**
     * The manager of the geofences calls this to notify that a geofence has been triggered.<br>
     * Increments the {@link RealmRaceRecord#geofenceProgression}
     *
     * @param checkpointId the id of the triggered checkpoint
     */
    public void onGeofenceTriggered(String checkpointId) {

        if (raceRecord.getProgression() != raceRecord.getGeofenceProgression()) {
            Log.d(LOG, "Geofence was triggered but saved progression and geofence progression are already synced");
            Log.d(LOG, "progression should always be one step behind geofence progression at geofence trigger moment because normal progression is only validated on question " +
                    "answer");
            throw new IllegalStateException("Geofence was triggered but saved progression and geofence progression are already synced");
        }

        if (currentRace.isOnLastCheckpoint(raceRecord.getGeofenceProgression())) {
            Log.d(LOG, "last checkpoint has just been geofence validated");

        } else {
            Log.d(LOG, "checkpoint " + checkpointId + " has just been geofence validated");
            RealmHelper.getInstance(null).beginTransaction();
            raceRecord.incrementGeofenceProgression();
            RealmHelper.getInstance(null).commitTransaction();
        }

        RealmCheckpoint targetingCheckpoint = currentRace.getCheckpointByProgression(raceRecord.getGeofenceProgression());
        raceCommunication.askRiddle(targetingCheckpoint.getRiddle().toDTO());
    }

    /**
     *
     * @param index the index of the answer selected
     */
    public void onQuestionCorrectlyAnswered(int index) {
        RealmCheckpoint targetingCheckpoint = currentRace.getCheckpointByProgression(raceRecord.getGeofenceProgression());
        if (index != targetingCheckpoint.getAnswerIndex()) {
            throw new IllegalStateException("Expected correct answer index");
        }

        Log.d(LOG, "the user chose the correct answer");

        incrementProgression();
    }

    /**
     * If the end of the race is not finished:<br>
     * Increments the progression in {@link #raceRecord}, updates {@link #currentCheckpoint}, calls to {@link RaceCommunication#revealNextCheckpoint(CustomOverlayItem)} and
     * {@link RaceCommunication#addGeoFence(RealmCheckpoint)}.<br>
     * <br>
     * If the race is finished:<br>
     * Stops the race & notifies the end of the race through {@link RaceCommunication}
     */
    private void incrementProgression() {
        if (currentRace.isOnLastCheckpoint(raceRecord.getProgression())) {
            Log.d(LOG, "last checkpoint's answer has just been validated");

            // TODO end race
        } else {
            Log.d(LOG, "inc progression, revealing next w/ geofence");

            RealmHelper.getInstance(null).beginTransaction();
            raceRecord.incrementProgression();
            RealmHelper.getInstance(null).commitTransaction();

            currentCheckpoint = currentRace.getCheckpointByProgression(raceRecord.getProgression());

            raceCommunication.revealNextCheckpoint(currentCheckpoint.toCustomOverlayItem());

            // TODO add next geofence
        }
    }

    /**
     * Call from the View (or manual use) to ask the Model to animate to the current location.
     */
    public void onMyLocationClicked() {
        raceCommunication.onMyLocationClicked();
    }

    /**
     * {@link android.content.Context} calls this once the layout is initialized.
     * Loads the appropriate config (w/ raceActive?) and with applies to according the layout with databinding.
     */
    public void onStart() {
        Log.d(LOG, "onStart, UI is ready to be manipulated and Google API is connected");

        // set this at the start because normally it has to be triggered by a change
        RealmRaceRecord previouslyOngoingRaceRecord = RealmHelper.getInstance(null).findCurrentRaceRecord();
        if (previouslyOngoingRaceRecord != null) {
            Log.d(LOG, "found an ongoing record in db: " + previouslyOngoingRaceRecord);

            currentRace = RealmHelper.getInstance(null).findRaceById(previouslyOngoingRaceRecord.getRaceId());
            if (currentRace == null) {
                Log.d(LOG, "could not find the interrupted race");
                raceCommunication.toast("The race you were previously on was corrupted");

                RealmHelper.getInstance(null).beginTransaction();
                Log.d(LOG, "deleting the one initialized on start: " + raceRecord);
                raceRecord.deleteFromRealm();
                Log.d(LOG, "deleting the ongoing corrupted one: " + previouslyOngoingRaceRecord);
                previouslyOngoingRaceRecord.deleteFromRealm();
                RealmHelper.getInstance(null).commitTransaction();
                initRaceRecord();

                changeVisibilitiesOnRaceState(false);
                return;
            }
            currentCheckpoint = raceRecord.getCurrentCheckpoint(currentRace);
            startRace();
        } else {
            Log.d(LOG, "no previous ongoing race, hiding bottom sheet");
            changeVisibilitiesOnRaceState(false);
        }
    }

    ;

    protected void changeVisibilitiesOnRaceState(boolean raceActive) {
        passiveInfoVisibility = raceActive ? View.GONE : View.VISIBLE;
        activeInfoVisibility = raceActive ? View.VISIBLE : View.GONE;

        fabStartVisibility = raceActive || currentRace == null ? View.GONE : View.VISIBLE;
        bottomSheetVisibility = currentRace == null ? View.GONE : View.VISIBLE;

        fabMyLocationAnchoredStartVisibility = fabStartVisibility == View.VISIBLE ? View.VISIBLE : View.GONE;
        fabMyLocationAnchoredBottomSheetVisibility = bottomSheetVisibility == View.VISIBLE && fabStartVisibility == View.GONE ? View.VISIBLE : View.GONE;
        fabMyLocationCornerVisibility = bottomSheetVisibility == View.GONE ? View.VISIBLE : View.GONE;


        notifyChange();

    }

    /**
     * The {@link android.content.Context} calls this when the background of the map has been touched, to unselect the current marker.<br>
     * Therefore we must hide the bottom sheet and anything associated by called {@link #changeVisibilitiesOnRaceState(boolean)}.
     */
    public void onMapBackgroundTouch() {
        if (!raceActive) {
            currentRace = null;
            changeVisibilitiesOnRaceState(false);
            raceCommunication.unsetFocusedItem();
        }
    }

    /**
     * Updates the current info for the bottom sheet through the bindings.
     *
     * @param selectedItem
     */
    protected abstract void updateCurrent(CustomOverlayItem selectedItem);

    /**
     * Call to stop the current race. Used by passiveBottomSheet.
     */
    public abstract void onStartClicked();

    /**
     * The real method that starts the race.
     */
    protected abstract void startRace();
    /**
     * The real method that starts the race.
     * @param toArchive
     */
    protected abstract void stopRace(boolean toArchive);

    /**
     * Call to stop the current race. Used by activeBottomSheet.
     */
    public abstract void onStopClicked();

    /**
     * Call when the user quits the activity but doesnt stop the activity
     */
    public abstract void saveOngoingBaseTime();

    public RealmRace getActiveRace() {
        return currentRace;
    }

    public boolean isRaceActive() {
        return raceActive;
    }

    /**
     * Call from the activity to confirm that user wants to stop in response to {@link RaceCommunication#confirmStopRace()}
     */
    public abstract void confirmStop();
}
