package com.ccaroni.kreasport.explore.services.location.impl;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ccaroni.kreasport.explore.services.location.BaseLocationService;
import com.ccaroni.kreasport.utils.Constants;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by Master on 02/07/2017.
 */

public class GoogleLocationService extends BaseLocationService {

    public static final String REQUIRES_LOCATION_SETTINGS_PROMPT = KEY_BASE + "requires_location_settings_prompt";
    public static final String KEY_LOCATION_SETTINGS__RESOLUTION_PI = KEY_BASE + "location_settings_request_pending_intent";

    private static final String TAG = GoogleLocationService.class.getSimpleName();


    private FusedLocationProviderClient mFusedLocationClient;

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;


    @SuppressWarnings({"MissingPermission"})
    @Override
    protected void startLocationUpdates() {
        buildRequest();
        verifyLocationSettings();

        this.mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "location updates request SUCCESS");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "location updates request FAILURE");
                        Log.d(TAG, e.toString());
                    }
                });
    }

    /**
     * Builds the location requests, callbacks and settings verifications
     */
    private void buildRequest() {
        // Using the new 11.0.0 location api without a GoogleApiClient
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        this.mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    /**
     * Builds {@link #mLocationCallback} to receive the location updates
     */
    private void createLocationCallback() {
        this.mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, "got location result: " + location.toString());

                    notifySubscribers(location);
                }
            }
        };
    }

    /**
     * Builds {@link #mLocationRequest} to request location updates
     */
    private void createLocationRequest() {
        this.mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.GEOLOCATION_UPDATE_INTERVAL)
                .setFastestInterval(Constants.GEOLOCATION_UPDATE_FASTEST_INTERVAL);

    }

    /**
     * Builds {@link #mLocationSettingsRequest} to request the necessary permissions to execute {@link #mLocationRequest}
     */
    private void buildLocationSettingsRequest() {
        this.mLocationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .build();
    }

    /**
     * Verifies that the user has given us the appropriate permissions for our {@link #mLocationRequest}
     */
    @SuppressWarnings({"MissingPermission"})
    private void verifyLocationSettings() {
        // Begin by checking if the device has the necessary location settings.
        Log.d(TAG, "checking if google play services is compatible with our location request");
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getOnSuccessListener())
                .addOnFailureListener(getOnFailureListener());
    }

    /**
     * @return the listener that will start the location updates once the necessary settings have been verified as available
     */
    @SuppressWarnings({"MissingPermission"})
    @NonNull
    private OnSuccessListener<LocationSettingsResponse> getOnSuccessListener() {
        return new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.i(TAG, "All location settings are satisfied, starting updates");

                //noinspection MissingPermission
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback, Looper.myLooper());
            }
        };
    }

    /**
     * @return the listener that will attempt to resolve the settings error
     */
    @NonNull
    private OnFailureListener getOnFailureListener() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        attemptResolution((ResolvableApiException) e);
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        onLocationSettingsNonResolvable();
                }
            }
        };
    }

    /**
     * Broadcasts the request to modify the user settings with the action: {@link #REQUIRES_LOCATION_SETTINGS_PROMPT} and key {@link #KEY_LOCATION_SETTINGS__RESOLUTION_PI}
     *
     * @param resolvableException the exception that contains the possible resolutions
     */
    private void attemptResolution(@NonNull ResolvableApiException resolvableException) {
        Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                "location settings ");
        // Show the dialog by calling startResolutionForResult(), and check the
        // result in onActivityResult().
        PendingIntent settingsRequestPI = resolvableException.getResolution();

        Intent parentIntent = new Intent(REQUIRES_LOCATION_SETTINGS_PROMPT)
                .putExtra(KEY_LOCATION_SETTINGS__RESOLUTION_PI, settingsRequestPI);

        Log.d(TAG, "broadcasting location settings error");
        LocalBroadcastManager.getInstance(GoogleLocationService.this).sendBroadcast(parentIntent);
    }

    /**
     * Logs the non resolvable error
     */
    private void onLocationSettingsNonResolvable() {
        String errorMessage = "Location settings are inadequate, and cannot be " +
                "fixed here. Fix in Settings.";
        Log.e(TAG, errorMessage);
//                                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    /**
     * Stops the location updates
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
}
