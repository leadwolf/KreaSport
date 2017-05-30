package com.ccaroni.kreasport.map.viewmodels;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.ccaroni.kreasport.BR;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
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
    protected int passiveInfoVisibility;
    protected int activeInfoVisibility;
    protected int fabVisibility;

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
        if (!raceActive) {
            Log.d(LOG, "No race active to get progression from");
            return null;
        }

        int progression = raceRecord.getProgression();
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
            items.addAll(currentRace.toCustomOverlayWithCheckpoints(raceRecord.getProgression()));
        } else {
            RealmResults<RealmRace> allRaces = RealmHelper.getInstance(null).getAllRaces(false);
            items.addAll(RealmRace.racesToOverlay(allRaces));
        }

        return items;
    }

    /**
     * The manager of the geofences calls this to notify that a geofence has been triggered
     *
     * @param checkpointId the id of the triggered checkpoint
     */
    public void onGeofenceTriggered(String checkpointId) {
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
            Log.d(LOG, "last checkpoint has just been validated");
            // TODO stop and save
            // TODO notify end
        } else {
            Log.d(LOG, "checkpoint validated, inc progression, revealing next w/ geofence");

            RealmHelper.getInstance(null).beginTransaction();
            raceRecord.incrementProgression();
            RealmHelper.getInstance(null).commitTransaction();

            currentCheckpoint = currentRace.getCheckpointByProgression(raceRecord.getProgression());

            raceCommunication.revealNextCheckpoint(currentCheckpoint.toCustomOverlayItem());
            raceCommunication.addGeoFence(currentCheckpoint);
        }
    }

    /**
     * {@link android.content.Context} calls this once the layout is initialized.
     * Loads the appropriate config (w/ raceActive?) and with applies to according the layout with databinding.
     */
    public abstract void onStart();

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

}
