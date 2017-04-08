package fr.univ_lille1.iut_info.caronic.kreasport.activities;

import android.content.Intent;
import android.os.Bundle;

import fr.univ_lille1.iut_info.caronic.kreasport.R;
import fr.univ_lille1.iut_info.caronic.kreasport.fragments.BottomSheetFragment;
import fr.univ_lille1.iut_info.caronic.kreasport.fragments.ExploreFragment;
import fr.univ_lille1.iut_info.caronic.kreasport.other.PreferenceManager;
import fr.univ_lille1.iut_info.caronic.kreasport.map.viewmodels.RaceVM;

/**
 * Created by Master on 02/04/2017.
 */

public class ExploreActivity extends MainActivity implements ExploreFragment.ExploreInteractionListener, BottomSheetFragment.BottomSheetInteractionListener{

    private ExploreFragment exploreFragemnt;
    private BottomSheetFragment bottomSheetFragment;

    private RaceVM raceVM;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(this, ExploreActivity.class.getSimpleName());
        restoreRaceVM();

        navigationView.getMenu().getItem(1).setChecked(true);

        setupFragments();
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
    public void onExploreInteraction(Intent requestIntent) {
        if (requestIntent == null) {
            throw new NullPointerException("Request intent should not be null");
        }

        String requestCode = requestIntent.getStringExtra(CALLBACK_KEY);
        if (requestCode == null) {
            return;
        }

        switch (requestCode) {
            case ExploreFragment.OVERLAY_ITEM_SELECTION:
                int raceIndex = requestIntent.getIntExtra(ExploreFragment.KEY_SELECTED_RACE, -1);
                int checkpointIndex = requestIntent.getIntExtra(ExploreFragment.KEY_SELECTED_CHECKPOINT, -1);
                if (raceIndex != -1 && checkpointIndex != -1)
                    raceVM.updateCurrentIndexes(raceIndex, checkpointIndex);
            default:
                break;
        }

    }

    @Override
    public void onBottomSheetInteraction(Intent requestIntent) {
    }

    public RaceVM getRaceVM() {
        return raceVM;
    }
}
