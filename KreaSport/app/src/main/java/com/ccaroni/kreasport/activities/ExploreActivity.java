package com.ccaroni.kreasport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.fragments.BottomSheetFragment;
import com.ccaroni.kreasport.fragments.ExploreFragment;
import com.ccaroni.kreasport.map.viewmodels.RaceVM;
import com.ccaroni.kreasport.other.PreferenceManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


/**
 * Created by Master on 02/04/2017.
 */

public class ExploreActivity extends MainActivity implements BottomSheetFragment.BottomSheetInteractionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient
        .OnConnectionFailedListener {

    private static final String LOG = ExploreActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private RaceVM raceVM;
    private PreferenceManager preferenceManager;

    private GoogleApiClient mGoogleApiClient;
    private ExploreFragment exploreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetNavigationDrawer(navigationView.getMenu().getItem(1));
        setCurrentActivityIndex(1);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        preferenceManager = new PreferenceManager(this, ExploreActivity.class.getSimpleName());
        restoreRaceVM();

        setupFragments();
    }

    @Override
    protected void onResume() {
        resetNavigationDrawer(navigationView.getMenu().getItem(1));
        setCurrentActivityIndex(1);
        super.onResume();
    }


    /**
     * Creates and adds this activities' fragments to R.id.content_main_frame_layout
     */
    private void setupFragments() {
        exploreFragment = (ExploreFragment) getFragment(R.id.nav_explore);
        BottomSheetFragment bottomSheetFragment = (BottomSheetFragment) getFragment(R.id.ll_bottom_sheet);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main_frame_layout, exploreFragment, TAG_EXPLORE)
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
    public void onBottomSheetInteraction(Intent requestIntent) {
    }

    public RaceVM getRaceVM() {
        return raceVM;
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
    public void onConnected(@Nullable Bundle bundle) {
        exploreFragment.startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }
}
