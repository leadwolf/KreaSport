package com.ccaroni.kreasport.fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.activities.ExploreActivity;
import com.ccaroni.kreasport.databinding.FragmentExploreBinding;
import com.ccaroni.kreasport.map.GeofenceTransitionsIntentService;
import com.ccaroni.kreasport.map.models.Checkpoint;
import com.ccaroni.kreasport.map.models.MapOptions;
import com.ccaroni.kreasport.map.models.Race;
import com.ccaroni.kreasport.map.viewmodels.MapVM;
import com.ccaroni.kreasport.map.viewmodels.RaceVM;
import com.ccaroni.kreasport.map.views.CustomMapView;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.other.Constants;
import com.ccaroni.kreasport.other.PreferenceManager;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment implements LocationListener, ResultCallback<Status> {

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

    private boolean hasFix;
    private PendingIntent mGeofencePendingIntent;

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
            addGeofence();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void addGeofence() {
        LocationServices.GeofencingApi.addGeofences(
                ((ExploreActivity) getActivity()).getGoogleApiClient(),
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }

    private GeofencingRequest getGeofencingRequest() {
        Checkpoint checkpoint = raceVM.getActiveCheckpoint();
        if (!checkpoint.getId().equals("")) {
            GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

            builder.addGeofence(
                    new Geofence.Builder()
                            .setRequestId(checkpoint.getId())

                            .setCircularRegion(
                                    checkpoint.getLatitude(),
                                    checkpoint.getLongitude(),
                                    Constants.GEOFENCE_RADIUS_METERS
                            )
                            .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_MILLISECONDS)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL)
                            .build()
            );
            return builder.build();
        } else {
            Log.d(LOG, "checkpoint to trigger does not exist");
            return null;
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getContext(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
                mLocationRequest, this);
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                ((ExploreActivity) getActivity()).getGoogleApiClient(), this);
    }


    @Override
    public void onLocationChanged(Location location) {
        //after the first fix, schedule the task to change the icon

        final DirectedLocationOverlay overlay = mMapView.getLocationOverlay();

        if (!hasFix) {
            TimerTask changeIcon = new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.direction_arrow);
                                    overlay.setDirectionArrow(drawable.getBitmap());
                                } catch (Throwable t) {
                                    //insultates against crashing when the user rapidly switches fragments/activities
                                }
                            }
                        });

                }
            };
            Timer timer = new Timer();
            timer.schedule(changeIcon, 5000);

        }
        hasFix = true;
        overlay.setBearing(location.getBearing());
        overlay.setAccuracy((int) location.getAccuracy());
        overlay.setLocation(new GeoPoint(location.getLatitude(), location.getLongitude()));
        mMapView.invalidate();
    }

    /**
     * Result of geofence callback
     * @param status
     */
    @Override
    public void onResult(@NonNull Status status) {
        Log.d(LOG, "callback form geofence");
    }
}
