package com.ccaroni.kreasport.race;

import android.content.res.Configuration;

import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.RealmResults;

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
        if (currentRaceRecord == null) {
            return -1;
        }
        return currentRaceRecord.getProgression();
    }

    public int getNumberCheckpoints() {
        if (currentRace == null) {
            return -1;
        }
        return currentRace.getNbCheckpoints();
    }

    public boolean isRaceSelected() {
        return currentRace != null;
    }

    public String getCurrentRaceTitle() {
        if (currentRace == null) {
            return "";
        }
        return currentRace.getTitle();
    }

    public String getCurrentRaceDescription() {
        if (currentRace == null) {
            return "";
        }
        return currentRace.getDescription();
    }

    public String getCurrentCheckpointTitle() {
        if (currentCheckpoint == null) {
            return "";
        }
        return currentCheckpoint.getTitle();
    }

    public String getCurrentCheckpointDescription() {
        if (currentCheckpoint == null) {
            return "";
        }
        return currentCheckpoint.getDescription();
    }

    public void updateCurrentCheckpoint(String newCheckpointId) {
        currentCheckpoint = currentRace.getCheckpointById(newCheckpointId);
    }

    public void updateCurrentRace(String raceId) {
        currentRace = RealmHelper.getInstance(null).findRaceById(raceId);
    }

    public List<? extends CustomOverlayItem> raceToCustomOverlay() {
        return currentRace.toCustomOverlayWithCheckpoints(currentRaceRecord.getGeofenceProgression());
    }
}
