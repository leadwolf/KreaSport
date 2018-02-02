package com.ccaroni.kreasport.background.rebuild;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.background.rebuild.geofence.GeofenceTransitionsIntentService;
import com.ccaroni.kreasport.background.rebuild.geofence.GeofenceUtils;
import com.ccaroni.kreasport.background.rebuild.location.LocationUtils;
import com.ccaroni.kreasport.race.impl.RaceHolder;
import com.ccaroni.kreasport.view.activities.ExploreActivity;

/**
 * Created by Master on 02/02/2018.
 */

public class RacingService extends Service implements IRaceService {

    private static final int ONGOING_NOTIFICATION_ID = 42;

    private static final String TAG = RacingService.class.getSimpleName();
    private final long NOTIF_UPDATE_FREQ = 1000;    // milliseconds
    private final int HANDLER_MESSAGE = 2;


    private LocationUtils mLocationUtils;
    private GeofenceUtils mGeofenceUtils;
    private GeofenceReceiver geofenceReceiver;


    // PendingIntents for the notification
    private PendingIntent piOnClick;
    private PendingIntent piStopRace;
    /**
     * The single notification instance that we'll keep modifying instead of creating new notifications.
     */
    private Notification notification;
    /**
     * The manager that will send out our notification
     */
    private NotificationManager mNotificationManager;
    /**
     * The handler that will periodically update the notification.
     */
    private Handler mHandler;

    private static String formatElapsedTime(long millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    @Override
    public void startLocationUpdates() {
        // TODO

    }

    @Override
    public void stopLocationUpdates() {
        // TODO

    }

    @Override
    public void addGeofence(Location location) {
        // TODO

    }

    @Override
    public void removeAllGeofences() {
        // TODO

    }

    /**
     * Creates PendingIntents for the button actions in the notification. These will stay the same even if the notification is updated, therefore we initialise them once here.
     */
    private void initPendingIntentsForNotification() {
        // setup on notification click
        Intent onClickIntent = new Intent(this, ExploreActivity.class);
        piOnClick = PendingIntent.getActivity(this, 0, onClickIntent, 0);

        // setup stop race button
        Intent stopRaceIntent = new Intent(this, com.ccaroni.kreasport.background.RacingService.class);
        stopRaceIntent.setAction("STOP");
        piStopRace = PendingIntent.getService(this, 0, stopRaceIntent, 0);
    }

    @Override
    public Notification createNotification() {
        String raceTitle = RaceHolder.getInstance().getCurrentRaceTitle();
        String geofenceProgression = "Progression " + RaceHolder.getInstance().getCheckpointProgression() + "/" + RaceHolder.getInstance().getNumberCheckpoints();

        final long elapsedTime = SystemClock.elapsedRealtime() - RaceHolder.getInstance().getTimeStart();
        String timeString = formatElapsedTime(elapsedTime);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_kreasport_notification_icon)
                        .setContentIntent(piOnClick)
                        .setContentTitle(raceTitle) // race name
                        .setContentText(timeString) // elapsed time
                        /*
                         * Sets the big view "big text" style and supplies the
                         * text (the user's reminder message) that will be displayed
                         * in the detail area of the expanded notification.
                         * These calls are ignored by the support library for
                         * pre-4.1 devices.
                         */
                        .setStyle(new NotificationCompat.InboxStyle()
                                .setBigContentTitle(raceTitle) // race name
                                .addLine(timeString)
                                .addLine(geofenceProgression) // geofence progression
                                .setSummaryText("Race active")) // Useful ?
                        .addAction(R.drawable.ic_stop_grey_600_24dp,
                                "Stop race", piStopRace);
        return builder.build();
    }

    @Override
    public void updateNotification() {
        // TODO
        mNotificationManager.notify(ONGOING_NOTIFICATION_ID, notification);

    }

    /**
     * Initializes the handler that will periodically update the notification.
     */
    private void initHandler() {
        this.mHandler = new Handler() {
            public void handleMessage(Message m) {
                // update then notification
                updateNotification();
                // send the same message again to repeat this action
                sendMessageDelayed(Message.obtain(this, HANDLER_MESSAGE), NOTIF_UPDATE_FREQ);
            }
        };
    }

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

        initAsForegroundService();

        // TODO
        // init location service
        // init geofence service
    }

    /**
     * Creates the notification and uses it to start this service as a foreground service with.
     */
    private void initAsForegroundService() {
        this.mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        initPendingIntentsForNotification();
        this.notification = createNotification();
        startForeground(ONGOING_NOTIFICATION_ID, notification);

        initHandler();
        // start the periodic updates
        this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, HANDLER_MESSAGE), NOTIF_UPDATE_FREQ);
    }

    /**
     * Kills {@link #mHandler} and unregisters {@link #geofenceReceiver} to avoid memory leaks.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(geofenceReceiver);
        mHandler.removeMessages(HANDLER_MESSAGE);
    }

    /**
     * Custom BroadcastReceiver to act upon the reception of a broadcast from {@link GeofenceTransitionsIntentService#GEOFENCE_TRIGGERED}
     */
    class GeofenceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String checkpointId = intent.getStringExtra(GeofenceTransitionsIntentService.KEY_GEOFENCE_ID);
            if (checkpointId == null) {
                throw new IllegalArgumentException("Received intent for geofenceReceiver with no checkpoint associated");
            }

            Log.d(TAG, "received geofence broadcast for checkpoint: " + checkpointId);

            // TODO
        }
    }
}
