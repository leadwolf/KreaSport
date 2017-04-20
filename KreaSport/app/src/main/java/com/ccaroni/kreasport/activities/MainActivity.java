package com.ccaroni.kreasport.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.ccaroni.kreasport.databinding.ActivityMainBinding;
import com.ccaroni.kreasport.fragments.BottomSheetFragment;
import com.ccaroni.kreasport.fragments.ExploreFragment;
import com.ccaroni.kreasport.fragments.HomeFragment;
import com.ccaroni.kreasport.map.models.MapOptions;
import com.ccaroni.kreasport.map.viewmodels.MapVM;

import org.osmdroid.util.GeoPoint;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String LOG = MainActivity.class.getSimpleName();

    public static final String CALLBACK_KEY = "kreasport.frag_request.reason";
    private static final java.lang.String KEY_CURRENT_ACTIVITY_INDEX = "kreasport.savedinstancestate.current_frag_index";

    protected static final String TAG_HOME = "kreasport.tag.home";
    protected static final String TAG_EXPLORE = "kreasport.tag.explore";

    protected DrawerLayout drawer;
    protected NavigationView navigationView;

    private int currentActivityIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    protected void setCurrentActivityIndex(int index) {
        currentActivityIndex = index;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        selectDrawerItem(item);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void selectDrawerItem(MenuItem menuItem) {

        hideKeyboard();


        if (currentActivityIndex != -1) {
            MenuItem currentItem = navigationView.getMenu().getItem(currentActivityIndex);
            if (currentItem.equals(menuItem)) {
                // prevent switching when already in selected activity
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
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            default:
                break;
        }

        if (activityIntent != null) {
            completeDrawerAction(menuItem, activityIntent);
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
    private void completeDrawerAction(MenuItem menuItem, Intent activityIntent) {
        menuItem.setChecked(true);

        setTitle(menuItem.getTitle());

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
                Log.d(LOG, "created new HomeFragment");
                return HomeFragment.newInstance("", "");
            case R.id.nav_explore:
                Log.d(LOG, "created new ExploreFragment");
                return ExploreFragment.newInstance(
                        new MapVM(new GeoPoint(50.633621, 3.0651845),
                                9),
                        new MapOptions()
                                .setEnableLocationOverlay(true)
                                .setEnableCompass(true)
                                .setEnableMultiTouchControls(true)
                                .setEnableScaleOverlay(true));
            case R.id.ll_bottom_sheet:
                Log.d(LOG, "created new BottomFragment");
                return BottomSheetFragment.newInstance("", "");
        }
        return null;
    }


}
