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
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.z3r0byte.magis.CalendarActivity;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.LoginUtils;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;
import net.ilexiconn.magister.util.HttpUtil;
import net.ilexiconn.magister.util.SchoolUrl;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;

/**
 * Created by bas on 6-6-16.
 */
public class LoginTask extends AsyncTask<Void, Void, Magister> {
    private static final String TAG = "LoginTask";

    public CalendarActivity activity;
    public School school;
    public String username;
    public String password;

    public String error;

    public MaterialDialog.Builder dialogBuilder;
    public MaterialDialog dialog;

    public LoginTask(CalendarActivity activity, School school, User user) {
        this.activity = activity;
        this.school = school;
        this.username = user.username;
        this.password = user.password;

    }

    @Override
    protected void onPreExecute() {
        dialogBuilder = new MaterialDialog.Builder(activity);
        dialogBuilder.title(R.string.msg_logging_in)
                .content(R.string.msg_patience)
                .positiveText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        cancel(true);
                    }
                })
                .progress(true, 0);
        dialog = dialogBuilder.build();
        dialog.show();
    }

    @Override
    protected Magister doInBackground(Void... params) {
        try {
            SchoolUrl url = new SchoolUrl(school);
            HttpUtil.httpDelete(url.getCurrentSessionUrl());
            return Magister.login(school, username, password);
        } catch (IOException e) {
            Log.e(TAG, "Unable to login", e);
            error = activity.getString(R.string.err_no_connection);
            return null;
        } catch (ParseException e) {
            Log.e(TAG, "Unable to login", e);
            error = activity.getString(R.string.err_unknown);
            return null;
        } catch (InvalidParameterException e) {
            Log.e(TAG, "Invalid credentials", e);
            error = activity.getString(R.string.err_wrong_username_or_password);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Magister magister) {
        if (magister != null) {
            activity.mMagister = magister;
            activity.getAppointments();
            LoginUtils.loginError(activity, false);
            Snackbar.make(activity.getCurrentFocus(), "Ingelogd", Snackbar.LENGTH_LONG).show();
        } else {
            LoginUtils.loginError(activity, true);
            Log.e(TAG, error);
            Snackbar.make(activity.getCurrentFocus(), error, Snackbar.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }
}
