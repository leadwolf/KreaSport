package com.ccaroni.kreasport.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import static com.ccaroni.kreasport.background.rebuild.geofence.GeofenceTransitionsIntentService.GEOFENCE_TRIGGERED;

/**
 * Created by Master on 19/08/2017.
 */

public class RacingService extends Service implements LocationUtils.LocationUtilsSubscriber {

    private static final String TAG = RacingService.class.getSimpleName();

    private static final int ONGOING_NOTIFICATION_ID = 42;


    private LocationUtils mLocationUtils;
    private GeofenceUtils mGeofenceUtils;
    private GeofenceReceiver geofenceReceiver;

    private NotificationManager mNotificationManager;

    private Handler mHandler;

    // Timer to update the ongoing notification
    private final long NOTIF_UPDATE_FREQ = 100;    // milliseconds
    private final int TICK_WHAT = 2;

    // PendingIntents for the notification
    private PendingIntent piOnClick;
    private PendingIntent piStopRace;

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

        initHandler();
        initPendingIntentsForNotification();

        initAsForegroundService();

        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), NOTIF_UPDATE_FREQ);

        initGeofenceReceiver();

    }

    /**
     * Initializes the handler that will periodically update the notification.
     * Also initialises {@link #mNotificationManager} so we don't have to call {@link #getSystemService(String)} multiple times
     */
    private void initHandler() {
        mHandler = new Handler() {
            public void handleMessage(Message m) {
                updateNotification();
                sendMessageDelayed(Message.obtain(this, TICK_WHAT), NOTIF_UPDATE_FREQ);
            }
        };

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * Creates a new notification and posts it to the status bar.
     */
    private void updateNotification() {
        Notification notification = createNotification();
        mNotificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
    }

    /**
     * Creates the notification and uses it to start this service as a foreground service with.
     */
    private void initAsForegroundService() {
        Notification notification = createNotification();
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    /**
     * @return the notification for an ongoing race with the current time
     */
    private Notification createNotification() {

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

    /**
     * Creates PendingIntents for the button actions in the notification. These will stay the same even if the notification is updated, therefore we initialise them once here.
     */
    private void initPendingIntentsForNotification() {
        // setup on notification click
        Intent onClickIntent = new Intent(this, ExploreActivity.class);
        piOnClick = PendingIntent.getActivity(this, 0, onClickIntent, 0);

        // setup stop race button
        Intent stopRaceIntent = new Intent(this, RacingService.class);
        stopRaceIntent.setAction("STOP");
        piStopRace = PendingIntent.getService(this, 0, stopRaceIntent, 0);
    }

    /**
     * Sets up {@link #mLocationUtils} to receive callbacks, {@link #mGeofenceUtils} to create/delete new geofences
     * and {@link #geofenceReceiver} to receive broadcasts sent out with {@link GeofenceTransitionsIntentService#GEOFENCE_TRIGGERED}
     */
    private void initGeofenceReceiver() {
        mLocationUtils = new LocationUtils(this);
        // don't need to call start since it was already started in ExploreActivity


        // TODO make sure we can cancel geofences created in ExploreActivity from this instance
        mGeofenceUtils = new GeofenceUtils(this);

        geofenceReceiver = new GeofenceReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(geofenceReceiver, new IntentFilter(GEOFENCE_TRIGGERED));
    }

    /***
     * Given the time elapsed in tenths of seconds, returns the string
     * representation of that time.
     *
     * @param now, the current time in tenths of seconds
     * @return String with the current time in the format MM:SS.T or
     * 			HH:MM:SS.T, depending on elapsed time.
     */
    private String formatElapsedTime(long now) {
        long hours = 0, minutes = 0, seconds = 0, tenths = 0;
        StringBuilder sb = new StringBuilder();

        if (now < 1000) {
            tenths = now / 100;
        } else if (now < 60000) {
            seconds = now / 1000;
            now -= seconds * 1000;
            tenths = (now / 100);
        } else if (now < 3600000) {
            hours = now / 3600000;
            now -= hours * 3600000;
            minutes = now / 60000;
            now -= minutes * 60000;
            seconds = now / 1000;
            now -= seconds * 1000;
            tenths = (now / 100);
        }

        if (hours > 0) {
            sb.append(hours).append(":")
                    .append(formatDigits(minutes)).append(":")
                    .append(formatDigits(seconds)).append(".")
                    .append(tenths);
        } else {
            sb.append(formatDigits(minutes)).append(":")
                    .append(formatDigits(seconds)).append(".")
                    .append(tenths);
        }

        return sb.toString();
    }

    private String formatDigits(long num) {
        return (num < 10) ? "0" + num : new Long(num).toString();
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO
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

    /**
     * Kills {@link #mHandler} and unregisters {@link #geofenceReceiver} to avoid memory leaks.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(geofenceReceiver);
        mHandler.removeMessages(TICK_WHAT);
    }
}
