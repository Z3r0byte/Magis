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

package com.z3r0byte.magis.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.z3r0byte.magis.Adapters.GradesAdapter;
import com.z3r0byte.magis.GradesSubjectActivity;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Tasks.GradesTask;
import com.z3r0byte.magis.Utils.DB_Handlers.GradesDB;
import com.z3r0byte.magis.Utils.MagisFragment;

import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.container.Study;

public class MainGradesFragment extends MagisFragment {
    private static final String TAG = "MainGradesFragment";
    public static Study study = null;

    View view;

    GradesDB gradesDB;

    public static MainGradesFragment newInstance(ParcelableMagister magister) {
        MainGradesFragment fragment = new MainGradesFragment();
        Bundle args = new Bundle();
        args.putParcelable("Magister", magister);
        fragment.setArguments(args);
        return fragment;
    }

    public MainGradesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_grades, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_refresh);
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

        mMagister = getArguments().getParcelable("Magister");

        grades = new Grade[0];

        listView = (ListView) view.findViewById(R.id.list_grades);
        mGradesAdapter = new GradesAdapter(getActivity(), grades, true);
        listView.setAdapter(mGradesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showStudyGrades(i);
            }
        });

        loadGrades();


        return view;
    }


    public void loadGrades() {
        new GradesTask(this, mMagister, study).execute();
    }

    private void showStudyGrades(int index) {
        Grade grade = grades[index];
        try {
            if (grade.gradeRow.rowSort.getID() == 2) {
                Gson gson = new Gson();
                Intent intent = new Intent(getActivity(), GradesSubjectActivity.class);
                intent.putExtra("Magister", mMagister);
                intent.putExtra("Study", gson.toJson(study));
                intent.putExtra("Subject", gson.toJson(grade.subject));
                startActivity(intent);
            } else if (grade.gradeRow.rowSort.getID() == 6) {
                Toast.makeText(getActivity(), R.string.msg_no_subject, Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "showStudyGrades: Ongeldig cijfer!", e);
        }
    }
    
}
