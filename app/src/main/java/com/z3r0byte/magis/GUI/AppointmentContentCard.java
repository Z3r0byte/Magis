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

package com.z3r0byte.magis.GUI;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.z3r0byte.magis.DetailActivity.AppointmentDetails;
import com.z3r0byte.magis.DetailActivity.HomeworkDetails;
import com.z3r0byte.magis.Listeners.SharedListener;
import com.z3r0byte.magis.R;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.handler.AppointmentHandler;

import org.json.JSONException;

import java.io.IOException;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by z3r0byte on 25-6-16.
 */
public class AppointmentContentCard extends Card {
    private static final String TAG = "AppointmentContentCard";

    TextView ContentTextView;
    TextView ContentButton;
    Context Context;
    ParcelableMagister magister;
    Appointment appointment;
    AppointmentDetails details;
    HomeworkDetails homeworkDetails;
    Boolean ready = false;

    public AppointmentContentCard(Context context, ParcelableMagister magister, Appointment appointment, HomeworkDetails homeworkdetails) {
        this(context, homeworkdetails, magister, appointment, R.layout.card_appointments_content_layout);
    }

    public AppointmentContentCard(Context context, HomeworkDetails homeworkdetails, ParcelableMagister magister, Appointment appointment, int innerLayout) {
        super(context, innerLayout);
        Context = context;
        this.magister = magister;
        this.appointment = appointment;
        this.homeworkDetails = homeworkdetails;
        init();
    }

    public AppointmentContentCard(Context context, ParcelableMagister magister, Appointment appointment, AppointmentDetails details) {
        this(context, details, magister, appointment, R.layout.card_appointments_content_layout);
    }

    public AppointmentContentCard(Context context, AppointmentDetails details, ParcelableMagister magister, Appointment appointment, int innerLayout) {
        super(context, innerLayout);
        Context = context;
        this.magister = magister;
        this.appointment = appointment;
        this.details = details;
        init();
    }

    private void init() {
    }

    public void waitForReady() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
                return;
            }
        }).start();
    }

    public void setContent(final Activity context, final String description, final Boolean afgerond) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "setContent: setting content...");
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAfgerond(afgerond, description);
                    }
                });
            }
        }).start();
    }

    public void setAfgerond(final Boolean afgerond, final String description) {
        if (afgerond) {
            ContentTextView.setText(description);
            ContentButton.setText(R.string.msg_finish);
        } else {
            ContentTextView.setText(description);
            ContentButton.setText(R.string.msg_finish);
        }
    }

    public void setClickListener(final Magister magister, final Appointment appointment, final AppointmentDetails activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (magister != null) {
                    ContentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    AppointmentHandler appointmentHandler = new AppointmentHandler(magister);
                                    try {
                                        appointment.finished = !appointment.finished;
                                        Boolean finished = appointmentHandler.finishAppointment(appointment);
                                        Log.d(TAG, "run: Gelukt: " + finished);
                                        if (finished) {
                                            activity.updateAppointment(appointment);
                                            SharedListener.finishInitiator.finished();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Context, R.string.err_no_connection, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Context, R.string.err_unknown, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).start();

                        }
                    });
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, R.string.err_not_logged_in, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

    public void setClickListener(final Magister magister, final Appointment appointment, final HomeworkDetails activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
                if (magister != null) {
                    ContentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    AppointmentHandler appointmentHandler = new AppointmentHandler(magister);
                                    try {
                                        appointment.finished = !appointment.finished;
                                        Boolean finished = appointmentHandler.finishAppointment(appointment);
                                        Log.d(TAG, "run: Gelukt: " + finished);
                                        if (finished) {
                                            SharedListener.finishInitiator.finished();
                                            activity.updateAppointment(appointment);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Context, R.string.err_no_connection, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Context, R.string.err_unknown, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).start();

                        }
                    });
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, R.string.err_not_logged_in, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        ContentTextView = (TextView) view.findViewById(R.id.card_content_textview);
        ContentButton = (TextView) view.findViewById(R.id.card_content_button);


        if (details != null) {
            setClickListener(magister, appointment, details);
        } else if (homeworkDetails != null) {
            setClickListener(magister, appointment, homeworkDetails);
        } else {
            Log.e(TAG, "setupInnerViewElements: No activity supplied!", new IllegalArgumentException());
        }
        ready = true;
        Log.d(TAG, "setupInnerViewElements: Done setting up");
    }
}
