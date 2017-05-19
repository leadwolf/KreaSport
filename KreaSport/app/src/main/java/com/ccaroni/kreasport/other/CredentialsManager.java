package com.ccaroni.kreasport.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.activities.LoginActivity;

/**
 * Manages Auth0 credentials through SharedPreferences using {@link SharedPreferences} saved in {@link CredentialsManager#AUTH_PREFERENCES_NAME} file.
 */
public class CredentialsManager {

    private static final String LOG = CredentialsManager.class.getSimpleName();

    private static final String AUTH_PREFERENCES_NAME = "auth0";

    private final static String KEY_REFRESH_TOKEN = "refresh_token";
    private final static String KEY_ACCESS_TOKEN = "access_token";
    private final static String KEY_ID_TOKEN = "id_token";
    private final static String KEY_TOKEN_TYPE = "token_type";
    private final static String KEY_EXPIRES_IN = "expires_in";

    public static void saveCredentials(Context context, Credentials credentials) {
        Log.d(LOG, "saving access token " + credentials.getAccessToken());
        Log.d(LOG, "saving id token " + credentials.getIdToken());


        SharedPreferences sp = context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit()
                .putString(KEY_ID_TOKEN, credentials.getIdToken())
                .putString(KEY_REFRESH_TOKEN, credentials.getRefreshToken())
                .putString(KEY_ACCESS_TOKEN, credentials.getAccessToken())
                .putString(KEY_TOKEN_TYPE, credentials.getType())
                .putLong(KEY_EXPIRES_IN, credentials.getExpiresIn())
                .apply();
    }

    public static Credentials getCredentials(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        return new Credentials(
                sp.getString(KEY_ID_TOKEN, null),
                sp.getString(KEY_ACCESS_TOKEN, null),
                sp.getString(KEY_TOKEN_TYPE, null),
                sp.getString(KEY_REFRESH_TOKEN, null),
                sp.getLong(KEY_EXPIRES_IN, 0));
    }

    public static void deleteCredentials(Context context) {
        Log.d(LOG, "deleted credentials");

        SharedPreferences sp = context.getSharedPreferences(
                AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);

        sp.edit()
                .putString(KEY_ID_TOKEN, null)
                .putString(KEY_REFRESH_TOKEN, null)
                .putString(KEY_ACCESS_TOKEN, null)
                .putString(KEY_TOKEN_TYPE, null)
                .putLong(KEY_EXPIRES_IN, 0)
                .apply();
    }

    /**
     * Deletes the credentials stored with callingActivity and then uses it to start {@link LoginActivity}.
     * @param callingAcitivity
     */
    public static void signOut(Activity callingAcitivity) {
        CredentialsManager.deleteCredentials(callingAcitivity);

        Intent intent = new Intent(callingAcitivity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.d(LOG, "signed out, cleared activity stack, launching login");
        callingAcitivity.startActivity(intent);
        callingAcitivity.finish();
    }

    /**
     * Uses callingActivity to get the access token from Prefs, then creates an {@link AuthenticationAPIClient} and attemps to call {@link AuthenticationAPIClient#userInfo(String)}.
     * <br></rb><br>On success, nothing happens.
     * <br>On failure, calls {@link #signOut(Activity)}
     * @param callingActivity
     */
    public static void verifyAccessToken(final Activity callingActivity) {
        Auth0 auth0 = new Auth0(callingActivity.getString(R.string.auth0_client_id), callingActivity.getString(R.string.auth0_domain));
        auth0.setOIDCConformant(true);

        String accessToken = CredentialsManager.getCredentials(callingActivity).getAccessToken();
        if (accessToken == null) {
            signOut(callingActivity);
        } else {
            final AuthenticationAPIClient aClient = new AuthenticationAPIClient(auth0);
            aClient.userInfo(accessToken)
                    .start(new BaseCallback<UserProfile, AuthenticationException>() {
                        @Override
                        public void onSuccess(final UserProfile payload) {
                            Log.d(LOG, "access token validation: SUCCESS");
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            Log.d(LOG, "access token validation: FAIL");
                            signOut(callingActivity);
                        }
                    });
        }
    }
}