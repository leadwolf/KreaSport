package com.ccaroni.kreasport.race.services.geofence;


import com.ccaroni.kreasport.data.dummy.DummyCheckpoint;

/**
 * Created by Master on 03/02/2018.
 */

public interface IGeofenceUtil {

    /**
     * Adds a geofence to the location from the checkpoint
     *
     * @param dummyCheckpoint
     */
    @SuppressWarnings({"MissingPermission"})
    void addGeofence(DummyCheckpoint dummyCheckpoint);

    /**
     * Removes all previous geofences
     */
    void removePreviousGeofences();
}
