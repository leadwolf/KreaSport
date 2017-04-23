package com.ccaroni.kreasport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.fragments.BottomSheetFragment;
import com.ccaroni.kreasport.fragments.ExploreFragment;
import com.ccaroni.kreasport.map.viewmodels.RaceVM;
import com.ccaroni.kreasport.other.PreferenceManager;


/**
 * Created by Master on 02/04/2017.
 */

public class ExploreActivity extends MainActivity implements BottomSheetFragment.BottomSheetInteractionListener{

    private ExploreFragment exploreFragemnt;
    private BottomSheetFragment bottomSheetFragment;

    private RaceVM raceVM;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(this, ExploreActivity.class.getSimpleName());
        restoreRaceVM();

        setupFragments();
    }

    @Override
    protected void onResume() {
        resetNavigationDrawer(navigationView.getMenu().getItem(1));
        setCurrentActivityIndex(1);
        super.onResume();
    }


    /**
     * Creates and adds this activities' fragments to R.id.content_main_frame_layout
     */
    private void setupFragments() {
        exploreFragemnt = (ExploreFragment) getFragment(R.id.nav_explore);
        bottomSheetFragment = (BottomSheetFragment) getFragment(R.id.ll_bottom_sheet);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main_frame_layout, exploreFragemnt, TAG_EXPLORE)
                .add(R.id.fragment_explore_root_coordlayout, bottomSheetFragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveState();
        super.onSaveInstanceState(outState);
    }

    /**
     * Saves MapVM with {@link PreferenceManager}
     */
    private void saveState() {
        if (raceVM == null) {
            raceVM = new RaceVM();
        }
        preferenceManager.saveRaceVM(raceVM);
    }

    /**
     * Restores the {@link RaceVM} with {@link PreferenceManager}
     */
    private void restoreRaceVM() {
        raceVM = preferenceManager.getRaceVM();
    }

    @Override
    public void onBottomSheetInteraction(Intent requestIntent) {
    }

    public RaceVM getRaceVM() {
        return raceVM;
    }
}
