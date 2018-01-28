package com.ccaroni.kreasport.race2;

import android.content.Context;
import android.location.Location;

/**
 * Created by Master on 28/01/2018.
 * Wrapper for the context that will receive all data through {@link LocationServicesHandler}
 */
public interface RaceContext {


    Context getContext();

    /**
     * Receives location through {@link LocationServicesHandler}.
     *
     * @param location the new location
     */
    void onLocationChanged(Location location);

    /**
     * Is triggered by {@link LocationServicesHandler}
     *
     * @param checkpointId the checkpoint associated to the geofence
     */
    void onGeofenceTriggered(String checkpointId);
}
