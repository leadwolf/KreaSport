package com.ccaroni.kreasport.data.local;

import android.os.SystemClock;

import com.ccaroni.kreasport.data.model.IRecord;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;

import java.util.Random;
import java.util.UUID;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Master on 10/02/2018.
 */
@Entity
public class Record implements IRecord {

    @Id
    private long id;
    private long raceId;
    private String userId;
    private long baseTime;
    private long timeExpired;
    private String dateTime;
    private boolean inProgress;

    public Record() {
        this.id = new Random().nextLong();
        this.dateTime = OffsetDateTime.now().toString();
        this.baseTime = SystemClock.elapsedRealtime();
    }

    public Record(long raceId, String userId) {
        this();
        this.raceId = raceId;
        this.userId = userId;
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

    public long getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(long baseTime) {
        this.baseTime = baseTime;
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

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }
}
