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


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.z3r0byte.magis.R;

public class LoginFragment extends SlideFragment {

    private static final String TAG = "LoginFragment";


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    Boolean mAllowForward = false;
    Boolean mLoginError = false;

    EditText mUserNameEditText;
    EditText mPasswordEditText;
    Button mLogin;

    View view;

    Context c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        c = getActivity();


        mUserNameEditText = (EditText) view.findViewById(R.id.edit_text_username);
        mPasswordEditText = (EditText) view.findViewById(R.id.edit_text_password);
        mLogin = (Button) view.findViewById(R.id.button_login);


        return view;
    }


    @Override
    public boolean canGoForward() {
        return mAllowForward;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}

