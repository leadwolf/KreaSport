package com.ccaroni.kreasport.data.secondary.dto.impl;

import com.ccaroni.kreasport.data.secondary.realm.impl.RaceRecordDAO;
import com.ccaroni.kreasport.data.secondary.dto.IRaceRecordDTO;

/**
 * Created by Master on 01/11/2017.
 */

public class RaceRecordDTO implements IRaceRecordDTO<RaceRecordDAO> {

    protected String id;
    protected String raceId;
    protected String userId;
    protected long timeExpired;
    protected String dateTime;

    public RaceRecordDTO() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getRaceId() {
        return raceId;
    }

    @Override
    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public long getTimeExpired() {
        return timeExpired;
    }

    @Override
    public void setTimeExpired(long timeExpired) {
        this.timeExpired = timeExpired;
    }

    @Override
    public String getDateTime() {
        return dateTime;
    }

    @Override
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public RaceRecordDAO toDAO() {
        RaceRecordDAO raceRecordDAO = new RaceRecordDAO();
        raceRecordDAO.setServerID(getId());
        raceRecordDAO.setUserId(getUserId());
        raceRecordDAO.setRaceId(getRaceId());
        raceRecordDAO.setDateTime(getDateTime());
        raceRecordDAO.setTimeExpired(getTimeExpired());

        return raceRecordDAO;
    }
}
