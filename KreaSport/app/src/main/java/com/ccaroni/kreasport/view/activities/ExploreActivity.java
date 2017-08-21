package com.ccaroni.kreasport.view.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.dto.Riddle;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.databinding.ActivityExploreBinding;
import com.ccaroni.kreasport.map.MapDefaults;
import com.ccaroni.kreasport.map.MapOptions;
import com.ccaroni.kreasport.map.views.CustomMapView;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.race.RaceHolder;
import com.ccaroni.kreasport.race.RaceVM;
import com.ccaroni.kreasport.race.RaceViewComms;
import com.ccaroni.kreasport.race.legacy.LEGACYRaceVM;
import com.ccaroni.kreasport.service.geofence.GeofenceTransitionsIntentService;
import com.ccaroni.kreasport.service.geofence.GeofenceUtils;
import com.ccaroni.kreasport.service.location.LocationUtils;
import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.utils.CredentialsManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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

public class ExploreActivity extends BaseActivity implements RaceViewComms, CustomMapView.MapViewCommunication, LocationUtils.LocationUtilsSubscriber {

    private static final String LOG = ExploreActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_CODE_RIDDLE_ANSWER = 100;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    private static final String KEY_BASE = "com.ccaroni.kreasport." + ExploreActivity.class.getSimpleName() + ".key.";
    public static final String KEY_RIDDLE = KEY_BASE + ".riddle";

    public static final String KEY_LOCATION_SETTINGS_PI = KEY_BASE + "location_settings_request_pending_intent";
    public static final String REQUIRES_LOCATION_SETTINGS_PROMPT = KEY_BASE + "requires_location_settings_prompt";


    private ActivityExploreBinding binding;

    private CustomMapView mMapView;
    private Chronometer chronometer;
    private ItemizedOverlayWithFocus raceListOverlay;

    private RaceVM raceVM;

    private GeofenceReceiver geofenceReceiver;

    /**
     * Receiver to listen to requests to change the user's location settings
     */
    private LocationSettingsReceiver locationSettingsReceiver;

    /**
     * The intent containing the resolution for the location settings. null if we don't need to change any settings
     */
    private PendingIntent locationSettingsPI;

    private boolean hasFix;

    private LocationUtils mLocationUtils;
    private GeofenceUtils mGeofenceUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_explore);
        super.secondaryCreate();

        resetNavigationDrawer(navigationView.getMenu().getItem(1));
        setCurrentActivityIndex(1);

        // First we need to check availability of play services
        checkPlayServicesAvailable();

        setupLocationAndGeofenceServices();

