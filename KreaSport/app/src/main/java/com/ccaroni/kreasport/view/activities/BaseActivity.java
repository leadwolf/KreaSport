package com.ccaroni.kreasport.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.view.fragments.HomeFragment;
import com.ccaroni.kreasport.utils.CredentialsManager;

/**
 * Created by Master on 24/04/2017.
 */

/**
 * Base activity presenting methods common to all the root activities.
 * Each class must implement its own customCreate();
 */
public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG = BaseActivity.class.getSimpleName();

    public static final String CALLBACK_KEY = "kreasport.activity_base.frag_request.reason";
    private static final java.lang.String KEY_CURRENT_ACTIVITY_INDEX = "kreasport.savedinstancestate.current_frag_index";

    protected static final String TAG_HOME = "kreasport.tag.home";
    protected static final String TAG_EXPLORE = "kreasport.tag.explore";

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    protected int currentActivityIndex;

    /**
     * Just a call to setContentView to avoid overriding onCreate.
     * <br>Calls {@link #secondaryCreate()}
     * @param savedInstanceState
     * @param layout
     */
    protected void customCreate(@Nullable Bundle savedInstanceState, int layout) {
        setContentView(layout);
        secondaryCreate();
    }

    /**
     * Sets up the toolbar, drawerlayout and navgiationView.
     * <br>Also verifies the user's access token with {@link CredentialsManager#verifyAccessToken(Activity)}
     */
    protected void secondaryCreate() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CredentialsManager.verifyAccessToken(BaseActivity.this);
            }
        }, 500);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    protected void setCurrentActivityIndex(int index) {
        currentActivityIndex = index;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectDrawerItem(item);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void selectDrawerItem(MenuItem menuItem) {

        hideKeyboard();

        if (currentActivityIndex != -1) {
            MenuItem currentItem = navigationView.getMenu().getItem(currentActivityIndex);
            if (currentItem.equals(menuItem)) {
                // prevent switching when already in selected activity
                Log.d(LOG, "DENIED: user requested switch to activity already displayed.");
                return;
            }
        }

        Intent activityIntent = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                activityIntent = new Intent(this, HomeActivity.class);
                break;
            case R.id.nav_explore:
                activityIntent = new Intent(this, ExploreActivity.class);
                break;
            case R.id.nav_profile:
                activityIntent = new Intent(this, ProfileActivity.class);
                break;
            case R.id.nav_offline_area:
                activityIntent = new Intent(this, OfflineAreasActivity.class);
                break;
//            case R.id.nav_share:
//                break;
//            case R.id.nav_send:
//                break;
            default:
                break;
        }

        if (activityIntent != null) {
            completeDrawerAction(menuItem, activityIntent, menuItem.getItemId() == R.id.nav_home);
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Sets the menuItem to be checked and changes the title
     *
     * @param menuItem
     */
    private void completeDrawerAction(MenuItem menuItem, Intent activityIntent, boolean clearStack) {
        menuItem.setChecked(true);

        setTitle(menuItem.getTitle());

        if (clearStack) {
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }
        startActivity(activityIntent);
    }

    protected void resetNavigationDrawer(MenuItem item) {
        item.setChecked(true);
        setTitle(item.getTitle());
    }

    /**
     * Creates new Fragments with default parameters.
     *
     * @param id
     * @return
     */
    protected Fragment getFragment(int id) {
        Fragment fragment;
        switch (id) {
            case R.id.nav_home:
//                Log.d(LOG, "created new HomeFragment");
                return HomeFragment.newInstance();
        }
        return null;
    }

}