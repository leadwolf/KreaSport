package com.ccaroni.kreasport.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.view.activities.OfflineAreasActivity;

import org.osmdroid.tileprovider.cachemanager.CacheManager;
import org.osmdroid.tileprovider.modules.SqliteArchiveTileWriter;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Master on 01/06/2017.
 */

public class CustomCacheManagerCallback implements CacheManager.CacheManagerCallback {

    private final String LOG = CustomCacheManagerCallback.class.getSimpleName();

    private final int DOWNLOAD_ERROR = 1;
    private final int DOWNLOAD_SUCCESS = DOWNLOAD_ERROR + 1;
    private final int DOWNLOAD_ONGOING = DOWNLOAD_SUCCESS + 1;
    private final int DOWNLOAD_START = DOWNLOAD_ONGOING + 1;

    private Activity activity;
    private CacheCommunicationInterface cacheCommunicationInterface;

    private SqliteArchiveTileWriter writer;
    private int notificationID;
    private NotificationManager mNotifyMgr;

    private int possibleTiles;
    private String downloadingAreaName;

    public CustomCacheManagerCallback(Activity activity, SqliteArchiveTileWriter writer, String downloadingAreaName) {
        if (activity instanceof CacheCommunicationInterface) {
            this.cacheCommunicationInterface = (CacheCommunicationInterface) activity;
        } else {
            throw new RuntimeException(activity + " must implement " + CacheCommunicationInterface.class.getSimpleName());
        }
        this.activity = activity;
        this.writer = writer;
        this.notificationID = 42;
        this.downloadingAreaName = downloadingAreaName;

        this.mNotifyMgr = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onTaskComplete() {
        Log.d(LOG, "Download complete!");
        if (writer != null)
            writer.onDetach();

        NotificationCompat.Builder mBuilder = createNotification(DOWNLOAD_SUCCESS, downloadingAreaName, 0);
        mNotifyMgr.notify(notificationID, mBuilder.build());
    }

    @Override
    public void onTaskFailed(int errors) {
        Log.d(LOG, "Download complete with " + errors + " errors");
        if (writer != null)
            writer.onDetach();

        NotificationCompat.Builder mBuilder = createNotification(DOWNLOAD_ERROR, downloadingAreaName, 0);
        mNotifyMgr.notify(notificationID, mBuilder.build());

    }

    /**
     *
     * @param progress the number of tiles currently downloaded out of the {@link #setPossibleTilesInArea(int)}
     * @param currentZoomLevel
     * @param zoomMin
     * @param zoomMax
     */
    @Override
    public void updateProgress(int progress, int currentZoomLevel, int zoomMin, int zoomMax) {
//            Log.d(LOG, "progress " + progress);

        NotificationCompat.Builder mBuilder = createNotification(DOWNLOAD_ONGOING, downloadingAreaName, progress);
        mNotifyMgr.notify(notificationID, mBuilder.build());
    }

    @Override
    public void downloadStarted() {
        Log.d(LOG, "started download");

        NotificationCompat.Builder mBuilder = createNotification(DOWNLOAD_START, downloadingAreaName, 0);
        mNotifyMgr.notify(notificationID, mBuilder.build());
    }

    @Override
    public void setPossibleTilesInArea(int total) {
        Log.d(LOG, "actual tile size: " + total);
        possibleTiles = total;
    }

    private NotificationCompat.Builder createNotification(int status, String downloadingAreaName, int progress) {
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(activity)
                .setSmallIcon(R.drawable.ic_file_download_black_24dp)
                .setContentTitle(downloadingAreaName)
                .setOngoing(false);
        String content = "";
        switch (status) {
            case DOWNLOAD_ERROR:
                content = String.format(activity.getString(R.string.offline_area_selection_notification_error), downloadingAreaName);
                break;
            case DOWNLOAD_ONGOING:
                content = activity.getString(R.string.offline_area_selection_notification_ongoing, progress, possibleTiles);
                mBuilder.setProgress(possibleTiles, progress, false);
                mBuilder.setOngoing(true);
                break;
            case DOWNLOAD_SUCCESS:
                content = String.format(activity.getString(R.string.offline_area_selection_notification_success), downloadingAreaName);
                mBuilder.setProgress(0, 0, false);
                break;
            case DOWNLOAD_START:
                content = String.format(activity.getString(R.string.offline_area_selection_notification_start), downloadingAreaName);
                break;
            default:
                break;
        }

        mBuilder.setContentText(content);
        setPendingIntentForBuilder(mBuilder);

        return mBuilder;
    }

    /**
     * Creates an intent for the notification to lead to {@link OfflineAreasActivity}
     *
     * @param mBuilder
     * @return the builder inputted with the changes.
     */
    private NotificationCompat.Builder setPendingIntentForBuilder(NotificationCompat.Builder mBuilder) {
        Intent resultIntent = new Intent(activity.getApplicationContext(), OfflineAreasActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity.getApplicationContext()); // to preserve navigation
        stackBuilder.addParentStack(OfflineAreasActivity.class); // adds all parents of OfflineAreas to the stack
        stackBuilder.addNextIntent(resultIntent); // sets the the most recent intent to the intent we just created

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT); // get a pending intent from the stack, will get the one we just created

        mBuilder.setContentIntent(pendingIntent);
        return mBuilder;
    }

    public int getTotalTiles() {
        return possibleTiles;
    }

    /**
     * Interface to notify the attached activity about the download.
     */
    public interface CacheCommunicationInterface {

        void onTaskComplete();

        void updateProgress(int progress);

        void downloadStarted();

        void onTaskFailed(int errors);
    }
}