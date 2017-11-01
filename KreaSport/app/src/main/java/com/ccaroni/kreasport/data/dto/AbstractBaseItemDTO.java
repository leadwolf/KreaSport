package com.ccaroni.kreasport.data.dto;

import com.ccaroni.kreasport.data.dao.AbstractBaseItemDAO;

/**
 * Created by Master on 01/11/2017.
 */

public abstract class AbstractBaseItemDTO<T extends AbstractBaseItemDAO> implements BaseDTO<T> {

    protected String id;
    protected String title;
    protected String description;
    protected Double latitude;
    protected Double longitude;
    protected Double altitude;

    public AbstractBaseItemDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    @Override
    public abstract T toDAO();
}
