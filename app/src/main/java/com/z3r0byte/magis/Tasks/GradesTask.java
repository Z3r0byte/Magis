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
import com.z3r0byte.magis.Utils.DB_Handlers.GradesDB;
import com.z3r0byte.magis.Utils.MagisFragment;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.container.Study;
import net.ilexiconn.magister.handler.GradeHandler;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Created by bas on 7-7-16.
 */
public class GradesTask extends AsyncTask<Void, Void, Grade[]> {
    private static final String TAG = "GradesTask";


    public MagisFragment fragment;
    public Magister magister;
    public Study study;

    public String error;


    public GradesTask(MagisFragment fragment, Magister magister, Study study) {
        Log.d(TAG, "GradesTask() called with: fragment = [" + fragment + "], magister = [" + magister + "], study = [" + study + "]");
        this.fragment = fragment;
        this.magister = magister;
        this.study = study;
    }

    @Override
    protected void onPreExecute() {
        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment.mSwipeRefreshLayout.setRefreshing(true);
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
                grades = gradeHandler.getGrades(true, false, true);
            } else {
                grades = gradeHandler.getGradesFromStudy(study, true, false);
            }
            Log.d(TAG, "doInBackground: Amount of new grades: " + grades.length);


            //TODO make this work again.
            GradesDB gradesDB = new GradesDB(fragment.getActivity());
            gradesDB.addGrades(grades, study.id);

            grades = gradesDB.getUniqueAverageGrades(study);
            return grades;
        } catch (IOException e) {
            Log.e(TAG, "Unable to retrieve data", e);
            error = fragment.getString(R.string.err_no_connection);
            return null;
        } catch (InvalidParameterException e) {
            Log.e(TAG, "Invalid Parameters", e);
            error = fragment.getString(R.string.err_unknown);
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Grade[] grades) {
        if (grades == null || grades.length == 0) {
            Log.e(TAG, "onPostExecute: No Grades!");
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fragment.listView.setVisibility(View.GONE);
                    fragment.mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fragment.grades = grades;
                    fragment.mGradesAdapter = new GradesAdapter(fragment.getActivity(), fragment.grades);
                    fragment.mGradesAdapter.notifyDataSetChanged();
                    fragment.listView.setAdapter(fragment.mGradesAdapter);
                    fragment.listView.setVisibility(View.VISIBLE);
                    fragment.mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
}
