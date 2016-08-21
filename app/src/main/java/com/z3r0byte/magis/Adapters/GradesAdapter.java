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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.DateUtils;

import net.ilexiconn.magister.container.Grade;

/**
 * Created by bas on 7-7-16.
 */
public class GradesAdapter extends ArrayAdapter<Grade> {
    private static final String TAG = "NewGradesAdapter";

    private final Context context;
    private final Grade[] grades;
    private final Boolean onlyAverage;

    public GradesAdapter(Context context, Grade[] grades, Boolean onlyAverage) {
        super(context, -1, grades);
        this.context = context;
        this.grades = grades;
        this.onlyAverage = onlyAverage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_grades, parent, false);

        IconicsDrawable emptyStar = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_star).color(Color.LTGRAY).sizeDp(24);

        TextView subject = (TextView) rowView.findViewById(R.id.list_text_subject);
        if (onlyAverage) {
            subject.setText(grades[position].subject.name);
        } else {
            try {
                subject.setText(grades[position].singleGrade.description);
            } catch (NullPointerException e) {
                subject.setText(R.string.err_unknown);
            }

        }
        try {
            TextView date = (TextView) rowView.findViewById(R.id.list_text_date);
            date.setText(DateUtils.formatDate(grades[position].filledInDate, "dd-MM-yyyy"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView grade = (TextView) rowView.findViewById(R.id.list_text_grade);
        grade.setText(grades[position].grade);
        try {
            if (Double.parseDouble(grades[position].grade.replace(",", ".")) < 5.5) {
                grade.setTextColor(Color.RED);
            }
        } catch (Exception e) {
        }

        TextView multiplier = (TextView) rowView.findViewById(R.id.list_multiplier_grade);
        if (onlyAverage) {
            multiplier.setVisibility(View.GONE);
        } else {
            try {
                multiplier.setText("× " + grades[position].singleGrade.wage);
            } catch (NullPointerException e) {
                multiplier.setText("× 0.0");
            }
        }


        return rowView;
    }

}
