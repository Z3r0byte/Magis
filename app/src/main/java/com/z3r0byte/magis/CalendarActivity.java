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
import android.os.Handler;
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
import com.z3r0byte.magis.Utils.DB_Handlers.CalendarDB;
import com.z3r0byte.magis.Utils.DateUtils;
import com.z3r0byte.magis.Utils.LoginUtils;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;
import net.ilexiconn.magister.handler.AppointmentHandler;

import java.io.IOException;
import java.text.ParseException;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";


    Toolbar mToolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ImageButton mNextButton, mPreviousButton;

    Profile mProfile;
    Appointment[] mAppointments;
    Magister mMagister;
    School mSchool;
    User mUser;

    CalendarDB mCalendarDB;

    View view;

    Boolean mError = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        view = findViewById(R.id.layout_calendar);

        mCalendarDB = new CalendarDB(this);

        mNextButton = (ImageButton) findViewById(R.id.button_next_day);
        mPreviousButton = (ImageButton) findViewById(R.id.button_previous_day);

        mNextButton.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_forward)
                .color(Color.WHITE).sizeDp(24));
        mPreviousButton.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back)
                .color(Color.WHITE).sizeDp(24));
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

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }, 6000);
                    }
                }
        );

        mToolbar = (Toolbar) findViewById(R.id.Toolbar);
        mToolbar.setTitle(R.string.msg_today);
        setSupportActionBar(mToolbar);

        String account = getSharedPreferences("data", MODE_PRIVATE).getString("Account", null);
        if (account == null) {
            Toast.makeText(CalendarActivity.this, R.string.err_terrible_wrong_on_login, Toast.LENGTH_LONG).show();
            mError = true;
        } else {
            mProfile = new Gson().fromJson(account, Profile.class);
            mUser = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("User", null), User.class);
            try {
                mMagister = Magister.login(new School(), mUser.username, mUser.password);
            } catch (IOException e) {
                Snackbar.make(view, R.string.err_no_connection, Snackbar.LENGTH_LONG);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        NavigationDrawer.SetupNavigationDrawer(this, this, mToolbar, mProfile, "Agenda");


        if (LoginUtils.reLogin(this)) {
            startActivity(new Intent(this, ReLogin.class));
            finish();
        } else if (LoginUtils.loginError(this)) {
            Snackbar.make(view, R.string.snackbar_login_error, Snackbar.LENGTH_INDEFINITE).setAction(R.string.msg_refresh_session_short, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ReLogin.class));
                    finish();
                }
            }).show();
        } else {
            getCalendar();
        }


    }

    private void getCalendar() {
        String baseUrl = mAccount.getSchool().getUrl();
        final String fullUrl = baseUrl + "/api/personen/" + mAccount.getId() + "/afspraken?status=1&tot="
                + DateUtils.formatDate(DateUtils.addDays(DateUtils.getToday(), 1), "yyyy-MM-dd")
                + "&van="
                + DateUtils.formatDate(DateUtils.addDays(DateUtils.getToday(), -14), "yyyy-MM-dd");
        final String cookie = this.getSharedPreferences("data", MODE_PRIVATE).getString("Cookie", null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mAppointments = AppointmentHandler.get
                    mCalendarDB.addItems(mCalendarItems);
                } catch (IOException e) {
                    Snackbar.make(view, R.string.err_no_connection, Snackbar.LENGTH_LONG);
                }
            }
        }).start();
    }


    @Override
    public void onBackPressed() {
        if (NavigationDrawer.isDrawerOpen()) {
            NavigationDrawer.CloseDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
