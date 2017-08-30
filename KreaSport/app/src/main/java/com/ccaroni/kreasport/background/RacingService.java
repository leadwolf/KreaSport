package com.ccaroni.kreasport.background;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.background.geofence.GeofenceTransitionsIntentService;
import com.ccaroni.kreasport.background.geofence.GeofenceUtils;
import com.ccaroni.kreasport.background.location.LocationUtils;
import com.ccaroni.kreasport.view.activities.ExploreActivity;

import static com.ccaroni.kreasport.background.geofence.GeofenceTransitionsIntentService.GEOFENCE_TRIGGERED;

/**
 * Created by Master on 19/08/2017.
 */

public class RacingService extends Service implements LocationUtils.LocationUtilsSubscriber {

    private static final String TAG = RacingService.class.getSimpleName();

    private static final int ONGOING_NOTIFICATION_ID = 42;


    private LocationUtils mLocationUtils;
    private GeofenceUtils mGeofenceUtils;
    private GeofenceReceiver geofenceReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initForground();

        initGeofenceReceiver();

    }

    private void initForground() {
        // setup on notification click
        Intent onClickIntent = new Intent(this, ExploreActivity.class);
        PendingIntent piOnClick = PendingIntent.getActivity(this, 0, onClickIntent, 0);

        // setup stop race button
        Intent stopRaceIntent = new Intent(this, RacingService.class);
        stopRaceIntent.setAction("STOP");
        PendingIntent piStopRace = PendingIntent.getService(this, 0, stopRaceIntent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_kreasport_notification_icon)
                        .setContentIntent(piOnClick)
                        .setContentTitle("Title") // race name
                        .setContentText("Text") // geofence progression
                        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                        /*
                         * Sets the big view "big text" style and supplies the
                         * text (the user's reminder message) that will be displayed
                         * in the detail area of the expanded notification.
                         * These calls are ignored by the support library for
                         * pre-4.1 devices.
                         */
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine("Checkpoint progression: n/n") // geofence progression
                                .setSummaryText("Race active")
                                .setBigContentTitle("big title")) // race name
                        .addAction(R.drawable.ic_kreasport_notification_icon,
                                "Stop race", piStopRace);


        startForeground(ONGOING_NOTIFICATION_ID, builder.build());
    }

    private void initGeofenceReceiver() {
        mLocationUtils = new LocationUtils(this);


        // TODO make sure we can cancel geofences created in ExploreActivity from this instance
        mGeofenceUtils = new GeofenceUtils(this);

        geofenceReceiver = new GeofenceReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(geofenceReceiver, new IntentFilter(GEOFENCE_TRIGGERED));
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO
    }

    class GeofenceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String checkpointId = intent.getStringExtra(GeofenceTransitionsIntentService.KEY_GEOFENCE_ID);
            if (checkpointId == null) {
                throw new IllegalArgumentException("Received intent for geofenceReceiver with no checkpoint associated");
            }

            Log.d(TAG, "received geofence broadcast for checkpoint: " + checkpointId);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(geofenceReceiver);
    }
}
