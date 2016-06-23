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

package com.z3r0byte.magis.DetailActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.z3r0byte.magis.GUI.AppointmentDetailCard;
import com.z3r0byte.magis.R;

import net.ilexiconn.magister.container.Appointment;

import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardViewNative;

public class AppointmentDetails extends AppCompatActivity {
    private static final String TAG = "AppointmentDetails";

    Appointment appointment;
    CardViewNative cardMain;
    CardViewNative cardHomeWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            appointment = new Gson().fromJson(extras.getString("Appointment"), Appointment.class);
        } else {
            Log.e(TAG, "onCreate: Impossible to show details of a null Appointment!", new IllegalArgumentException());
            Toast.makeText(AppointmentDetails.this, R.string.err_unknown, Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbar.setTitle(appointment.description);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppointmentDetailCard mainCardContent = new AppointmentDetailCard(this);
        CardHeader cardHeader = new CardHeader(this);
        cardHeader.setTitle(getString(R.string.msg_details));
        mainCardContent.addCardHeader(cardHeader);
        cardMain = (CardViewNative) findViewById(R.id.card_main_details);
        cardMain.setCard(mainCardContent);
    }
}
