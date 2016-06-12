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

package com.z3r0byte.magis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.DateUtils;

import net.ilexiconn.magister.container.Appointment;

/**
 * Created by bas on 7-6-16.
 */
public class AppointmentsAdapter extends ArrayAdapter<Appointment> {
    private final Context context;
    private final Appointment[] appointments;

    public AppointmentsAdapter(Context context, Appointment[] appointments) {
        super(context, -1, appointments);
        this.context = context;
        this.appointments = appointments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_lesson, parent, false);

        TextView period = (TextView) rowView.findViewById(R.id.text_list_period);
        TextView lesson = (TextView) rowView.findViewById(R.id.text_lesson);
        TextView classroom = (TextView) rowView.findViewById(R.id.text_classroom);
        TextView time = (TextView) rowView.findViewById(R.id.text_time);
        ImageView homework = (ImageView) rowView.findViewById(R.id.image_homework);


        period.setText(appointments[position].periodFrom + "");
        lesson.setText(appointments[position].description);
        classroom.setText(appointments[position].location);
        time.setText(DateUtils.formatDate(appointments[0].startDate, "HH:mm") + " - "
                + DateUtils.formatDate(appointments[0].endDate, "HH:mm"));

        homework.setImageDrawable(null);



        return rowView;
    }
}
