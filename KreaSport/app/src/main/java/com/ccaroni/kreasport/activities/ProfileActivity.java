package com.ccaroni.kreasport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.fragments.ProfileFragment;

public class ProfileActivity extends MainActivity implements ProfileFragment.ProfileInteractionListener {

    private static final String LOG = ProfileActivity.class.getSimpleName();

    private static final String TAG_PROFILE = "kreasport.tag_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    @Override
    public void onExploreInteraction(Intent requestIntent) {
        if (requestIntent == null) {
            throw new NullPointerException("Request intent should not be null");
        }

        String requestCode = requestIntent.getStringExtra(CALLBACK_KEY);
        if (requestCode == null) {
            return;
        }

        Intent intent = null;
        switch (requestCode) {
            case ProfileFragment.LAUNCH_LOGIN:
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Log.d(LOG, "cleared activity stack");
                Log.d(LOG, "signed out, launching login");
                startActivity(intent);
                finish();
                break;
            case ProfileFragment.PROFILE_DELETED:
                intent = new Intent(this, SignupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}