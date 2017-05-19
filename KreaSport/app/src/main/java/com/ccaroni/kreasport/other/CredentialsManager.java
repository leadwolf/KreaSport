package com.ccaroni.kreasport.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.auth0.android.result.Credentials;

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

}
