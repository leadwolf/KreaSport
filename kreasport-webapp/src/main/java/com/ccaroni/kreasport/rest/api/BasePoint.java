package com.ccaroni.kreasport.rest.api;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Master on 04/04/2017.
 */
@Entity
public class BasePoint {

    @Id
    ObjectId id;
    String title;
    String description;
    double latitude;
    double longitude;

    public BasePoint(ObjectId id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public BasePoint() {

    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
