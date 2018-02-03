package com.ccaroni.kreasport.background.rebuild.location;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

    private static final String TAG = BaseLocationService.class.getSimpleName();

    protected static final String KEY_BASE = APP_PACKAGE + TAG + ".key.";
    public static final String KEY_LAST_LOCATION = KEY_BASE + "last_location";
    private static final String ACTION_LOCATION_UPDATED = APP_PACKAGE + "intent." + "location_updated";

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
    }

    /**
     * The broadcast receiver that will listen to location updates. You need to register it with the broadcast manager.
     * It will callback with the location to the {@link LocationListener} passed in the constructor
     */
    class LocationReceiver extends BroadcastReceiver {

        private LocationListener locationListener;

        /**
         * @param locationListener the instance that will receive location callbacks
         */
        public LocationReceiver(LocationListener locationListener) {
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
    }

}
