package com.ccaroni.kreasport.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.DownloadedArea;
import com.ccaroni.kreasport.view.adapter.DownloadedAreaAdapter;

import io.realm.RealmResults;

public class OfflineAreasActivity extends BaseActivity {

    private static final String LOG = OfflineAreasActivity.class.getSimpleName();
    private static final int CUSTOM_AREA_REQUEST_CODE = 100;


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

        setupListView();
    }

    private void setupListView() {
        ListView listView = (ListView) findViewById(R.id.list_view_downloaded_areas);

        RealmResults<DownloadedArea> downloadedAreas = RealmHelper.getInstance(this).findAllDownloadedAreas();
        DownloadedAreaAdapter adapter = new DownloadedAreaAdapter(this, downloadedAreas);

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
}
