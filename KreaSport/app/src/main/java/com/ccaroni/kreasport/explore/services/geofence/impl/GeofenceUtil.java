package com.ccaroni.kreasport.explore.services.geofence.impl;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ccaroni.kreasport.data.remote.Checkpoint;
import com.ccaroni.kreasport.explore.services.geofence.GeofenceErrorMessages;
import com.ccaroni.kreasport.explore.services.geofence.GeofenceTransitionsIntentService;
import com.ccaroni.kreasport.explore.services.geofence.IGeofenceUtil;
import com.ccaroni.kreasport.utils.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

/**
 * Created by Master on 19/08/2017.
 */

public class GeofenceUtil implements OnCompleteListener<Void>, IGeofenceUtil {


    private static final String TAG = GeofenceUtil.class.getSimpleName();
    /**
     * Provides access to the Geofence API.
     */
    private GeofencingClient mGeofenceClient;

    /**
     * The list of geofences used in this sample.
     */
    private ArrayList<Geofence> mGeofenceList;

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    private Context context;

    public GeofenceUtil(Context context) {
        this.context = context;
        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<>();

        // Initially set the PendingIntent used in addGeofence() and removeGeofences() to null.
        mGeofencePendingIntent = null;

        mGeofenceClient = LocationServices.getGeofencingClient(context);
    }


    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     *
     * @param checkpoint
     */
    private GeofencingRequest getGeofenceRequest(Checkpoint checkpoint) {
        Log.d(TAG, "building geofence request for checkpoint: " + checkpoint.getId() + " " + checkpoint.getTitle());

        if (checkpoint.getId() == 0) {

            GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

            // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
            // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
            // is already inside that geofence.
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

            // Add the geofences to be monitored by geofencing service.
            builder.addGeofence(
                    new Geofence.Builder()
                            .setRequestId(Long.toString(checkpoint.getId()))

                            .setCircularRegion(
                                    checkpoint.getLatitude(),
                                    checkpoint.getLongitude(),
                                    Constants.GEOFENCE_RADIUS_METERS
                            )
                            .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_MILLISECONDS)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL)
                            .setLoiteringDelay(Constants.GEOFENCE_LOITERING_DELAY)
                            .build()
            );

            // Return a GeofencingRequest.
            return builder.build();
        } else {
            Log.d(TAG, "checkpoint to trigger does not exist");
            return null;
        }
    }

    /**
     * Entry point to add a geofence
     *
     * @param checkpoint
     */
    @Override
    @SuppressWarnings({"MissingPermission"})
    public void addGeofence(Checkpoint checkpoint) {
        mGeofenceClient.addGeofences(getGeofenceRequest(checkpoint), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }


    /**
     * Runs when the result of calling {@link IGeofenceUtil#addGeofence(Checkpoint)} and/or {@link #removePreviousGeofences()}
     * is available.
     *
     * @param task the resulting Task, containing either a result or error.
     */
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "geofence addition/removal success!");
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(context, task.getException());
            Log.w(TAG, errorMessage);
        }
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofence() and removeGeofences().
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * removes all previous geofences since {@link #getGeofencePendingIntent()} uses {@link PendingIntent#FLAG_UPDATE_CURRENT}
     */
    @Override
    public void removePreviousGeofences() {
        mGeofenceClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(this);
    }
}
