package com.ccaroni.kreasport.data.model;

import android.location.Location;

import java.util.List;

/**
 * Created by Master on 09/02/2018.
 */

public interface IRecord extends Data {

    long getId();

    long getRaceId();

    String getUserId();

    long getTimeExpired();

    String getDateTime();

    List<Location> getPathToDTO();
}
