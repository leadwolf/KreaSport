package com.ccaroni.kreasport.data.secondary.dto.impl;

import com.ccaroni.kreasport.data.secondary.dto.ICheckpointDTO;
import com.ccaroni.kreasport.data.secondary.dto.IRaceDTO;
import com.ccaroni.kreasport.data.secondary.realm.ICheckpointDAO;
import com.ccaroni.kreasport.data.secondary.realm.IRaceDAO;
import com.ccaroni.kreasport.data.secondary.realm.impl.RaceDAO;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by Master on 01/11/2017.
 */

public class RaceDTO<T extends ICheckpointDTO<?>> implements IRaceDTO<T> {

    protected String id;
    protected String title;
    protected String description;
    protected Double latitude;
    protected Double longitude;
    protected Double altitude;

    private List<T> checkpoints;

    public RaceDTO() {
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
    public List<T> getCheckpoints() {
        return checkpoints;
    }

    @Override
    public void setCheckpoints(List<T> checkpoints) {
        this.checkpoints = checkpoints;
    }

    @Override
    public IRaceDAO<?> toDAO() {
        IRaceDAO abstractRaceDAO = new RaceDAO();
        abstractRaceDAO.setServerID(getId());
        abstractRaceDAO.setTitle(getTitle());
        abstractRaceDAO.setDescription(getDescription());
        abstractRaceDAO.setLatitude(getLatitude());
        abstractRaceDAO.setLongitude(getLongitude());
        abstractRaceDAO.setAltitude(getAltitude());

        abstractRaceDAO.setCheckpoints(checkpointsToDAOList(checkpointsToDAO()));

        return abstractRaceDAO;
    }

    @Override
    public List<ICheckpointDAO<?>> checkpointsToDAO() {
        List<ICheckpointDAO<?>> checkpointDAOS = new ArrayList<>();
        for (T checkpointDTO : getCheckpoints()) {
            checkpointDAOS.add(checkpointDTO.toDAO());
        }
        return checkpointDAOS;
    }

    public RealmList<ICheckpointDAO<?>> checkpointsToDAOList(List<ICheckpointDAO<?>> iCheckpointDAOS) {
        RealmList<ICheckpointDAO<?>> checkpointDAOSList = new RealmList<>();
        checkpointDAOSList.addAll(iCheckpointDAOS);
        return checkpointDAOSList;
    }

}
