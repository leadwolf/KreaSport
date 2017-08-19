package com.ccaroni.kreasport.view.activities;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.dto.Riddle;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.databinding.ActivityExploreBinding;
import com.ccaroni.kreasport.map.MapDefaults;
import com.ccaroni.kreasport.map.MapOptions;
import com.ccaroni.kreasport.map.views.CustomMapView;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.race.RaceCommunication;
import com.ccaroni.kreasport.race.RaceVM;
import com.ccaroni.kreasport.race.impl.RaceVMImpl;
import com.ccaroni.kreasport.service.RacingService;
import com.ccaroni.kreasport.service.geofence.GeofenceTransitionsIntentService;
import com.ccaroni.kreasport.service.geofence.GeofenceUtils;
import com.ccaroni.kreasport.service.location.GoogleLocationService;
import com.ccaroni.kreasport.service.location.LocationUtils;
import com.ccaroni.kreasport.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.ccaroni.kreasport.service.geofence.GeofenceTransitionsIntentService.GEOFENCE_TRIGGERED;
import static com.ccaroni.kreasport.service.location.LocationUtils.KEY_LOCATION_PREFS_FILENAME;

public class ExploreActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback
        <Status>, RaceCommunication, CustomMapView.MapViewCommunication, LocationUtils.LocationUtilsSubscriber {

    private static final String LOG = ExploreActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_CODE_RIDDLE_ANSWER = 100;

    private static final String KEY_BASE = "com.ccaroni.kreasport." + ExploreActivity.class.getSimpleName() + ".key.";
    public static final String KEY_RIDDLE = KEY_BASE + ".riddle";


    private ActivityExploreBinding binding;

    private CustomMapView mMapView;
    private Chronometer chronometer;
    private ItemizedOverlayWithFocus raceListOverlay;

    private RaceVM raceVM;

    private GeofenceReceiver receiver;

    private GoogleApiClient mGoogleApiClient;
    private boolean hasFix;
    private PendingIntent mGeofencePendingIntent;

    private boolean googlePlayServicesAvailable;

    private LocationUtils mLocationUtils;
    private GeofenceUtils mGeofenceUtils;
    private SharedPreferences.OnSharedPreferenceChangeListener locationPrefsListener;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_explore);
        super.secondaryCreate();

        resetNavigationDrawer(navigationView.getMenu().getItem(1));
        setCurrentActivityIndex(1);

        gson = new Gson();

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            // GeofenceReceiver will receive the geofence results once validated by GeofenceTransitionsIntentService
            LocalBroadcastManager lbc = LocalBroadcastManager.getInstance(this);
            receiver = new GeofenceReceiver();
            lbc.registerReceiver(receiver, new IntentFilter(GEOFENCE_TRIGGERED));
        } else {
            // Force to go back to Home
//            onNavigationItemSelected(navigationView.getMenu().getItem(0));
//            finish();

            // TODO don't redirect here, move the whole verification in the launcher activity instead, this commit is only a quickfix
        }

        mLocationUtils = new LocationUtils(this);
        mGeofenceUtils = new GeofenceUtils(this);

        // Ensure that the racing service is active, multiple calls to an already running service are taken care of
        Intent racingServiceIntent = new Intent(this, RacingService.class);
        startService(racingServiceIntent);

        raceVM = new RaceVMImpl(this, mLocationUtils);
        binding.setRaceVM(raceVM);

        setBindings();

        setupMap();
    }

    private void setBindings() {
        chronometer = binding.appBarMain.layoutExplore.bottomSheet.bottomSheetActiveInfo.bottomSheetChronometer;
    }

    private void setupMap() {
        MapOptions mMapOptions = new MapOptions()
                .setEnableLocationOverlay(true)
                .setEnableMultiTouchControls(true)
                .setEnableScaleOverlay(true);

        MapDefaults mMapDefaults = new MapDefaults(new GeoPoint(50.633621, 3.0651845), 9);

        // needs to be called before the MapView is created to enable hw acceleration
        Configuration.getInstance().setMapViewHardwareAccelerated(true);

        mMapView = new CustomMapView(this, mMapOptions, mMapDefaults);
        initRaceOverlays();

        binding.appBarMain.layoutExplore.frameLayoutMap.addView(mMapView);
    }

    /**
     * Creates an overlay of all the race(s) needing to be displayed with help from the {@link RaceVM}.
     * Either creates an overlay for all the races or full overlay of one race.
     * The {@link RaceVM} is in charge of returning the relevant race with all its checkpoints or all the races.
     */
    private void initRaceOverlays() {
        ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem> itemGestureListener = raceVM.getIconGestureListener();
        List<CustomOverlayItem> items = raceVM.getOverlayItems();

        raceListOverlay = new ItemizedOverlayWithFocus<>(items, itemGestureListener, this);
//        raceListOverlay.setFocusItemsOnTap(true);

        mMapView.getOverlays().add(raceListOverlay);
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
        Log.d(LOG, "creating google api client");
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
            Log.d(LOG, "Google Play Services not available!");

            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(LOG, "This device does not support Google Play Services.");
                selectDrawerItem(navigationView.getMenu().getItem(0));
                Toast.makeText(this, "Your device does not support Google Play Services.", Toast.LENGTH_SHORT).show();
                // TODO error
            }
            googlePlayServicesAvailable = false;
            return false;
        }
        Log.d(LOG, "Google Play Services is available!");
        googlePlayServicesAvailable = true;
        return true;
    }

    /**
     * Geofence creation callback
     *
     * @param status
     */
    @Override
    public void onResult(@NonNull Status status) {
        Log.d(LOG, "geofence creation callback with status " + status);
    }

    /**
     * Updates the location icon
     *
     * @param location where the new location is
     */
    private void updateLocationIcon(Location location) {
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


    /**
     * The single point where we connect to the google client api to make sure we only use resources when this activity is in use.
     */
    @Override
    protected void onStart() {
        if (googlePlayServicesAvailable) {
            mGoogleApiClient.connect();
        }
        mLocationUtils.startLocationUpdates();
        super.onStart();
    }

    /**
     * Disconnects the google client api for battery considerations.
     */
    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Stops location updates when this activity is paused for battery considerations.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mLocationUtils.stopLocationUpdates();
        raceVM.saveOngoingBaseTime();
    }

    /**
     * On connection to the google api client
     *
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        raceVM.onStart();
        if (raceVM.isRaceActive()) {
            Log.d(LOG, "adding geofence");
            mGeofenceUtils.addGeofences(raceVM.getActiveCheckpoint());
        }
    }

    /**
     * On connection suspended to the google api client
     *
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * On connection failed to the google api client
     *
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    /* RACE COMMUNICATION */

    @Override
    public void startChronometer(long newBase) {
        chronometer.setBase(newBase);
        chronometer.start();
    }

    @Override
    public void stopChronometer() {
        chronometer.stop();
    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addGeoFence(RealmCheckpoint checkpoint) {
        mGeofenceUtils.addGeofences(checkpoint);
    }


    @Override
    public void revealNextCheckpoint(CustomOverlayItem nextCheckpoint) {
        Log.d(LOG, "revealing next checkpoint: " + nextCheckpoint);

        raceListOverlay.addItem(nextCheckpoint);

        mMapView.invalidate();

    }

    /**
     * Once the checkpoint has been validated by geofence, the user needs to answer the riddle.
     *
     * @param riddle
     */
    @Override
    public void askRiddle(Riddle riddle) {
        Intent intent = new Intent(this, RiddleActivity.class);
        String riddleJson = new Gson().toJson(riddle, Riddle.class);
        intent.putExtra(KEY_RIDDLE, riddleJson);
        startActivityForResult(intent, REQUEST_CODE_RIDDLE_ANSWER);
    }

    @Override
    public void onMyLocationClicked() {
        final Location lastKnownLocation = mLocationUtils.getLastKnownLocation();
        updateLocationIcon(lastKnownLocation);

        final MapController mapController = (MapController) mMapView.getController();
        mapController.animateTo(new GeoPoint(lastKnownLocation));

        // delay this otherwise it interrupts the animateTo animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapController.zoomTo(Constants.AUTO_ZOOM_LEVEL);
            }
        }, 500);

    }

    public void unsetFocusedItem() {
        raceListOverlay.unSetFocusedItem();
        mMapView.invalidate();
    }

    /**
     * @param startPoint the point we need to theoretically animate to
     * @return if the current {@link BoundingBox} scaled by a factor of 0.5 contains the startPoint
     */
    public boolean needToAnimateToStart(GeoPoint startPoint) {

        BoundingBox currentBb = mMapView.getBoundingBox();
        BoundingBox reducedBB = currentBb.increaseByScale((float) 0.5);

        return !reducedBB.contains(startPoint);

    }

    @Override
    public void confirmStopRace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to stop the race? All progress will be lost.")
                .setPositiveButton("Yes, stop it", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        raceVM.onConfirmStop();
                    }
                })
                .setNegativeButton("No, continue the race", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    /* END RACE COMMS */

    @Override
    public void onMapBackgroundTouch() {
        raceVM.onMapBackgroundTouch();
    }

    /**
     * Notifiy the subscriber that location has been updated
     *
     * @param location the new location
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG, "received location from " + LocationUtils.class.getSimpleName() + ": " + location);
        updateLocationIcon(location);
    }


    class GeofenceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String checkpointId = intent.getStringExtra(GeofenceTransitionsIntentService.KEY_GEOFENCE_ID);
            if (checkpointId == null) {
                throw new IllegalArgumentException("Received intent for geofence with no checkpoint associated");
            }

            raceVM.onGeofenceTriggered(checkpointId);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_RIDDLE_ANSWER) {
            if (resultCode == RESULT_OK) {
                int answerIndex = data.getIntExtra(RiddleActivity.KEY_USER_ANSWER, -1);

                if (answerIndex == -1) {
                    throw new IllegalStateException("Received answer index -1 but requestCode was RESULT_OK");
                }

                onQuestionAnswered(answerIndex);
            }
        }
    }

    /**
     * Internal callback for when question is answered.<br>
     * Calls {@link RaceVM#onQuestionCorrectlyAnswered(int)}
     *
     * @param answerIndex
     */

    private void onQuestionAnswered(int answerIndex) {
        raceVM.onQuestionCorrectlyAnswered(answerIndex);
    }
}
