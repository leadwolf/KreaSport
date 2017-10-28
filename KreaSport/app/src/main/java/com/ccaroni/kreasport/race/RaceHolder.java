package com.ccaroni.kreasport.race;

import android.location.Location;
import android.util.Log;

import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.data.realm.RealmRiddle;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 20/08/2017.
 * This singleton class allows any instance to access the current race and the user's race record.
 */
public class RaceHolder {

    private static final String TAG = RaceHolder.class.getSimpleName();


    /**
     * The singleton instance to access this class' methods
     */
    private static RaceHolder instance;

    // Actual class attributes

    /**
     * The selected race. Is null when a race hasn't yet been selected.
     */
    private RealmRace currentRace;

    /**
     * The selected checkpoint, not necessarily the targeting one. Is null when there a checkpoint hasn't yet been selected.
     */
    private RealmCheckpoint currentCheckpoint;

    /**
     * The current record, it may not be in use as it is preloaded
     */
    private RealmRaceRecord currentRaceRecord;

    private String currentTitle;

    private String currentDescription;

    /**
     * The user id to tie to the race record.
     */
    private String userId;


    private RaceHolder() {
    }


    /**
     * Creates the singleton instance
     *
     * @param userId
     */
    public static synchronized void init(String userId) {
        if (instance == null) {
            synchronized (RealmHelper.class) {
                if (instance == null) {
                    init();
                    instance.setUserId(userId);
                }
            }
        }
    }

    public static RaceHolder getInstance() {
        return instance;
    }

    /**
     * Initializes the attributes to be empty
     */
    private static void init() {
        instance = new RaceHolder();
        instance.currentRace = null;
        instance.currentCheckpoint = null;
        instance.currentRaceRecord = null;
    }

