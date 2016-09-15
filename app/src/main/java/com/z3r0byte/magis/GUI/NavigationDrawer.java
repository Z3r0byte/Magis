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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.z3r0byte.magis.AccountActivity;
import com.z3r0byte.magis.GradeActivity;
import com.z3r0byte.magis.HomeworkActivity;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.SettingsActivity;
import com.z3r0byte.magis.StartActivity;
import com.z3r0byte.magis.Tasks.LoginTask;
import com.z3r0byte.magis.Utils.DB_Handlers.CalendarDB;
import com.z3r0byte.magis.Utils.DB_Handlers.GradesDB;
import com.z3r0byte.magis.Utils.MagisActivity;

import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.User;

/**
 * Created by basva on 14-5-2016.
 */
public class NavigationDrawer {

    private static final String TAG = "NavigationDrawer";

    Drawer drawer;

    MagisActivity activity;
    Toolbar toolbar;
    Profile profile;
    User user;
    String selection;

    public NavigationDrawer(MagisActivity activity, Toolbar toolbar, Profile profile, User user, String selection) {
        this.activity = activity;
        this.toolbar = toolbar;
        this.profile = profile;
        this.user = user;
        this.selection = selection;
    }


    static PrimaryDrawerItem calendarItem = new PrimaryDrawerItem().withName(R.string.title_calendar)
            .withIcon(GoogleMaterial.Icon.gmd_today);
    static PrimaryDrawerItem gradeItem = new PrimaryDrawerItem().withName(R.string.title_grades)
            .withIcon(GoogleMaterial.Icon.gmd_timeline).withSelectable(false);
    static PrimaryDrawerItem homeworkItem = new PrimaryDrawerItem().withName(R.string.title_homework)
            .withIcon(GoogleMaterial.Icon.gmd_assignment).withSelectable(false);
    static PrimaryDrawerItem refreshSessionItem = new SecondaryDrawerItem().withName(R.string.drawer_refresh_session)
            .withIcon(GoogleMaterial.Icon.gmd_refresh).withSelectable(false);
    static PrimaryDrawerItem logoutItem = new SecondaryDrawerItem().withName(R.string.drawer_logout)
            .withIcon(GoogleMaterial.Icon.gmd_exit_to_app).withSelectable(false);
    static PrimaryDrawerItem bugItem = new SecondaryDrawerItem().withName(R.string.report_bug)
            .withIcon(GoogleMaterial.Icon.gmd_bug_report).withSelectable(false);
    static PrimaryDrawerItem settingsItem = new SecondaryDrawerItem().withName(R.string.drawer_settings)
            .withIcon(GoogleMaterial.Icon.gmd_settings).withSelectable(false);

    public void SetupNavigationDrawer() {

        final AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header_bg)
                .addProfiles(
                        new ProfileDrawerItem().withName(profile.nickname).withEmail(user.username).withIcon(R.drawable.magis512)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        activity.startActivity(new Intent(activity, AccountActivity.class));
                        drawer.closeDrawer();
                        return false;
                    }
                })
                .withSelectionListEnabledForSingleProfile(true)
                .build();


        drawer = new DrawerBuilder()
                .withAccountHeader(accountHeader)
                .withActivity(activity)
                .withToolbar(toolbar)
                .addDrawerItems(
                        calendarItem,
                        gradeItem,
                        homeworkItem,
                        new SectionDrawerItem().withName(R.string.drawer_tools),
                        //refreshSessionItem,
                        settingsItem,
                        bugItem,
                        logoutItem
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem == refreshSessionItem) {
                            new LoginTask(activity, activity.mSchool, activity.mUser);
                        } else if (drawerItem == logoutItem) {
                            new MaterialDialog.Builder(activity)
                                    .title(R.string.dialog_logout_title)
                                    .content(R.string.dialog_logout_desc)
                                    .positiveText(android.R.string.ok)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            activity.getSharedPreferences("data", Context.MODE_PRIVATE).edit().clear().apply();
                                            new CalendarDB(activity).removeAll();
                                            new GradesDB(activity).removeAll();
                                            activity.startActivity(new Intent(activity, StartActivity.class));
                                            activity.finish();
                                        }
                                    })
                                    .negativeText(android.R.string.cancel)
                                    .show();

                        } else if (drawerItem == settingsItem) {
                            activity.startActivity(new Intent(activity, SettingsActivity.class));
                            drawer.closeDrawer();
                        } else if (drawerItem == calendarItem && selection != "Agenda") {
                            activity.finish();
                            drawer.closeDrawer();
                        } else if (drawerItem == homeworkItem && selection != "Huiswerk") {
                            drawer.closeDrawer();
                            Intent intent = new Intent(activity, HomeworkActivity.class);
                            intent.putExtra("Magister", activity.mMagister);
                            activity.startActivity(intent);
                        } else if (drawerItem == gradeItem && selection != "Cijfers") {
                            CloseDrawer();
                            Intent intent = new Intent(activity, GradeActivity.class);
                            intent.putExtra("Magister", activity.mMagister);
                            activity.startActivity(new Intent(intent));
                        } else if (drawerItem == bugItem) {
                            reportBug();
                            drawer.closeDrawer();
                        }
                        return true;
                    }
                })
                .build();

        setSelection(selection);
    }

    private void setSelection(String selection) {
        switch (selection) {
            case "Agenda":
                drawer.setSelection(calendarItem);
                break;
            case "Cijfers":
                drawer.setSelection(gradeItem);
                break;
            case "Huiswerk":
                drawer.setSelection(homeworkItem);
                break;
            case "":
                drawer.setSelection(-1);
                break;
        }
    }

    public void CloseDrawer() {
        drawer.closeDrawer();
    }

    public Boolean isDrawerOpen() {
        return drawer.isDrawerOpen();
    }

    private void reportBug() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(activity.getString(R.string.dialog_bug_title));
        alertDialogBuilder.setMessage(activity.getString(R.string.dialog_bug_desc));
        alertDialogBuilder.setPositiveButton("Oké", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
