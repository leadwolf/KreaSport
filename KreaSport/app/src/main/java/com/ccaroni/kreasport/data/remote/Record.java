package com.ccaroni.kreasport.data.remote;

import android.location.Location;

import com.ccaroni.kreasport.data.model.IRecord;

import java.util.List;

/**
 * Created by Master on 09/02/2018.
 */

public class Record implements IRecord {

    private long id;
    private long raceId;
    private String userId;
    private long timeExpired;
    private String dateTime;

    private List<Location> path;

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
    public long getRaceId() {
        return raceId;
    }

    public void setRaceId(long raceId) {
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

    @Override
    public List<Location> getPathToDTO() {
        return getPath();
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<Location> getPath() {
        return path;
    }

    public void setPath(List<Location> path) {
        this.path = path;
    }
}
