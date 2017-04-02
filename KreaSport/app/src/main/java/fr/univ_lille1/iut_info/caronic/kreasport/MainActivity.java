package fr.univ_lille1.iut_info.caronic.kreasport;

import android.content.Context;
import android.content.Intent;
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

import org.osmdroid.util.GeoPoint;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.univ_lille1.iut_info.caronic.kreasport.fragments.BottomSheet;
import fr.univ_lille1.iut_info.caronic.kreasport.fragments.ExploreFragment;
import fr.univ_lille1.iut_info.caronic.kreasport.fragments.HomeFragment;
import fr.univ_lille1.iut_info.caronic.kreasport.maps.MapOptions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String LOG = MainActivity.class.getSimpleName();

    public static final String CALLBACK_KEY = "kreasport.frag_request.reason";
    private static final java.lang.String KEY_CURRENT_ACTIVITY_INDEX = "kreasport.savedinstancestate.current_frag_index";

    protected static final String TAG_HOME = "kreasport.tag.home";
    protected static final String TAG_EXPLORE = "kreasport.tag.explore";
    private static final String TAG_BOTTOM_SHEET = "kreasport.tag.bottom_sheet";

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;
    protected NavigationView navigationView;

    private int currentActivityIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        restoreCurrentActivityIndex();
    }

    private void restoreCurrentActivityIndex() {
        currentActivityIndex = getIntent().getIntExtra(KEY_CURRENT_ACTIVITY_INDEX, -1);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
                currentActivityIndex = 0;
                activityIntent = new Intent(this, HomeActivity.class);
                break;
            case R.id.nav_explore:
                currentActivityIndex = 1;
                activityIntent = new Intent(this, ExploreActivity.class);
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            default:
                break;
        }

        if (activityIntent != null) {
            activityIntent.putExtra(KEY_CURRENT_ACTIVITY_INDEX, currentActivityIndex);
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

    protected Fragment restoreFragment(int id) {
        Fragment fragment;
        switch (id) {
            case R.id.nav_home:
                fragment = getSupportFragmentManager().findFragmentByTag(TAG_HOME);
                if (fragment != null) {
                    Log.d(LOG, "found home in manager");
                    return fragment;
                } else {
                    Log.d(LOG, "created new HomeFragment");
                    return HomeFragment.newInstance("", "");
                }
            case R.id.nav_explore:
                fragment = getSupportFragmentManager().findFragmentByTag(TAG_EXPLORE);
                if (fragment != null) {
                    Log.d(LOG, "found explore in manager");
                    return fragment;
                } else {
                    Log.d(LOG, "created new ExploreFragment");
                    return ExploreFragment.newInstance(
                            new GeoPoint(50.633621, 3.0651845),
                            9,
                            new MapOptions()
                                    .setEnableLocationOverlay(true)
                                    .setEnableCompass(true)
                                    .setEnableMultiTouchControls(true)
                                    .setEnableScaleOverlay(true));
                }
            case R.id.ll_bottom_sheet:
                fragment = getSupportFragmentManager().findFragmentByTag(TAG_BOTTOM_SHEET);
                if (fragment != null) {
                    Log.d(LOG, "found bottomSheet in manager");
                    return fragment;
                } else {
                    Log.d(LOG, "created new BottomFragment");
                    return BottomSheet.newInstance("", "");
                }
        }
        return null;
    }


}
