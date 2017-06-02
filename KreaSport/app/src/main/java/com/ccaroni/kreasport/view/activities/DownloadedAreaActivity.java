package com.ccaroni.kreasport.view.activities;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.DownloadedArea;

import static com.ccaroni.kreasport.view.activities.AreaSelectionActivity.KEY_AREA_ID;

public class DownloadedAreaActivity extends AppCompatActivity {

    private ViewDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_downloaded_area);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupInfo();
    }

    private void setupInfo() {
        String areaId = getIntent().getStringExtra(KEY_AREA_ID);
        if (areaId == null) {
            throw new IllegalArgumentException("Expected area id to be passed");
        }

        DownloadedArea downloadedArea = RealmHelper.getInstance(this).findDownloadedAreaById(areaId);

        TextView tvAreaName = (TextView) findViewById(R.id.tv_area_name);
        tvAreaName.setText(downloadedArea.getName());


    }

}
