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

package com.z3r0byte.magis.Listeners;

import android.util.Log;

import com.z3r0byte.magis.CalendarActivity;
import com.z3r0byte.magis.HomeworkActivity;

/**
 * Created by bas on 3-10-16.
 */

public class FinishResponder implements FinishListener {
    private static final String TAG = "Responder";

    CalendarActivity calendarActivity;
    HomeworkActivity homeworkActivity;

    public FinishResponder(CalendarActivity calendarActivity) {
        this.calendarActivity = calendarActivity;
    }

    public FinishResponder(HomeworkActivity homeworkActivity) {
        this.homeworkActivity = homeworkActivity;
    }

    @Override
    public void applyFinish() {
        Log.d(TAG, "applyFinish: Got event.");
        if (homeworkActivity != null) {
            homeworkActivity.applyFinish();
        } else if (calendarActivity != null) {
            calendarActivity.applyFinish();
        } else {
            //print exception, but do nothing.
            Log.e(TAG, "applyFinish: Invalid activity parsed", new IllegalArgumentException("No activity parsed."));
        }
    }
}