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
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.z3r0byte.magis.AccountActivity;
import com.z3r0byte.magis.GradeActivity;
import com.z3r0byte.magis.HomeworkActivity;
import com.z3r0byte.magis.Networking.GetRequest;
import com.z3r0byte.magis.PresenceActivity;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.SettingsActivity;
import com.z3r0byte.magis.StartActivity;
import com.z3r0byte.magis.Tasks.LoginTask;
import com.z3r0byte.magis.Utils.DB_Handlers.CalendarDB;
import com.z3r0byte.magis.Utils.DB_Handlers.GradesDB;
import com.z3r0byte.magis.Utils.DB_Handlers.NewGradesDB;
import com.z3r0byte.magis.Utils.MagisActivity;

import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.Status;
import net.ilexiconn.magister.container.User;

import java.io.IOException;

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
    static PrimaryDrawerItem presenceItem = new PrimaryDrawerItem().withName(R.string.title_presence)
            .withIcon(GoogleMaterial.Icon.gmd_highlight_off).withSelectable(false);
    static PrimaryDrawerItem refreshSessionItem = new SecondaryDrawerItem().withName(R.string.drawer_refresh_session)
            .withIcon(GoogleMaterial.Icon.gmd_refresh).withSelectable(false);
    static PrimaryDrawerItem logoutItem = new SecondaryDrawerItem().withName(R.string.drawer_logout)
            .withIcon(GoogleMaterial.Icon.gmd_exit_to_app).withSelectable(false);
    static PrimaryDrawerItem bugItem = new SecondaryDrawerItem().withName(R.string.report_bug)
            .withIcon(GoogleMaterial.Icon.gmd_bug_report).withSelectable(false);
    static PrimaryDrawerItem settingsItem = new SecondaryDrawerItem().withName(R.string.drawer_settings)
            .withIcon(GoogleMaterial.Icon.gmd_settings).withSelectable(false);
    static PrimaryDrawerItem statusItem = new SecondaryDrawerItem().withName(R.string.drawer_status)
            .withIcon(GoogleMaterial.Icon.gmd_dns).withSelectable(false).withBadgeStyle(new BadgeStyle(Color.GRAY, Color.GRAY).withTextColor(Color.WHITE)).withBadge("?").withIdentifier(123);
    static PrimaryDrawerItem sponsorItem = new SecondaryDrawerItem().withName(R.string.title_ad)
            .withIcon(GoogleMaterial.Icon.gmd_attach_money).withSelectable(false);


    public void SetupNavigationDrawer() {
        getStatus();

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
                .withSelectionListEnabledForSingleProfile(false)
                .build();


        drawer = new DrawerBuilder()
                .withAccountHeader(accountHeader)
                .withActivity(activity)
                .withToolbar(toolbar)
                .addDrawerItems(
                        calendarItem,
                        gradeItem,
                        homeworkItem,
                        presenceItem,
                        new SectionDrawerItem().withName(R.string.drawer_tools),
                        //refreshSessionItem,
                        settingsItem,
                        bugItem,
                        //sponsorItem,
                        statusItem,
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
                                            new NewGradesDB(activity).removeAll();
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
                            closeActivity();
                        } else if (drawerItem == presenceItem && selection != "Aanwezigheid") {
                            drawer.closeDrawer();
                            Intent intent = new Intent(activity, PresenceActivity.class);
                            intent.putExtra("Magister", activity.mMagister);
                            activity.startActivity(intent);
                            closeActivity();
                        } else if (drawerItem == gradeItem && selection != "Cijfers") {
                            CloseDrawer();
                            Intent intent = new Intent(activity, GradeActivity.class);
                            intent.putExtra("Magister", activity.mMagister);
                            activity.startActivity(new Intent(intent));
                            closeActivity();
                        } else if (drawerItem == bugItem) {
                            reportBug();
                            drawer.closeDrawer();
                        } else if (drawerItem == statusItem) {
                            explainStatus();
                            drawer.closeDrawer();
                        } else if (drawerItem == sponsorItem) {
                            CloseDrawer();
                            //Intent intent = new Intent(activity, AdActivity.class);
                            //activity.startActivity(new Intent(intent));
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
            case "Aanwezigheid":
                drawer.setSelection(presenceItem);
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

    private void closeActivity() {
        if (selection != "Agenda") {
            activity.finish();
        }
    }

    private void getStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Status status;
                try {
                    status = Status.getStatusByString(GetRequest.getRequest("http://status.magistify.nl/API/status", null));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                Log.d(TAG, "run: Status: " + status.getStatus());
                if (status == Status.OK) {
                    statusItem.withBadgeStyle(new BadgeStyle(Color.rgb(0, 153, 0), Color.rgb(0, 153, 0)).withTextColor(Color.WHITE)).withBadge("✔");
                } else if (status == Status.SLOW) {
                    statusItem.withBadgeStyle(new BadgeStyle(Color.rgb(255, 128, 0), Color.rgb(255, 128, 0)).withTextColor(Color.WHITE)).withBadge("~");
                } else if (status == Status.OFFLINE) {
                    statusItem.withBadgeStyle(new BadgeStyle(Color.RED, Color.RED).withTextColor(Color.WHITE)).withBadge("✖");
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        drawer.updateItem(statusItem);
                    }
                });
            }
        }).start();
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

    private void explainStatus() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(activity.getString(R.string.dialog_status_title));
        alertDialogBuilder.setMessage(activity.getString(R.string.dialog_status_desc));
        alertDialogBuilder.setPositiveButton("Oké", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
