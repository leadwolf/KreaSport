package com.ccaroni.kreasport.view.activities.rebuild;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    private static final long ID_FIRST_SELECTED_ITEM = 1;


    private ActivityMainBinding binding;
    private Drawer drawer;

    private String[] fragmentTAGS;
    private Fragment[] fragments;

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

        this.initFragments();

        // this activity always launches the home fragment
        this.selectDrawerItem(ID_FIRST_SELECTED_ITEM, (int) (ID_FIRST_SELECTED_ITEM - 1));
    }

    private void initFragments() {
        HomeFragment fragment1 = new HomeFragment();
        ExploreFragment fragment2 = new ExploreFragment();
        ProfileFragment fragment3 = new ProfileFragment();
        OfflineAreasFragment fragment4 = new OfflineAreasFragment();

        this.fragments = new Fragment[]{fragment1, fragment2, fragment3, fragment4};
        this.fragmentTAGS = new String[]{"fragment0_tag", "fragment1_tag", "fragment2_tag", "fragment3_tag"};
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
                .withIdentifier(ID_FIRST_SELECTED_ITEM)
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
                .withSelectedItem(ID_FIRST_SELECTED_ITEM)
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
                        return MainActivity.this.selectDrawerItem(position, position - 1);
                    }
                })
                .build();
    }

    private boolean selectDrawerItem(long id, int realPosition) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        if (realPosition == 0) {
            supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragmentTransaction.replace(R.id.flContent, fragments[realPosition], fragmentTAGS[realPosition]);
        if (realPosition != 0) {
            fragmentTransaction.addToBackStack(fragmentTAGS[realPosition]);
        }

        fragmentTransaction.commit();

        this.drawer.setSelection(id, false);
        return false;
    }

}
