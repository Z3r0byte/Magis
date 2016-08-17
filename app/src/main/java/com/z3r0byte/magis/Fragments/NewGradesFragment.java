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


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.z3r0byte.magis.Adapters.NewGradesAdapter;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Tasks.NewGradesTask;
import com.z3r0byte.magis.Utils.MagisFragment;

import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.Grade;


public class NewGradesFragment extends MagisFragment {
    private static final String TAG = "NewGradesFragment";

    View view;

    public static NewGradesFragment newInstance(ParcelableMagister magister) {
        NewGradesFragment fragment = new NewGradesFragment();
        Bundle args = new Bundle();
        args.putParcelable("Magister", magister);
        fragment.setArguments(args);
        return fragment;
    }

    public NewGradesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_grades, container, false);
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
                        refresh();
                    }
                }
        );

        mMagister = getArguments().getParcelable("Magister");

        grades = new Grade[0];

        listView = (ListView) view.findViewById(R.id.list_new_grades);
        mNewGradesAdapter = new NewGradesAdapter(getActivity(), grades);
        listView.setAdapter(mNewGradesAdapter);

        new NewGradesTask(this, mMagister).execute();

        return view;
    }

    private void refresh() {
        new NewGradesTask(this, mMagister).execute();
    }

}
