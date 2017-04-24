package com.ccaroni.kreasport.fragments;

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

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.activities.ExploreActivity;
import com.ccaroni.kreasport.databinding.FragmentExploreBinding;
import com.ccaroni.kreasport.map.views.CustomLocationListener;
import com.ccaroni.kreasport.map.views.CustomMapView;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.map.models.MapOptions;
import com.ccaroni.kreasport.map.viewmodels.MapVM;
import com.ccaroni.kreasport.map.models.Race;
import com.ccaroni.kreasport.other.PreferenceManager;
import com.ccaroni.kreasport.map.viewmodels.RaceVM;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    private static final String LOG = ExploreFragment.class.getSimpleName();
    private static final long UPDATE_INTERVAL = 2000;
    private static final long FASTEST_INTERVAL = 1000;

    /* DEFAULTS */
    private static final String KEY_MAP_OPTIONS = "kreasport.fragment_explore.keys.map_options";
    private MapOptions mMapOptions;

    /* KEYS */
    private static final String KEY_MAP_STATE = "kreasport.fragment_explore.keys.map_state";

    private CustomMapView mMapView;
    private MapVM mMapVM;

    private ItemizedOverlayWithFocus<CustomOverlayItem> raceListOverlay;

    private RaceVM raceVM;

    private PreferenceManager preferenceManager;
    private CustomLocationListener locationListener;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * @param defaultState default params, used only if none found in {@link android.content.SharedPreferences}
     * @param mMapOptions  to always be set
     * @return
     */
    public static ExploreFragment newInstance(MapVM defaultState, @NonNull MapOptions mMapOptions) {
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

        raceVM = ((ExploreActivity) getActivity()).getRaceVM();

        preferenceManager = new PreferenceManager(getContext(), ExploreFragment.class.getSimpleName());
        restoreState();

        if (getArguments() != null) {
            mMapVM = (MapVM) getArguments().getSerializable(KEY_MAP_STATE);
            mMapOptions = (MapOptions) getArguments().getSerializable(KEY_MAP_OPTIONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentExploreBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);
        binding.setBottomSheetVM(raceVM);

        View rootView = binding.getRoot();

        mMapView = new CustomMapView(getActivity(), mMapOptions, mMapVM);
        locationListener = new CustomLocationListener(getActivity(), mMapView, mMapView.getLocationOverlay());

        initRaceOverlays();

        binding.fragmentExploreFrameLayout.addView(mMapView);

        return rootView;
    }

    /**
     * Creates an overlay of all the races needing to be displayed.
     * Either creates an overlay for all the races or full overlay of one race.
     */
    private void initRaceOverlays() {
        ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem> itemGestureListener = raceVM.getIconGestureListener();
        List<CustomOverlayItem> raceAsOverlay = Race.toPrimaryCustomOverlay(raceVM.getRacesForOverlay());

        raceListOverlay = new ItemizedOverlayWithFocus<>(raceAsOverlay, itemGestureListener, getActivity());
        raceListOverlay.setFocusItemsOnTap(true);

        mMapView.getOverlays().add(raceListOverlay);
        if (raceVM.isRaceActive()) {
            addCheckpointTriggers();
        }
    }

    private void addCheckpointTriggers() {
        // TODO
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
        if (mMapView == null) {
            return;
        } else {
            mMapVM = new MapVM(mMapView);
        }
        preferenceManager.saveMapState(mMapVM);

    }

    /**
     * Restores the {@link MapVM} with {@link PreferenceManager}
     */
    private void restoreState() {
        mMapVM = preferenceManager.getMapState();
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

        raceListOverlay.addItems(items);
        mMapView.invalidate();
    }

    @SuppressWarnings({"MissingPermission"})
    public void startLocationUpdates() {
        // Create the location request
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(((ExploreActivity) getActivity()).getGoogleApiClient(),
                mLocationRequest, locationListener);
    }
}
