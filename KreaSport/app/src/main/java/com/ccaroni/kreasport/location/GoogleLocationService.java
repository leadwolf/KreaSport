package com.ccaroni.kreasport.location;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ccaroni.kreasport.utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;

/**
 * Created by Master on 02/07/2017.
 */

public class GoogleLocationService extends BaseLocationService {

    private static final String TAG = GoogleLocationService.class.getSimpleName();
    private FusedLocationProviderClient mLocationProviderClient;

    private LocationCallback mLocationCallback;
    private OnFailureListener onFailureListener;


    @SuppressWarnings({"MissingPermission"})
    @Override
    protected void startLocationUpdates() {

        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Create the location request
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.GEOLOCATION_UPDATE_INTERVAL)
                .setFastestInterval(Constants.GEOLOCATION_UPDATE_FASTEST_INTERVAL);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, "got location result: "+ location.toString());

                    writeLocation(location);
                }
            }
        };

        onFailureListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "location request failure");
                Log.d(TAG, e.toString());
            }
        };

        mLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
                .addOnFailureListener(onFailureListener);

    }

}
