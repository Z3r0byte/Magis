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

package com.z3r0byte.magis.Utils;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.z3r0byte.magis.Adapters.AppointmentsAdapter;
import com.z3r0byte.magis.Adapters.GradesAdapter;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Tasks.AppointmentsTask;
import com.z3r0byte.magis.Tasks.LoginTask;

import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;

import java.util.Date;

import tr.xip.errorview.ErrorView;

/**
 * Created by bas on 7-6-16.
 */
public class MagisActivity extends AppCompatActivity {
    public ParcelableMagister mMagister;
    public School mSchool;
    public User mUser;
    public Appointment[] mAppointments;
    public Grade[] grades;
    public GradesAdapter mGradesAdapter;

    public SwipeRefreshLayout mSwipeRefreshLayout;
    public CoordinatorLayout coordinatorLayout;
    public AppointmentsAdapter mAppointmentAdapter;
    public ListView listView;
    public ErrorView errorView;

    public Date date;


    Integer type = 0;

    public void getMagister(final MagisActivity activity, final School mSchool, final User mUser) {
        if (LoginUtils.reLogin(this)) {
            new LoginTask(activity, mSchool, mUser).execute();
        } else if (LoginUtils.loginError(this)) {
            Snackbar.make(coordinatorLayout, R.string.snackbar_login_error, Snackbar.LENGTH_INDEFINITE).setAction(R.string.msg_refresh_session_short, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new LoginTask(activity, mSchool, mUser).execute();
                    finish();
                }
            }).show();
        }

    }

    public void retrieveData(final MagisActivity activity) {
        if (mMagister != null) {
            switch (type) {
                case 0:
                    if (mMagister != null || mMagister.isExpired()) {
                        Date from = DateUtils.addDays(DateUtils.getToday(), -7);
                        Date until = DateUtils.addDays(DateUtils.getToday(), 14);
                        new AppointmentsTask(activity, mMagister, from, until, 3).execute();
                    } else {
                        Snackbar.make(coordinatorLayout, R.string.err_invalid_session, Snackbar.LENGTH_SHORT)
                                .setAction(R.string.msg_refresh_session_short, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getMagister(activity, mSchool, mUser);
                                    }
                                }).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
            }
        } else {
            Snackbar.make(coordinatorLayout, R.string.err_invalid_session, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.msg_refresh_session_short, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getMagister(activity, mSchool, mUser);
                        }
                    }).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }
}
