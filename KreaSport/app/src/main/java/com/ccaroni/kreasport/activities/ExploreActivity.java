package com.ccaroni.kreasport.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.databinding.ActivityExploreBinding;
import com.ccaroni.kreasport.map.models.MapOptions;
import com.ccaroni.kreasport.map.viewmodels.MapVM;
import com.ccaroni.kreasport.map.viewmodels.RaceVM;
import com.ccaroni.kreasport.map.views.CustomMapView;
import com.ccaroni.kreasport.other.PreferenceManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.util.GeoPoint;

public class ExploreActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG = ExploreActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    
    private ActivityExploreBinding binding;

    private CustomMapView mMapView;
    private RaceVM raceVM;

    private PreferenceManager preferenceManager;

    private GoogleApiClient mGoogleApiClient;

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
                .setEnableCompass(true)
                .setEnableMultiTouchControls(true)
                .setEnableScaleOverlay(true);

        MapVM mMapVM = new MapVM(new GeoPoint(50.633621, 3.0651845), 9);

        mMapView = new CustomMapView(this, mMapOptions, mMapVM);
        binding.appBarMain.layoutExplore.frameLayoutMap.addView(mMapView);
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
//        exploreFragment.stopLocationUpdates();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        exploreFragment.startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

}
