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

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.z3r0byte.magis.Utils.DateUtils;

import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.PersonalAppointment;
import net.ilexiconn.magister.container.type.AppointmentType;
import net.ilexiconn.magister.handler.AppointmentHandler;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddAppointmentActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "AddAppointmentActivity";

    ParcelableMagister mMagister;

    Toolbar mToolbar;
    EditText mEditTitle;
    EditText mEditLocation;
    CheckBox mCheckEntireDay;
    Button mButtonDate;
    Button mButtonStart;
    Button mButtonEnd;
    Button mButtonCreate;
    EditText mEditDescription;

    Date date;
    Date startTime;
    Date endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mMagister = extras.getParcelable("Magister");
        } else {
            Log.e(TAG, "onCreate: No valid Magister!", new InvalidParameterException());
            Toast.makeText(this, R.string.err_unknown, Toast.LENGTH_SHORT).show();
            finish();
        }

        mToolbar = (Toolbar) findViewById(R.id.Toolbar);
        mToolbar.setTitle(getString(R.string.title_new_appointment));
        setSupportActionBar(mToolbar);

        mEditTitle = (EditText) findViewById(R.id.edit_text_title);
        mEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    mToolbar.setTitle(charSequence.toString());
                } else {
                    mToolbar.setTitle(getString(R.string.title_new_appointment));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mEditLocation = (EditText) findViewById(R.id.edit_text_location);
        mEditDescription = (EditText) findViewById(R.id.edit_text_description);

        mButtonDate = (Button) findViewById(R.id.button_date);
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });

        mButtonStart = (Button) findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseTime(1);
            }
        });

        mButtonEnd = (Button) findViewById(R.id.button_end);
        mButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseTime(2);
            }
        });

        mButtonCreate = (Button) findViewById(R.id.button_create);
        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAppointment();
            }
        });

        mCheckEntireDay = (CheckBox) findViewById(R.id.checkbox_entire_day);
        mCheckEntireDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                entireDay(b);
            }
        });
    }

    private void chooseDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show(getFragmentManager(), "Picker");
    }

    private void chooseTime(int button) {
        if (date != null) {
            Calendar now = Calendar.getInstance();
            TimePickerDialog timePicker = TimePickerDialog.newInstance(
                    this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true
            );
            if (button == 1) {
                //custom Listener to avoid conflicts with second dialog
                timePicker.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        Calendar start = Calendar.getInstance();
                        start.setTime(date);
                        start.add(Calendar.HOUR_OF_DAY, hourOfDay);
                        start.add(Calendar.MINUTE, minute);
                        startTime = start.getTime();
                        mButtonStart.setText(DateUtils.formatDate(startTime, "HH:mm"));
                    }
                });
            }
            timePicker.show(getFragmentManager(), "TimePicker");
        } else {
            Toast.makeText(AddAppointmentActivity.this, R.string.err_set_date_first, Toast.LENGTH_SHORT).show();
        }
    }

    private void entireDay(Boolean entireday) {
        if (date != null) {
            if (entireday) {
                mButtonStart.setEnabled(false);
                mButtonEnd.setEnabled(false);

                startTime = date;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                endTime = calendar.getTime();

                mButtonStart.setText(DateUtils.formatDate(startTime, "HH:mm"));
                mButtonEnd.setText(DateUtils.formatDate(endTime, "HH:mm"));
            } else {
                mButtonStart.setEnabled(true);
                mButtonEnd.setEnabled(true);
            }
        } else {
            Toast.makeText(AddAppointmentActivity.this, R.string.err_set_date_first, Toast.LENGTH_SHORT).show();
            mCheckEntireDay.setChecked(!entireday);
        }
    }

    private void createAppointment() {
        Log.d(TAG, "createAppointment() called");
        if (startTime == null || endTime == null) {
            Toast.makeText(AddAppointmentActivity.this, R.string.err_fill_in_everything, Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            final PersonalAppointment appointment = new PersonalAppointment(mEditTitle.getText().toString(), mEditDescription.getText().toString(),
                    mEditLocation.getText().toString(), AppointmentType.getTypeById(1), startTime, endTime);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    try {
                        AppointmentHandler appointmentHandler = new AppointmentHandler(mMagister);
                        appointmentHandler.createAppointment(appointment);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), R.string.msg_appointment_created_successfully,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), R.string.err_no_connection, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(AddAppointmentActivity.this, R.string.err_unknown, Toast.LENGTH_SHORT).show();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
            Toast.makeText(AddAppointmentActivity.this, R.string.err_fill_in_everything, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int IDKWhatThisIsFor) {
        Calendar end = Calendar.getInstance();
        end.setTime(date);
        end.add(Calendar.HOUR_OF_DAY, hourOfDay);
        end.add(Calendar.MINUTE, minute);
        endTime = end.getTime();
        mButtonEnd.setText(DateUtils.formatDate(endTime, "HH:mm"));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.YEAR, year);
        date = calendar.getTime();
        mButtonDate.setText(DateUtils.formatDate(date, "EEEE dd MMM"));
    }


}
