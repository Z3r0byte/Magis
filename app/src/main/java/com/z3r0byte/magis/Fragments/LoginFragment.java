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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.z3r0byte.magis.Networking.DeleteRequest;
import com.z3r0byte.magis.Networking.GetRequest;
import com.z3r0byte.magis.Networking.PostRequest;
import com.z3r0byte.magis.R;

import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;

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
    Boolean mSuccessfulLogin = false;
    Boolean mLoginError = false;

    EditText mUserNameEditText;
    EditText mPasswordEditText;
    Button mLogin;

    String mUrl;
    String mCookie;

    Profile mProfile = new Profile();
    School mSchool;
    User mUser;

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
                    mSchool = new Gson().fromJson(url, School.class);
                    mUrl = mSchool.url;
                }

                login(mUserNameEditText.getText().toString(), mPasswordEditText.getText().toString());
            }
        });

        return view;
    }

    private void ResetButton() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSuccessfulLogin) {
                    mLogin.setText(R.string.msg_logged_in);
                } else {
                    mLogin.setEnabled(true);
                    mUserNameEditText.setEnabled(true);
                    mPasswordEditText.setEnabled(true);
                    mLogin.setText(R.string.msg_login);
                }
            }
        });
    }

    private void login(final String UserName, final String Password) {
        mLoginError = false;
        mLogin.setEnabled(false);
        mUserNameEditText.setEnabled(false);
        mPasswordEditText.setEnabled(false);
        mLogin.setText(R.string.msg_logging_in);

        if (UserName == null || UserName.length() == 0 || UserName == "") {
            Log.e(TAG, "login: Username is not filled in");
            ResetButton();
        }

        if (Password == null || Password.length() == 0 || Password == "") {
            Log.e(TAG, "login: Password is not filled in");
            ResetButton();
        }

        Log.d(TAG, "login() called with: " + "UserName = [" + UserName + "], Password = " +
                "[" + Password.substring(0, 2) + "******" + "]"); //Not displaying entire password in log for safety reasons

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Deleting old session
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
                    ResetButton();
                    return;
                }

                //Logging in with username/password
                try {
                    JSONObject jo = new JSONObject();
                    jo.put("Gebruikersnaam", UserName);
                    jo.put("Wachtwoord", Password);
                    jo.put("IngelogdBlijven", true);
                    String Data = jo.toString();
                    mCookie = PostRequest.postRequest(mUrl + "/api/sessies", initiateCookie, Data);
                    if (mCookie == null || mCookie == "null") {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLoginError = true;
                                Toast.makeText(c, getResources().getString(R.string.err_wrong_username_or_password), Toast.LENGTH_SHORT).show();
                                ResetButton();
                                return;
                            }
                        });
                    }
                    Log.d(TAG, "run: mCookie: " + mCookie);
                } catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, getResources().getString(R.string.err_no_connection), Toast.LENGTH_SHORT).show();
                        }
                    });
                    ResetButton();
                    return;
                } catch (JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, getResources().getString(R.string.err_unknown), Toast.LENGTH_SHORT).show();
                        }
                    });
                    ResetButton();
                    return;
                }


                if (!mLoginError) {
                    //Getting sessions
                    try {
                        String session = GetRequest.getRequest(mUrl + "/api/sessie/huidige", mCookie);
                    } catch (IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(c, getResources().getString(R.string.err_no_connection), Toast.LENGTH_SHORT).show();
                            }
                        });
                        ResetButton();
                        return;
                    }


                    try {
                        String account = GetRequest.getRequest(mUrl + "/api/account", mCookie);

                        JsonParser parser = new JsonParser();
                        JsonObject jsonObject = parser.parse(account).getAsJsonObject();

                        mProfile = new Gson().fromJson(jsonObject.getAsJsonObject("Persoon"), Profile.class);
                        if (mProfile.nickname != null && mProfile.nickname != "null") {
                            mUser = new User(UserName, Password, true);
                            String Account = new Gson().toJson(mProfile, Profile.class);
                            String User = new Gson().toJson(mUser, net.ilexiconn.magister.container.User.class);

                            SharedPreferences.Editor editor = c.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                            editor.putString("Profile", Account);
                            editor.putString("User", User);
                            editor.putBoolean("LoggedIn", true);
                            editor.putInt("DataVersion", 3);
                            editor.apply();

                            mSuccessfulLogin = true;
                            mAllowForward = true;
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(c, getResources().getString(R.string.err_unknown), Toast.LENGTH_SHORT).show();
                                }
                            });
                            ResetButton();
                            return;
                        }
                    } catch (IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(c, getResources().getString(R.string.err_no_connection), Toast.LENGTH_SHORT).show();
                            }
                        });
                        ResetButton();
                        return;
                    }


                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ResetButton();
            }
        }).start();
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

