package com.ccaroni.kreasport.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.util.Log;

import com.ccaroni.kreasport.data.dto.DownloadedArea;

import org.osmdroid.tileprovider.cachemanager.CacheManager;
import org.osmdroid.tileprovider.modules.SqliteArchiveTileWriter;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Master on 02/06/2017.
 */

public abstract class CacheManagerCallback implements CacheManager.CacheManagerCallback {

    private static final String LOG = CacheManagerCallback.class.getSimpleName();

    protected final int DOWNLOAD_ERROR = 1;
    protected final int DOWNLOAD_SUCCESS = DOWNLOAD_ERROR + 1;
    protected final int DOWNLOAD_ONGOING = DOWNLOAD_SUCCESS + 1;
    protected final int DOWNLOAD_START = DOWNLOAD_ONGOING + 1;

    protected Activity activity;
    protected CacheCommunicationInterface cacheCommunicationInterface;

    protected SqliteArchiveTileWriter writer;
    protected int notificationID;
    protected NotificationManager mNotifyMgr;

    protected int possibleTiles;
    protected DownloadedArea downloadingArea;

    public CacheManagerCallback(Activity activity, SqliteArchiveTileWriter writer, DownloadedArea downloadingArea) {
        if (activity instanceof CacheCommunicationInterface) {
            this.cacheCommunicationInterface = (CacheCommunicationInterface) activity;
        } else {
            throw new RuntimeException(activity + " must implement " + CacheCommunicationInterface.class.getSimpleName());
        }
        this.activity = activity;
        this.writer = writer;
        this.notificationID = 42;
        this.downloadingArea = downloadingArea;

        this.mNotifyMgr = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
    }


    /**
     * @param progress         the number of tiles currently downloaded out of the {@link #setPossibleTilesInArea(int)}
     * @param currentZoomLevel
     * @param zoomMin
     * @param zoomMax
     */
    @Override
    public void updateProgress(int progress, int currentZoomLevel, int zoomMin, int zoomMax) {
        double percentage = ((double) progress / (double) possibleTiles) * 100;
        cacheCommunicationInterface.updateProgress(downloadingArea, percentage);
    }

    @Override
    public void onTaskComplete() {
        cacheCommunicationInterface.onTaskComplete(downloadingArea);
    }

    @Override
    public void setPossibleTilesInArea(int total) {
        Log.d(LOG, "actual tile size: " + total);
        possibleTiles = total;
    }

    public int getTotalTiles() {
        return possibleTiles;
    }

    /**
     * Interface to notify the attached activity about the download.
     */
    public interface CacheCommunicationInterface {

        void onTaskComplete(DownloadedArea downloadedArea);

        /**
         * @param downloadedArea
         * @param progressPercentage as a percentage of total
         */
        void updateProgress(DownloadedArea downloadedArea, double progressPercentage);

    }
}
