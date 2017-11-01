package com.ccaroni.kreasport.data.secondary.dto.impl;

import com.ccaroni.kreasport.data.secondary.dto.ICheckpointDTO;
import com.ccaroni.kreasport.data.secondary.dto.IRiddleDTO;
import com.ccaroni.kreasport.data.secondary.realm.impl.RiddleCheckpointDAO;

/**
 * Created by Master on 01/11/2017.
 */

public class RiddleCheckpointDTO implements ICheckpointDTO<RiddleCheckpointDAO> {


    protected String id;
    protected String title;
    protected String description;
    protected Double latitude;
    protected Double longitude;
    protected Double altitude;

    protected String raceID;
    protected int order;

    protected IRiddleDTO riddle;

    public RiddleCheckpointDTO() {
        riddle = new RiddleDTO();
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

    public IRiddleDTO getRiddle() {
        return riddle;
    }

    public void setRiddle(IRiddleDTO riddle) {
        this.riddle = riddle;
    }

    @Override
    public RiddleCheckpointDAO toDAO() {
        RiddleCheckpointDAO riddleCheckpointDAO = new RiddleCheckpointDAO();
        riddleCheckpointDAO.setServerID(getId());
        riddleCheckpointDAO.setRaceID(getRaceID());
        riddleCheckpointDAO.setTitle(getTitle());
        riddleCheckpointDAO.setDescription(getDescription());
        riddleCheckpointDAO.setLatitude(getLatitude());
        riddleCheckpointDAO.setLongitude(getLongitude());
        riddleCheckpointDAO.setAltitude(getAltitude());

        riddleCheckpointDAO.setRiddle(riddle.toDAO());

        return riddleCheckpointDAO;
    }
}
