package com.ccaroni.kreasport.data.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Index;

/**
 * Created by Master on 01/11/2017.
 */

@Entity
public abstract class AbstractBaseItemDAO {

    @Index
    protected String serverID;
    protected String title;
    protected String description;
    protected double latitude;
    protected double longitude;
    protected double altitude;

    public AbstractBaseItemDAO() {
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }
}
