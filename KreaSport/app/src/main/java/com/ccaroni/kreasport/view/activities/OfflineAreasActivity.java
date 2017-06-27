package com.ccaroni.kreasport.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

import static android.text.format.Formatter.formatShortFileSize;
import static com.ccaroni.kreasport.utils.Constants.KEY_AREA_ID;

public class OfflineAreasActivity extends BaseActivity implements CacheManagerCallbackImpl.CacheCommunicationInterface {

    private static final String LOG = OfflineAreasActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CUSTOM_AREA = 100;
    public static final int REQUEST_CODE_DELETION = REQUEST_CODE_CUSTOM_AREA + 1;


    private HashMap<String, CacheManager.DownloadingTask> taskMap;
    private DownloadedAreaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.customCreate(savedInstanceState, R.layout.activity_offliine_areas);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        taskMap = new HashMap<>();

        setupListView();
    }

    private void setupListView() {
        ListView listView = (ListView) findViewById(R.id.list_view_downloaded_areas);

        RealmResults<DownloadedArea> realmDownloadedAreas = RealmHelper.getInstance(this).findAllDownloadedAreas();
        List<DownloadedArea> downloadedAreas = new ArrayList<>();
        for (DownloadedArea realmDownloadedArea : realmDownloadedAreas) {
            downloadedAreas.add(realmDownloadedArea);
        }

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
        switch (requestCode) {
            case REQUEST_CODE_CUSTOM_AREA:
                if (resultCode == RESULT_OK) {
                    Log.d(LOG, "received download from " + AreaSelectionActivity.class.getSimpleName());

                    String areaId = data.getStringExtra(KEY_AREA_ID);
                    if (areaId == null) {
                        throw new IllegalArgumentException("could not find area id in the intent");
                    }

                    DownloadedArea downloadedArea = RealmHelper.getInstance(this).findDownloadedAreaById(areaId);
                    Log.d(LOG, "got transferred id: " + areaId + " for " + downloadedArea.getName());

                    startDownload(downloadedArea);

                }
                break;
            case REQUEST_CODE_DELETION:
                if (resultCode == RESULT_OK) {
                    String idToDelete = data.getStringExtra(KEY_AREA_ID);
                    if (idToDelete == null) {
                        throw new IllegalArgumentException("could not find area id in the intent");
                    }
                    DownloadedArea downloadedArea = RealmHelper.getInstance(this).findDownloadedAreaById(idToDelete);
                    String id = downloadedArea.getId();
                    String name = downloadedArea.getName();

                    File file = new File(downloadedArea.getPath());
                    if (file.delete()) {
                        Log.d(LOG, "deleted from filesystem area " + id + " " + name);
                    } else {
                        Log.e(LOG, "failed to delete from filesystem area: " + id + " " + name);
                    }

                    adapter.remove(downloadedArea);
                    adapter.notifyDataSetChanged();
                    RealmHelper.getInstance(this).beginTransaction();
                    RealmHelper.getInstance(this).deleteDownloadedArea(downloadedArea);
                    RealmHelper.getInstance(this).commitTransaction();

                    Log.d(LOG, "deleted from realm area " + id + " " + name);
                }

            default:
                break;
        }
    }

    public void downloadCustomArea(View view) {
        startActivityForResult(new Intent(OfflineAreasActivity.this, AreaSelectionActivity.class), REQUEST_CODE_CUSTOM_AREA);
    }

    public void asyncActivity(View view) {
        startActivity(new Intent(this, ThreadsLifecycleActivity.class));
    }

    /**
     * Creates the cache managers and starts the download with {@link CacheManager#downloadAreaAsync(Context, BoundingBox, int, int, CacheManager.CacheManagerCallback)}
     *
     * @param downloadedArea
     */
    public void startDownload(DownloadedArea downloadedArea) {

        adapter.add(downloadedArea);
        adapter.notifyDataSetChanged();

        SqliteArchiveTileWriter writer = null;
        try {
            writer = new SqliteArchiveTileWriter(downloadedArea.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MapView mMapView = new MapView(this, null, null);
        CacheManager mgr = new CacheManager(mMapView, writer);
        CacheManagerCallback cacheManagerCallbackImpl = new CacheManagerCallbackImpl(this, writer, downloadedArea);

        BoundingBox bareBoundingBox = downloadedArea.getBoundingBox().toBareBoundingBox();

        CacheManager.DownloadingTask downloadingTask = mgr.downloadAreaAsyncNoUI(this, bareBoundingBox,
                downloadedArea.getMinZoom(), Constants.DOWNLOAD_MAX_ZOOM, cacheManagerCallbackImpl);

        taskMap.put(downloadedArea.getId(), downloadingTask);
        // TODO use downloadingTask to cancel in notification


        int nTiles = cacheManagerCallbackImpl.getTotalTiles();
        double estimatedSize = 1000 * (Constants.TILE_KB_SIZE * nTiles); // multiply to get in bytes

        RealmHelper.getInstance(this).beginTransaction();

        downloadedArea.setSize(estimatedSize);

        RealmHelper.getInstance(this).commitTransaction();
    }


    @Override
    public void onTaskComplete(DownloadedArea downloadedArea) {
        File file = new File(downloadedArea.getPath());
        long actualSize = file.length();
        String estimatedSizeFormatted = Formatter.formatShortFileSize(this, (long) downloadedArea.getSize());
        String actualSizeFormatted = Formatter.formatShortFileSize(this, actualSize);
        Log.d(LOG, "from estimated: " + estimatedSizeFormatted + " to " + actualSizeFormatted);


        RealmHelper.getInstance(this).beginTransaction();

        downloadedArea.setOngoing(false);
        downloadedArea.setSize(actualSize);

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
