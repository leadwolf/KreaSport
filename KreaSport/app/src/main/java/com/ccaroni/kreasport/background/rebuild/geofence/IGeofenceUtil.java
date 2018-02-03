package com.ccaroni.kreasport.background.rebuild.geofence;

import com.ccaroni.kreasport.data.realm.RealmCheckpoint;

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
    void addGeofence(RealmCheckpoint checkpoint);

    /**
     * Removes all previous geofences
     */
    void removePreviousGeofences();
}
