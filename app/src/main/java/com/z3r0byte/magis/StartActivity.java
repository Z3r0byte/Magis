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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.z3r0byte.magis.Services.NewGradeService;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    Boolean relogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomActivityOnCrash.install(this);
        setContentView(R.layout.activity_start);

        if (getSharedPreferences("data", MODE_PRIVATE).getInt("DataVersion", 1) != 2 && getSharedPreferences("data", MODE_PRIVATE).getBoolean("LoggedIn", false)) {
            relogin = true;
            Toast.makeText(StartActivity.this, getString(R.string.msg_old_version), Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, SetupActivity.class));
            finish();
        }

        if (!getSharedPreferences("data", MODE_PRIVATE).getBoolean("LoggedIn", false) || relogin) {
            startActivity(new Intent(this, SetupActivity.class));
            finish();
        } else if (!relogin) {
            startService(new Intent(this, NewGradeService.class));
            startActivity(new Intent(this, CalendarActivity.class));
            finish();
        }
    }
}
