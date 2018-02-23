package com.ccaroni.kreasport.explore.events;

import android.location.Location;

/**
 * Created by Master on 09/02/2018.
 */

public class LocationChanged {

    public final Location location;

    public LocationChanged(Location location) {
        this.location = location;
    }
}
