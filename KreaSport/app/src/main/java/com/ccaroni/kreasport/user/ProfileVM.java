package com.ccaroni.kreasport.user;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Master on 20/04/2017.
 */

public class ProfileVM {

    private Activity activity;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    public ProfileVM(Activity activity) {
        this.activity = activity;

        setupFirebase();
    }

    private void setupFirebase() {
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
//                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
//                    finish();
                }
            }
        };
    }
}
