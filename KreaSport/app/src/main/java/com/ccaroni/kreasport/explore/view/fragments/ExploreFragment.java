package com.ccaroni.kreasport.explore.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.databinding.FragmentExploreBinding;
import com.ccaroni.kreasport.explore.view.IExploreView;
import com.ccaroni.kreasport.explore.vm.impl.ExploreVM;
import com.ccaroni.kreasport.map.MapOptions;
import com.ccaroni.kreasport.map.MapState;
import com.ccaroni.kreasport.map.views.CustomMapView;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;

public class ExploreFragment extends Fragment implements CustomMapView.MapTouchReceiver, IExploreView {

    private static final String TAG = ExploreFragment.class.getSimpleName();

    private FragmentExploreBinding binding;

    private ExploreVM exploreVM;
    private CustomMapView mMapView;


    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);
        View view = binding.getRoot();

        exploreVM = new ExploreVM(this);
        binding.setExploreVM(exploreVM);

        setupMap();

        return view;
    }

    private void setupMap() {
        initMap();
        // TODO init race overlays

        binding.flMap.addView(mMapView);
    }


    /**
     * Initializes {@link #mMapView}
     */
    private void initMap() {
        MapOptions mMapOptions = new MapOptions()
                .setEnableLocationOverlay(true)
                .setEnableMultiTouchControls(true)
                .setEnableScaleOverlay(true);

        MapState mMapState = new MapState(new GeoPoint(50.633621, 3.0651845), 9);

        // needs to be called before the MapView is created to enable hw acceleration
        Configuration.getInstance().setMapViewHardwareAccelerated(true);
        mMapView = new CustomMapView(this.getContext(), this, mMapOptions, mMapState);
    }

    @Override
    public void onMapBackgroundTouch() {
        this.exploreVM.onBackgroundPressed();
    }

    @Override
    public void displayStartError(String message) {
        // TODO

    }

    @Override
    public void displayStopError(String message) {
        // TODO

    }

    @Override
    public void recenterOnUserPosition() {
        // TODO

    }
}
