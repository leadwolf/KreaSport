package com.ccaroni.kreasport.data.secondary.realm.impl;

import com.ccaroni.kreasport.data.secondary.dto.ICheckpointDTO;
import com.ccaroni.kreasport.data.secondary.dto.IRaceDTO;
import com.ccaroni.kreasport.data.secondary.dto.impl.RaceDTO;
import com.ccaroni.kreasport.data.secondary.realm.ICheckpointDAO;
import com.ccaroni.kreasport.data.secondary.realm.IRaceDAO;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by Master on 01/11/2017.
 */

public class RaceDAO<T extends ICheckpointDAO<ICheckpointDTO>> extends RealmObject implements IRaceDAO<T> {

    protected String serverID;
    protected String title;
    protected String description;
    protected Double latitude;
    protected Double longitude;
    protected Double altitude;

    private RealmList<T> checkpoints;

    public RaceDAO() {
        checkpoints = new RealmList<>();
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
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public Double getAltitude() {
        return altitude;
    }

    @Override
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    @Override
    public RealmList<T> getCheckpoints() {
        return checkpoints;
    }

    @Override
    public void setCheckpoints(RealmList<T> checkpoints) {
        this.checkpoints = checkpoints;
    }

    @Override
    public IRaceDTO<ICheckpointDTO<?>> toDTO() {
        IRaceDTO<ICheckpointDTO<?>> raceDTO = new RaceDTO<>();
        raceDTO.setId(getServerID());
        raceDTO.setTitle(getTitle());
        raceDTO.setDescription(getDescription());
        raceDTO.setLatitude(getLatitude());
        raceDTO.setLongitude(getLongitude());
        raceDTO.setAltitude(getAltitude());

        raceDTO.setCheckpoints(checkpointsToDTO());

        return raceDTO;
    }

    @Override
    public List<ICheckpointDTO<?>> checkpointsToDTO() {
        List<ICheckpointDTO<?>> checkpointDTOS = new ArrayList<>();
        for (T checkpointDAO : getCheckpoints()) {
            checkpointDTOS.add(checkpointDAO.toDTO());
        }
        return checkpointDTOS;
    }

}
