package com.ccaroni.kreasport.race2;

import android.content.Context;
import android.location.Location;

/**
 * Created by Master on 28/01/2018.
 * Wrapper for the context that will receive all data through {@link AbstractRacingService}
 */
public abstract class RaceContext {

    private Context context;

    public RaceContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    /**
     * Receives location through {@link AbstractRacingService}.
     *
     * @param location the new location
     */
    public abstract void onLocationChanged(Location location);
}
