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
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.z3r0byte.magis.Adapters.HomeworkAdapter;
import com.z3r0byte.magis.DetailActivity.HomeworkDetails;
import com.z3r0byte.magis.GUI.NavigationDrawer;
import com.z3r0byte.magis.Listeners.FinishResponder;
import com.z3r0byte.magis.Listeners.SharedListener;
import com.z3r0byte.magis.Tasks.HomeworkTask;
import com.z3r0byte.magis.Utils.DB_Handlers.CalendarDB;
import com.z3r0byte.magis.Utils.DateUtils;
import com.z3r0byte.magis.Utils.ErrorViewConfigs;
import com.z3r0byte.magis.Utils.MagisActivity;

import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.User;

import java.security.InvalidParameterException;
import java.util.Date;

import tr.xip.errorview.ErrorView;

public class HomeworkActivity extends MagisActivity {

    private static final String TAG = "HomeworkActivity";

    Toolbar mToolbar;
    NavigationDrawer navigationDrawer;
    Profile mProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mMagister = extras.getParcelable("Magister");
        } else {
            Log.e(TAG, "onCreate: No valid Magister!", new InvalidParameterException());
            Toast.makeText(this, R.string.err_unknown, Toast.LENGTH_SHORT).show();
            finish();
        }

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.Toolbar);
        mToolbar.setTitle(R.string.title_homework);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProfile = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("Profile", null), Profile.class);
        mUser = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("User", null), User.class);
        navigationDrawer = new NavigationDrawer(this, mToolbar, mProfile, mUser, "Huiswerk");
        navigationDrawer.SetupNavigationDrawer();

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

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
                        loadHomework();
                    }
                }
        );
        mSwipeRefreshLayout.setRefreshing(true);
        errorView = (ErrorView) findViewById(R.id.error_view_homework);

        mAppointments = new Appointment[0];

        listView = (ListView) findViewById(R.id.list_homework);
        mHomeworkAdapter = new HomeworkAdapter(this, mAppointments);
        listView.setAdapter(mHomeworkAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Appointment appointment = mAppointments[i];
                finishAppointment(appointment);
            }
        });

        loadHomework();
        FinishResponder responder = new FinishResponder(this);
        SharedListener.finishInitiator.addListener(responder);
    }

    private void finishAppointment(final Appointment appointment) {
        Intent intent = new Intent(this, HomeworkDetails.class);
        intent.putExtra("Magister", mMagister);
        intent.putExtra("Appointment", new Gson().toJson(appointment));
        startActivity(intent);
    }


    public void applyFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CalendarDB db = new CalendarDB(getApplicationContext());
                mAppointments = db.getHomework(DateUtils.getToday());
                if (mAppointments != null && mAppointments.length != 0) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    errorView.setVisibility(View.GONE);

                    mHomeworkAdapter = new HomeworkAdapter(getApplicationContext(), mAppointments);
                    listView.setAdapter(mHomeworkAdapter);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setConfig(ErrorViewConfigs.NoHomeworkConfig);
                }
            }
        });
    }


    public void loadHomework() {
        if (mMagister == null) {
            applyFinish();
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            Date start = DateUtils.getToday();
            Date end = DateUtils.addDays(DateUtils.getToday(), 14);
            new HomeworkTask(this, mMagister, start, end).execute();
        }
    }
}
