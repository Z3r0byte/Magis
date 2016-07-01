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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.z3r0byte.magis.Adapters.AppointmentsAdapter;
import com.z3r0byte.magis.DetailActivity.AppointmentDetails;
import com.z3r0byte.magis.GUI.NavigationDrawer;
import com.z3r0byte.magis.Tasks.AppointmentsTask;
import com.z3r0byte.magis.Tasks.LoginTask;
import com.z3r0byte.magis.Utils.DB_Handlers.CalendarDB;
import com.z3r0byte.magis.Utils.DateUtils;
import com.z3r0byte.magis.Utils.ErrorViewConfigs;
import com.z3r0byte.magis.Utils.LoginUtils;
import com.z3r0byte.magis.Utils.MagisActivity;

import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;

import java.util.Calendar;
import java.util.Date;

import tr.xip.errorview.ErrorView;

public class CalendarActivity extends MagisActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "CalendarActivity";


    Toolbar mToolbar;
    ImageButton mNextButton, mPreviousButton;
    public Date firstDate;
    public Date lastDate;

    Profile mProfile;

    Boolean mError = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layout_calendar);
        listView = (ListView) findViewById(R.id.list_calendar);
        errorView = (ErrorView) findViewById(R.id.error_view_calendar);
        mNextButton = (ImageButton) findViewById(R.id.button_next_day);
        mPreviousButton = (ImageButton) findViewById(R.id.button_previous_day);

        mNextButton.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_forward)
                .color(Color.WHITE).sizeDp(24));
        mPreviousButton.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back)
                .color(Color.WHITE).sizeDp(24));
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDay();
            }
        });
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousDay();
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

        errorView.setVisibility(View.GONE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDetails(i);
            }
        });

        new Gson().toJson(mMagister);
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

        date = DateUtils.getToday();

        SharedPreferences sharedPreferences = this.getSharedPreferences("data", MODE_PRIVATE);
        firstDate = DateUtils.parseDate(sharedPreferences.getString("firstDate", ""), "yyyy-MM-dd");
        lastDate = DateUtils.parseDate(sharedPreferences.getString("lastDate", ""), "yyyy-MM-dd");

    }


    public void getMagister() {
        if (LoginUtils.reLogin(this)) {
            new LoginTask(this, mSchool, mUser).execute();
        } else if (LoginUtils.loginError(this)) {
            final MagisActivity activity = this;
            Snackbar.make(coordinatorLayout, R.string.snackbar_login_error, Snackbar.LENGTH_INDEFINITE).setAction(R.string.msg_refresh_session_short, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new LoginTask(activity, mSchool, mUser).execute();
                    finish();
                }
            }).show();
        }

    }

    public void getAppointments() {
        if (mMagister != null) {
            Date from = DateUtils.addDays(date, -7);
            Date until = DateUtils.addDays(date, 14);
            new AppointmentsTask(this, mMagister, from, until, 3).execute();
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

    private void previousDay() {
        date = DateUtils.addDays(date, -1);
        if (date.before(firstDate)) {
            Date until = firstDate;
            Date from = DateUtils.addDays(firstDate, -14);
            if (mMagister != null) {
                new AppointmentsTask(this, mMagister, from, until, 1).execute();
            } else {
                Snackbar.make(coordinatorLayout, R.string.err_invalid_session, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.msg_refresh_session_short, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getMagister();
                            }
                        }).show();
                date = DateUtils.addDays(date, 1);
            }

        } else {
            getAppointmentsByDate(date);
        }

        setToolbarTitle(date);
    }

    private void nextDay() {
        date = DateUtils.addDays(date, 1);
        if (date.after(lastDate)) {
            Date from = date;
            Date until = DateUtils.addDays(lastDate, 14);
            if (mMagister != null) {
                new AppointmentsTask(this, mMagister, from, until, 2).execute();
            } else {
                Snackbar.make(coordinatorLayout, R.string.err_invalid_session, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.msg_refresh_session_short, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getMagister();
                            }
                        }).show();
                date = DateUtils.addDays(date, -1);
            }
        } else {
            getAppointmentsByDate(date);
        }

        setToolbarTitle(date);
    }

    private void getAppointmentsByDate(Date date) {
        mAppointments = new CalendarDB(this).getAppointmentsByDate(date);

        if (mAppointments.length == 0) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setConfig(ErrorViewConfigs.NoLessonConfig);
        } else {
            errorView.setVisibility(View.GONE);
        }

        mAppointmentAdapter = new AppointmentsAdapter(this, mAppointments);
        listView.setAdapter(mAppointmentAdapter);

        setToolbarTitle(date);
    }

    private void setToolbarTitle(Date date) {
        if (date.toString().substring(0, 10).equals(DateUtils.getToday().toString().substring(0, 10))) {
            mToolbar.setTitle(R.string.msg_today);
        } else {
            mToolbar.setTitle(DateUtils.formatDate(date, "EEEE dd MMM"));
        }
    }

    private void chooseDate() {
        Calendar first = Calendar.getInstance();
        first.setTime(firstDate);
        Calendar last = Calendar.getInstance();
        last.setTime(lastDate);
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.setMinDate(first);
        datePicker.setMaxDate(last);
        datePicker.show(getFragmentManager(), "Picker");
    }

    private void showDetails(int i) {
        Intent intent = new Intent(this, AppointmentDetails.class);
        intent.putExtra("Appointment", new Gson().toJson(mAppointments[i]));
        intent.putExtra("Magister", mMagister);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.YEAR, year);
        date = calendar.getTime();
        getAppointmentsByDate(date);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            startActivity(new Intent(getApplicationContext(), AddAppointmentActivity.class).putExtra("Magister", mMagister));
            return true;
        } else if (id == R.id.action_date) {
            chooseDate();
            return true;
        } else if (id == R.id.action_today) {
            date = DateUtils.getToday();
            getAppointmentsByDate(date);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
