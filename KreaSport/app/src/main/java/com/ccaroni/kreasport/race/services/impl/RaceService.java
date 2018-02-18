package com.ccaroni.kreasport.race.services.impl;

import android.content.Intent;
import android.util.Log;

import com.ccaroni.kreasport.data.remote.Checkpoint;
import com.ccaroni.kreasport.race.events.GeofenceTriggered;
import com.ccaroni.kreasport.race.events.LocationChanged;
import com.ccaroni.kreasport.race.services.AbstractRaceService;
import com.ccaroni.kreasport.race.services.geofence.impl.GeofenceUtil;
import com.ccaroni.kreasport.race.services.location.impl.GoogleLocationService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Master on 02/02/2018.
 */

public class RaceService extends AbstractRaceService {

    private static final String TAG = RaceService.class.getSimpleName();


    private GeofenceUtil mGeofenceUtil;

    /**
     * The intent bound to start/stop {@link GoogleLocationService}
     */
    private Intent locationServiceIntent;


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
    public void addGeofence(Checkpoint checkpoint) {
        this.mGeofenceUtil.addGeofence(checkpoint);
    }

    @Override
    public void removeAllGeofences() {
        this.mGeofenceUtil.removePreviousGeofences();

    }

    /**
     * Calls {@link #startLocationUpdates()}
     */
    @Override
    protected void initLocationUpdates() {
        this.locationServiceIntent = new Intent(this, GoogleLocationService.class);
        startLocationUpdates();
    }

    @Override
    protected void initGeofenceServices() {
        this.mGeofenceUtil = new GeofenceUtil(this);
    }

    /**
     * Unregisters from {@link EventBus}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopLocationUpdates();
    }

    @Subscribe
    @Override
    protected void onLocationChanged(LocationChanged locationChanged) {
        // TODO
        // recording ?
        // YES
        //      save in RaceModel
        // NO
        //      do nothing

        Log.d(TAG, "onLocationChanged: received new location");

    }

    @Subscribe
    @Override
    protected void onGeofenceTriggered(GeofenceTriggered geofenceTriggered) {
        // TODO save in RaceModel
        Log.d(TAG, "onGeofenceTriggered: received geofence trigger");
    }

}
