package com.ccaroni.kreasport.background.rebuild.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ccaroni.kreasport.background.rebuild.AbstractRaceService;
import com.ccaroni.kreasport.background.rebuild.geofence.GeofenceTransitionsIntentService;
import com.ccaroni.kreasport.background.rebuild.geofence.GeofenceUtils;
import com.ccaroni.kreasport.background.rebuild.location.BaseLocationService;
import com.ccaroni.kreasport.background.rebuild.location.GoogleLocationService;

/**
 * Created by Master on 02/02/2018.
 */

public class RaceService extends AbstractRaceService implements BaseLocationService.LocationListener {

    private static final String TAG = RaceService.class.getSimpleName();


    private GeofenceUtils mGeofenceUtils;
    private GeofenceReceiver geofenceReceiver;

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
        // TODO

    }

    /**
     * Unregisters {@link #geofenceReceiver}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(geofenceReceiver);

        this.stopLocationUpdates();
        this.locationReceiver.unregisterFromLocalBroadcastManager(this);
    }

    @Override
    public void onLocationReceived(Location location) {
        // TODO
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * Custom BroadcastReceiver to act upon the reception of a broadcast from {@link GeofenceTransitionsIntentService#GEOFENCE_TRIGGERED}
     */
    class GeofenceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String checkpointId = intent.getStringExtra(GeofenceTransitionsIntentService.KEY_GEOFENCE_ID);
            if (checkpointId == null) {
                throw new IllegalArgumentException("Received intent for geofenceReceiver with no checkpoint associated");
            }

            Log.d(TAG, "received geofence broadcast for checkpoint: " + checkpointId);

            // TODO
        }
    }
}
