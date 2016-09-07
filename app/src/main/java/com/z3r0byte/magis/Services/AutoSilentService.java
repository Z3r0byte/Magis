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
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.z3r0byte.magis.Utils.ConfigUtil;
import com.z3r0byte.magis.Utils.DB_Handlers.CalendarDB;

import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.type.AppointmentType;

import java.util.Timer;
import java.util.TimerTask;

public class AutoSilentService extends Service {
    private static final String TAG = "AutoSilentService";

    ConfigUtil configUtil;
    CalendarDB calendarDB;
    Boolean silentByApp;
    Boolean autoSilent;
    Appointment[] appointments;
    Timer timer = new Timer();

    IconicsDrawable small;

    public AutoSilentService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        calendarDB = new CalendarDB(getApplicationContext());
        configUtil = new ConfigUtil(getApplicationContext());
        autoSilent = configUtil.getBoolean("auto-silent");
        small = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_event_note);
        setup();
        return START_STICKY;
    }

    private void setup() {
        if (autoSilent) {
            TimerTask notificationTask = new TimerTask() {
                @Override
                public void run() {
                    appointments = calendarDB.getNotificationAppointments();
                    Log.d(TAG, "run: amount " + appointments.length);
                    if (appointments.length >= 1) {
                        if (doSilent(appointments)) {
                            silenced(true);
                            AudioManager audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        }
                    } else {
                        if (isSilencedByApp()) {
                            AudioManager audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            silenced(false);
                        }
                    }
                }
            };
            timer.schedule(notificationTask, 20000, 1000);
        }
    }

    private Boolean doSilent(Appointment[] appointments) {
        for (Appointment appointment :
                appointments) {
            try {
                if (appointment.type.getID() == AppointmentType.PERSONAL.getID() || true) {
                    Log.d(TAG, "doSilent: valid appointment");
                    return true;
                } else {
                    Log.d(TAG, "doSilent: No valid appointment");
                }
            } catch (NullPointerException e) {
                Log.d(TAG, "doSilent: No valid appointment");
            }
        }
        return false;
    }

    private void silenced(Boolean silenced) {
        silentByApp = true;
        SharedPreferences prefs = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("silent", silenced);
        editor.apply();
    }

    private Boolean isSilencedByApp() {
        SharedPreferences prefs = getSharedPreferences("data", MODE_PRIVATE);
        return prefs.getBoolean("silent", false);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
