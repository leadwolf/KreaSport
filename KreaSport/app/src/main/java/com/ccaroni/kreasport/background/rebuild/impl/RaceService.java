package com.ccaroni.kreasport.background.rebuild.impl;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.ccaroni.kreasport.background.rebuild.AbstractRaceService;
import com.ccaroni.kreasport.background.rebuild.geofence.GeofenceTransitionsIntentService;
import com.ccaroni.kreasport.background.rebuild.geofence.impl.GeofenceUtil;
import com.ccaroni.kreasport.background.rebuild.location.BaseLocationService;
import com.ccaroni.kreasport.background.rebuild.location.impl.GoogleLocationService;

/**
 * Created by Master on 02/02/2018.
 */

public class RaceService extends AbstractRaceService implements BaseLocationService.LocationListener, GeofenceTransitionsIntentService.GeofenceListener {

    private static final String TAG = RaceService.class.getSimpleName();


    private GeofenceUtil mGeofenceUtil;
    private GeofenceTransitionsIntentService.GeofenceReceiver geofenceReceiver;

    /**
     * The intent bound to start/stop {@link GoogleLocationService}
     */
    private Intent locationServiceIntent;

    // Global to avoid GC
    private BaseLocationService.LocationReceiver locationReceiver;


    /**
     * Starts the service with {@link #locationServiceIntent}
     */
    @Override
    public void startLocationUpdates() {
        startService(this.locationServiceIntent);
    }

    /**
     * Stops the service with {@link #locationServiceIntent}
     */
    @Override
    public void stopLocationUpdates() {
        stopService(this.locationServiceIntent);

    }

    @Override
    public void addGeofence(Location location) {
        // TODO

    }

    @Override
    public void removeAllGeofences() {
        // TODO

    }


    /**
     * Registers {@link #locationReceiver} and calls {@link #startLocationUpdates()}
     */
    @Override
    protected void initLocationUpdates() {
        this.locationReceiver = BaseLocationService.getLocationReceiver(this);

        this.locationServiceIntent = new Intent(this, GoogleLocationService.class);
        startLocationUpdates();
    }

    @Override
    protected void initGeofenceServices() {
        this.mGeofenceUtil = new GeofenceUtil(this);

        this.geofenceReceiver = GeofenceTransitionsIntentService.getLocationReceiver(this);
    }

    /**
     * Unregisters {@link #geofenceReceiver}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        this.geofenceReceiver.unregisterFromLocalBroadcastManager(this);

        this.stopLocationUpdates();
        this.locationReceiver.unregisterFromLocalBroadcastManager(this);
    }

    @Override
    public void onLocationReceived(Location location) {
        // TODO
    }

    @Override
    public void onGeofenceTriggered(String geofenceID) {
        // TODO

    }

    @Override
    public Context getContext() {
        return this;
    }

}
