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

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.DateUtils;

import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.type.InfoType;

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
        if (appointments[position].periodFrom == 0) {
            period.setText("");
            rowView.findViewById(R.id.layout_list_calendar_period).setBackgroundResource(0);
        }
        lesson.setText(appointments[position].description);
        classroom.setText(appointments[position].location);
        time.setText(DateUtils.formatDate(appointments[position].startDate, "HH:mm") + " - "
                + DateUtils.formatDate(appointments[position].endDate, "HH:mm"));


        InfoType infoType = appointments[position].infoType;
        int type = infoType.getID();
        switch (type) {
            case 1:
                IconicsDrawable drawable1 = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_assignment);
                homework.setImageDrawable(drawable1);
                break;
            case 2:
                IconicsDrawable drawable2 = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_class);
                homework.setImageDrawable(drawable2);
                break;
            case 3:
                IconicsDrawable drawable3 = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_class);
                homework.setImageDrawable(drawable3);
                break;
            case 4:
                IconicsDrawable drawable4 = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_class);
                homework.setImageDrawable(drawable4);
                break;
            case 5:
                IconicsDrawable drawable5 = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_question_answer);
                homework.setImageDrawable(drawable5);
                break;
            case 6:
                IconicsDrawable drawable6 = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_info);
                homework.setImageDrawable(drawable6);
                break;
            case 7:
                IconicsDrawable drawable7 = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_announcement);
                homework.setImageDrawable(drawable7);
                break;
            default:
                homework.setImageDrawable(null);
                break;
        }

        if (appointments[position].finished && appointments[position].infoType.getID() == 1) {
            IconicsDrawable drawable = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_check);
            homework.setImageDrawable(drawable);
        }


        return rowView;
    }
}
