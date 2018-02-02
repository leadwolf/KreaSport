package com.ccaroni.kreasport.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Master on 02/02/2018.
 */

public class NotificationUtil {

    private static final String TAG = NotificationUtil.class.getSimpleName();

    public static void registerRaceNotificationChannel(Context context, String channelID) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (manager == null || manager.getNotificationChannel(channelID) != null) {
                return;
            }

            NotificationChannel channel = new NotificationChannel(
                    channelID,
                    "Race service",
                    NotificationManager.IMPORTANCE_LOW);

            channel.setDescription("Notification for racing service");
            channel.enableLights(false);
            channel.enableVibration(false);

            manager.createNotificationChannel(channel);
            Log.d(TAG, "created notification channel for " + channelID);
        }
    }

}
