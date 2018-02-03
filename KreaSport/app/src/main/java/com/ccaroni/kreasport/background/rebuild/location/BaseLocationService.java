package com.ccaroni.kreasport.background.rebuild.location;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static com.ccaroni.kreasport.utils.Constants.APP_PACKAGE;

/**
 * Created by Master on 19/08/2017.
 */

public abstract class BaseLocationService extends Service {

    public static final String ACTION_LOCATION_UPDATED = APP_PACKAGE + "intent." + "location_updated";
    private static final String TAG = BaseLocationService.class.getSimpleName();
    protected static final String KEY_BASE = APP_PACKAGE + TAG + ".key.";
    public static final String KEY_LAST_LOCATION = KEY_BASE + "last_location";

    /**
     * @param locationListener the instance that will receive location callbacks and provide the {@link LocalBroadcastManager}
     * @return a {@link LocationReceiver} that is registered with {@link LocalBroadcastManager} (from the context of locationListener) that will callback with new locations
     * to locationListener
     */
    public static LocationReceiver getLocationReceiver(LocationListener locationListener) {
        LocationReceiver locationReceiver = new LocationReceiver(locationListener);
        locationReceiver.registerWithLocalBroadcastManager(locationListener.getContext());
        return locationReceiver;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startLocationUpdates();
    }

    /**
     * Implements the location requests
     */
    protected abstract void startLocationUpdates();

    protected void broadcastLocation(Location location) {
        Intent intent = new Intent(ACTION_LOCATION_UPDATED);
        intent.putExtra(KEY_LAST_LOCATION, location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Implement this to receive callbacks from {@link LocationReceiver}
     */
    public interface LocationListener {
        void onLocationReceived(Location location);

        Context getContext();
    }

    /**
     * The broadcast receiver that will listen to location updates. You need to register it with the broadcast manager.
     * It will callback with the location to the {@link LocationListener} passed in the constructor
     */
    public static class LocationReceiver extends BroadcastReceiver {

        private LocationListener locationListener;

        /**
         * @param locationListener the instance that will receive location callbacks
         */
        private LocationReceiver(LocationListener locationListener) {
            this.locationListener = locationListener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(KEY_LAST_LOCATION);
            if (location == null) {
                Log.d(TAG, "received location broadcast but could not deserialize location");
                return;
            }

            this.locationListener.onLocationReceived(location);
        }

        /**
         * Registers this instance with the {@link LocalBroadcastManager}
         *
         * @param context the context to get the {@link LocalBroadcastManager} with
         */
        public void registerWithLocalBroadcastManager(Context context) {
            LocalBroadcastManager.getInstance(context).registerReceiver(this, new IntentFilter(ACTION_LOCATION_UPDATED));
        }

        /**
         * Unregisters this instance from the LBC to avoid memory leaks
         * @param locationListener the listener that will provide the context to access the LBC
         */
        public void unregisterFromLocalBroadcastManager(LocationListener locationListener) {
            LocalBroadcastManager.getInstance(locationListener.getContext()).unregisterReceiver(this);
        }
    }
}
