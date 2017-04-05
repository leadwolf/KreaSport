package fr.univ_lille1.iut_info.caronic.kreasport.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.univ_lille1.iut_info.caronic.kreasport.R;
import fr.univ_lille1.iut_info.caronic.kreasport.maps.CustomMapView;
import fr.univ_lille1.iut_info.caronic.kreasport.maps.CustomOverlayItem;
import fr.univ_lille1.iut_info.caronic.kreasport.maps.MapOptions;
import fr.univ_lille1.iut_info.caronic.kreasport.maps.MapState;
import fr.univ_lille1.iut_info.caronic.kreasport.maps.RaceState;

import static fr.univ_lille1.iut_info.caronic.kreasport.activities.MainActivity.CALLBACK_KEY;

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


    @BindView(R.id.fragment_explore_frame_layout)
    FrameLayout frameLayout;

    /* DEFAULTS */
    private static final String KEY_MAP_OPTIONS = "kreasport.fragment_explore.keys.map_options";
    private MapOptions mMapOptions;

    /* KEYS */
    private static final String KEY_MAP_STATE = "kreasport.fragment_explore.keys.map_state";
    private static final String KEY_RACE_STATE = "kreasport.fragment_explore.keys.race_state";


    private CustomMapView mMapView;
    private MapState mMapState;
    private RaceState raceState;

    private ItemizedOverlayWithFocus<CustomOverlayItem> ongoingRaceOverlay;
    private ItemizedOverlayWithFocus<CustomOverlayItem> raceListOverlay;
    private ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem> itemGestureListener;

    private ExploreInteractionListener mListener;

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
            Log.d(LOG, "onCreate restore args");
            mMapState = (MapState) getArguments().getSerializable(KEY_MAP_STATE);
            mMapOptions = (MapOptions) getArguments().getSerializable(KEY_MAP_OPTIONS);
        }
        restoreState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, rootView);

        mMapView = new CustomMapView(getActivity(), mMapOptions, mMapState);

        initItemListener();
        initRaceListOverlay();
        initOngoingRaceOverlay();

        frameLayout.addView(mMapView);

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
     * Creates the overlay for listing all the races
     */
    private void initRaceListOverlay() {
        raceListOverlay = new ItemizedOverlayWithFocus<>(new ArrayList<CustomOverlayItem>(), itemGestureListener, getActivity());

        raceListOverlay.setFocusItemsOnTap(true);
        raceListOverlay.setFocusedItem(0);

        mMapView.getOverlays().add(raceListOverlay);
    }

    /**
     * Creates an overlay with the current race
     */
    private void initOngoingRaceOverlay() {
        ongoingRaceOverlay = new ItemizedOverlayWithFocus<>(new ArrayList<CustomOverlayItem>(), itemGestureListener, getActivity());

        ongoingRaceOverlay.setFocusItemsOnTap(true);
        ongoingRaceOverlay.setFocusedItem(0);

        mMapView.getOverlays().add(ongoingRaceOverlay);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveState();
        super.onSaveInstanceState(outState);
    }

    /**
     * Saves MapState and RaceState
     */
    private void saveState() {
        if (mMapView == null) {
            return;
        }

        if (raceState == null) {
            raceState = new RaceState(false);
        }

        Gson gson = new Gson();

        mMapState = new MapState(mMapView);
        String mapStateJson = gson.toJson(mMapState, MapState.class);
        String raceStateJson = gson.toJson(raceState, RaceState.class);

        getActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_MAP_STATE, mapStateJson)
                .putString(KEY_RACE_STATE, raceStateJson)
                .apply();

    }

    private void restoreState() {
        Gson gson = new Gson();
        restoreMapState(gson);
        restoreRaceState(gson);
    }

    private void restoreRaceState(Gson gson) {
        String raceStateJson = getActivity().getPreferences(Context.MODE_PRIVATE)
                .getString(KEY_RACE_STATE, "");
        if (raceStateJson.equals("")) {
            raceState = new RaceState(false);
            return;
        }
        raceState = gson.fromJson(raceStateJson, RaceState.class);
    }

    /**
     * Restores the map state from SharedPreferences if finds any.
     */
    private void restoreMapState(Gson gson) {
        String mapStateJson = getActivity().getPreferences(Context.MODE_PRIVATE)
                .getString(KEY_MAP_STATE, "");
        if (mapStateJson.equals("")) {
            return;
        }

        mMapState = gson.fromJson(mapStateJson, MapState.class);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExploreInteractionListener) {
            mListener = (ExploreInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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

    private void initItemListener() {
        if (itemGestureListener == null) {
            itemGestureListener = new ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem>() {
                @Override
                public boolean onItemSingleTapUp(final int index, final CustomOverlayItem item) {
                    Intent request = new Intent();

                    request.putExtra(CALLBACK_KEY, ExploreFragment.OVERLAY_ITEM_SELECTION);
                    request.putExtra(KEY_SELECTED_RACE, item.getRaceId());
                    request.putExtra(KEY_SELECTED_CHECKPOINT, item.getId());

                    mListener.onExploreInteraction(request);
                    return true;
                }

                @Override
                public boolean onItemLongPress(final int index, final CustomOverlayItem item) {
                    return false;
                }
            };
        }
    }
}
