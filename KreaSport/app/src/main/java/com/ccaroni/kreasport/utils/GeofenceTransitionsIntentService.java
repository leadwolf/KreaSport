package com.ccaroni.kreasport.utils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ccaroni.kreasport.view.activities.ExploreActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by Master on 24/04/2017.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String LOG = GeofenceTransitionsIntentService.class.getSimpleName();

    public static final String KEY_GEOFENCE_ID = "com.ccaroni.kreasport." + GeofenceTransitionsIntentService.class.getSimpleName() + ".key.geofence_id";


    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG, "got intent");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent == null)
            return;
        if (geofencingEvent.hasError()) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode());
            Log.e(LOG, errorMsg);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            for (Geofence geofence : triggeringGeofences) {
                Log.d(LOG, "sending geofence to " + ExploreActivity.class.getSimpleName() + " : " + geofence.getRequestId());

                Intent resendIntent = new Intent(Constants.GEOFENCE_RECEIVER_ID); //Send to the receiver listening for this in ExploreActivity
                resendIntent.putExtra(KEY_GEOFENCE_ID, geofence.getRequestId());
                LocalBroadcastManager.getInstance(this).sendBroadcast(resendIntent);
            }

        } else {
            Log.d(LOG, "invalid transition type");
        }
    }

    // Handle errors
    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }
}
