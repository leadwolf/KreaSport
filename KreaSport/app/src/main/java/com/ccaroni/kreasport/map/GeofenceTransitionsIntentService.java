package com.ccaroni.kreasport.map;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by Master on 24/04/2017.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String LOG = GeofenceTransitionsIntentService.class.getSimpleName();


    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG, "got intent");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent == null)
            return;
        if (geofencingEvent.hasError())
            Log.d(LOG, "error in geofence: " + geofencingEvent.getErrorCode());

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            for (Geofence geofence : triggeringGeofences) {
                Log.d(LOG, "trigger for geofence " + geofence);
            }

        } else {
            Log.d(LOG, "invalid transition type");
        }
    }
}
