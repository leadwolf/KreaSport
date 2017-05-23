package com.ccaroni.kreasport.utils.impl;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.utils.LocationUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Master on 20/05/2017.
 */

/**
 * Implementation of {@link LocationUtils}. Uses {@link GoogleApiClient} to access the location.
 */
public class LocationUtilsImpl extends LocationUtils {

    private static final String LOG = LocationUtilsImpl.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;

    /**
     *
     * @param context
     * @param mGoogleApiClient the implementation this class uses for its location updates.
     */
    public LocationUtilsImpl(Context context, GoogleApiClient mGoogleApiClient) {
        super(context);
        this.mGoogleApiClient = mGoogleApiClient;
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public void startLocationUpdates() {
        Log.d(LOG, "starting location update request");

        // Create the location request
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.GEOLOCATION_UPDATE_INTERVAL)
                .setFastestInterval(Constants.GEOLOCATION_UPDATE_FASTEST_INTERVAL);

        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    @Override
    public void stopLocationUpdates() {
        Log.d(LOG, "stopping location updates");

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public Location getLastKnownLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }
}
