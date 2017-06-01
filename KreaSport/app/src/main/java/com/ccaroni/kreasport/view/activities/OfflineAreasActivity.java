package com.ccaroni.kreasport.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import com.ccaroni.kreasport.R;

public class OfflineAreasActivity extends BaseActivity {

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

        setButtons();
    }

    private void setButtons() {
        Button downloadCustomArea = (Button) findViewById(R.id.btn_download_custom_area);
        downloadCustomArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(OfflineAreasActivity.this, Dummy.class), CUSTOM_AREA_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetNavigationDrawer(navigationView.getMenu().getItem(3));
        setCurrentActivityIndex(3);
    }

}
