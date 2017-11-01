package com.ccaroni.kreasport.data.secondary.realm.impl;

import com.ccaroni.kreasport.data.legacy.realm.RealmLocation;
import com.ccaroni.kreasport.data.secondary.realm.IRaceRecordDAO;
import com.ccaroni.kreasport.data.secondary.dto.impl.RaceRecordDTO;

import org.threeten.bp.OffsetDateTime;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Master on 01/11/2017.
 */

public class RaceRecordDAO extends RealmObject implements IRaceRecordDAO<RaceRecordDTO> {

    protected String serverID;
    protected String raceId;
    protected String userId;
    protected boolean inProgress;
    protected long baseTime;
    protected long timeExpired;
    protected int targetCheckpointIndex;
    protected int geofenceProgression;
    protected boolean synced;
    protected boolean toDelete;
    protected String dateTime;
    protected RealmList<RealmLocation> userPath;

    public RaceRecordDAO() {
        geofenceProgression = -1;

        serverID = UUID.randomUUID().toString();
        dateTime = OffsetDateTime.now().toString();
        userPath = new RealmList<>();
    }

    @Override
    public String getServerID() {
        return serverID;
    }

    @Override
    public void setServerID(String serverID) {
        this.serverID = serverID;
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

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public long getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(long baseTime) {
        this.baseTime = baseTime;
    }

    public int getTargetCheckpointIndex() {
        return targetCheckpointIndex;
    }

    public void setTargetCheckpointIndex(int targetCheckpointIndex) {
        this.targetCheckpointIndex = targetCheckpointIndex;
    }

    public int getGeofenceProgression() {
        return geofenceProgression;
    }

    public void setGeofenceProgression(int geofenceProgression) {
        this.geofenceProgression = geofenceProgression;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public RealmList<RealmLocation> getUserPath() {
        return userPath;
    }

    public void setUserPath(RealmList<RealmLocation> userPath) {
        this.userPath = userPath;
    }

    @Override
    public RaceRecordDTO toDTO() {
        RaceRecordDTO raceRecordDTO = new RaceRecordDTO();
        raceRecordDTO.setId(getServerID());
        raceRecordDTO.setUserId(getUserId());
        raceRecordDTO.setRaceId(getRaceId());
        raceRecordDTO.setDateTime(getDateTime());
        raceRecordDTO.setTimeExpired(getTimeExpired());

        return raceRecordDTO;
    }
}
