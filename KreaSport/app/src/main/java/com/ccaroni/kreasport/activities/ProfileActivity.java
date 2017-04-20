package com.ccaroni.kreasport.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.fragments.ProfileFragment;

import static com.ccaroni.kreasport.R.id.progressBar;

public class ProfileActivity extends MainActivity implements ProfileFragment.ProfileInteractionListener {

    private static final String TAG_PROFILE = "kreasport.tag_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupFragment();

    }

    private void setupFragment() {
        ProfileFragment profileFragment = ProfileFragment.newInstance("", "");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main_frame_layout, profileFragment, TAG_PROFILE)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetNavigationDrawer(navigationView.getMenu().getItem(2));
        setCurrentActivityIndex(2);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}