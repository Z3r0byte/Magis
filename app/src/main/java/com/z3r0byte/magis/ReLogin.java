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

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;

public class ReLogin extends AppCompatActivity {

    String mCookie;
    Profile mProfile;
    School mSchool;
    Magister mMagister;
    User mUser;

    TextView mTextViewStatus;

    Boolean mLoginError = false;
    private static final String TAG = "ReLogin";
}

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_login);

        mTextViewStatus = (TextView) findViewById(R.id.text_login_progress);

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        mProfile = new Gson().fromJson(sharedPreferences.getString("Profile", null), Profile.class);
        mUser = new Gson().fromJson(sharedPreferences.getString("User", null), User.class);
        mSchool = new Gson().fromJson(sharedPreferences.getString("School", null), School.class);


        try{
            mMagister = Magister.login(mSchool, mUser.username, mUser.password);
        } catch (IOException e){
            mLoginError = true;
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.err_wrong_username_or_password), Toast.LENGTH_SHORT).show();
            return;
        } catch ()


        new Thread(new Runnable() {
            @Override
            public void run() {
                String initiateCookie;
                try {
                    initiateCookie = DeleteRequest.deleteRequest(mSchool.getUrl() + "/api/sessies/huidige");
                    Log.d(TAG, "run: inatiateCookie: " + initiateCookie);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextViewStatus.setText(R.string.msg_login_progress_2);
                        }
                    });
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.err_no_connection), Toast.LENGTH_SHORT).show();
                        }
                    });
                    loginFailed();
                    return;
                }

                //Logging in with username/password
                try {
                    JSONObject jo = new JSONObject();
                    jo.put("Gebruikersnaam", mAccount.getUsername());
                    jo.put("Wachtwoord", mAccount.getPasssword());
                    jo.put("IngelogdBlijven", true);
                    String Data = jo.toString();
                    mCookie = PostRequest.postRequest(mSchool.getUrl() + "/api/sessies", initiateCookie, Data);
                    if (mCookie == null || mCookie == "null") {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLoginError = true;
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.err_wrong_username_or_password), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                    }
                    Log.d(TAG, "run: mCookie: " + mCookie);
                    getApplicationContext().getSharedPreferences("data", MODE_PRIVATE).edit().putString("Cookie", mCookie).apply();

                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    date.setTime(calendar.getTimeInMillis());
                    DateUtils.lastLogin(getApplicationContext(), DateUtils.formatDate(date, "yyyy-MM-dd-HH:mm:ss"));

                    LoginUtils.loginError(getApplicationContext(), false);
                    startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                    finish();
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.err_no_connection), Toast.LENGTH_SHORT).show();
                        }
                    });
                    loginFailed();
                    return;
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.err_unknown), Toast.LENGTH_SHORT).show();
                        }
                    });
                    loginFailed();
                    return;
                }
            }
        }).start();

    }

    private void loginFailed() {
        LoginUtils.loginError(getApplicationContext(), true);
        startActivity(new Intent(this, CalendarActivity.class));
        finish();
    }

    private void skipLogin() {
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_skip_login_title)
                .content(R.string.dialog_skip_login_desc)
                .positiveText(android.R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        loginFailed();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        skipLogin();
    }


}*/
