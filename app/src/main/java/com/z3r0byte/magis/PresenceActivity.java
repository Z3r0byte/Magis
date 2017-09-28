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

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.z3r0byte.magis.Adapters.PresencePeriodAdapter;
import com.z3r0byte.magis.GUI.NavigationDrawer;
import com.z3r0byte.magis.Tasks.PresenceTask;
import com.z3r0byte.magis.Utils.ErrorViewConfigs;
import com.z3r0byte.magis.Utils.GlobalMagister;
import com.z3r0byte.magis.Utils.MagisActivity;

import net.ilexiconn.magister.container.PresencePeriod;
import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;
import net.ilexiconn.magister.handler.PresenceHandler;

import java.io.IOException;
import java.util.ArrayList;

import tr.xip.errorview.ErrorView;

public class PresenceActivity extends MagisActivity {
    private static final String TAG = "PresenceActivity";


    Profile mProfile;
    Toolbar mToolbar;
    Spinner spinner;
    PresencePeriodAdapter presencePeriodAdapter;
    PresencePeriod[] presencePeriods = new PresencePeriod[1];
    PresencePeriod presencePeriod = null;
    NavigationDrawer navigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence);

        mMagister = GlobalMagister.MAGISTER;

        mProfile = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("Profile", null), Profile.class);
        mUser = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("User", null), User.class);
        mSchool = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("School", null), School.class);

        mToolbar = (Toolbar) findViewById(R.id.Toolbar);
        mToolbar.setTitle(R.string.title_presence);
        setSupportActionBar(mToolbar);

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
                        if (presencePeriods == null || presencePeriods[0].start == null) {
                            getPresencePeriods();
                        }
                        getPresence();
                    }
                }
        );


        presencePeriods[0] = new PresencePeriod();
        presencePeriods[0].description = "Laden...";
        presencePeriodAdapter = new PresencePeriodAdapter(this, presencePeriods);
        spinner = (Spinner) findViewById(R.id.studyPicker);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP);
        spinner.setAdapter(presencePeriodAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected: PresencePeriod: " + adapterView.getItemAtPosition(i).toString());
                presencePeriod = (PresencePeriod) adapterView.getItemAtPosition(i);
                Log.d(TAG, "onItemSelected: precense date: " + presencePeriod.start);
                if (mMagister != null) {
                    getPresence();
                } else {
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setConfig(ErrorViewConfigs.NotLoggedInConfig);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView = (ListView) findViewById(R.id.list_presence);
        errorView = (ErrorView) findViewById(R.id.error_view_presence);

        navigationDrawer = new NavigationDrawer(this, mToolbar, mProfile, mUser, "Aanwezigheid");
        navigationDrawer.SetupNavigationDrawer();


        if (mMagister != null) {
            getPresencePeriods();
        } else {
            errorView.setVisibility(View.VISIBLE);
            errorView.setConfig(ErrorViewConfigs.NotLoggedInConfig);
        }
        //getPresence();
    }


    private void getPresence() {
        new PresenceTask(this, mMagister, presencePeriod).execute();
    }

    private void getPresencePeriods() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PresenceHandler presenceHandler = new PresenceHandler(mMagister);
                    PresencePeriod[] Periods = presenceHandler.getPresencePeriods();
                    //removing empty ones
                    ArrayList<PresencePeriod> presencePeriods1 = new ArrayList<PresencePeriod>();
                    for (PresencePeriod presencePeriod : Periods) {
                        if (presencePeriod.description != "" && presencePeriod.description != null
                                && !presencePeriod.description.equals("")) {
                            presencePeriods1.add(presencePeriod);
                        }
                    }
                    presencePeriods = new PresencePeriod[presencePeriods1.size()];
                    presencePeriods = presencePeriods1.toArray(presencePeriods);

                    presencePeriodAdapter = new PresencePeriodAdapter(getApplicationContext(), presencePeriods);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinner.setAdapter(presencePeriodAdapter);
                        }
                    });
                    Log.d(TAG, "onCreate: Amount of presencePeriods: " + presencePeriods.length);
                    for (PresencePeriod presencePeriod : presencePeriods) {
                        Log.d(TAG, "onCreate: PresencePeriod: " + presencePeriod.description);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "onCreate: Geen verbinding");
                } catch (NullPointerException e) {
                    Log.e(TAG, "run: Not logged in", e);
                    presencePeriods = new PresencePeriod[1];
                    presencePeriods[0] = new PresencePeriod();
                    presencePeriods[0].description = getString(R.string.err_not_logged_in);
                    presencePeriodAdapter = new PresencePeriodAdapter(getApplicationContext(), presencePeriods);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinner.setAdapter(presencePeriodAdapter);
                        }
                    });
                    for (PresencePeriod presencePeriod : presencePeriods) {
                        Log.d(TAG, "onCreate: PresencePeriod: " + presencePeriod.description);
                    }
                }
            }
        }).start();
    }
}
