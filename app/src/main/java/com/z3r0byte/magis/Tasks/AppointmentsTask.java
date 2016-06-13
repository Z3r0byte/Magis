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
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.z3r0byte.magis.Adapters.AppointmentsAdapter;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.DB_Handlers.CalendarDB;
import com.z3r0byte.magis.Utils.MagisActivity;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.handler.AppointmentHandler;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Date;

/**
 * Created by bas on 6-6-16.
 */
public class AppointmentsTask extends AsyncTask<Void, Void, Appointment[]> {
    private static final String TAG = "AppointmentsTask";

    public MagisActivity activity;
    public Magister magister;
    public Date date1;
    public Date date2;

    public String error;


    public AppointmentsTask(MagisActivity activity, Magister magister, Date date1, Date date2) {
        this.activity = activity;
        this.magister = magister;
        this.date1 = date1;
        this.date2 = date2;

    }

    @Override
    protected void onPreExecute() {
        activity.mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected Appointment[] doInBackground(Void... params) {
        try {
            AppointmentHandler appointmentHandler = new AppointmentHandler(magister);
            Appointment[] appointments = appointmentHandler.getAppointments(date1, date2);

            CalendarDB db = new CalendarDB(activity);
            db.addItems(appointments);
            appointments = db.getAppointmentsByDate(activity.date);

            Log.d(TAG, "doInBackground: " + appointments.length);
            return appointments;
        } catch (IOException e) {
            Log.e(TAG, "Unable to retrieve data", e);
            error = activity.getString(R.string.err_no_connection);
            return null;
        } catch (InvalidParameterException e) {
            Log.e(TAG, "Invalid Parameters", e);
            error = activity.getString(R.string.err_unknown);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Appointment[] appointments) {
        if (appointments != null) {
            activity.mAppointments = appointments;
            activity.mSwipeRefreshLayout.setRefreshing(false);

            //refreshing adapter
            activity.mAppointmentAdapter = new AppointmentsAdapter(activity, activity.mAppointments);
            activity.listView.setAdapter(activity.mAppointmentAdapter);
        } else {
            appointments = new Appointment[1];
            activity.mAppointments = appointments;
            activity.mSwipeRefreshLayout.setRefreshing(false);
            Log.e(TAG, error);
            Snackbar.make(activity.getCurrentFocus(), error, Snackbar.LENGTH_LONG).show();
        }
    }
}
