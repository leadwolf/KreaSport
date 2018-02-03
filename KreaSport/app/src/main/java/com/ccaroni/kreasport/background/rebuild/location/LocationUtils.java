package com.ccaroni.kreasport.background.rebuild.location;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.ccaroni.kreasport.background.rebuild.location.impl.GoogleLocationService;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

@Deprecated
public class LocationUtils {

    private static final String TAG = LocationUtils.class.getSimpleName();
    private static final String KEY_BASE = "com.ccaroni.kreasport." + LocationUtils.class.getSimpleName() + ".key.";

    public static final String KEY_LOCATION_PREFS_FILENAME = KEY_BASE + "location_prefs_filename";
    private static final String locationPrefsFilename = "locations";


    private final LocationUtilsSubscriber locationUtilsSubscriber;

    private SharedPreferences locationPrefs;
    private Gson gson;

    // Global variable to prevent garbage collection
    private SharedPreferences.OnSharedPreferenceChangeListener locationPrefsListener;

    /**
     * Attaches the context to a {@link LocationUtilsSubscriber} field and sets up an {@link android.content.SharedPreferences.OnSharedPreferenceChangeListener} to listen to
     * location updates written by {@link GoogleLocationService}
     *
     * @param locationUtilsSubscriber the subscriber
     */
    public LocationUtils(LocationUtilsSubscriber locationUtilsSubscriber) {
        this.locationUtilsSubscriber = locationUtilsSubscriber;

        locationPrefs = locationUtilsSubscriber.getContext().getSharedPreferences(locationPrefsFilename, MODE_PRIVATE);

        gson = new Gson();

        setupLocationPrefsListener();
    }

    /**
     * Request location updates. For now usses {@link GoogleLocationService}
     */
    public void startLocationUpdates() {
        Log.d(TAG, "request to start location updates. Using " + GoogleLocationService.class.getSimpleName());
        Intent locationServiceIntent = new Intent(locationUtilsSubscriber.getContext(), GoogleLocationService.class);
        locationServiceIntent.putExtra(KEY_LOCATION_PREFS_FILENAME, locationPrefsFilename);
        locationUtilsSubscriber.getContext().startService(locationServiceIntent);
    }

    public void stopLocationUpdates() {
        Log.d(TAG, "stopping location updates");

        locationUtilsSubscriber.getContext().stopService(new Intent(locationUtilsSubscriber.getContext(), GoogleLocationService.class));
    }

    private void setupLocationPrefsListener() {
        locationPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                String data = sharedPreferences.getString(key, "");

                if (!data.equals("")) {
//                    Log.d(TAG, "received from location prefs: " + data);

                    Location location = gson.fromJson(data, Location.class);

                    locationUtilsSubscriber.onLocationChanged(location);
                }

            }
        };

        locationUtilsSubscriber.getContext().getSharedPreferences(locationPrefsFilename, MODE_PRIVATE).registerOnSharedPreferenceChangeListener(locationPrefsListener);
    }

    @SuppressWarnings({"MissingPermission"})
    public Location getLastKnownLocation() {
//        return LocationServices.getFusedLocationProviderClient(context).getLastLocation().getResult();

//        Use our own previously sourced data
        String locationJson = locationPrefs.getString(BaseLocationService.KEY_LAST_LOCATION, "");
        Location lastLocation = gson.fromJson(locationJson, Location.class);

        Log.d(TAG, "last known location: " + lastLocation);
        return lastLocation;
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

        Context getContext();
    }

}
