package com.ccaroni.kreasport.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.background.location.LocationUtils;
import com.ccaroni.kreasport.race.impl.RaceHolder;
import com.ccaroni.kreasport.race2.AbstractRacingService;
import com.ccaroni.kreasport.race2.RaceContext;
import com.ccaroni.kreasport.view.activities.ExploreActivity;

/**
 * Created by Master on 19/08/2017.
 */

public class RacingService extends Service implements LocationUtils.LocationUtilsSubscriber, RaceContext {

    private static final String TAG = RacingService.class.getSimpleName();

    private static final int ONGOING_NOTIFICATION_ID = 42;


    private AbstractRacingService abstractRacingService;

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

        abstractRacingService = new AbstractRacingService(this);

        initHandler();
        initPendingIntentsForNotification();

        initAsForegroundService();

        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), NOTIF_UPDATE_FREQ);

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


    private static String formatElapsedTime(long millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO
    }

    @Override
    public void onGeofenceTriggered(String checkpointId) {
        // TODO
    }


    /**
     * Kills {@link #mHandler} and calls {@link AbstractRacingService#destroy()}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        this.abstractRacingService.destroy();
        mHandler.removeMessages(TICK_WHAT);
    }
}
