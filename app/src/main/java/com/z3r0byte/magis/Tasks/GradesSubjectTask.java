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

package com.z3r0byte.magis.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.z3r0byte.magis.Adapters.GradesAdapter;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.MagisActivity;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.container.Study;
import net.ilexiconn.magister.container.sub.SubSubject;
import net.ilexiconn.magister.handler.GradeHandler;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Created by z3r0byte on 19-8-16.
 */

public class GradesSubjectTask extends AsyncTask<Void, Void, Grade[]> {

    private static final String TAG = "GradesTask";


    public MagisActivity activity;
    public Magister magister;
    public Study study;
    public SubSubject subject;

    public String error;


    public GradesSubjectTask(MagisActivity activity, Magister magister, Study study, SubSubject subject) {
        Log.d(TAG, "GradesSubjectTask() called with: activity = [" + activity + "], magister = [" + magister + "], study = [" + study + "], subject = [" + subject + "]");
        this.activity = activity;
        this.magister = magister;
        this.study = study;
        this.subject = subject;
    }

    @Override
    protected void onPreExecute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    protected Grade[] doInBackground(Void... params) {
        try {
            GradeHandler gradeHandler = new GradeHandler(magister);
            Grade[] grades;


            if (study == null) {
                study = magister.currentStudy;
                grades = gradeHandler.getGradesFromSubject(subject, false, false, true);
            } else {
                grades = gradeHandler.getGradesFromSubject(subject, false, false, study);
            }
            Log.d(TAG, "doInBackground: Amount of new grades: " + grades.length);

            /*
            GradesDB gradesDB = new GradesDB(activity);
            gradesDB.addGrades(grades, study.id);

            grades = gradesDB.getUniqueAverageGrades(study)*/
            ;
            return grades;
        } catch (IOException e) {
            Log.e(TAG, "Unable to retrieve data", e);
            error = activity.getString(R.string.err_no_connection);
            return null;
        } catch (InvalidParameterException e) {
            Log.e(TAG, "Invalid Parameters", e);
            error = activity.getString(R.string.err_unknown);
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Grade[] grades) {
        if (grades == null || grades.length == 0) {
            Log.e(TAG, "onPostExecute: No Grades!");
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.listView.setVisibility(View.GONE);
                    activity.mSwipeRefreshLayout.setRefreshing(false);
                    activity.mProgressBar.setVisibility(View.GONE);
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.grades = grades;
                    activity.mGradesAdapter = new GradesAdapter(activity, activity.grades, false);
                    activity.mGradesAdapter.notifyDataSetChanged();
                    activity.listView.setAdapter(activity.mGradesAdapter);
                    activity.listView.setVisibility(View.VISIBLE);
                    activity.mSwipeRefreshLayout.setRefreshing(false);
                    activity.mProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
