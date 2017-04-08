package fr.univ_lille1.iut_info.caronic.kreasport.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;

import java.util.ArrayList;
import java.util.List;

import fr.univ_lille1.iut_info.caronic.kreasport.R;
import fr.univ_lille1.iut_info.caronic.kreasport.activities.ExploreActivity;
import fr.univ_lille1.iut_info.caronic.kreasport.databinding.FragmentExploreBinding;
import fr.univ_lille1.iut_info.caronic.kreasport.map.CustomMapView;
import fr.univ_lille1.iut_info.caronic.kreasport.map.CustomOverlayItem;
import fr.univ_lille1.iut_info.caronic.kreasport.map.MapOptions;
import fr.univ_lille1.iut_info.caronic.kreasport.map.MapState;
import fr.univ_lille1.iut_info.caronic.kreasport.map.orienteering.Race;
import fr.univ_lille1.iut_info.caronic.kreasport.other.PreferenceManager;
import fr.univ_lille1.iut_info.caronic.kreasport.viewmodels.RaceVM;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExploreInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    private static final String LOG = ExploreFragment.class.getSimpleName();

    public static final String OVERLAY_ITEM_SELECTION = "kreasport.fragment_explore.request_reason.overlay_item_selection";
    public static final String KEY_SELECTED_RACE = "kreasport.fragment_explore.keys.selected_race";
    public static final String KEY_SELECTED_CHECKPOINT = "kreasport.fragment_explore.keys.selected_checkpoint";


    /* DEFAULTS */
    private static final String KEY_MAP_OPTIONS = "kreasport.fragment_explore.keys.map_options";
    private MapOptions mMapOptions;

    /* KEYS */
    private static final String KEY_MAP_STATE = "kreasport.fragment_explore.keys.map_state";
    private static final String KEY_RACE_VM = "kreasport.fragment_explore.keys.race_state";


    private CustomMapView mMapView;
    private MapState mMapState;

    private ItemizedOverlayWithFocus<CustomOverlayItem> ongoingRaceOverlay;
    private ItemizedOverlayWithFocus<CustomOverlayItem> raceListOverlay;

    private ExploreInteractionListener mListener;

    private List<Race> raceList;
    private RaceVM raceVM;

    private PreferenceManager preferenceManager;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * @param defaultState default params, used only if none found in {@link android.content.SharedPreferences}
     * @param mMapOptions  to always be set
     * @return
     */
    public static ExploreFragment newInstance(MapState defaultState, @NonNull MapOptions mMapOptions) {
        ExploreFragment fragment = new ExploreFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_MAP_STATE, defaultState);
        args.putSerializable(KEY_MAP_OPTIONS, mMapOptions);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMapState = (MapState) getArguments().getSerializable(KEY_MAP_STATE);
            mMapOptions = (MapOptions) getArguments().getSerializable(KEY_MAP_OPTIONS);
        }

        raceVM = ((ExploreActivity) getActivity()).getRaceVM();

        preferenceManager = new PreferenceManager(getContext(), ExploreFragment.class.getSimpleName());
        restoreState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentExploreBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);
        binding.setBottomSheetVM(raceVM);

        View rootView = binding.getRoot();

        mMapView = new CustomMapView(getActivity(), mMapOptions, mMapState);
        initRaceOverlays();

        binding.fragmentExploreFrameLayout.addView(mMapView);

        return rootView;
    }

    /**
     * DUMMY, example of how to add items to the overlay
     */
    public void onClick() {

        Toast.makeText(getContext(), "adding item at random", Toast.LENGTH_SHORT).show();

        final ArrayList<CustomOverlayItem> items = new ArrayList<>();

        double random_lon = (Math.random() * 360) - 180;
        double random_lat = (Math.random() * 180) - 90;
        CustomOverlayItem overlayItem = new CustomOverlayItem("A random point", "SampleDescription", new GeoPoint(random_lat,
                random_lon));
        overlayItem.setMarker(ContextCompat.getDrawable(getContext(), R.drawable.ic_beenhere_blue_700_24dp));
        items.add(overlayItem);

        ongoingRaceOverlay.addItems(items);
        mMapView.invalidate();
    }

    /**
     * Creates the overlays for the current race and all the races
     */
    private void initRaceOverlays() {

        // TODO add switch to verify which overlay to add

        ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem> itemGestureListener = RaceVM.getIconGestureListener(mListener);
        initRaceListOverlay(itemGestureListener);
        initOngoingRaceOverlay(itemGestureListener);
    }

    /**
     * Creates the overlay listing all the races and adds it to mMapView
     */
    private void initRaceListOverlay(ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem> itemGestureListener) {

        // TODO get the races from the VM

        raceListOverlay = new ItemizedOverlayWithFocus<>(new ArrayList<CustomOverlayItem>(), itemGestureListener, getActivity());
        raceListOverlay.setFocusItemsOnTap(true);

        mMapView.getOverlays().add(raceListOverlay);
    }

    /**
     * Creates an overlay with the current race and adds it to mMapView
     */
    private void initOngoingRaceOverlay(ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem> itemGestureListener) {

        // TODO get the races from the VM

        ongoingRaceOverlay = new ItemizedOverlayWithFocus<>(new ArrayList<CustomOverlayItem>(), itemGestureListener, getActivity());
        ongoingRaceOverlay.setFocusItemsOnTap(true);

        mMapView.getOverlays().add(ongoingRaceOverlay);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveState();
        super.onSaveInstanceState(outState);
    }

    /**
     * Saves MapState and RaceState with {@link PreferenceManager}
     */
    private void saveState() {
        if (mMapView == null) {
            return;
        }
        preferenceManager.saveMapState(mMapState);

    }

    /**
     * Restores the {@link MapState} with {@link PreferenceManager}
     */
    private void restoreState() {
        mMapState = preferenceManager.getMapState();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExploreInteractionListener) {
            mListener = (ExploreInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ExploreInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ExploreInteractionListener {
        void onExploreInteraction(Intent requestIntent);
    }

}
