package com.ccaroni.kreasport.location;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;
import static com.ccaroni.kreasport.location.BaseLocationService.KEY_LAST_LOCATION;

public class LocationUtils {

    private static final String TAG = LocationUtils.class.getSimpleName();
    private static final String KEY_BASE = "com.ccaroni.kreasport." + LocationUtils.class.getSimpleName() + ".key.";

    public static final String KEY_LOCATION_PREFS_FILENAME = KEY_BASE + "location_prefs_filename";
    private static final String locationPrefsFilename = "locations";


    private final LocationUtilsSubscriber locationUtilsSubscriber;

    private Context context;

    private SharedPreferences locationPrefs;
    private Gson gson;

    // Global variable to prevent garbage collection
    private SharedPreferences.OnSharedPreferenceChangeListener locationPrefsListener;

    public LocationUtils(Context context) {
        this.context = context;

        if (context instanceof LocationUtilsSubscriber) {
            this.locationUtilsSubscriber = (LocationUtilsSubscriber) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement " + LocationUtilsSubscriber.class.getSimpleName());
        }

        locationPrefs = context.getSharedPreferences(locationPrefsFilename, MODE_PRIVATE);

        gson = new Gson();

        setupLocationPrefsListener();
    }

    /**
     * Request location updates. For now usses {@link GoogleLocationService}
     */
    public void startLocationUpdates() {
        Log.d(TAG, "request to start location updates. Using " + GoogleLocationService.class.getSimpleName());
        Intent locationServiceIntent = new Intent(context, GoogleLocationService.class);
        locationServiceIntent.putExtra(KEY_LOCATION_PREFS_FILENAME, locationPrefsFilename);
        context.startService(locationServiceIntent);
    }

    public void stopLocationUpdates() {
        Log.d(TAG, "stopping location updates");

        context.stopService(new Intent(context, GoogleLocationService.class));
    }

    private void setupLocationPrefsListener() {
        locationPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                String data = sharedPreferences.getString(key, "");

                if (!data.equals("")) {
                    Log.d(TAG, "received from location prefs: " + data);

                    Location location = gson.fromJson(data, Location.class);

                    locationUtilsSubscriber.onLocationChanged(location);
                }

            }
        };

        context.getSharedPreferences(locationPrefsFilename, MODE_PRIVATE).registerOnSharedPreferenceChangeListener(locationPrefsListener);
    }

    @SuppressWarnings({"MissingPermission"})
    public Location getLastKnownLocation() {
//        return LocationServices.getFusedLocationProviderClient(context).getLastLocation().getResult();

//        Use our own previously sourced data
        String locationJson = locationPrefs.getString(KEY_LAST_LOCATION, "");
        return gson.fromJson(locationJson, Location.class);
    }

    /**
     * This is the context that is using an instance of the class {@link LocationUtils}.
     */
    public interface LocationUtilsSubscriber {

        /**
         * Notifiy the subscriber that location has been updated
         *
         * @param location the new location
         */
        void onLocationChanged(Location location);
    }

}