//        Intent racingServiceIntent = new Intent(this, RacingService.class);
//        startService(racingServiceIntent);
        // TODO only use this service for background

        setupUI();
    }

    private void setupLocationAndGeofenceServices() {
        LocalBroadcastManager lbc = LocalBroadcastManager.getInstance(this);

        // initialize broadcast receiver for location settings error before we start location updates with mLocationUtils
        locationSettingsReceiver = new LocationSettingsReceiver();
        lbc.registerReceiver(locationSettingsReceiver, new IntentFilter(REQUIRES_LOCATION_SETTINGS_PROMPT));


        mLocationUtils = new LocationUtils(this);
        mGeofenceUtils = new GeofenceUtils(this);

        // GeofenceReceiver will receive the geofence results once validated by GeofenceTransitionsIntentService
        geofenceReceiver = new GeofenceReceiver();
        lbc.registerReceiver(geofenceReceiver, new IntentFilter(GEOFENCE_TRIGGERED));
    }

    /**
     * Sets up the UI by setting up the map and loads everything needing to be displayed
     */
    private void setupUI() {
        RealmHelper.getInstance(this);
        RaceHolder.init(CredentialsManager.getUserId(this));

        raceVM = new RaceVM(this);
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
     * Creates an overlay of all the race(s) needing to be displayed with help from the {@link LEGACYRaceVM}.
     * Either creates an overlay for all the races or full overlay of one race.
     * The {@link LEGACYRaceVM} is in charge of returning the relevant race with all its checkpoints or all the races.
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

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            Log.d(LOG, "Google Play Services not available!");
            Log.d(LOG, "Error Code: " + resultCode + ", " + apiAvailability.getErrorString(resultCode));

            if (apiAvailability.isUserResolvableError(resultCode)) {
                // WARNING even this may indicate that the user simply cannot use the app if he gets code "SERVICE_INVALID"
                Log.d(LOG, "attempting user resolution");
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(LOG, "This device does not support Google Play Services and is not user resolvable");
                Toast.makeText(this, "Your device does not support Google Play Services.", Toast.LENGTH_LONG).show();
                closeActivity();
            }
            if (resultCode == ConnectionResult.SERVICE_INVALID) {
                closeActivity();
            }
            return false;
        }
        Log.d(LOG, "Google Play Services is available!");
        return true;
    }

    /**
     * Quits this activity and forces back to app home screen, or whatever was in the back stack
     */
    private void closeActivity() {
        Log.d(LOG, "Device completely does not support Google Play Services. Forcing back to app home screen");
        // this finishes this activity and forces to go back to home screen since its in the back stack
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finishAffinity();
        }
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
        if (mLocationUtils != null) {
            mLocationUtils.startLocationUpdates();
        }
        raceVM.checkPreviousRace();
        super.onStart();
    }


    /**
     * Stops location updates when this activity is paused for battery considerations.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationUtils != null) {
            mLocationUtils.stopLocationUpdates();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(geofenceReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationSettingsReceiver);
//        LEGACYRaceVM.saveOngoingBaseTime(); TODO
    }


    /* RACE COMMUNICATION */

    @Override
    public void onMyLocationClicked() {
        if (verifyLocationSettings()) {
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
    }

    @Override
    public void askStopConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to stop the race? All progress will be lost.")
                .setPositiveButton("Yes, stop it", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        chronometer.stop();
                        raceVM.onStopConfirmation();
                    }
                })
                .setNegativeButton("No, continue the race", null);
        builder.create().show();
    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startChronometer(long timeStart) {
        chronometer.setBase(timeStart);
        chronometer.start();
    }

    @Override
    public Location getLastKnownLocation() {
        return mLocationUtils.getLastKnownLocation();
    }

    @Override
    public boolean needToAnimateToStart(GeoPoint startPoint) {
        BoundingBox currentBb = mMapView.getBoundingBox();
        BoundingBox reducedBB = currentBb.increaseByScale((float) 0.5);

        return !reducedBB.contains(startPoint);
    }

    @Override
    public void stopChronometer() {
        chronometer.stop();
    }

    @Override
    public void askRiddle(Riddle riddle) {
        Intent intent = new Intent(this, RiddleActivity.class);
        String riddleJson = new Gson().toJson(riddle, Riddle.class);
        intent.putExtra(KEY_RIDDLE, riddleJson);
        startActivityForResult(intent, REQUEST_CODE_RIDDLE_ANSWER);
    }

    @Override
    public void addGeoFence(RealmCheckpoint targetingCheckpoint) {
        mGeofenceUtils.addGeofences(targetingCheckpoint);
    }

    @Override
    public void removeLastGeofence() {
        mGeofenceUtils.removePreviousGeofences();
    }


    /**
     * Asks the user to agree to Google location settings
     *
     * @return if the settings are correct
     */
    @Override
    public boolean verifyLocationSettings() {
        if (locationSettingsPI != null) {
            attemptLocationSetttingsResolution();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void focusOnRace(List<CustomOverlayItem> overlayItemsList) {
        Log.d(LOG, "clearing all markers except current race");
        raceListOverlay.removeAllItems();
        raceListOverlay.addItems(overlayItemsList);
        mMapView.invalidate();
    }

    private void attemptLocationSetttingsResolution() {
        try {
            startIntentSenderForResult(locationSettingsPI.getIntentSender(), REQUEST_CHECK_SETTINGS, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void revealNextCheckpoint(CustomOverlayItem nextCheckpointOverlayItem) {
        Log.d(LOG, "revealing next checkpoint: " + nextCheckpointOverlayItem);

        raceListOverlay.addItem(nextCheckpointOverlayItem);

        mMapView.invalidate();
    }


    /* END RACE COMMS */

    @Override
    public void onMapBackgroundTouch() {
        raceVM.onMapBackgroundTouch();
        raceListOverlay.unSetFocusedItem();
        mMapView.invalidate();
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


    private class GeofenceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String checkpointId = intent.getStringExtra(GeofenceTransitionsIntentService.KEY_GEOFENCE_ID);
            if (checkpointId == null) {
                throw new IllegalArgumentException("Received intent for geofence with no checkpoint associated");
            }

            raceVM.onGeofenceTriggered(checkpointId);

        }
    }

    private class LocationSettingsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            locationSettingsPI = intent.getParcelableExtra(KEY_LOCATION_SETTINGS_PI);
            attemptLocationSetttingsResolution();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(LOG, "User agreed to make required location settings changes.");
                        locationSettingsPI = null;
                        setupLocationAndGeofenceServices();
                        mLocationUtils.startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(LOG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
            case REQUEST_CODE_RIDDLE_ANSWER:
                if (resultCode == RESULT_OK) {
                    int answerIndex = data.getIntExtra(RiddleActivity.KEY_USER_ANSWER, -1);

                    if (answerIndex == -1) {
                        throw new IllegalStateException("Received answer index -1 but requestCode was RESULT_OK");
                    }

                    raceVM.onQuestionCorrectlyAnswered(answerIndex);
                }
                break;
            default:
                break;
        }

    }
}
