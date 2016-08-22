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

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.ConfigUtil;
import com.z3r0byte.magis.Utils.DB_Handlers.CalendarDB;

import net.ilexiconn.magister.container.Appointment;

import java.util.Timer;
import java.util.TimerTask;

public class AppointmentService extends Service {

    ConfigUtil configUtil;
    CalendarDB calendarDB;
    Boolean showNotification;
    Appointment[] appointments;
    Timer timer;
    String previousAppointment = "";

    private static final String TAG = "AgendaNotification";

    public AppointmentService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        calendarDB = new CalendarDB(getApplicationContext());
        configUtil = new ConfigUtil(getApplicationContext());
        showNotification = configUtil.getBoolean("notification");
        setup();
        return START_STICKY;
    }

    private void setup() {
        if (showNotification) {
            TimerTask notificationTask = new TimerTask() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    appointments = calendarDB.getNotificationAppointments();
                    if (appointments.length >= 1) {
                        Appointment appointment = appointments[0];
                        if (gson.toJson(appointment) != previousAppointment) {
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                            mBuilder.setSmallIcon(R.drawable.magis512);

                            /*Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                            stackBuilder.addParentStack(MainActivity.class);
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(resultPendingIntent);*/

                            mBuilder.setContentTitle("Volgende les (" + appointment.startDateString + ")");
                            mBuilder.setContentText(appointment.description + " in " + appointment.location);
                            mBuilder.setAutoCancel(true);
                            mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(9992, mBuilder.build());
                        }
                    }
                }
            };
            timer.schedule(notificationTask, 20000, 60 * 1000);
        }
    }

    @Override
    public void onDestroy() {
        //timer.cancel();
        getApplicationContext().getSharedPreferences("data", MODE_PRIVATE).edit().putBoolean("NotificationServiceRunning", false).apply();
        Log.e("Why r u destroying me?", "You will fail!!!");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
