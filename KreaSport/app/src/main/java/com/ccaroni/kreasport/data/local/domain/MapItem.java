package com.ccaroni.kreasport.data.local.domain;

import com.ccaroni.kreasport.data.model.IMapItem;

import io.objectbox.annotation.BaseEntity;
import io.objectbox.annotation.Id;

/**
 * Created by Master on 10/02/2018.
 */
@BaseEntity
public class MapItem implements IMapItem {

    @Id
    private long id;
    private String title;
    private String description;
    private double latitude;
    private double longitude;

    public MapItem() {
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
