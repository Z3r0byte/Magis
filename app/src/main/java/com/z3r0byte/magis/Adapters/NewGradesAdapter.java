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
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.DB_Handlers.NewGradesDB;
import com.z3r0byte.magis.Utils.DateUtils;

import net.ilexiconn.magister.container.Grade;

/**
 * Created by bas on 6-7-16.
 */
public class NewGradesAdapter extends ArrayAdapter<Grade> {
    private static final String TAG = "NewGradesAdapter";

    private final Context context;
    private final Grade[] grades;

    public NewGradesAdapter(Context context, Grade[] grades) {
        super(context, -1, grades);
        this.context = context;
        this.grades = grades;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_new_grades, parent, false);

        NewGradesDB newGradesDB = new NewGradesDB(context);

        IconicsDrawable emptyStar = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_star).color(Color.LTGRAY).sizeDp(24);
        IconicsDrawable fullStar = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_star)
                .color(context.getResources().getColor(R.color.accent)).sizeDp(24);


        TextView subject = (TextView) rowView.findViewById(R.id.list_text_subject);
        subject.setText(grades[position].subject.name);
        TextView date = (TextView) rowView.findViewById(R.id.list_text_date);
        date.setText(DateUtils.formatDate(grades[position].filledInDate, "dd-MM-yyyy"));

        TextView grade = (TextView) rowView.findViewById(R.id.list_text_grade);
        grade.setText(grades[position].grade);
        try {
            if (Double.parseDouble(grades[position].grade.replace(",", ".")) < 5.5) {
                grade.setTextColor(Color.RED);
            }
        } catch (Exception e) {
        }

        ImageView newGrade = (ImageView) rowView.findViewById(R.id.list_imageview_grade_new);
        if (newGradesDB.hasBeenSeen(grades[position])) {
            newGrade.setImageDrawable(emptyStar);
        } else {
            newGrade.setImageDrawable(fullStar);
        }



        return rowView;
    }
}
