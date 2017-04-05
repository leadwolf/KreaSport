package fr.univ_lille1.iut_info.caronic.kreasport.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;

import java.util.List;

import fr.univ_lille1.iut_info.caronic.kreasport.R;
import fr.univ_lille1.iut_info.caronic.kreasport.fragments.BottomSheetFragment;
import fr.univ_lille1.iut_info.caronic.kreasport.fragments.ExploreFragment;
import fr.univ_lille1.iut_info.caronic.kreasport.orienteering.Checkpoint;
import fr.univ_lille1.iut_info.caronic.kreasport.orienteering.Race;
import fr.univ_lille1.iut_info.caronic.kreasport.other.Constants;

import static fr.univ_lille1.iut_info.caronic.kreasport.other.Constants.savedRaceLisType;

/**
 * Created by Master on 02/04/2017.
 */

public class ExploreActivity extends MainActivity implements ExploreFragment.ExploreInteractionListener, BottomSheetFragment.BottomSheetInteractionListener{

    private ExploreFragment exploreFragemnt;
    private BottomSheetFragment bottomSheetFragment;

    private List<Race> raceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationView.getMenu().getItem(1).setChecked(true);

        setupFragments();

        initRaceList();
    }

    private void initRaceList() {
        String raceListJson = getPreferences(MODE_PRIVATE).getString(Constants.KEY_SAVED_RACES, "");
        if (raceListJson.equals("")) {
            return;
        }

        raceList = new Gson().fromJson(raceListJson, savedRaceLisType);
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
                updateBottomSheetInfo(raceIndex, checkpointIndex);
            default:
                break;
        }

    }

    @Override
    public void onBottomSheetInteraction(Intent requestIntent) {
    }

    /**
     * Updates the bottom sheet according the checkpoint specified in the parameters
     * @param raceIndex
     * @param checkpointIndex
     */
    private void updateBottomSheetInfo(int raceIndex, int checkpointIndex) {
        Race currentRace = raceList.get(raceIndex);
        Checkpoint currentCheckpoint = currentRace.getCheckpoint(checkpointIndex);
        bottomSheetFragment.updateInfo(currentRace, currentCheckpoint);
    }
}
