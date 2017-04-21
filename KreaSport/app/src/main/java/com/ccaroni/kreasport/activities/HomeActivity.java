package com.ccaroni.kreasport.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.fragments.HomeFragment;
import com.ccaroni.kreasport.other.Constants;
import com.ccaroni.kreasport.volley.VolleySingleton;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Master on 02/04/2017.
 */

public class HomeActivity extends MainActivity implements HomeFragment.HomeInteractionListener {

    private static final String LOG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.d(LOG, "no user logged in, launching login activity");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Log.d(LOG, "user already logged in:" + FirebaseAuth.getInstance().getCurrentUser().toString());
        }

        super.onCreate(savedInstanceState);

        setupFragments();
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


    /**
     * Unpacks the intent, sets up ProgressDialog, creates StringRequest and adds it to {@link VolleySingleton}'s queue.
     *
     * @param intent
     */
    private void downloadRace(Intent intent) {

        final boolean requestPrivate = intent.getBooleanExtra(HomeFragment.DOWNLOAD_PRIVATE_RACE, false);
        String key = intent.getStringExtra(HomeFragment.DOWNLOAD_PRIVATE_RACE_KEY);

        if (requestPrivate && (key == null || key.isEmpty())) {
            Log.d(LOG, "received private download request but key was empty");
            return;
        }

        // TODO validate key format for private race before download

        String url;
        if (requestPrivate)
            url = Constants.publicRacesURL;
        else
            url = String.format(Constants.privateRaceURL, key);

        final ProgressDialog progressDialog = createDownloadProgressDialog(requestPrivate);
        progressDialog.show();

        StringRequest stringRequest = createDownloadRequest(url, requestPrivate, progressDialog);

        Log.d(LOG, "sending volley request for " + (requestPrivate ? "private race" : "public races"));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * @param url            the target url which will give the race
     * @param requestPrivate is the request is a private race
     * @param progressDialog the ProgressDialog to dismiss on response/error
     * @return creates a request for a string download
     */
    private StringRequest createDownloadRequest(final String url, final boolean requestPrivate, final ProgressDialog progressDialog) {
        return new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();
                if (response != null && !response.equals("")) {
                    progressDialog.dismiss();

                    // TODO save json and transfer to ExploreFragment

                } else {
                    progressDialog.dismiss();
                    showNoRaceFoundDialog(false, requestPrivate);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                showNoRaceFoundDialog(true, requestPrivate);
                Log.d(LOG, "download error : " + error.toString());
            }
        });
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
                downloadRace(requestIntent);
            default:
                break;
        }
    }
}
