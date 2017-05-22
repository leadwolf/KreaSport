package com.ccaroni.kreasport.data.realm;

import org.threeten.bp.OffsetDateTime;

import io.realm.RealmObject;

/**
 * Created by Master on 22/05/2017.
 */

public class RaceRecord extends RealmObject {

    private String raceId;
    private String userId;


    private boolean finished;
    private long timeExpired;

    private boolean synced;

    private OffsetDateTime dateTime;

    public RaceRecord() {
    }

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public long getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(long timeExpired) {
        this.timeExpired = timeExpired;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
