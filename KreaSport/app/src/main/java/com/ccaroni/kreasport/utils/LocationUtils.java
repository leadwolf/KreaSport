package com.ccaroni.kreasport.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.ccaroni.kreasport.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Master on 20/05/2017.
 */

/**
 * This class will handle all location request including registering and render the results available through getters.
 * It is initialized from an activity for a Context but on instantiation everything is handled internally.
 */
public class LocationUtils implements LocationListener {

    private static final String LOG = LocationUtils.class.getSimpleName();

    private LocationCommunicationInterface mLocationReceiver;
    private GoogleApiClient mGoogleApiClient;

    public LocationUtils(Context context, GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;

        if (context instanceof LocationCommunicationInterface) {
            this.mLocationReceiver = (LocationCommunicationInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement " + LocationCommunicationInterface.class.getSimpleName());
        }
    }

    @SuppressWarnings({"MissingPermission"})
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

    public interface LocationCommunicationInterface {

        /**
         * Any location update in {@link LocationUtils} calls this method.
         *
         * @param location
         */
        public void onLocationChanged(Location location);
    }
}
