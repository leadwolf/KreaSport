package com.ccaroni.kreasport.race;

import android.location.Location;
import android.os.SystemClock;
import android.util.Log;

import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

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
        return currentRaceRecord == null ? -1 : currentRaceRecord.getProgression();
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

    public void setRaceRecordInProgress(boolean raceRecordInProgress) {
        RealmHelper.getInstance(null).beginTransaction();
        currentRaceRecord.setInProgress(raceRecordInProgress);
        RealmHelper.getInstance(null).commitTransaction();
    }

    public void deleteRaceRecord() {
        Log.d(TAG, "deleting record" + currentRaceRecord.getId());
        RealmHelper.getInstance(null).beginTransaction();
        currentRaceRecord.deleteFromRealm();
        RealmHelper.getInstance(null).commitTransaction();

        currentRaceRecord = null;
    }
}
