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

package com.z3r0byte.magis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.z3r0byte.magis.GUI.NavigationDrawer;
import com.z3r0byte.magis.Tasks.AppointmentsTask;
import com.z3r0byte.magis.Tasks.LoginTask;
import com.z3r0byte.magis.Utils.DB_Handlers.CalendarDB;
import com.z3r0byte.magis.Utils.DateUtils;
import com.z3r0byte.magis.Utils.LoginUtils;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;

import java.util.Date;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";


    Toolbar mToolbar;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    ImageButton mNextButton, mPreviousButton;
    CoordinatorLayout coordinatorLayout;

    Profile mProfile;
    public Appointment[] mAppointments;
    public Magister mMagister;
    School mSchool;
    User mUser;

    CalendarDB mCalendarDB;


    Boolean mError = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layout_calendar);


        mCalendarDB = new CalendarDB(this);

        mNextButton = (ImageButton) findViewById(R.id.button_next_day);
        mPreviousButton = (ImageButton) findViewById(R.id.button_previous_day);

        mNextButton.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_forward)
                .color(Color.WHITE).sizeDp(24));
        mPreviousButton.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back)
                .color(Color.WHITE).sizeDp(24));

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: showing snackbar");
                Snackbar.make(coordinatorLayout, R.string.err_no_connection, Snackbar.LENGTH_LONG).show();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.setup_color_3,
                R.color.setup_color_5);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.d(TAG, "onRefresh: Refreshing!");
                        getAppointments();
                    }
                }
        );

        mToolbar = (Toolbar) findViewById(R.id.Toolbar);
        mToolbar.setTitle(R.string.msg_today);
        setSupportActionBar(mToolbar);

        String account = getSharedPreferences("data", MODE_PRIVATE).getString("Profile", null);
        if (account == null) {
            Toast.makeText(CalendarActivity.this, R.string.err_terrible_wrong_on_login, Toast.LENGTH_LONG).show();
            mError = true;
        } else {
            mProfile = new Gson().fromJson(account, Profile.class);
            Log.d(TAG, "onCreate: Profile: " + mProfile.surname);
            mUser = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("User", null), User.class);
            mSchool = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("School", null), School.class);
            getMagister();
            NavigationDrawer.SetupNavigationDrawer(this, coordinatorLayout, this, mToolbar, mProfile, mUser, "Agenda");
        }



    }


    public void getMagister() {
        if (LoginUtils.reLogin(this)) {
            new LoginTask(this, mSchool, mUser).execute();
        } else if (LoginUtils.loginError(this)) {
            Snackbar.make(coordinatorLayout, R.string.snackbar_login_error, Snackbar.LENGTH_INDEFINITE).setAction(R.string.msg_refresh_session_short, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ReLogin.class));
                    finish();
                }
            }).show();
        }

    }

    public void getAppointments() {
        if (mMagister != null) {
            Date from = DateUtils.addDays(DateUtils.getToday(), 0);
            Date until = DateUtils.addDays(DateUtils.getToday(), 1);
            new AppointmentsTask(this, mMagister, from, until).execute();
        } else {
            Snackbar.make(coordinatorLayout, R.string.err_invalid_session, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.msg_refresh_session_short, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getMagister();
                        }
                    }).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /*
    private void getAppointments() {
        String baseUrl = mSchool.url;
        final String fullUrl = baseUrl + "/api/personen/" + mProfile.id + "/afspraken?status=1&tot="
                + DateUtils.formatDate(DateUtils.addDays(DateUtils.getToday(), 1), "yyyy-MM-dd")
                + "&van="
                + DateUtils.formatDate(DateUtils.addDays(DateUtils.getToday(), -14), "yyyy-MM-dd");
        final String cookie = this.getSharedPreferences("data", MODE_PRIVATE).getString("Cookie", null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Date from = DateUtils.addDays(DateUtils.getToday(), -14);
                    Date until = DateUtils.addDays(DateUtils.getToday(), 1);
                    AppointmentHandler appointmentHandler = new AppointmentHandler(mMagister);
                    mAppointments = appointmentHandler.getAppointments(from, until);
                    //mCalendarDB.addItems(mCalendarItems);
                } catch (IOException e) {
                    Snackbar.make(coordinatorLayout, R.string.err_no_connection, Snackbar.LENGTH_LONG);
                }
            }
        }).start();
    }*/


    @Override
    public void onBackPressed() {
        if (NavigationDrawer.isDrawerOpen()) {
            NavigationDrawer.CloseDrawer();
        } else {
            super.onBackPressed();
        }
    }


}
