package fr.univ_lille1.iut_info.caronic.kreasport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import fr.univ_lille1.iut_info.caronic.kreasport.R;
import fr.univ_lille1.iut_info.caronic.kreasport.fragments.BottomSheet;
import fr.univ_lille1.iut_info.caronic.kreasport.fragments.ExploreFragment;

/**
 * Created by Master on 02/04/2017.
 */

public class ExploreActivity extends MainActivity implements ExploreFragment.ExploreInteractionListener, BottomSheet.BottomSheetInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationView.getMenu().getItem(1).setChecked(true);

        setupFragments();
    }

    /**
     * Creates and adds this activities' fragments to R.id.content_main_frame_layout
     */
    private void setupFragments() {
        Fragment fragment = restoreFragment(R.id.nav_explore);
        Fragment bottomSheet = restoreFragment(R.id.ll_bottom_sheet);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main_frame_layout, fragment, TAG_EXPLORE)
                .add(R.id.fragment_explore_root_coordlayout, bottomSheet)
                .commit();
    }

    @Override
    public void onExploreInteraction(Intent requestIntent) {

    }

    @Override
    public void onBottomSheetInteraction(Intent requestIntent) {

    }
}
