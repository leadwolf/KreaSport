package com.ccaroni.kreasport.service.location;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.view.activities.ExploreActivity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.ccaroni.kreasport.view.activities.ExploreActivity.REQUIRES_LOCATION_SETTINGS_PROMPT;

/**
 * Created by Master on 02/07/2017.
 */

public class GoogleLocationService extends BaseLocationService {

    private static final String TAG = GoogleLocationService.class.getSimpleName();
    private FusedLocationProviderClient mFusedLocationClient;

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;


    @SuppressWarnings({"MissingPermission"})
    @Override
    protected void startLocationUpdates() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        verifyLocationSettings();

        // Using the new 11.0.0 location api without a GoogleApiClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
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

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, "got location result: " + location.toString());

                    writeLocation(location);
                }
            }
        };
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.GEOLOCATION_UPDATE_INTERVAL)
                .setFastestInterval(Constants.GEOLOCATION_UPDATE_FASTEST_INTERVAL);

    }

    private void buildLocationSettingsRequest() {
        mLocationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .build();
    }

    private void verifyLocationSettings() {
        // Begin by checking if the device has the necessary location settings.
        Log.d(TAG, "checking if google play services is compatible with out location request");
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                PendingIntent pI = rae.getResolution();
                                startActivity(new Intent(GoogleLocationService.this, ExploreActivity.class)
                                        .putExtra(REQUIRES_LOCATION_SETTINGS_PROMPT, pI)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                                // TODO just use broadcast?

                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
//                                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
}
