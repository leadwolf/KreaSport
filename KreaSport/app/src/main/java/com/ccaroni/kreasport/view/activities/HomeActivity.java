package com.ccaroni.kreasport.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RaceHelper;
import com.ccaroni.kreasport.view.fragments.HomeFragment;
import com.ccaroni.kreasport.data.dto.Race;
import com.ccaroni.kreasport.network.ApiUtils;
import com.ccaroni.kreasport.network.RaceService;
import com.ccaroni.kreasport.utils.CredentialsManager;
import com.ccaroni.kreasport.utils.PreferenceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Master on 02/04/2017.
 */

public class HomeActivity extends BaseActivity implements HomeFragment.HomeInteractionListener {

    private static final String LOG = HomeActivity.class.getSimpleName();

    private PreferenceManager preferenceManager;

    private RaceService raceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.customCreate(savedInstanceState, R.layout.activity_base);

        setupFragments();

        String accessToken = CredentialsManager.getCredentials(this).getAccessToken();
        raceService = ApiUtils.getRaceService(true, accessToken);
        preferenceManager = new PreferenceManager(this, HomeActivity.class.getSimpleName());
    }

    @Override
    protected void onResume() {
        resetNavigationDrawer(navigationView.getMenu().getItem(0));
        setCurrentActivityIndex(0);
        super.onResume();
    }

    private void setupFragments() {
        Fragment fragment = getFragment(R.id.nav_home);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main_frame_layout, fragment, TAG_HOME)
                .commit();
    }

    private void downloadRaceRetrofit(Intent intent) {

        final boolean requestPrivate = intent.getBooleanExtra(HomeFragment.DOWNLOAD_PRIVATE_RACE, false);
        String key = intent.getStringExtra(HomeFragment.DOWNLOAD_PRIVATE_RACE_KEY);

        if (requestPrivate && TextUtils.isEmpty(key)) {
            Log.d(LOG, "received private download request but key was empty");
            Toast.makeText(this, "Please enter a valid key", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = createDownloadProgressDialog(requestPrivate);
        progressDialog.show();

        if (requestPrivate) {
            downloadPrivateRace(progressDialog);
        } else {
            downloadPublicRaces(progressDialog);
        }
    }

    private void downloadPublicRaces(final ProgressDialog progressDialog) {
        raceService.getPublicRaces().enqueue(new Callback<List<Race>>() {
            @Override
            public void onResponse(Call<List<Race>> call, retrofit2.Response<List<Race>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d(LOG, "downloaded \n " + response.body());
                    List<Race> downloadedRaces = response.body();
                    Toast.makeText(HomeActivity.this, "Downloaded " + downloadedRaces.size() + " races", Toast.LENGTH_SHORT).show();

                    RaceHelper.getInstance(HomeActivity.this).saveRaceList(downloadedRaces);
                } else {
                    Log.d(LOG, "response unsuccessful with code " + response.code());
                    showNoRaceFoundDialog(false, false);
                }
            }

            @Override
            public void onFailure(Call<List<Race>> call, Throwable t) {
                Log.d(LOG, "download failed: " + t.toString());
                progressDialog.dismiss();
                showNoRaceFoundDialog(true, false);
            }
        });
    }

    private void downloadPrivateRace(final ProgressDialog progressDialog) {

    }

    /**
     * Creates and show an AlertDialog for no race(s) found and gives the reason (none found/connection error)
     *
     * @param connectionError if the reason is a connection error
     * @param requestPrivate  if the request was for a private race
     */
    private void showNoRaceFoundDialog(boolean connectionError, boolean requestPrivate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.download_fail_title))
                .setPositiveButton("OK", null);

        if (!connectionError && requestPrivate) {
            builder.setMessage(R.string.no_race_found_private);
        } else if (!connectionError && !requestPrivate) {
            builder.setMessage(R.string.no_race_found_public);
        } else {
            builder.setMessage(R.string.download_fail_description);
        }
        builder.show();
    }

    /**
     * @param requestPrivate if the race being downloaded is a private race
     * @return a {@link ProgressDialog} in accordance to which race(s) is being downloaded
     */
    private ProgressDialog createDownloadProgressDialog(boolean requestPrivate) {
        ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        if (requestPrivate) {
            progressDialog.setMessage(getString(R.string.download_private_race_message));
        } else {
            progressDialog.setMessage(getString(R.string.download_public_race_message));
        }
        return progressDialog;
    }

    /**
     * Called on interaction in {@link HomeFragment} needing to be handled inside the activity.
     * @param requestIntent
     */
    @Override
    public void onHomeInteraction(Intent requestIntent) {
        if (requestIntent == null) {
            throw new NullPointerException("Request intent should not be null");
        }

        String requestCode = requestIntent.getStringExtra(CALLBACK_KEY);
        if (requestCode == null) {
            return;
        }

        switch (requestCode) {
            case HomeFragment.DOWNLOAD_REQUEST:
                downloadRaceRetrofit(requestIntent);
            default:
                break;
        }
    }
}
