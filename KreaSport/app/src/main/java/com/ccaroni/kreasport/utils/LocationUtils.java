package com.ccaroni.kreasport.utils;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

/**
 * Created by Master on 20/05/2017.<br>
 * This abstraction sets down what methods the implementation must contain to be able to act independently from the {@link Context} all while giving the necessary callbacks
 * (implementation verified via the constructor). The implementation is free to use whatever means to get the location as long as it passes through
 * {@link #onLocationChanged(Location)}.
 */
public abstract class LocationUtils implements LocationListener {

    private static final String LOG = LocationUtils.class.getSimpleName();

    /**
     * The {@link Context} associated is cast to {@link LocationCommunicationInterface}, i.e. this instance. Any communication between this class and the {@link Context} should
     * only and must be done through this interface.
     */
    protected LocationCommunicationInterface mLocationReceiver;

    /**
     * Ensures that the {@link Context} is instanceof {@link LocationCommunicationInterface}
     *
     * @param context the context that will use {@link #onLocationChanged(Location)}
     */
    public LocationUtils(Context context) {
        if (context instanceof LocationCommunicationInterface) {
            this.mLocationReceiver = (LocationCommunicationInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement " + LocationCommunicationInterface.class.getSimpleName());
        }
    }

    /**
     * General method invoked on location changed when creating requests with "this" as location listener.
     * <br>Just a simple call to {@link LocationCommunicationInterface#onLocationChanged(Location)} to notify the class that this is attached to.
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG, "received location update" + location);
        Log.d(LOG, "passing on to " + mLocationReceiver);

        mLocationReceiver.onLocationChanged(location);
    }

    public abstract Location getLastKnownLocation();

    /**
     * Stops calling for location updates.<br>
     * <p>
     * Depending on what implementation decided on, the location updates may only need to be called on a certain point in time in the lifecycle of the {@link Context}.
     */
    public abstract void stopLocationUpdates();

    /**
     * Starts calling whatever implementation decided on to give location updates. <br>
     * <p>
     * Depending on what implementation decided on, the location updates may only need to be called on a certain point in time in the lifecycle of the {@link Context}.
     */
    public abstract void startLocationUpdates();

    /**
     * This interface must be implemented by the {@link Context} associated. It defines how this class may send callbacks if the view needs to be updated accordingly.
     */
    public interface LocationCommunicationInterface {

        /**
         * Any location update in this should call this method to notify the view.
         *
         * @param location
         */
        void onLocationChanged(Location location);
    }
}
