package com.ccaroni.kreasport.race.services.location;

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

import com.ccaroni.kreasport.race.events.LocationChanged;

import org.greenrobot.eventbus.EventBus;

import static com.ccaroni.kreasport.utils.Constants.APP_PACKAGE;

/**
 * Created by Master on 19/08/2017.
 */

public abstract class BaseLocationService extends Service {

    public static final String ACTION_LOCATION_UPDATED = APP_PACKAGE + "intent." + "location_updated";
    private static final String TAG = BaseLocationService.class.getSimpleName();
    protected static final String KEY_BASE = APP_PACKAGE + TAG + ".key.";
    public static final String KEY_LAST_LOCATION = KEY_BASE + "last_location";

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

    protected void notifySubscribers(Location location) {
        EventBus.getDefault().post(new LocationChanged(location));
    }


}
