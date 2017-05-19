package com.ccaroni.kreasport.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.management.ManagementException;
import com.auth0.android.management.UsersAPIClient;
import com.auth0.android.result.UserProfile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.databinding.ActivityProfileBinding;
import com.ccaroni.kreasport.other.CredentialsManager;

import static java.security.AccessController.getContext;

public class ProfileActivity extends BaseActivity {

    private static final String LOG = ProfileActivity.class.getSimpleName();

    private ActivityProfileBinding binding;
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        super.secondaryCreate();

        resetNavigationDrawer(navigationView.getMenu().getItem(2));
        setCurrentActivityIndex(2);

        setupToolbar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        CredentialsManager.verifyAccessToken(this);

        getUserData();
    }

    private void setupToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Christopher Caroni");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }

    private void getUserData() {
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        auth0.setOIDCConformant(true);

        String idToken = CredentialsManager.getCredentials(this).getIdToken();
        String accessToken = CredentialsManager.getCredentials(this).getAccessToken();

        AuthenticationAPIClient authClient = new AuthenticationAPIClient(auth0); // gets simple auth profile
        final UsersAPIClient usersClient = new UsersAPIClient(auth0, idToken); // gets complete profile

        if (accessToken == null) {
            CredentialsManager.signOut(this);
        } else {
            // gets simple user profile open_id
            Log.d(LOG, "using access token: " + accessToken);

            authClient.userInfo(accessToken)
                    .start(new OpenIDCallback(usersClient));
        }
    }

    private void setUserData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setProfilePicture();
            }
        });
    }

    private void setProfilePicture() {
        if (userProfile != null && userProfile.getPictureURL() != null) {
            Glide.with(ProfileActivity.this)
                    .load(userProfile.getPictureURL())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_person_outline_white_24dp)
                            .error(R.drawable.ic_person_outline_white_24dp)
                            .bitmapTransform(new CircleCrop())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
//                            .transition(Glide.withCrossFade().crossFade(750))
                    .into(binding.appBarMain.imgProfilePic);
        } else {
            Log.d(LOG, "could not find user profile image");
        }
    }

    private class OpenIDCallback implements BaseCallback<UserProfile, AuthenticationException> {

        private UsersAPIClient usersClient;

        private OpenIDCallback(UsersAPIClient usersClient) {
            this.usersClient = usersClient;
        }

        @Override
        public void onSuccess(final UserProfile userInfo) {
            Log.d(LOG, "got simple data");
            userProfile = userInfo;
            String userId = userInfo.getId();
            Log.d(LOG, "using userId: " + userId);

            // get the full profile
            usersClient.getProfile(userId)
                    .start(new ProfileCallback());
        }

        @Override
        public void onFailure(AuthenticationException error) {
            Log.d(LOG, "error getting simple user profile");
            Log.d(LOG, error.toString());
            setUserData();
        }
    }

    private class ProfileCallback implements BaseCallback<UserProfile, ManagementException> {
        @Override
        public void onSuccess(final UserProfile profile) {
            userProfile = profile;
            Log.d(LOG, "got user profile");
            setUserData();
        }

        @Override
        public void onFailure(ManagementException error) {
            Log.d(LOG, "error getting complete user profile");
            Log.d(LOG, error.toString());
            setUserData();
        }
    }

}
