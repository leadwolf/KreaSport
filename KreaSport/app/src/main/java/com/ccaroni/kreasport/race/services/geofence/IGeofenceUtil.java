package com.ccaroni.kreasport.race.services.geofence;


import com.ccaroni.kreasport.data.dto.Checkpoint;

/**
 * Created by Master on 03/02/2018.
 */

public interface IGeofenceUtil {

    /**
     * Adds a geofence to the location from the checkpoint
     *
     * @param checkpoint
     */
    @SuppressWarnings({"MissingPermission"})
    void addGeofence(Checkpoint checkpoint);

    /**
     * Removes all previous geofences
     */
    void removePreviousGeofences();
}
