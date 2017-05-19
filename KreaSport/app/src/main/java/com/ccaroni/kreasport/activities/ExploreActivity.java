package com.ccaroni.kreasport.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.databinding.ActivityExploreBinding;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExploreActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback
        <Status> {

    private static final String LOG = ExploreActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private ActivityExploreBinding binding;

    private CustomMapView mMapView;
    private RaceVM raceVM;

    private PreferenceManager preferenceManager;

    private GoogleApiClient mGoogleApiClient;
    private boolean hasFix;
    private PendingIntent mGeofencePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_explore);
        super.secondaryCreate();

        resetNavigationDrawer(navigationView.getMenu().getItem(1));
        setCurrentActivityIndex(1);

        // First we need to check availability of play services
        if (checkPlayServices())
            // Building the GoogleApi client
            buildGoogleApiClient();
        else
            onNavigationItemSelected(navigationView.getMenu().getItem(0));

        preferenceManager = new PreferenceManager(this, ExploreActivity.class.getSimpleName());

        raceVM = preferenceManager.getRaceVM();
        binding.setRaceVM(raceVM);

        setupMap();
    }

    private void setupMap() {
        MapOptions mMapOptions = new MapOptions()
                .setEnableLocationOverlay(true)
                .setEnableMultiTouchControls(true)
                .setEnableScaleOverlay(true);

        MapVM mMapVM = new MapVM(new GeoPoint(50.633621, 3.0651845), 9);

        // needs to be called before the MapView is created to enable hw
        Configuration.getInstance().setMapViewHardwareAccelerated(true);

        mMapView = new CustomMapView(this, mMapOptions, mMapVM);
        initRaceOverlays();

        binding.appBarMain.layoutExplore.frameLayoutMap.addView(mMapView);
    }

    /**
     * Creates an overlay of all the races needing to be displayed.
     * Either creates an overlay for all the races or full overlay of one race.
     */
    private void initRaceOverlays() {
        ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem> itemGestureListener = raceVM.getIconGestureListener();
        List<CustomOverlayItem> raceAsOverlay = Race.toPrimaryCustomOverlay(raceVM.getRacesForOverlay());

        ItemizedOverlayWithFocus raceListOverlay = new ItemizedOverlayWithFocus<>(raceAsOverlay, itemGestureListener, this);
        raceListOverlay.setFocusItemsOnTap(true);

        mMapView.getOverlays().add(raceListOverlay);
        if (raceVM.isRaceActive()) {
            Log.d(LOG, "adding geofence");
            addGeofence();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void addGeofence() {
        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
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
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetNavigationDrawer(navigationView.getMenu().getItem(1));
        setCurrentActivityIndex(1);
    }

    /* GOOGLE API CLIENT */

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(LOG, "This device does not support Google Play Services.");
                selectDrawerItem(navigationView.getMenu().getItem(0));
                Toast.makeText(this, "Your device does not support Google Play Services.", Toast.LENGTH_SHORT).show();
                // TODO error
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    @SuppressWarnings({"MissingPermission"})
    private void startLocationUpdates() {
        // Create the location request
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.GEOLOCATION_UPDATE_INTERVAL)
                .setFastestInterval(Constants.GEOLOCATION_UPDATE_FASTEST_INTERVAL);
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        //after the first fix, schedule the task to change the icon

        final DirectedLocationOverlay overlay = mMapView.getLocationOverlay();

        if (!hasFix) {
            TimerTask changeIcon = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.direction_arrow);
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

    @Override
    public void onResult(@NonNull Status status) {
        Toast.makeText(this, "Geofence callback", Toast.LENGTH_SHORT).show();
    }
}
