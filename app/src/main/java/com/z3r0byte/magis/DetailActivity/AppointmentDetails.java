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
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.z3r0byte.magis.GUI.AppointmentContentCard;
import com.z3r0byte.magis.GUI.AppointmentDetailCard;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.DB_Handlers.CalendarDB;
import com.z3r0byte.magis.Utils.DateUtils;

import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.type.InfoType;

import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardViewNative;

public class AppointmentDetails extends AppCompatActivity {
    private static final String TAG = "AppointmentDetails";

    Appointment appointment;
    String Type;
    ParcelableMagister mMagister;
    CardViewNative cardMain;
    CardViewNative cardHomeWork;
    AppointmentContentCard contentCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            appointment = new Gson().fromJson(extras.getString("Appointment"), Appointment.class);
            mMagister = extras.getParcelable("Magister");
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
        mainCardContent.setDescription(this, appointment.description);
        mainCardContent.setLocation(this, appointment.location);
        mainCardContent.setPeriod(this, appointment.periodFrom + "");
        try {
            mainCardContent.setTeacher(this, appointment.teachers[0].name);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        mainCardContent.setTime(this, DateUtils.formatDate(DateUtils.addHours(appointment.startDate, 2), "HH:mm") + " - "
                + DateUtils.formatDate(DateUtils.addHours(appointment.endDate, 2), "HH:mm"));
        mainCardContent.addCardHeader(cardHeader);
        cardMain = (CardViewNative) findViewById(R.id.card_main_details);
        cardMain.setCard(mainCardContent);


        cardHomeWork = (CardViewNative) findViewById(R.id.card_content);
        if (appointment.infoType.getID() != 0 && !appointment.content.isEmpty()) {

            InfoType infoType = appointment.infoType;
            int type = infoType.getID();
            switch (type) {
                case 1:
                    Type = getString(R.string.msg_homework);
                    break;
                case 2:
                    Type = getString(R.string.msg_test);
                    break;
                case 3:
                    Type = getString(R.string.msg_exam);
                    break;
                case 4:
                    Type = getString(R.string.msg_quiz);
                    break;
                case 5:
                    Type = getString(R.string.msg_oral);
                    break;
                case 6:
                    Type = getString(R.string.msg_info);
                    break;
                case 7:
                    Type = getString(R.string.msg_annotation);
                    break;
                default:
                    Type = getString(R.string.msg_homework);
                    break;
            }

            contentCard = new AppointmentContentCard(this);
            CardHeader cardHeader2 = new CardHeader(this);
            if (appointment.finished) {
                cardHeader2.setTitle(Type + " (" + getString(R.string.msg_finished) + ")");
            } else {
                cardHeader2.setTitle(Type);
            }
            contentCard.addCardHeader(cardHeader2);
            contentCard.setContent(Html.fromHtml(appointment.content).toString(), appointment.finished);
            contentCard.setClickListener(mMagister, appointment, this);
            cardHomeWork.setCard(contentCard);
        } else {
            cardHomeWork.setVisibility(View.GONE);
        }
    }

    public void updateAppointment(final Appointment appointment) {
        this.appointment = appointment;

        final AppointmentDetails activity = this;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (appointment.finished) {
                    contentCard.getCardHeader().setTitle(Type + " (" + getString(R.string.msg_finished) + ")");
                } else {
                    contentCard.getCardHeader().setTitle(Type);
                }
                contentCard.notifyDataSetChanged();
                cardHomeWork.refreshCard(contentCard);
            }
        });

        CalendarDB dbHelper = new CalendarDB(this);
        dbHelper.finishAppointment(appointment);
    }
}
