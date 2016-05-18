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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.z3r0byte.magis.Magister.MagisterSchool;
import com.z3r0byte.magis.Networking.DeleteRequest;
import com.z3r0byte.magis.Networking.PostRequest;
import com.z3r0byte.magis.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginFragment extends SlideFragment {

    private static final String TAG = "LoginFragment";


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    Boolean mAllowForward = false;
    Boolean mSuccessfulLogin;
    Boolean mLoginError = false;

    EditText mUserNameEditText;
    EditText mPasswordEditText;
    Button mLogin;

    String mUrl;
    String mCookie;

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

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUrl == null) {
                    String url = c.getSharedPreferences("data", Context.MODE_PRIVATE).getString("School", null);
                    if (url == null) {
                        throw new IllegalArgumentException("No school is saved!");
                    }
                    mUrl = new Gson().fromJson(url, MagisterSchool.class).getUrl();
                }

                mSuccessfulLogin = login(mUserNameEditText.getText().toString(), mPasswordEditText.getText().toString());
            }
        });

        return view;
    }

    private Boolean login(final String UserName, final String Password) {
        if (UserName == null || UserName.length() == 0 || UserName == "") {
            Log.e(TAG, "login: Username is not filled in");
            return false;
        }

        if (Password == null || Password.length() == 0 || Password == "") {
            Log.e(TAG, "login: Password is not filled in");
            return false;
        }

        Log.d(TAG, "login() called with: " + "UserName = [" + UserName + "], Password = " +
                "[" + Password.substring(0, 2) + "******" + "]"); //Not displaying entire password in log, for safety reasons

        new Thread(new Runnable() {
            @Override
            public void run() {
                String initiateCookie;
                try {
                    initiateCookie = DeleteRequest.deleteRequest(mUrl + "/api/sessies/huidige");
                    Log.d(TAG, "run: inatiateCookie: " + initiateCookie);
                } catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, getResources().getString(R.string.err_no_connection), Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                try {
                    JSONObject jo = new JSONObject();
                    jo.put("Gebruikersnaam", UserName);
                    jo.put("Wachtwoord", Password);
                    jo.put("IngelogdBlijven", true);
                    String Data = jo.toString();
                    mCookie = PostRequest.postRequest(mUrl + "/api/sessies", initiateCookie, Data);
                    Log.d(TAG, "run: mCookie: " + mCookie);
                } catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, getResources().getString(R.string.err_no_connection), Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                } catch (JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, getResources().getString(R.string.err_unknown), Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

            }
        }).start();
        return true;
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

