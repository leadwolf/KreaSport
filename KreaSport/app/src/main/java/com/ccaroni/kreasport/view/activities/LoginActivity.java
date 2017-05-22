package com.ccaroni.kreasport.view.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.management.ManagementException;
import com.auth0.android.management.UsersAPIClient;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.utils.CredentialsManager;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG = LoginActivity.class.getSimpleName();

    private Auth0 auth0;
    private String idToken;

    private Lock mLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        auth0.setOIDCConformant(true);

        mLock = Lock.newBuilder(auth0, mCallback)
                .withScope("openid offline_access create:races read:races update:races delete:races")
                .withAudience("kreasport-jwt-api")
                .build(this);

        Credentials credentials = CredentialsManager.getCredentials(this);
        String accessToken = credentials.getAccessToken();
        idToken = credentials.getIdToken();
        if (accessToken == null) {
            Log.d(LOG, "access token is null, showing login");
            startLockWidget();
        } else {
            final AuthenticationAPIClient aClient = new AuthenticationAPIClient(auth0);
            aClient.userInfo(accessToken)
                    .start(new BaseCallback<UserProfile, AuthenticationException>() {
                        @Override
                        public void onSuccess(final UserProfile payload) {
                            Log.d(LOG, "access token validation: SUCCESS");
                            autoLoginRedirect();
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            Log.d(LOG, "access token validation: FAIL");
                            attemptRefresh(aClient);
                        }
                    });
        }
    }

    private void attemptRefresh(AuthenticationAPIClient aClient) {
        String refreshToken = CredentialsManager.getCredentials(this).getRefreshToken();
        if (refreshToken == null) {
            startLockWidget();
        } else {
            aClient.renewAuth(refreshToken)

                    .start(new BaseCallback<Credentials, AuthenticationException>() {

                        @Override
                        public void onSuccess(Credentials credentials) {
                            Log.d(LOG, "refresh token validation: SUCCESS");
                            CredentialsManager.saveCredentials(LoginActivity.this, credentials);
                            autoLoginRedirect();
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            Log.d(LOG, "refresh token validation: FAIL");
                            startLockWidget();
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Your own Activity code
        mLock.onDestroy(this);
        mLock = null;
    }

    private final LockCallback mCallback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            Log.d(LOG, "Login - Success");
            CredentialsManager.saveCredentials(LoginActivity.this, credentials);
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        @Override
        public void onCanceled() {
            Log.d(LOG, "Login - Cancelled");
        }

        @Override
        public void onError(LockException error) {
            Log.d(LOG, "Login - Error:" + error.toString());
        }
    };

    private void startLockWidget() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(LoginActivity.this, "Session Expired, please Log In", Toast.LENGTH_SHORT).show();
            }
        });

        CredentialsManager.deleteCredentials(this);
        startActivity(mLock.newIntent(this));
    }

    /**
     * Saves the userId with {@link CredentialsManager}, finishes this activity and goes to {@link HomeActivity}.
     */
    private void autoLoginRedirect() {
        Log.d(LOG, "Login - Automatic login success");

        UsersAPIClient usersClient = new UsersAPIClient(auth0, idToken);

        usersClient.getProfile(idToken)
                .start(new BaseCallback<UserProfile, ManagementException>() {
                    @Override
                    public void onSuccess(UserProfile payload) {
                        Log.d(LOG, "got user profile");
                        CredentialsManager.saveUserId(LoginActivity.this, payload.getId());
                    }

                    @Override
                    public void onFailure(ManagementException error) {
                        Log.d(LOG, "error getting complete user profile");
                    }
                });

        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

}
