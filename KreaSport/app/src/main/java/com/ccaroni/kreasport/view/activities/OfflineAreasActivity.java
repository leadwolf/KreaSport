package com.ccaroni.kreasport.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.DownloadedArea;
import com.ccaroni.kreasport.utils.CacheManagerCallback;
import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.utils.impl.CacheManagerCallbackImpl;
import com.ccaroni.kreasport.view.adapter.DownloadedAreaAdapter;

import org.osmdroid.tileprovider.cachemanager.CacheManager;
import org.osmdroid.tileprovider.modules.SqliteArchiveTileWriter;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;

import java.util.HashMap;

import io.realm.RealmResults;

public class OfflineAreasActivity extends BaseActivity implements CacheManagerCallbackImpl.CacheCommunicationInterface {

    private static final String LOG = OfflineAreasActivity.class.getSimpleName();
    private static final int CUSTOM_AREA_REQUEST_CODE = 100;


    private HashMap<String, CacheManager.DownloadingTask> taskMap;
    private DownloadedAreaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.customCreate(savedInstanceState, R.layout.activity_offliine_areas);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        taskMap = new HashMap<>();

        setupListView();
    }

    private void setupListView() {
        ListView listView = (ListView) findViewById(R.id.list_view_downloaded_areas);

        RealmResults<DownloadedArea> downloadedAreas = RealmHelper.getInstance(this).findAllDownloadedAreas();
        adapter = new DownloadedAreaAdapter(this, downloadedAreas);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetNavigationDrawer(navigationView.getMenu().getItem(3));
        setCurrentActivityIndex(3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CUSTOM_AREA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d(LOG, "received download from " + AreaSelectionActivity.class.getSimpleName());

                String areaId = data.getStringExtra(AreaSelectionActivity.KEY_AREA_ID);
                if (areaId == null) {
                    throw new IllegalArgumentException("could not find area id in the intent");
                }

                DownloadedArea downloadedArea = RealmHelper.getInstance(this).findDownloadedAreaById(areaId);
                Log.d(LOG, "got transferred id: " + areaId + " for " + downloadedArea.getName());

                startDownload(downloadedArea);

            }
        } else {
            Log.d(LOG, "area selection cancelled");
        }
    }

    public void downloadCustomArea(View view) {
        startActivityForResult(new Intent(OfflineAreasActivity.this, AreaSelectionActivity.class), CUSTOM_AREA_REQUEST_CODE);
    }

    public void asyncActivity(View view) {
        startActivity(new Intent(this, ThreadsLifecycleActivity.class));
    }

    /**
     * Creates the cache managers and starts the download with {@link CacheManager#downloadAreaAsync(Context, BoundingBox, int, int, CacheManager.CacheManagerCallback)}
     * @param downloadedArea
     */
    public void startDownload(DownloadedArea downloadedArea) {

        SqliteArchiveTileWriter writer = null;
        try {
            writer = new SqliteArchiveTileWriter(downloadedArea.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MapView mMapView = new MapView(this, null, null);
        CacheManager mgr = new CacheManager(mMapView, writer);
        CacheManagerCallback cacheManagerCallbackImpl = new CacheManagerCallbackImpl(this, writer, downloadedArea);

        CacheManager.DownloadingTask downloadingTask = mgr.downloadAreaAsyncNoUI(this, downloadedArea.getBoundingBox(),
                downloadedArea.getMinZoom(), Constants.DOWNLOAD_MAX_ZOOM, cacheManagerCallbackImpl);

        taskMap.put(downloadedArea.getId(), downloadingTask);
        // TODO use downloadingTask to cancel in notification


        int nTiles = cacheManagerCallbackImpl.getTotalTiles();
        double estimatedSize = 0.001 * (Constants.TILE_KB_SIZE * nTiles); // divide to get in MB
        int roundedSize = (int) Math.round(estimatedSize);
    }


    @Override
    public void onTaskComplete(DownloadedArea downloadedArea) {
        RealmHelper.getInstance(this).beginTransaction();

        downloadedArea.setOngoing(false);

        RealmHelper.getInstance(this).commitTransaction();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateProgress(DownloadedArea downloadedArea, double progressPercentage) {
        RealmHelper.getInstance(this).beginTransaction();

        downloadedArea.setProgress(progressPercentage);

        RealmHelper.getInstance(this).commitTransaction();

        adapter.notifyDataSetChanged();
    }
}
