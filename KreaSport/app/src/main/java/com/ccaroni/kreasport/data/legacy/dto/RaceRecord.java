package com.ccaroni.kreasport.data.legacy.dto;

/**
 * Created by Master on 30/05/2017.
 */

public class RaceRecord {


    private String id;

    private String raceId;
    private String userId;

    private long timeExpired;

    private String dateTime;

    public RaceRecord() {
    }

    public String getId() {
        return id;
    }

    public RaceRecord setId(String id) {
        this.id = id;
        return this;
    }

    public String getRaceId() {
        return raceId;
    }

    public RaceRecord setRaceId(String raceId) {
        this.raceId = raceId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public RaceRecord setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public long getTimeExpired() {
        return timeExpired;
    }

    public RaceRecord setTimeExpired(long timeExpired) {
        this.timeExpired = timeExpired;
        return this;
    }

    public String getDateTime() {
        return dateTime;
    }

    public RaceRecord setDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }
}
