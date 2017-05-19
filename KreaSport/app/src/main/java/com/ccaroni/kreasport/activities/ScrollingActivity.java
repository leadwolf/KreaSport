package com.ccaroni.kreasport.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.fragments.ProfileFragment;

public class ScrollingActivity extends AppCompatActivity {

    private static final String LOG = ScrollingActivity.class.getSimpleName();

    private static final String TAG_PROFILE = "kreasport.tag_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

//        setupFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupFragment() {
        ProfileFragment profileFragment = ProfileFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main_frame_layout, profileFragment, TAG_PROFILE)
                .commit();
    }
}
