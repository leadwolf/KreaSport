package com.ccaroni.kreasport.background.rebuild;

import android.location.Location;

/**
 * Created by Master on 02/02/2018.
 */

public interface IRaceService {

    void startLocationUpdates();

    void stopLocationUpdates();

    void addGeofence(Location location);

    void removeAllGeofences();

}
