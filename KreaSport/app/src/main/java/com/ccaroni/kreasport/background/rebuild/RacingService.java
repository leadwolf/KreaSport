package com.ccaroni.kreasport.background.rebuild;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ccaroni.kreasport.background.rebuild.geofence.GeofenceTransitionsIntentService;
import com.ccaroni.kreasport.background.rebuild.geofence.GeofenceUtils;
import com.ccaroni.kreasport.background.rebuild.location.LocationUtils;

/**
 * Created by Master on 02/02/2018.
 */

public class RacingService extends Service implements IRaceService {

    private static final String TAG = RacingService.class.getSimpleName();


    private LocationUtils mLocationUtils;
    private GeofenceUtils mGeofenceUtils;
    private GeofenceReceiver geofenceReceiver;

    @Override
    public void startLocationUpdates() {
        // TODO

    }

    @Override
    public void stopLocationUpdates() {
        // TODO

    }

    @Override
    public void addGeofence(Location location) {
        // TODO

    }

    @Override
    public void removeAllGeofences() {
        // TODO

    }

    @Override
    public Notification createNotification() {
        // TODO
        return null;
    }

    @Override
    public void updateNotification() {
        // TODO

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // TODO set as foreground service
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
