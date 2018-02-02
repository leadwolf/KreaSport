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
import com.ccaroni.kreasport.utils.NotificationUtil;
import com.ccaroni.kreasport.view.activities.ExploreActivity;

/**
 * Created by Master on 02/02/2018.
 */

public class RaceService extends Service implements IRaceService {

    public static final String NOTIFICATION_CHANNEL_ID_RACE = "com.ccaroni.kreasport.NOTIF_CHANNEL";
    private static final int ONGOING_NOTIFICATION_ID = 42;
    private static final String TAG = RaceService.class.getSimpleName();
    private final long NOTIF_UPDATE_FREQ = 1000;    // milliseconds
    private final int HANDLER_MESSAGE = 2;


    private LocationUtils mLocationUtils;
    private GeofenceUtils mGeofenceUtils;
    private GeofenceReceiver geofenceReceiver;


    // PendingIntents for the notification
    private PendingIntent piOnClick;
    private PendingIntent piStopRace;

    /**
     * The builder that will constantly modify to create updated notifications
     */
    private NotificationCompat.Builder mNotifyBuilder;
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
        Intent stopRaceIntent = new Intent(this, RaceService.class);
        stopRaceIntent.setAction("STOP");
        piStopRace = PendingIntent.getService(this, 0, stopRaceIntent, 0);
    }

    private void initNotificationBuilder() {
        String raceTitle = RaceHolder.getInstance().getCurrentRaceTitle();

        mNotifyBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_RACE)
                        .setContentTitle(raceTitle) // race name
                        .setContentText("") // elapsed time
                        .setSmallIcon(R.drawable.ic_kreasport_notification_icon)
                        .setChannelId(NOTIFICATION_CHANNEL_ID_RACE)
                        .setContentIntent(piOnClick)
                        .setStyle(new NotificationCompat.InboxStyle()
                                .setBigContentTitle(raceTitle) // race name
                                .addLine("")
                                .addLine("") // geofence progression
                                .setSummaryText("Race active")) // Useful ?
                        .addAction(R.drawable.ic_stop_grey_600_24dp,
                                "Stop race", piStopRace);
    }

    @Override
    public void sendUpdatedNotification() {
        Notification notification = this.updateRelevantNotificationFields();
        mNotificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
    }

    /**
     * Updates the pre-generated {@link #mNotifyBuilder} to the current time and progression
     *
     * @return the updated notification
     */
    @Override
    public Notification updateRelevantNotificationFields() {
        String raceTitle = RaceHolder.getInstance().getCurrentRaceTitle();

        // TODO use xml to format this
        String geofenceProgression = "Progression " + RaceHolder.getInstance().getCheckpointProgression() + "/" + RaceHolder.getInstance().getNumberCheckpoints();

        String timeString = getElapsedTime();

        this.mNotifyBuilder
                .setContentTitle(raceTitle) // race name
                .setSmallIcon(R.drawable.ic_kreasport_notification_icon)
                .setChannelId(NOTIFICATION_CHANNEL_ID_RACE)
                .setContentText(timeString) // elapsed time
                .setStyle(new NotificationCompat.InboxStyle()
                        .setBigContentTitle(raceTitle) // race name, have to set it again?
                        .addLine(timeString)
                        .addLine(geofenceProgression) // geofence progression
                        .setSummaryText("Race active")); // Useful ?

        return mNotifyBuilder.build();
    }

    private String getElapsedTime() {
        final long elapsedTime = SystemClock.elapsedRealtime() - RaceHolder.getInstance().getTimeStart();
        return formatElapsedTime(elapsedTime);
    }

    /**
     * Initializes the handler that will periodically update the notification.
     */
    private void initHandler() {
        this.mHandler = new Handler() {
            public void handleMessage(Message m) {
                // update then notification
                sendUpdatedNotification();
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
        NotificationUtil.registerRaceNotificationChannel(this, NOTIFICATION_CHANNEL_ID_RACE);
        initNotificationBuilder();

        Notification notification = updateRelevantNotificationFields();
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
