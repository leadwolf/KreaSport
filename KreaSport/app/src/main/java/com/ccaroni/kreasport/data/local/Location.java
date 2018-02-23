package com.ccaroni.kreasport.data.local;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by Master on 18/02/2018.
 */
@Entity
public class Location {

    @Id
    private long id;

    private ToOne<Record> record;

    private double latitude;
    private double longitude;
    private double altitude;

    public Location() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public ToOne<Record> getRecord() {
        return record;
    }

    public void setRecord(ToOne<Record> record) {
        this.record = record;
    }
}
