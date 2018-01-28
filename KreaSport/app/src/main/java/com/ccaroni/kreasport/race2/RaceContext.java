package com.ccaroni.kreasport.race2;

import android.content.Context;
import android.content.ContextWrapper;
import android.location.Location;

/**
 * Created by Master on 28/01/2018.
 * Custom context that will receive all data through {@link AbstractRacingService}
 */
public abstract class RaceContext extends ContextWrapper {

    public RaceContext(Context base) {
        super(base);
    }

    /**
     * Receives location through {@link AbstractRacingService}.
     *
     * @param location the new location
     */
    public abstract void onLocationChanged(Location location);
}
