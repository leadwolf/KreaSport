package com.ccaroni.kreasport.race;

import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;

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

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
