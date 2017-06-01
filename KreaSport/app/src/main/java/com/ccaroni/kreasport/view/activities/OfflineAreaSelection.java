package com.ccaroni.kreasport.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.map.views.CustomMapView;
import com.ccaroni.kreasport.view.fragments.AreaSelectionFragment;

/**
 * Created by Master on 01/06/2017.
 */

public class OfflineAreaSelection extends BaseActivity implements AreaSelectionFragment.OnAreaSelectionInteractionListener, CustomMapView.MapViewCommunication {

    private CustomMapView mMapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.customCreate(savedInstanceState, R.layout.activity_base);

        setCurrentActivityIndex(3);

        setupMapFragment();
    }

    private void setupMapFragment() {
        Fragment fragment = getFragment(R.id.nav_offline_area);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main_frame_layout, fragment, TAG_HOME)
                .commit();

    }

    @Override
    protected void onResume() {
        resetNavigationDrawer(navigationView.getMenu().getItem(3));
        setCurrentActivityIndex(3);
        super.onResume();
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onMapBackgroundTouch() {

    }
}
