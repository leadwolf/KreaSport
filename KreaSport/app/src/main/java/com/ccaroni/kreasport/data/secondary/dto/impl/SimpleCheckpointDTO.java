package com.ccaroni.kreasport.data.secondary.dto.impl;

import com.ccaroni.kreasport.data.secondary.realm.impl.SimpleCheckpointDAO;
import com.ccaroni.kreasport.data.secondary.dto.ICheckpointDTO;

/**
 * Created by Master on 01/11/2017.
 */

public class SimpleCheckpointDTO implements ICheckpointDTO<SimpleCheckpointDAO> {

    protected String id;
    protected String title;
    protected String description;
    protected Double latitude;
    protected Double longitude;
    protected Double altitude;

    protected String raceID;
    protected int order;

    public SimpleCheckpointDTO() {
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
    public SimpleCheckpointDAO toDAO() {
        SimpleCheckpointDAO simpleCheckpointDAO = new SimpleCheckpointDAO();
        simpleCheckpointDAO.setServerID(getId());
        simpleCheckpointDAO.setRaceID(getRaceID());
        simpleCheckpointDAO.setTitle(getTitle());
        simpleCheckpointDAO.setDescription(getDescription());
        simpleCheckpointDAO.setLatitude(getLatitude());
        simpleCheckpointDAO.setLongitude(getLongitude());
        simpleCheckpointDAO.setAltitude(getAltitude());

        return simpleCheckpointDAO;
    }

}
