package com.ccaroni.kreasport.utils;

import android.location.Location;

import com.google.android.gms.location.LocationListener;

/**
 * Created by Master on 20/05/2017.
 */

public abstract class LocationUtils implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {

    }

    public abstract Location getLastKnownLocation();

    public abstract void stopLocationUpdates();

    public abstract void startLocationUpdates();
}
