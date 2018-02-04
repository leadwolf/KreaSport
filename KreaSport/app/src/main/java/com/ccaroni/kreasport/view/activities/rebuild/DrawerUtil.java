package com.ccaroni.kreasport.view.activities.rebuild;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ccaroni.kreasport.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

/**
 * Created by Master on 03/02/2018.
 */

public class DrawerUtil {

    public static void getDrawer(final Activity activity, Toolbar toolbar) {
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
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


        PrimaryDrawerItem drawerItemOfflineAreas = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName("Offline Areas")
                .withIcon(R.drawable.ic_cloud_off_black_24dp);

        PrimaryDrawerItem drawerItemProfile = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName("Profile")
                .withIcon(R.drawable.ic_person_pin_black_24dp);

        PrimaryDrawerItem drawerItemSettings = new PrimaryDrawerItem().withIdentifier(3)
                .withName("Settings").withIcon(R.drawable.ic_settings_black_24dp);


        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
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
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return false;
                    }
                })
                .build();
    }

}
