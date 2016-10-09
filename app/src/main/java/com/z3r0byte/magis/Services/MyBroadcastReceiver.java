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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.z3r0byte.magis.Utils.ConfigUtil;
import com.z3r0byte.magis.Utils.ServiceUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by bas on 4-10-16.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    Boolean relogin = false;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (context.getSharedPreferences("data", MODE_PRIVATE).getInt("DataVersion", 1) != 3 && context.getSharedPreferences("data", MODE_PRIVATE).getBoolean("LoggedIn", false)) {
            relogin = true;
        }

        if (!context.getSharedPreferences("data", MODE_PRIVATE).getBoolean("LoggedIn", false) || relogin) {
            //do nothing
        } else if (!relogin) {
            ConfigUtil configUtil = new ConfigUtil(context.getApplicationContext());
            if (!ServiceUtil.isMyServiceRunning(AppointmentService.class, context.getApplicationContext()) && configUtil.getBoolean("notification")) {
                context.startService(new Intent(context.getApplicationContext(), AppointmentService.class));
            }
            if (!ServiceUtil.isMyServiceRunning(AutoSilentService.class, context.getApplicationContext()) && configUtil.getBoolean("auto-silent")) {
                context.startService(new Intent(context.getApplicationContext(), AutoSilentService.class));
            }
        }
    }
}