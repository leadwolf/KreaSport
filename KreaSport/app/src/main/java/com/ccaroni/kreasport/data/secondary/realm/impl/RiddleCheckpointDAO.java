package com.ccaroni.kreasport.data.secondary.realm.impl;

import com.ccaroni.kreasport.data.secondary.realm.ICheckpointDAO;
import com.ccaroni.kreasport.data.secondary.realm.IRiddleDAO;
import com.ccaroni.kreasport.data.secondary.dto.impl.RiddleCheckpointDTO;

import io.realm.RealmObject;

/**
 * Created by Master on 01/11/2017.
 */

public class RiddleCheckpointDAO extends RealmObject implements ICheckpointDAO<RiddleCheckpointDTO> {

    protected SimpleCheckpointDAO simpleCheckpoint;

    protected RiddleDAO riddle;

    public RiddleCheckpointDAO() {
        simpleCheckpoint = new SimpleCheckpointDAO();
        riddle = new RiddleDAO();
    }

    @Override
    public String getServerID() {
        return simpleCheckpoint.getServerID();
    }

    @Override
    public void setServerID(String serverID) {
        this.simpleCheckpoint.setServerID(serverID);
    }

    @Override
    public String getTitle() {
        return simpleCheckpoint.getTitle();
    }

    @Override
    public void setTitle(String title) {
        this.simpleCheckpoint.setTitle(title);
    }

    @Override
    public String getDescription() {
        return simpleCheckpoint.getDescription();
    }

    @Override
    public void setDescription(String description) {
        this.simpleCheckpoint.setDescription(description);
    }

    @Override
    public Double getLatitude() {
        return simpleCheckpoint.getLatitude();
    }

    @Override
    public void setLatitude(Double latitude) {
        this.simpleCheckpoint.setLatitude(latitude);
    }

    @Override
    public Double getLongitude() {
        return simpleCheckpoint.getLongitude();
    }

    @Override
    public void setLongitude(Double longitude) {
        this.simpleCheckpoint.setLongitude(longitude);
    }

    @Override
    public Double getAltitude() {
        return simpleCheckpoint.getAltitude();
    }

    @Override
    public void setAltitude(Double altitude) {
        this.simpleCheckpoint.setAltitude(altitude);
    }

    @Override
    public String getRaceID() {
        return simpleCheckpoint.getRaceID();
    }

    @Override
    public void setRaceID(String raceID) {
        this.simpleCheckpoint.setRaceID(raceID);
    }

    @Override
    public int getOrder() {
        return simpleCheckpoint.getOrder();
    }

    @Override
    public void setOrder(int order) {
        this.simpleCheckpoint.setOrder(order);
    }

    public RiddleDAO getRiddle() {
        return riddle;
    }

    public void setRiddle(RiddleDAO riddle) {
        this.riddle = riddle;
    }

    @Override
    public RiddleCheckpointDTO toDTO() {
        RiddleCheckpointDTO riddleCheckpointDTO = new RiddleCheckpointDTO();
        riddleCheckpointDTO.setId(getServerID());
        riddleCheckpointDTO.setRaceID(getRaceID());
        riddleCheckpointDTO.setTitle(getTitle());
        riddleCheckpointDTO.setDescription(getDescription());
        riddleCheckpointDTO.setLatitude(getLatitude());
        riddleCheckpointDTO.setLongitude(getLongitude());
        riddleCheckpointDTO.setAltitude(getAltitude());

        riddleCheckpointDTO.setRiddle(riddle.toDTO());

        return riddleCheckpointDTO;
    }
}
