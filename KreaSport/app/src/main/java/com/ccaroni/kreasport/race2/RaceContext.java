package com.ccaroni.kreasport.race2;

import android.content.Context;
import android.location.Location;

/**
 * Created by Master on 28/01/2018.
 * Wrapper for the context that will receive all data through {@link AbstractRacingService}
 */
public interface RaceContext {


    Context getContext();

    /**
     * Receives location through {@link AbstractRacingService}.
     *
     * @param location the new location
     */
    void onLocationChanged(Location location);

    /**
     * Is triggered by {@link AbstractRacingService}
     *
     * @param checkpointId the checkpoint associated to the geofence
     */
    void onGeofenceTriggered(String checkpointId);
}
