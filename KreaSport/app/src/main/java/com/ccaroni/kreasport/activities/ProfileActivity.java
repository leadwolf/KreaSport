package com.ccaroni.kreasport.activities;

import android.os.Bundle;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.fragments.ProfileFragment;

public class ProfileActivity extends BaseActivity {

    private static final String LOG = ProfileActivity.class.getSimpleName();

    private static final String TAG_PROFILE = "kreasport.tag_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.customCreate(savedInstanceState, R.layout.activity_base);

        setupFragment();

    }

    private void setupFragment() {
        ProfileFragment profileFragment = ProfileFragment.newInstance();

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

}