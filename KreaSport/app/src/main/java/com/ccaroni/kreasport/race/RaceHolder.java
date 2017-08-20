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
 */

public class RaceHolder {

    private static final String TAG = RaceHolder.class.getSimpleName();


    /**
     * The singleton instance to access this class' methods
     */
    private static RaceHolder instance;

    // Actual class attributes

    /**
     * The selected race, with or without timer
     */
    protected RealmRace currentRace;

    /**
     * The selected checkpoint, not necessarily the targeting one
     */
    protected RealmCheckpoint currentCheckpoint;

    /**
     * The current record, it may not be in use as it it preloaded
     */
    protected RealmRaceRecord currentRaceRecord;

    /**
     * The user id to tie to the race record.
     */
    private String userId;


    private RaceHolder() {
    }


    /**
     * Creates the singleton instance
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

    public int getCheckpointProgression() {
        return currentRaceRecord == null ? -1 : currentRaceRecord.getTargetCheckpointIndex();
    }

    public int getNumberCheckpoints() {
        return currentRace == null ? -1 : currentRace.getNbCheckpoints();
    }

    public boolean isRaceSelected() {
        return currentRace != null;
    }

    public String getCurrentRaceTitle() {
        return currentRace == null ? "" : currentRace.getTitle();
    }

    public String getCurrentRaceDescription() {
        return currentRace == null ? "" : currentRace.getDescription();
    }

    public String getCurrentCheckpointTitle() {
        return currentCheckpoint == null ? "" : currentCheckpoint.getTitle();
    }

    public String getCurrentCheckpointDescription() {
        return currentCheckpoint == null ? "" : currentCheckpoint.getDescription();
    }

    public void updateCurrentCheckpoint(String newCheckpointId) {
        currentCheckpoint = currentRace.getCheckpointById(newCheckpointId);
    }

    public void updateCurrentRace(String raceId) {
        currentRace = RealmHelper.getInstance(null).findRaceById(raceId);
    }

    public List<? extends CustomOverlayItem> raceToCustomOverlay() {
        if (currentRace == null) {
            return new ArrayList<>();
        }
        return currentRace.toCustomOverlayWithCheckpoints(currentRaceRecord.getGeofenceProgression());
    }

    public void removeWholeSelection() {
        currentRace = null;
        currentCheckpoint = null;
    }

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

    public Location getCurrentRaceLocation() {
        return currentRace == null ? null : currentRace.getLocation();
    }

    public GeoPoint getCurrentRaceAsGeopoint() {
        return currentRace == null ? new GeoPoint(0.0, 0.0) : new GeoPoint(currentRace.getLatitude(), currentRace.getLongitude());
    }

    private void setRaceRecordInProgress(boolean raceRecordInProgress) {
        RealmHelper.getInstance(null).beginTransaction();
        currentRaceRecord.setInProgress(raceRecordInProgress);
        RealmHelper.getInstance(null).commitTransaction();
    }

    /**
     * Stops the {@link RealmRaceRecord} and either deletes it if the race is unfinished or leaves it be
     */
    public void stopRecording() {
        if (finishedRace()) {
            setRaceRecordInProgress(false);
        } else {
        Log.d(TAG, "deleting record" + currentRaceRecord.getId());
        RealmHelper.getInstance(null).beginTransaction();
        currentRaceRecord.deleteFromRealm();
        RealmHelper.getInstance(null).commitTransaction();

        currentRaceRecord = null;
        }
    }

    /**
     * Makes sure the geofence progression is one index behind checkpoint progression
     */
    public boolean verifyGeofenceProgression() {
        Log.d(TAG, "progress check: geofence: " + currentRaceRecord.getGeofenceProgression() + ", checkpoint: " + currentRaceRecord.getTargetCheckpointIndex());
        return currentRaceRecord.getGeofenceProgression() == (currentRaceRecord.getTargetCheckpointIndex() - 1);
    }

    /**
     * Increments or saves progression
     */
    public void onGeofenceTriggered() {

        RealmHelper.getInstance(null).beginTransaction();
        currentRaceRecord.incrementGeofenceProgression();
        RealmHelper.getInstance(null).commitTransaction();

        Log.d(TAG, "checkpoint " + getTargetingCheckpoint().getId() + " has just been geofence validated");
        if (currentRace.isOnLastCheckpoint(currentRaceRecord.getGeofenceProgression(), "geofence index")) {
            Log.d(TAG, "last checkpoint has just been geofence validated");
        }
    }

    RealmCheckpoint getTargetingCheckpoint() {
        return currentRace.getCheckpointByProgression(currentRaceRecord.getTargetCheckpointIndex());
    }

    private RealmCheckpoint getLastVerifiedCheckpoint() {
        return currentRace.getCheckpointByProgression(currentRaceRecord.getGeofenceProgression());
    }

    public RealmRiddle getTargetingCheckpointRiddle() {
        return getTargetingCheckpoint().getRiddle();
    }


    /**
     * Increments targeting progression if not on last one
     * @param answerIndex
     */
    public void onQuestionAnswered(int answerIndex) {
        if (getTargetingCheckpoint().getAnswerIndex() != answerIndex) {
            throw new IllegalStateException("Expected correct answer index");
        }
        Log.d(TAG, "the user chose the correct answer");


        Log.d(TAG, "checkpoint " + getTargetingCheckpoint().getId() + " has just been answered correctly");
        if (finishedRace()) {
            Log.d(TAG, "last checkpoint has just been answered correctly");
        } else {
            RealmHelper.getInstance(null).beginTransaction();
            currentRaceRecord.incrementTargetCheckpointIndex();
            RealmHelper.getInstance(null).commitTransaction();
        }
    }

    /**
     *
     * @return if the last checkpoint that was answered correctly was the last checkpoint, i.e. the race is finished
     */
    public boolean finishedRace() {
        // TODO verify last riddle was answered
        return currentRace.isOnLastCheckpoint(currentRaceRecord.getTargetCheckpointIndex(), "target") && currentRace.isOnLastCheckpoint(currentRaceRecord.getGeofenceProgression(), "geofence index");
    }
}
