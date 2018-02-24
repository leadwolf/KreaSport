package com.ccaroni.kreasport.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.management.UsersAPIClient;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.legacy.view.activities.entry.LoginActivity;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

/**
 * Manages Auth0 credentials through SharedPreferences using {@link SharedPreferences} saved in {@link CredentialsManager#AUTH_PREFERENCES_NAME} file.
 */
public class CredentialsManager {

    private final String LOG = CredentialsManager.class.getSimpleName();

    private final String AUTH_PREFERENCES_NAME = "auth0";

    private final String KEY_REFRESH_TOKEN = "refresh_token";
    private final String KEY_ACCESS_TOKEN = "access_token";
    private final String KEY_ID_TOKEN = "id_token";
    private final String KEY_TOKEN_TYPE = "token_type";
    private final String KEY_EXPIRES_IN = "expires_in";
    private final String KEY_USER_ID = "user_id";

    private long lastCheckTime;
    private String KEY_USER_PROFILE = "user_profile";

    private Context context;

    public CredentialsManager(Context context) {
        this.context = context;
    }

    public void saveCredentials(Credentials credentials) {
        Log.d(LOG, "saving access token " + credentials.getAccessToken());
        Log.d(LOG, "saving id token " + credentials.getIdToken());


        SharedPreferences sp = this.context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit()
                .putString(KEY_ID_TOKEN, credentials.getIdToken())
                .putString(KEY_REFRESH_TOKEN, credentials.getRefreshToken())
                .putString(KEY_ACCESS_TOKEN, credentials.getAccessToken())
                .putString(KEY_TOKEN_TYPE, credentials.getType())
                .putLong(KEY_EXPIRES_IN, credentials.getExpiresIn())
                .apply();
    }

    public Credentials getCredentials() {
        SharedPreferences sp = this.context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        return new Credentials(
                sp.getString(KEY_ID_TOKEN, null),
                sp.getString(KEY_ACCESS_TOKEN, null),
                sp.getString(KEY_TOKEN_TYPE, null),
                sp.getString(KEY_REFRESH_TOKEN, null),
                sp.getLong(KEY_EXPIRES_IN, 0));
    }

    public void deleteCredentials() {
        Log.d(LOG, "deleted credentials");

        SharedPreferences sp = this.context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit()
                .putString(KEY_ID_TOKEN, null)
                .putString(KEY_REFRESH_TOKEN, null)
                .putString(KEY_ACCESS_TOKEN, null)
                .putString(KEY_TOKEN_TYPE, null)
                .putLong(KEY_EXPIRES_IN, 0)
                .apply();

        deleteUserId();
        deleteUserProfile();
    }

    public void saveUserId(String id) {
//        Log.d(LOG, "saving userId: " + id);

        SharedPreferences sp = this.context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit()
                .putString(KEY_USER_ID, id)
                .apply();

    }

    public String getUserId() {
        SharedPreferences sp = this.context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        return sp.getString(KEY_USER_ID, null);
    }

    public void deleteUserId() {
        SharedPreferences sp = this.context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit()
                .putString(KEY_USER_ID, null)
                .apply();

    }

    /**
     * Deletes the credentials stored with callingActivity and then uses it to start {@link LoginActivity}.
     */
    public void signOut() {
        deleteCredentials();

        Intent intent = new Intent(this.context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.d(LOG, "signed out, cleared activity stack, launching login");
        this.context.startActivity(intent);
    }

    /**
     * Uses callingActivity to get the access token from Prefs, then creates an {@link AuthenticationAPIClient} and attemps to call
     * {@link AuthenticationAPIClient#userInfo(String)}.
     * <br><br>On success, nothing happens.
     * <br>On failure, calls {@link #signOut()}
     */
    public void verifyAccessToken() {
        Log.d(LOG, "request to verify access token");

        final long timeDifference = SystemClock.elapsedRealtime() - lastCheckTime;
        final int acceptableTime = 5; // in minutes
        if (timeDifference <= TimeUnit.MINUTES.toMillis(acceptableTime)) {
            Log.d(LOG, "last check was " + TimeUnit.MILLISECONDS.toMinutes(timeDifference) + "mn ago, within the acceptable time");
            return;
        }

        Auth0 auth0 = new Auth0(this.context.getString(R.string.auth0_client_id), this.context.getString(R.string.auth0_domain));
        auth0.setOIDCConformant(true);

        String accessToken = getCredentials().getAccessToken();
        if (accessToken == null) {
            signOut();
        } else {
            final AuthenticationAPIClient aClient = new AuthenticationAPIClient(auth0);
            aClient.userInfo(accessToken)
                    .start(new BaseCallback<UserProfile, AuthenticationException>() {
                        @Override
                        public void onSuccess(final UserProfile payload) {
                            Log.d(LOG, "access token validation: SUCCESS");
                            lastCheckTime = SystemClock.elapsedRealtime();
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            Log.d(LOG, "access token validation: FAIL");
                            Log.d(LOG, "Error: " + error);
                            Log.d(LOG, "Error description: " + error.getDescription());
                            signOut();
                        }
                    });
        }
    }


    public void downloadUserId() {
        Auth0 auth0 = new Auth0(this.context.getString(R.string.auth0_client_id), this.context.getString(R.string.auth0_domain));
        auth0.setOIDCConformant(true);

        String idToken = getCredentials().getIdToken();
        String accessToken = getCredentials().getAccessToken();

        AuthenticationAPIClient authClient = new AuthenticationAPIClient(auth0); // gets simple auth profile
        final UsersAPIClient usersClient = new UsersAPIClient(auth0, idToken); // gets complete profile

        if (accessToken == null) {
            signOut();
        } else {
            // gets simple user profile open_id
            Log.d(LOG, "getting simple user id");

            authClient.userInfo(accessToken)
                    .start(new BaseCallback<UserProfile, AuthenticationException>() {
                        @Override
                        public void onSuccess(UserProfile payload) {
                            Log.d(LOG, "got userId: " + payload.getId());

                            saveUserId(payload.getId());
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            Log.d(LOG, "error getting simple user profile");
                            Log.d(LOG, error.toString());
                        }
                    });
        }

    }

    public void saveUserProfile(Context context, UserProfile profile) {
        String profileJson = new Gson().toJson(profile, UserProfile.class);
        SharedPreferences sp = context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit()
                .putString(KEY_USER_PROFILE, profileJson)
                .apply();

    }

    public UserProfile getUserProfile() {
        SharedPreferences sp = this.context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        String profileJson = sp.getString(KEY_USER_PROFILE, null);
        if (profileJson != null) {
            UserProfile userProfile = new Gson().fromJson(profileJson, UserProfile.class);
            return userProfile;
        }
        return null;
    }

    public void deleteUserProfile() {
        SharedPreferences sp = this.context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit()
                .putString(KEY_USER_PROFILE, null);

    }
}