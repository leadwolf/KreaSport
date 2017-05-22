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
 * This class will handle all location request including registering and render the results available through getters.
 * It is initialized from an activity for a Context but on instantiation everything is handled internally.
 */
public class LocationUtilsImpl extends LocationUtils {

    private static final String LOG = LocationUtilsImpl.class.getSimpleName();

    private LocationCommunicationInterface mLocationReceiver;
    private GoogleApiClient mGoogleApiClient;

    public LocationUtilsImpl(Context context, GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;

        if (context instanceof LocationCommunicationInterface) {
            this.mLocationReceiver = (LocationCommunicationInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement " + LocationCommunicationInterface.class.getSimpleName());
        }
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

    /**
     * General method invoked on location changed when creating requests with "this" as location listener.
     * <br>Just a simple call to {@link LocationCommunicationInterface#onLocationChanged(Location)} to notify the class that this is attached to.
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG, "received location update" + location);
        Log.d(LOG, "passing on to " + mLocationReceiver);

        mLocationReceiver.onLocationChanged(location);
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public Location getLastKnownLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    public interface LocationCommunicationInterface {

        /**
         * Any location update in {@link LocationUtilsImpl} calls this method.
         *
         * @param location
         */
        public void onLocationChanged(Location location);
    }
}
