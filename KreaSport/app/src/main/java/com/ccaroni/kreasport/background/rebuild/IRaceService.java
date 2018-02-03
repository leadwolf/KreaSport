package com.ccaroni.kreasport.background.rebuild;

import android.app.Notification;
import android.location.Location;

/**
 * Created by Master on 02/02/2018.
 */

public interface IRaceService {

    void startLocationUpdates();

    void stopLocationUpdates();

    void addGeofence(Location location);

    void removeAllGeofences();

    /**
     * @return the updated notification
     */
    Notification updateRelevantNotificationFields();

    /**
     * Creates a new notification and sends it out
     */
    void sendUpdatedNotification();

}
