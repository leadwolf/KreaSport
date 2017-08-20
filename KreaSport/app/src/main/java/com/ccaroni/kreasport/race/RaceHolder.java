package com.ccaroni.kreasport.race;

import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Master on 20/08/2017.
 */

public class RaceHolder {

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
}