    public String getCurrentRaceId() {
        return currentRace == null ? "" : currentRace.getId();
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return -1 if no {@link RealmRaceRecord} exists, or the {@link RealmRaceRecord#getTargetCheckpointIndex()}
     */
    public int getCheckpointProgression() {
        return currentRaceRecord == null ? -1 : currentRaceRecord.getTargetCheckpointIndex();
    }

    /**
     * @return -1 if no {@link RealmRace} exists, or {@link RealmRace#getNbCheckpoints()}
     */
    public int getNumberCheckpoints() {
        return currentRace == null ? -1 : currentRace.getNbCheckpoints();
    }

    /**
     * @return if one of a race's markers has been selected as has info in the bottom sheet
     */
    public boolean isRaceSelected() {
        return currentRace != null;
    }

    /**
     * @return "" if no {@link RealmRace} exists, or the title of the current race
     */
    public String getCurrentRaceTitle() {
        return currentRace == null ? "" : currentRace.getTitle();
    }

    /**
     * @return "" if no {@link RealmRace} exists, or the description of the current race
     */
    public String getCurrentRaceDescription() {
        return currentRace == null ? "" : currentRace.getDescription();
    }

    /**
     * @return "" if there is no current {@link RealmCheckpoint}, or the checkpoint's title
     */
    public String getCurrentCheckpointTitle() {
        return currentCheckpoint == null ? "" : currentCheckpoint.getTitle();
    }

    /**
     * @return "" if there is no current {@link RealmCheckpoint}, or the checkpoint's title
     */
    public String getCurrentCheckpointDescription() {
        return currentCheckpoint == null ? "" : currentCheckpoint.getDescription();
    }

    /**
     * Updates {@link #currentCheckpoint} to the checkpoint corresponding to newCheckpointId.
     *
     * @param newCheckpointId the id of the checkpoint which should now be the current checkpoint. Must be a checkpoint belonging to currentRace
     */
    public void updateCurrentCheckpoint(String newCheckpointId) {
        currentCheckpoint = currentRace.getCheckpointById(newCheckpointId);
    }

    /**
     * Sets {@link #currentRace} to the one with the id = raceId
     *
     * @param raceId the id of the new race
     */
    public void updateCurrentRace(String raceId) {
        currentRace = RealmHelper.getInstance(null).findRaceById(raceId);
    }

    /**
     * @return A list of the race as {@link CustomOverlayItem} up to and including the targeting checkpoint according to {@link #currentRaceRecord}
     */
    public List<? extends CustomOverlayItem> currentRaceToCustomOverlay() {
        if (currentRace == null) {
            return new ArrayList<>();
        }
        return currentRace.toCustomOverlayWithCheckpoints(currentRaceRecord.getTargetCheckpointIndex());
    }

    /**
     * Sets {@link #currentRace} and {@link #currentCheckpoint} to null
     */
    public void removeWholeSelection() {
        currentRace = null;
        currentCheckpoint = null;
    }

    /**
     * Starts recording the current race in {@link #currentRaceRecord}
     *
     * @param timeStart the time of the start of the race
     */
    public void startRace(long timeStart) {
        RealmHelper.getInstance(null).beginTransaction();

        currentRaceRecord = RealmHelper.getInstance(null).createRealmRaceRecord();
        currentRaceRecord.setUserId(userId);

        Log.d(TAG, "started recording " + currentRaceRecord.getId() + " for raceId " + currentRace.getId());
        currentRaceRecord.setBaseTime(timeStart);

        currentRaceRecord.setInProgress(true);
        currentRaceRecord.setStarted(true);
        currentRaceRecord.setRaceId(currentRace.getId());

        RealmHelper.getInstance(null).commitTransaction();
    }

    /**
     * Actually stops the race. To call upon the user's confirmation
     */
    public void stopRace() {

    }

    /**
     * @return the location of the currentRace (i.e.: the first marker)
     */
    public Location getCurrentRaceLocation() {
        return currentRace == null ? null : currentRace.getLocation();
    }

    /**
     * @return a {@link GeoPoint} of the first marker of the currentRace
     */
    public GeoPoint getCurrentRaceAsGeoPoint() {
        return currentRace == null ? new GeoPoint(0.0, 0.0) : new GeoPoint(currentRace.getLatitude(), currentRace.getLongitude());
    }

    /**
     * Stops the {@link RealmRaceRecord} and either deletes it if the race is unfinished or leaves it be
     */
    public void stopRecording() {
        RealmHelper.getInstance(null).beginTransaction();
        if (isCurrentRaceFinished()) {
            currentRaceRecord.setInProgress(true);
        } else {
            Log.d(TAG, "deleting record" + currentRaceRecord.getId());
            currentRaceRecord.deleteFromRealm();
            currentRaceRecord = null;
        }
        RealmHelper.getInstance(null).commitTransaction();
    }

    /**
     * @return if the geofence progression really is one step behind the targeting checkpoint
     */
    public boolean isGeofenceProgressionCorrect() {
        Log.d(TAG, "progress check: geofence prog is: " + currentRaceRecord.getGeofenceProgression() + ", checkpoint is: " + currentRaceRecord.getTargetCheckpointIndex());
        return currentRaceRecord.getGeofenceProgression() == (currentRaceRecord.getTargetCheckpointIndex() - 1);
    }

    /**
     * Increments geofence progression
     */
    public void incrementGeofenceProgression() {
        RealmHelper.getInstance(null).beginTransaction();
        currentRaceRecord.incrementGeofenceProgression();
        RealmHelper.getInstance(null).commitTransaction();

        Log.d(TAG, "checkpoint " + getTargetingCheckpoint().getId() + " has just been geofence validated");
        if (currentRace.isOnLastCheckpoint(currentRaceRecord.getGeofenceProgression(), "geofence index")) {
            Log.d(TAG, "last checkpoint has just been geofence validated");
        }
    }

    public RealmCheckpoint getTargetingCheckpoint() {
        return currentRace.getCheckpointByProgression(currentRaceRecord.getTargetCheckpointIndex());
    }

    private RealmCheckpoint getLastVerifiedCheckpoint() {
        return currentRace.getCheckpointByProgression(currentRaceRecord.getGeofenceProgression());
    }

    public RealmRiddle getTargetingCheckpointRiddle() {
        return getTargetingCheckpoint().getRiddle();
    }


    /**
     * Verifies the correct answer, increments progression if NOT on last checkpoint
     *
     * @param answerIndex the answerIndex that the user inputted
     */
    public void onQuestionAnswered(int answerIndex) {
        if (getTargetingCheckpoint().getAnswerIndex() != answerIndex) {
            throw new IllegalStateException("Expected correct answer index");
        }
        Log.d(TAG, "the user chose the correct answer");


        Log.d(TAG, "checkpoint " + getTargetingCheckpoint().getId() + " has just been answered correctly");
        if (isCurrentRaceFinished()) {
            Log.d(TAG, "last checkpoint has just been answered correctly");
        } else {
            RealmHelper.getInstance(null).beginTransaction();
            currentRaceRecord.incrementTargetCheckpointIndex();
            RealmHelper.getInstance(null).commitTransaction();
        }
    }

    /**
     * @return if the last checkpoint that was answered correctly was the last checkpoint, i.e. the race is finished
     */
    public boolean isCurrentRaceFinished() {
        return currentRace.isOnLastCheckpoint(currentRaceRecord.getTargetCheckpointIndex(), "target")
                && currentRace.isOnLastCheckpoint(currentRaceRecord.getGeofenceProgression(), "geofence index");
    }

    public String getCurrentTitle() {
        return currentTitle;
    }

    public void setTitle(String title) {
        this.currentTitle = title;
    }

    public String getCurrentDescription() {
        return currentDescription;
    }

    public void setDescription(String description) {
        this.currentDescription = description;
    }

    /**
     * @return If {@link RealmRaceRecord#isInProgress()}
     */
    public boolean isRaceActive() {
        return currentRaceRecord != null && currentRaceRecord.isInProgress();
    }

    public long getTimeStart() {
        return currentRaceRecord == null ? 0 : currentRaceRecord.getBaseTime();
    }

    public String getCurrentRaceRecordId() {
        return currentRaceRecord.getId();
    }

    /**
     * Saves the location to the {@link #currentRaceRecord} if not null
     *
     * @param record the location to save
     */
    public void saveLocation(Location record) {
        if (currentRaceRecord != null) {
            RealmHelper.getInstance(null).beginTransaction();
            currentRaceRecord.addLocationRecord(record);
            RealmHelper.getInstance(null).commitTransaction();
        }
    }
}
