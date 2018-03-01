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
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.z3r0byte.magis.Listeners.SharedListener;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.DB_Handlers.CalendarDB;
import com.z3r0byte.magis.Utils.DateUtils;
import com.z3r0byte.magis.Utils.GlobalMagister;

import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.type.AppointmentType;
import net.ilexiconn.magister.container.type.InfoType;
import net.ilexiconn.magister.handler.AppointmentHandler;
import net.ilexiconn.magister.util.DateUtil;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class AppointmentDetails extends AppCompatActivity {
    private static final String TAG = "AppointmentDetails";

    Appointment appointment;
    String Type;
    ParcelableMagister mMagister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            appointment = new Gson().fromJson(extras.getString("Appointment"), Appointment.class);
            mMagister = GlobalMagister.MAGISTER;
        } else {
            Log.e(TAG, "onCreate: Impossible to show details of a null Appointment!", new IllegalArgumentException());
            Toast.makeText(AppointmentDetails.this, R.string.err_unknown, Toast.LENGTH_SHORT).show();
            finish();
        }

        if (appointment != null) {
            Log.d(TAG, "onCreate: appointment: " + appointment.toString());
        } else {
            Log.e(TAG, "onCreate: Appointment is null!", new IllegalArgumentException());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbar.setTitle(appointment.description);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDetailscard(appointment);
        if (appointment.content != null && appointment.content != "") {
            setupContentCard(appointment);
        } else {
            CardView cardView = (CardView) findViewById(R.id.card_view_2);
            cardView.setVisibility(View.GONE);
        }
    }

    private void setupDetailscard(Appointment appointment) {
        RelativeLayout descriptionLayout;
        TextView descriptionTextInput;
        ImageView descriptionImageView;
        RelativeLayout periodLayout;
        TextView periodTextInput;
        ImageView periodImageView;
        RelativeLayout teacherLayout;
        TextView teacherTextInput;
        ImageView teacherImageView;
        RelativeLayout durationLayout;
        TextView durationTextInput;
        ImageView durationImageView;
        RelativeLayout locationLayout;
        TextView locationTextInput;
        ImageView locationImageView;

        descriptionLayout = (RelativeLayout) findViewById(R.id.card_details_layout_description);
        descriptionTextInput = (TextView) findViewById(R.id.card_details_textview_description_input);
        descriptionImageView = (ImageView) findViewById(R.id.card_details_imageview_description);

        periodLayout = (RelativeLayout) findViewById(R.id.card_details_layout_period);
        periodTextInput = (TextView) findViewById(R.id.card_details_textview_period_input);
        periodImageView = (ImageView) findViewById(R.id.card_details_imageview_period);

        teacherLayout = (RelativeLayout) findViewById(R.id.card_details_layout_teacher);
        teacherTextInput = (TextView) findViewById(R.id.card_details_textview_teacher_input);
        teacherImageView = (ImageView) findViewById(R.id.card_details_imageview_teacher);

        durationLayout = (RelativeLayout) findViewById(R.id.card_details_layout_duration);
        durationTextInput = (TextView) findViewById(R.id.card_details_textview_duration_input);
        durationImageView = (ImageView) findViewById(R.id.card_details_imageview_duration);

        locationLayout = (RelativeLayout) findViewById(R.id.card_details_layout_location);
        locationTextInput = (TextView) findViewById(R.id.card_details_textview_location_input);
        locationImageView = (ImageView) findViewById(R.id.card_details_imageview_location);


        Log.d(TAG, "run: Setting desc...");
        IconicsDrawable drawable = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_description);
        descriptionLayout.setVisibility(View.VISIBLE);
        descriptionImageView.setImageDrawable(drawable);
        descriptionTextInput.setText(appointment.description);

        Log.d(TAG, "run: Setting period...");
        drawable = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_info);
        periodLayout.setVisibility(View.VISIBLE);
        periodImageView.setImageDrawable(drawable);
        periodTextInput.setText(appointment.periodFrom + "");


        Log.d(TAG, "run: Setting teacher...");
        if (appointment.type != AppointmentType.PERSONAL) {
            try {
                String teacher = appointment.teachers[0].name;
                drawable = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_person);
                teacherLayout.setVisibility(View.VISIBLE);
                teacherImageView.setImageDrawable(drawable);
                teacherTextInput.setText(teacher);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            teacherLayout.setVisibility(View.GONE);
        }


        Log.d(TAG, "run: Setting time...");
        String duration = DateUtils.formatDate(appointment.startDate, "HH:mm") + " - "
                + DateUtils.formatDate(appointment.endDate, "HH:mm");
        drawable = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_today);
        durationLayout.setVisibility(View.VISIBLE);
        durationImageView.setImageDrawable(drawable);
        durationTextInput.setText(duration);

        Log.d(TAG, "run: Setting location...");
        drawable = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_location_on);
        locationLayout.setVisibility(View.VISIBLE);
        locationImageView.setImageDrawable(drawable);
        locationTextInput.setText(appointment.location);

    }

    private void setupContentCard(final Appointment appointment) {
        TextView ContentTextView;
        TextView ContentButton;
        TextView ContentTitle;

        ContentTextView = (TextView) findViewById(R.id.card_content_textview);
        ContentButton = (TextView) findViewById(R.id.card_content_button);
        ContentTitle = (TextView) findViewById(R.id.card_title_content);
        final TextView lastEdited = (TextView) findViewById(R.id.card_content_time_edited_textview);

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


        if (appointment.finished) {
            ContentTitle.setText(Type + " (" + getString(R.string.msg_finished) + ")");
        } else {
            ContentTitle.setText(Type);
        }

        ContentTextView.setText(Html.fromHtml(appointment.content));

        ContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        AppointmentHandler appointmentHandler = new AppointmentHandler(mMagister);
                        try {
                            appointment.finished = !appointment.finished;
                            Boolean finished = appointmentHandler.finishAppointment(appointment);
                            Log.d(TAG, "run: Gelukt: " + finished);
                            if (finished) {
                                SharedListener.finishInitiator.finished();
                                updateAppointment(appointment);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), R.string.err_no_connection, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), R.string.err_unknown, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }
        });

        if (appointment.type == AppointmentType.PERSONAL) {
            lastEdited.setVisibility(View.GONE);
            return;
        }
        lastEdited.setText(String.format(getString(R.string.msg_last_edited), getString(R.string.msg_loading)));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Appointment appointmentDetails = new Gson().fromJson(new AppointmentHandler(mMagister).getRawAppointment(appointment.id), Appointment.class);
                    final Date lastEditedDate = DateUtil.stringToDate(appointmentDetails.homeworkLastEdited);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lastEdited.setText(String.format(getString(R.string.msg_last_edited), DateUtils.formatDate(lastEditedDate, "dd MMM HH:mm")));
                        }
                    });

                } catch (IOException | ParseException | NullPointerException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lastEdited.setText(getString(R.string.err_no_connection));
                        }
                    });
                    e.printStackTrace();
                }

            }
        }).start();


    }


    private void updateAppointment(final Appointment appointment) {
        this.appointment = appointment;


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView ContentTitle = (TextView) findViewById(R.id.card_title_content);
                if (appointment.finished) {
                    ContentTitle.setText(Type + " (" + getString(R.string.msg_finished) + ")");
                } else {
                    ContentTitle.setText(Type);
                }
            }
        });

        CalendarDB dbHelper = new CalendarDB(this);
        dbHelper.finishAppointment(appointment);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment_details, menu);
        MenuItem item = menu.findItem(R.id.action_delete);
        if (appointment.type.getID() != 1) {
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    AppointmentHandler appointmentHandler = new AppointmentHandler(mMagister);
                    try {
                        Log.d(TAG, "run: Deleting Appointment...");
                        appointmentHandler.removeAppointment(appointment);

                        CalendarDB db = new CalendarDB(getApplicationContext());
                        db.deleteAppointment(appointment);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AppointmentDetails.this, R.string.msg_deleted, Toast.LENGTH_SHORT).show();
                            }
                        });
                        SharedListener.finishInitiator.finished();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AppointmentDetails.this, R.string.err_no_connection, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
