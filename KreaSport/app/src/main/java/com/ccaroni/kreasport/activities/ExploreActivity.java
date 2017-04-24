package com.ccaroni.kreasport.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.databinding.ActivityExploreBinding;
import com.ccaroni.kreasport.map.models.MapOptions;
import com.ccaroni.kreasport.map.viewmodels.MapVM;
import com.ccaroni.kreasport.map.viewmodels.RaceVM;
import com.ccaroni.kreasport.map.views.CustomMapView;
import com.ccaroni.kreasport.other.PreferenceManager;

import org.osmdroid.util.GeoPoint;

public class ExploreActivity extends BaseActivity {

    private ActivityExploreBinding binding;

    private CustomMapView mMapView;
    private RaceVM raceVM;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_explore);
        super.secondaryCreate();

        preferenceManager = new PreferenceManager(this, ExploreActivity.class.getSimpleName());

        raceVM = preferenceManager.getRaceVM();
        binding.setRaceVM(raceVM);

        setupMap();
    }

    private void setupMap() {
        MapOptions mMapOptions = new MapOptions()
                .setEnableLocationOverlay(true)
                .setEnableCompass(true)
                .setEnableMultiTouchControls(true)
                .setEnableScaleOverlay(true);

        MapVM mMapVM = new MapVM(new GeoPoint(50.633621, 3.0651845), 9);

        mMapView = new CustomMapView(this, mMapOptions, mMapVM);
    }
}
