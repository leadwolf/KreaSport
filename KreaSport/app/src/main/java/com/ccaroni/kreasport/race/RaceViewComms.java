package com.ccaroni.kreasport.race;

import android.location.Location;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Master on 20/08/2017.
 */

public interface RaceViewComms {

    /**
     * The view's representation to go to the user's location is bound to this method. This method is to be implemented by the {@link android.app.Activity} since we need a
     * context to create a dialog box
     */
    void onMyLocationClicked();

    /**
     * The view's representation to stop the race is bound to this method. This method is to be implemented by the {@link android.app.Activity} since we need a
     * context to create a dialog box
     */
    void askStopConfirmation();

    void toast(String message);

    void startChronometer(long timeStart);

    Location getLastKnownLocation();

    boolean needToAnimateToStart(GeoPoint startPoint);

    void stopChronometer();
}
