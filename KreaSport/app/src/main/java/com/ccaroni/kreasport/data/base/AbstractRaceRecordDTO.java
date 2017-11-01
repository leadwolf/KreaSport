package com.ccaroni.kreasport.data.base;

import com.ccaroni.kreasport.data.dao.AbstractRaceRecordDAO;

/**
 * Created by Master on 01/11/2017.
 */

public abstract class AbstractRaceRecordDTO<T extends AbstractRaceRecordDAO> implements BaseDTO<T> {

    protected String id;
    protected String raceId;
    protected String userId;
    protected long timeExpired;
    protected String dateTime;

    public AbstractRaceRecordDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(long timeExpired) {
        this.timeExpired = timeExpired;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public abstract T toDAO();
}
