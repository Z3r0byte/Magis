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

package com.z3r0byte.magis.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;
import net.ilexiconn.magister.util.HttpUtil;
import net.ilexiconn.magister.util.SchoolUrl;

import java.io.IOException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

public class NewGradeService extends Service {

    private static final String TAG = "NewGradeService";

    Timer mTimer = new Timer();
    Magister mMagister;
    School mSchool;
    User mUser;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getData();
        shedule_Notification();

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void shedule_Notification() {
        TimerTask notificationTask = new TimerTask() {
            @Override
            public void run() {
                if (mMagister == null) {
                    login();
                } else if (mMagister.isExpired()) {
                    try {
                        mMagister.login();
                    } catch (IOException e) {
                        Log.e(TAG, "run: Failed to Login", e);
                    }
                }


                Log.d(TAG, "run: Im working on something");
            }
        };

        mTimer.schedule(notificationTask, 1000 * 30, 1000 * 60); //Delay to avoid conflicts with the Session of the app itself
    }

    private void login() {
        try {
            SchoolUrl url = new SchoolUrl(mSchool);
            HttpUtil.httpDelete(url.getCurrentSessionUrl());
            mMagister = ParcelableMagister.login(mSchool, mUser.username, mUser.password);
        } catch (IOException e) {
            Log.e(TAG, "login: Failed to login!", e);
        } catch (ParseException e) {
            Log.e(TAG, "login: Unknown Error", e);
        }
    }

    private void getData() {
        Gson gson = new Gson();
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", MODE_PRIVATE);
        mSchool = gson.fromJson(sharedPreferences.getString("School", null), School.class);
        mUser = gson.fromJson(sharedPreferences.getString("User", null), User.class);
    }
}
