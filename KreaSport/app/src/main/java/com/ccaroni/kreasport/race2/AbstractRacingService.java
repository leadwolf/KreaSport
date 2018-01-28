package com.ccaroni.kreasport.race2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ccaroni.kreasport.background.geofence.GeofenceTransitionsIntentService;
import com.ccaroni.kreasport.background.geofence.GeofenceUtils;
import com.ccaroni.kreasport.background.location.LocationUtils;

import static com.ccaroni.kreasport.background.geofence.GeofenceTransitionsIntentService.GEOFENCE_TRIGGERED;

/**
 * Created by Master on 28/01/2018.
 * This class will handle all the actual racing
 */
public class AbstractRacingService implements LocationUtils.LocationUtilsSubscriber {

    private static final String TAG = AbstractRacingService.class.getSimpleName();


    private LocationUtils mLocationUtils;
    private GeofenceUtils mGeofenceUtils;
    private GeofenceReceiver geofenceReceiver;

    private RaceContext raceContext;

    /**
     * @param raceContext the raceContext all utilities may subscribe onto
     */
    public AbstractRacingService(RaceContext raceContext) {
        this.raceContext = raceContext;
        this.initLocationActions();
    }

    /**
     * Sets up {@link #mLocationUtils} to receive callbacks, {@link #mGeofenceUtils} to create/delete new geofences
     * and {@link #geofenceReceiver} to receive broadcasts sent out with {@link GeofenceTransitionsIntentService#GEOFENCE_TRIGGERED}
     */
    private void initLocationActions() {
        mLocationUtils = new LocationUtils(this.raceContext.getContext());
        // don't need to call start since it was already started in ExploreActivity


        // TODO make sure we can cancel geofences created in ExploreActivity from this instance
        mGeofenceUtils = new GeofenceUtils(this.raceContext.getContext());

        geofenceReceiver = new GeofenceReceiver();
        LocalBroadcastManager.getInstance(this.raceContext.getContext()).registerReceiver(geofenceReceiver, new IntentFilter(GEOFENCE_TRIGGERED));
    }

    /**
     * Override this to act on location updates
     *
     * @param location the new location
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "received location from " + LocationUtils.class.getSimpleName() + ": " + location);
        // TODO save new location

        this.raceContext.onLocationChanged(location);
    }

    public void destroy() {
        LocalBroadcastManager.getInstance(this.raceContext.getContext()).unregisterReceiver(geofenceReceiver);
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

//            Log.d(TAG, "received geofence broadcast for checkpoint: " + checkpointId);
            raceContext.onGeofenceTriggered(checkpointId);
        }
    }

    public void replaceRaceContext(RaceContext raceContext) {
        if (raceContext == null) {
            throw new IllegalArgumentException("Cannot accept null context");
        }
        this.destroy();
        this.raceContext = raceContext;
        this.initLocationActions();
    }

}
