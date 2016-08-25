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


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.z3r0byte.magis.R;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;

import java.io.IOException;
import java.text.ParseException;

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
                Magister magister = null;
                try {
                    magister = Magister.login(mSchool, UserName, Password);
                } catch (final IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setTitle("Problemen met inloggen?");
                            alertDialogBuilder.setMessage("Zegt de app dat je geen verbinding hebt, maar heb je dat wel? Klik dan op \"Mail mij\" om " +
                                    "een error rapport naar mij te sturen. Zo help je mij met het oplossen van deze fout. Bedankt!");
                            alertDialogBuilder.setPositiveButton("Mail mij", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"z3r0byte.apps@gmail.com"});
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Login foutrapport");
                                    intent.putExtra(Intent.EXTRA_TEXT, "Foutrapport: " + e.getMessage());
                                    try {
                                        startActivity(Intent.createChooser(intent, "Verzend mail..."));
                                    } catch (ActivityNotFoundException ex) {
                                        Toast.makeText(getActivity(), "Geen email programma gevonden", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            alertDialogBuilder.setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                            e.printStackTrace();

                            Toast.makeText(c, getResources().getString(R.string.err_no_connection), Toast.LENGTH_SHORT).show();
                        }
                    });
                    ResetButton();
                    return;
                } catch (ParseException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, R.string.err_unknown, Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                if (magister != null) {
                    mProfile = magister.profile;
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
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, R.string.err_wrong_username_or_password, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //Adding timout to prevent session conflicts
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLogin.setText(R.string.msg_get_user_data);
                    }
                });
                try {
                    Thread.sleep(2800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLogin.setText(R.string.msg_preparing_databases);
                    }
                });

                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLogin.setText(R.string.msg_final_preparations);
                    }
                });
                try {
                    Thread.sleep(1500);
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

