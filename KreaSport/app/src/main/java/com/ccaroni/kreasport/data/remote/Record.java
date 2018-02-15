package com.ccaroni.kreasport.data.remote;

import com.ccaroni.kreasport.data.model.IRecord;

/**
 * Created by Master on 09/02/2018.
 */

public class Record implements IRecord {

    private long id;
    private String raceId;
    private String userId;
    private long timeExpired;
    private String dateTime;

    public Record() {
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public long getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(long timeExpired) {
        this.timeExpired = timeExpired;
    }

    @Override
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
