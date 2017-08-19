package com.ccaroni.kreasport.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ccaroni.kreasport.service.geofence.GeofenceTransitionsIntentService;

/**
 * Created by Master on 19/08/2017.
 */

public class RacingService extends Service {

    private static final String TAG = RacingService.class.getSimpleName();


    private GeofenceReceiver geofenceReceiver;

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

        initGeofenceReceiver();


    }

    private void initGeofenceReceiver() {
        geofenceReceiver = new GeofenceReceiver();

        IntentFilter filter = new IntentFilter(GeofenceTransitionsIntentService.GEOFENCE_TRIGGERED);
        LocalBroadcastManager.getInstance(this).registerReceiver(geofenceReceiver, filter);
    }

    class GeofenceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String checkpointId = intent.getStringExtra(GeofenceTransitionsIntentService.KEY_GEOFENCE_ID);
            if (checkpointId == null) {
                throw new IllegalArgumentException("Received intent for geofenceReceiver with no checkpoint associated");
            }

            Log.d(TAG, "received geofence broadcast for checkpoint: " + checkpointId);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(geofenceReceiver);
    }
}
