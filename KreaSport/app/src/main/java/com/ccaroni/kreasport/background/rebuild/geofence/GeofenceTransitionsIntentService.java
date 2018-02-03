package com.ccaroni.kreasport.background.rebuild.geofence;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.view.activities.ExploreActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 19/08/2017.
 * This intent service is always running and receives the intents triggered by the geofences. It then rebroadcasts them using the {@link #ACTION_GEOFENCE_TRIGGERED} id.
 */
public class GeofenceTransitionsIntentService extends IntentService {

    private static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();
    private static final String KEY_BASE = "com.ccaroni.kreasport." + GeofenceTransitionsIntentService.class.getSimpleName() + ".keys.";
    public static final String KEY_GEOFENCE_ID = KEY_BASE + "geofence_id";

    /**
     * The action that we send the geofence to
     */
    public static final String ACTION_GEOFENCE_TRIGGERED = KEY_BASE + "geofence_triggered";

    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    /**
     * @param geofenceListener the instance that will receive geofence trigger callbacks and provide the {@link LocalBroadcastManager}
     * @return a {@link GeofenceListener} that is registered with {@link LocalBroadcastManager} (from the context of locationListener) that will callback with the IDs of the
     * triggered geofences
     */
    public static GeofenceReceiver getLocationReceiver(GeofenceListener geofenceListener) {
        GeofenceReceiver geofenceReceiver = new GeofenceReceiver(geofenceListener);
        geofenceReceiver.registerWithLocalBroadcastManager(geofenceListener.getContext());
        return geofenceReceiver;
    }

    /**
     * Handles incoming intents when a geofence is triggered.
     *
     * @param intent sent by Location Services. This Intent is provided to Location Services (inside a PendingIntent) when addGeofence() is called.
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "got intent");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            notifyActivity(triggeringGeofences);
        } else {
            // Log the error.
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }

    private void notifyActivity(List<Geofence> triggeringGeofences) {
        for (Geofence geofence : triggeringGeofences) {
            Log.d(TAG, "sending geofence to " + ExploreActivity.class.getSimpleName() + " : " + geofence.getRequestId());

            Intent resendIntent = new Intent(ACTION_GEOFENCE_TRIGGERED); //Send to the receiver listening for this in ExploreActivity
            resendIntent.putExtra(KEY_GEOFENCE_ID, geofence.getRequestId());
            LocalBroadcastManager.getInstance(this).sendBroadcast(resendIntent);
        }
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param geofenceTransition  The ID of the geofence transition.
     * @param triggeringGeofences The geofence(s) triggered.
     * @return The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(), ExploreActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(ExploreActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.ic_kreasport_bitmap)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_kreasport_bitmap))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(getString(R.string.geofence_transition_notification_text))
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType A transition type constant defined in Geofence
     * @return A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }

    public interface GeofenceListener {
        void onGeofenceTriggered(String geofenceID);

        Context getContext();
    }

    public static class GeofenceReceiver extends BroadcastReceiver {

        private GeofenceListener geofenceListener;

        public GeofenceReceiver(GeofenceListener geofenceListener) {
            this.geofenceListener = geofenceListener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String geofenceID = intent.getStringExtra(KEY_GEOFENCE_ID);
            if (geofenceID == null) {
                Log.d(TAG, "received geofence triggered broadcast but id was null");
                return;
            }

            this.geofenceListener.onGeofenceTriggered(geofenceID);
        }

        /**
         * Registers this instance with the {@link LocalBroadcastManager}
         *
         * @param context the context to get the {@link LocalBroadcastManager} with
         */
        public void registerWithLocalBroadcastManager(Context context) {
            LocalBroadcastManager.getInstance(context).registerReceiver(this, new IntentFilter(ACTION_GEOFENCE_TRIGGERED));
        }

        /**
         * Unregisters this instance from the LBC to avoid memory leaks
         *
         * @param geofenceListener the listener that will provide the context to access the LBC
         */
        public void unregisterFromLocalBroadcastManager(GeofenceListener geofenceListener) {
            LocalBroadcastManager.getInstance(geofenceListener.getContext()).unregisterReceiver(this);
        }
    }
}
