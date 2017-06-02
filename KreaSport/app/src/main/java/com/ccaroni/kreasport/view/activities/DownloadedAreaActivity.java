package com.ccaroni.kreasport.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.DownloadedArea;
import com.ccaroni.kreasport.databinding.ActivityDownloadedAreaBinding;
import com.ccaroni.kreasport.map.viewmodels.MapVM;
import com.ccaroni.kreasport.map.views.CustomMapView;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import static com.ccaroni.kreasport.view.activities.AreaSelectionActivity.KEY_AREA_ID;

public class DownloadedAreaActivity extends AppCompatActivity implements CustomMapView.MapViewCommunication {

    private ActivityDownloadedAreaBinding binding;

    private MapView mMapView;
    private DownloadedArea downloadedArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_downloaded_area);

        setInfo();
        setMap();
        setMisc();
    }

    private void setInfo() {
        String areaId = getIntent().getStringExtra(KEY_AREA_ID);
        if (areaId == null) {
            throw new IllegalArgumentException("Expected area id to be passed");
        }

        downloadedArea = RealmHelper.getInstance(this).findDownloadedAreaById(areaId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(downloadedArea.getName());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int roundedSize = (int) Math.round(downloadedArea.getSize());
        String date = downloadedArea.getDateDownloaded();
        date = LocalDate.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toString();

        binding.contentDownloadedArea.tvAreaSize.setText(getString(R.string.downloaded_area_description, roundedSize, date));
    }

    private void setMap() {
        BoundingBox boundingBox = downloadedArea.getBoundingBox().toBareBoundingBox();
        MapVM mapVM = new MapVM(boundingBox.getCenter(), 13);

        mMapView = new CustomMapView(this, null, mapVM);

        binding.contentDownloadedArea.rlDownloadedAreaMap.addView(mMapView);

    }

    private void setMisc() {
    }

    @Override
    public void onMapBackgroundTouch() {

    }
}
