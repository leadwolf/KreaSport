package com.ccaroni.kreasport.activities;

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
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.other.CredentialsManager;

import java.util.HashMap;
import java.util.Map;

public class NewLogin extends AppCompatActivity {

    private static final String LOG = NewLogin.class.getSimpleName();

    private Lock mLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("scope", "openid offline_access");

        //Request a refresh token along with the access token.
        mLock = Lock.newBuilder(auth0, mCallback)
                .withScope("openid offline_access")
                .withAuthenticationParameters(parameters)
                .build(this);

        String accessToken = CredentialsManager.getCredentials(this).getAccessToken();
        // AccessToken means already logged in
        if (accessToken == null) {
            startActivity(mLock.newIntent(this));
            return;
        }

        // else verify old access token to automatically relogin
        AuthenticationAPIClient aClient = new AuthenticationAPIClient(auth0);
        aClient.userInfo(accessToken)
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(final UserProfile payload) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d(LOG, "Login - Automatic login success");
                            }
                        });
                        startActivity(new Intent(NewLogin.this, HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(NewLogin.this, "Session Expired, please Log In", Toast.LENGTH_SHORT).show();
                            }
                        });
                        CredentialsManager.deleteCredentials(NewLogin.this);
                        startActivity(mLock.newIntent(NewLogin.this));
                    }
                });
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
            CredentialsManager.saveCredentials(NewLogin.this, credentials);
            startActivity(new Intent(NewLogin.this, HomeActivity.class));
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

}
