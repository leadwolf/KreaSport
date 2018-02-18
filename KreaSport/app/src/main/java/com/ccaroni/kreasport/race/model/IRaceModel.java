package com.ccaroni.kreasport.race.model;

import android.location.Location;

import com.ccaroni.kreasport.race.exception.IllegalRaceStateException;

/**
 * Created by Master on 18/02/2018.
 */

public interface IRaceModel {


    /**
     * @param raceId the id of the race
     * @throws IllegalRaceStateException if an error occurred while trying to start recording
     */
    void requestStartRace(long raceId) throws IllegalRaceStateException;

    /**
     * @throws IllegalRaceStateException if an error occurred while trying to stop the recording
     */
    void requestStopRace() throws IllegalRaceStateException;

    void updateLocation(Location location);

    void triggerGeofence(long id);

}
