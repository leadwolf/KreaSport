package com.ccaroni.kreasport.explore.services;

import android.app.Notification;

import com.ccaroni.kreasport.data.remote.Checkpoint;

/**
 * Created by Master on 02/02/2018.
 */

public interface IExploreService {

    void startLocationUpdates();

    void stopLocationUpdates();

    void addGeofence(Checkpoint checkpoint);

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
