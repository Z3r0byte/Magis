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

package com.z3r0byte.magis.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.z3r0byte.magis.R;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;

/**
 * Created by bas on 24-5-16.
 */
public class LoginUtils {

    private static final String TAG = "LoginUtils";

    public static boolean reLogin(Context context) {
        String format = "yyyy-MM-dd-HH:mm:ss";
        Date dateNew = new Date();

        Calendar calnew = Calendar.getInstance();
        calnew.setTime(dateNew);
        SharedPreferences pref = context.getSharedPreferences("data", context.MODE_PRIVATE);
        String lastLogin = pref.getString("LastLogin", null);
        if (lastLogin == null) {
            Log.d(TAG, "reLogin: hasn't logged in even once!");
            return true;
        } else {
            Date dateOld = new Date();
            Calendar calold = Calendar.getInstance();
            calold.setTime(DateUtils.parseDate(lastLogin, format));
            dateOld.setTime(calold.getTimeInMillis());
            Log.d(TAG, "reLogin: Comparing dates: " + abs(dateNew.getTime() - dateOld.getTime()));
            if (abs(dateNew.getTime() - dateOld.getTime()) < 900000) {
                return false;
            }
        }

        if (loginError(context)) {
            Log.d(TAG, "reLogin: LoginError!");
            return false;
        }

        return true;
    }

    public static void loginError(Context context, Boolean error) {
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("LoginError", error).apply();
    }

    public static boolean loginError(Context context) {
        return context.getSharedPreferences("data", Context.MODE_PRIVATE).getBoolean("LoginError", false);
    }

    public static Magister magisterLogin(final Context c, User user, School school, View view) {
        /*MaterialDialog materialDialog = new MaterialDialog.Builder(c)
                .content(R.string.msg_logging_in)
                .progress(true, 0)
                .show();

        .negativeText(android.R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new MaterialDialog.Builder(c)
                                .title(R.string.dialog_skip_login_title)
                                .content(R.string.dialog_skip_login_desc)
                                .positiveText(android.R.string.ok)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        LoginUtils.loginError(c, true);
                                    }
                                })
                                .show();
                    }
                })*/
        Magister magister = null;
        try {
            magister = Magister.login(school, user.username, user.password);
        } catch (IOException e) {
            e.printStackTrace();
            Snackbar.make(view, R.string.err_no_connection, Snackbar.LENGTH_LONG);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //materialDialog.dismiss();
        return magister;
    }
}