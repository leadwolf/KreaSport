package com.ccaroni.kreasport.explore.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.explore.events.GeofenceTriggered;
import com.ccaroni.kreasport.explore.events.LocationChanged;
import com.ccaroni.kreasport.explore.services.impl.ExploreService;
import com.ccaroni.kreasport.explore.view.activity.MainActivity;
import com.ccaroni.kreasport.utils.NotificationUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Master on 03/02/2018.
 * Implements {@link IExploreService} and implements the notification handling.
 * <p>
 * Location and geofence services should be handled by subclasses.
 */
public abstract class AbstractExploreService extends Service implements IExploreService {

    public static final String NOTIFICATION_CHANNEL_ID_RACE = "com.ccaroni.kreasport.NOTIF_CHANNEL";
    private static final int ONGOING_NOTIFICATION_ID = 42;
    protected final int HANDLER_MESSAGE = 2;
    private final long NOTIFICATION_UPDATE_FREQ = 1000;    // milliseconds
    /**
     * The handler that will periodically update the notification.
     */
    protected Handler mHandler;
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

    private static String formatElapsedTime(long millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);
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

        subscribeToBus();
        // init location service
        initLocationUpdates();
        // init geofence service
        initGeofenceServices();
    }

    /**
     * Creates the notification and uses it to start this service as a foreground service with.
     */
    protected void initAsForegroundService() {
        this.mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        initPendingIntentsForNotification();
        NotificationUtil.registerRaceNotificationChannel(this, NOTIFICATION_CHANNEL_ID_RACE);
        initNotificationBuilder();

        Notification notification = updateRelevantNotificationFields();
        startForeground(ONGOING_NOTIFICATION_ID, notification);

        initHandler();
        // start the periodic updates
        this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, HANDLER_MESSAGE), NOTIFICATION_UPDATE_FREQ);
    }

    /**
     * Creates PendingIntents for the button actions in the notification. These will stay the same even if the notification is updated, therefore we initialise them once here.
     */
    private void initPendingIntentsForNotification() {
        // setup on notification click
        Intent onClickIntent = new Intent(this, MainActivity.class);
        // TODO add intent go to fragment explore

        piOnClick = PendingIntent.getActivity(this, 0, onClickIntent, 0);

        // setup stop race button
        Intent stopRaceIntent = new Intent(this, ExploreService.class);
        stopRaceIntent.setAction("STOP");
        piStopRace = PendingIntent.getService(this, 0, stopRaceIntent, 0);
    }

    /**
     * Initializes {@link #mNotifyBuilder} with the proper styles, notification channels
     */
    private void initNotificationBuilder() {
        String raceTitle = "";
        // TODO get from DAO

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

    public void sendUpdatedNotification() {
        Notification notification = this.updateRelevantNotificationFields();
        mNotificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
    }

    /**
     * Uses {@link #mNotifyBuilder} to modify the relevant fields to create a new notification
     *
     * @return the updated notification
     */
    public Notification updateRelevantNotificationFields() {
        String raceTitle = "";
        // TODO get from DAO

        // TODO use xml to format this
        String geofenceProgression = "Progression ";
        // TODO get progression from race holder

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
//        final long elapsedTime = SystemClock.elapsedRealtime() - RaceHolder.getInstance().getTimeStart();
        final long elapsedTime = SystemClock.elapsedRealtime();
        // TODO get start time of current race

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
                sendMessageDelayed(Message.obtain(this, HANDLER_MESSAGE), NOTIFICATION_UPDATE_FREQ);
            }
        };
    }

    /**
     * Subscribes to {@link EventBus}
     */
    private void subscribeToBus() {
        EventBus.getDefault().register(this);
    }

    /**
     * Initialize and start all location services here
     */
    protected abstract void initLocationUpdates();

    /**
     * Initialize and start all geofence services here
     */
    protected abstract void initGeofenceServices();

    /**
     * Kills {@link #mHandler}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        this.mHandler.removeMessages(HANDLER_MESSAGE);
        EventBus.getDefault().unregister(this);
    }

    /**
     * <b>WARNING:</b> make sure to add the @Subscribe annotation
     *
     * @param locationChanged the new location
     */
    protected abstract void onLocationChanged(LocationChanged locationChanged);

    /**
     * <b>WARNING:</b> make sure to add the @Subscribe annotation
     *
     * @param geofenceTriggered the id for the triggered geofence
     */
    protected abstract void onGeofenceTriggered(GeofenceTriggered geofenceTriggered);
}