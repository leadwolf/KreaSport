package com.ccaroni.kreasport.data.secondary.realm.impl;

import com.ccaroni.kreasport.data.secondary.realm.ICheckpointDAO;
import com.ccaroni.kreasport.data.secondary.dto.impl.SimpleCheckpointDTO;

import io.realm.RealmObject;

/**
 * Created by Master on 01/11/2017.
 */

public class SimpleCheckpointDAO extends RealmObject implements ICheckpointDAO<SimpleCheckpointDTO> {

    protected String serverID;
    protected String title;
    protected String description;
    protected Double latitude;
    protected Double longitude;
    protected Double altitude;

    protected String raceID;
    protected int order;

    public SimpleCheckpointDAO() {
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
    public String getRaceID() {
        return raceID;
    }

    @Override
    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public SimpleCheckpointDTO toDTO() {
        SimpleCheckpointDTO simpleCheckpoint = new SimpleCheckpointDTO();
        simpleCheckpoint.setId(getServerID());
        simpleCheckpoint.setRaceID(getRaceID());
        simpleCheckpoint.setTitle(getTitle());
        simpleCheckpoint.setDescription(getDescription());
        simpleCheckpoint.setLatitude(getLatitude());
        simpleCheckpoint.setLongitude(getLongitude());
        simpleCheckpoint.setAltitude(getAltitude());

        return simpleCheckpoint;
    }


}
