package com.ccaroni.kreasport.location;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.view.activities.ExploreActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.Iterator;
import java.util.Map;

import static android.R.attr.handle;
import static android.provider.Contacts.SettingsColumns.KEY;
import static com.ccaroni.kreasport.view.activities.ExploreActivity.KEY_LOCATION_PREFS_FILENAME;

/**
 * Created by Master on 02/07/2017.
 */

public class LocationService extends Service {

    private static final String TAG = LocationService.class.getSimpleName();
    private static final String KEY_BASE =  "com.ccaroni.kreasport." + TAG + ".key.";
    private static final String KEY_LAST_LOCATION = KEY_BASE + "last_location";

    private FusedLocationProviderClient mLocationProviderClient;
    private LocationCallback mLocationCallback;

    private SharedPreferences sharedPreferences;
    private String prefsFileName;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        prefsFileName = intent.getStringExtra(KEY_LOCATION_PREFS_FILENAME);
        sharedPreferences = getSharedPreferences(prefsFileName, MODE_PRIVATE);
        return Service.START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");

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

                    handleUpdate(location);

                    // TODO broadcast to activity?
                }
            }
        };

        mLocationProviderClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handleLocationRequestError();
                    }
                });

    }

    private void handleUpdate(Location location) {

        displayNotification();

        // Write to SharedPrefs, activity will subscribe and therfore see changes

        sharedPreferences.edit()
                .putString(KEY_LAST_LOCATION, location.toString())
                .apply();

    }

    private void displayNotification() {
        // will start ExploreActivity
        Intent notificationIntent = new Intent(this, ExploreActivity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(ExploreActivity.class);
        taskStackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle("Location update")
                .setContentText("Details")
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());

    }

    private void handleLocationRequestError() {
        Log.d(TAG, "location request error");
        // TODO cancel race
    }

    public void stopLocationRequests() {
        mLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }
}
