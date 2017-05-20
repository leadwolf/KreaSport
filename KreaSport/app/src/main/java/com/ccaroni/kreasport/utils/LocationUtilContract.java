package com.ccaroni.kreasport.utils;

import android.location.Location;

import com.google.android.gms.location.LocationListener;

/**
 * Created by Master on 20/05/2017.
 */

public abstract class LocationUtilContract implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {

    }

    public abstract Location getLastKnownLocation();
}
