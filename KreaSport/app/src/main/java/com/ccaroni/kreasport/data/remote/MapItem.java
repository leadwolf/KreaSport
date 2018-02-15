package com.ccaroni.kreasport.data.remote;

import com.ccaroni.kreasport.data.model.IMapItem;

/**
 * Created by Master on 09/02/2018.
 */

public class MapItem implements IMapItem {

    long id;
    String title;
    String description;
    double latitude;
    double longitude;

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
