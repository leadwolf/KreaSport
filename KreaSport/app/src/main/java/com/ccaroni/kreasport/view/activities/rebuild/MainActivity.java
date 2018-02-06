package com.ccaroni.kreasport.view.activities.rebuild;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.databinding.ActivityMainBinding;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    private ActivityMainBinding binding;
    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setDrawer();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // this activity always launches the home fragment
        this.selectDrawerItem(1);
    }

    private void setDrawer() {
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.side_nav_bar)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(android.R.drawable.sym_def_app_icon)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


        PrimaryDrawerItem drawerItemHome = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName("Home")
                .withIcon(R.drawable.ic_home_black_24dp);

        PrimaryDrawerItem drawerItemExplore = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName("Explore")
                .withIcon(R.drawable.ic_map_black_24dp);

        PrimaryDrawerItem drawerItemProfile = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName("Profile")
                .withIcon(R.drawable.ic_person_pin_black_24dp);


        PrimaryDrawerItem drawerItemOfflineAreas = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName("Offline Areas")
                .withIcon(R.drawable.ic_cloud_off_black_24dp);

        PrimaryDrawerItem drawerItemSettings = new PrimaryDrawerItem()
                .withIdentifier(5)
                .withName("Settings").withIcon(R.drawable.ic_settings_black_24dp);


        //create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(binding.toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(1)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        drawerItemHome,
                        drawerItemExplore,
                        drawerItemProfile,
                        drawerItemOfflineAreas,
                        new DividerDrawerItem(),
                        drawerItemSettings
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return MainActivity.this.selectDrawerItem(position);
                    }
                })
                .build();
    }

    private boolean selectDrawerItem(int position) {
        Log.d(TAG, "selected item " + position);


        Fragment fragment = null;
        Class fragmentClass;
        switch (position) {
            case 1:
                fragmentClass = HomeFragment.class;
                break;
            case 2:
                fragmentClass = ExploreFragment.class;
                break;
            case 3:
                fragmentClass = ProfileFragment.class;
                break;
            case 4:
                fragmentClass = OfflineAreasFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


        return false;
    }

}
