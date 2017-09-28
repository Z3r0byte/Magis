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

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.z3r0byte.magis.Adapters.GradesAdapter;
import com.z3r0byte.magis.Tasks.GradesSubjectTask;
import com.z3r0byte.magis.Utils.GlobalMagister;
import com.z3r0byte.magis.Utils.MagisActivity;

import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.container.Study;
import net.ilexiconn.magister.container.sub.SubSubject;

import java.security.InvalidParameterException;

public class GradesSubjectActivity extends MagisActivity {
    private static final String TAG = "GradesSubjectActivity";

    Study study;
    SubSubject subject;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_subject);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Gson gson = new Gson();
            study = gson.fromJson(extras.getString("Study"), Study.class);
            subject = gson.fromJson(extras.getString("Subject"), SubSubject.class);
        } else {
            Log.e(TAG, "onCreate: No valid Magister!", new InvalidParameterException());
            Toast.makeText(this, R.string.err_unknown, Toast.LENGTH_SHORT).show();
            finish();
        }
        mMagister = GlobalMagister.MAGISTER;

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.Toolbar);
        mToolbar.setTitle(subject.name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        loadGrades();
                    }
                }
        );
        mSwipeRefreshLayout.setRefreshing(true);

        grades = new Grade[0];

        listView = (ListView) findViewById(R.id.list_grades);
        mGradesAdapter = new GradesAdapter(this, grades, false);
        listView.setAdapter(mGradesAdapter);

        loadGrades();
    }


    public void loadGrades() {
        new GradesSubjectTask(this, mMagister, study, subject).execute();
    }
}
