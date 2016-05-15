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
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.z3r0byte.magis.Networking.GetRequest;
import com.z3r0byte.magis.R;

import java.io.IOException;

public class SearchSchoolFragment extends SlideFragment {

    public SearchSchoolFragment() {
        // Required empty public constructor
    }

    public static SearchSchoolFragment newInstance() {
        return new SearchSchoolFragment();
    }

    Boolean mAllowForward = false;

    EditText mEditTextSchool;
    Button mButtonSearch;
    ListView mListSchool;
    View view;

    String mSchoolJSON;

    Thread mSearchThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_school, container, false);

        mEditTextSchool = (EditText) view.findViewById(R.id.edit_text_search_school);
        mButtonSearch = (Button) view.findViewById(R.id.button_search_school);
        mListSchool = (ListView) view.findViewById(R.id.list_schools);

        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchSchool();
            }
        });


        return view;
    }

    private void SearchSchool() {
        final String school = mEditTextSchool.getText().toString();
        mEditTextSchool.setEnabled(false);
        mButtonSearch.setEnabled(false);
        mButtonSearch.setText(R.string.msg_searching);
        mSearchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSchoolJSON = GetRequest.getRequest("https://mijn.magister.net/api/schools?filter=" + school, null);
                } catch (IOException exception) {
                    Snackbar.make(view, R.string.err_unknown, Snackbar.LENGTH_LONG);
                    exception.printStackTrace();
                }
            }
        });
        mSearchThread.start();
    }

    @Override
    public boolean canGoForward() {
        return mAllowForward;
    }

    @Override
    public void onDestroy() {
        mSearchThread.stop();
        super.onDestroy();
    }


}
