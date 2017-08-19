package com.ccaroni.kreasport.location;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import static com.ccaroni.kreasport.view.activities.ExploreActivity.KEY_LOCATION_PREFS_FILENAME;

/**
 * Created by Master on 19/08/2017.
 */

public abstract class BaseLocationService extends Service {

    private static final String TAG = BaseLocationService.class.getSimpleName();
    protected static final String KEY_BASE =  "com.ccaroni.kreasport." + TAG + ".key.";
    protected static final String KEY_LAST_LOCATION = KEY_BASE + "last_location";

    protected SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String prefsFileName = intent.getStringExtra(KEY_LOCATION_PREFS_FILENAME);
        sharedPreferences = getSharedPreferences(prefsFileName, MODE_PRIVATE);
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

        gson = new Gson();

        startLocationUpdates();
    }

    protected abstract void startLocationUpdates();

    protected void writeLocation(Location location) {

        String locationString = gson.toJson(location, Location.class);

        sharedPreferences.edit()
                .putString(KEY_LAST_LOCATION, locationString)
                .apply();
    }

}
