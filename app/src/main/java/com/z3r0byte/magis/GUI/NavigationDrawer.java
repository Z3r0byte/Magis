/*
 * Copyright 2016 Bas van den Boom 'Z3r0byte'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.z3r0byte.magis.GUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.z3r0byte.magis.Magister.MagisterAccount;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.ReLogin;

/**
 * Created by basva on 14-5-2016.
 */
public class NavigationDrawer {

    static Drawer drawer;

    static PrimaryDrawerItem calendarItem = new PrimaryDrawerItem().withName(R.string.title_calendar)
            .withIcon(GoogleMaterial.Icon.gmd_today);
    static PrimaryDrawerItem refreshSessionItem = new SecondaryDrawerItem().withName(R.string.drawer_refresh_session)
            .withIcon(GoogleMaterial.Icon.gmd_refresh).withSelectable(false);
    static PrimaryDrawerItem logoutItem = new SecondaryDrawerItem().withName(R.string.drawer_logout)
            .withIcon(GoogleMaterial.Icon.gmd_exit_to_app).withSelectable(false);

    public static void SetupNavigationDrawer(final Context c, final Activity activity, Toolbar toolbar, MagisterAccount account, String selection) {

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header_bg)
                .addProfiles(
                        new ProfileDrawerItem().withName(account.getFullName()).withEmail(account.getUsername()).withIcon(R.drawable.magis512)
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();


        drawer = new DrawerBuilder()
                .withAccountHeader(accountHeader)
                .withActivity(activity)
                .withToolbar(toolbar)
                .addDrawerItems(
                        calendarItem,
                        new DividerDrawerItem(),
                        refreshSessionItem,
                        logoutItem
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem == refreshSessionItem) {
                            c.startActivity(new Intent(c, ReLogin.class));
                            activity.finish();
                        }
                        return true;
                    }
                })
                .build();

        setSelection(selection);
    }

    private static void setSelection(String selection) {
        switch (selection) {
            case "Agenda":
                drawer.setSelection(calendarItem);
                break;
            case "":
                drawer.setSelection(-1);
        }
    }

    public static void CloseDrawer() {
        drawer.closeDrawer();
    }

    public static Boolean isDrawerOpen() {
        return drawer.isDrawerOpen();
    }


}
