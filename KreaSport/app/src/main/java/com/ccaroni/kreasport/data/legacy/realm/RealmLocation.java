package com.ccaroni.kreasport.data.legacy.realm;

import android.location.Location;

import io.realm.RealmObject;

/**
 * Created by Master on 28/10/2017.
 */

public class RealmLocation extends RealmObject {

    private double latitude;
    private double longitude;
    private double altitude;

    public RealmLocation() {
    }

    public RealmLocation(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
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

    public static RealmLocation fromLocation(Location location) {
        return new RealmLocation()
                .withLatitude(location.getLatitude())
                .withLongitude(location.getLongitude())
                .withAltitude(location.getAltitude());
    }

    private RealmLocation withAltitude(double altitude) {
        this.altitude = altitude;
        return this;
    }

    private RealmLocation withLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    private RealmLocation withLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Location toLocation() {
        Location location = new Location("realm_location");
        location.setLongitude(getLongitude());
        location.setLatitude(getLatitude());
        location.setAltitude(getAltitude());
        return location;
    }

    @Override
    public String toString() {
        return "RealmLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                '}';
    }
}
