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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.z3r0byte.magis.R;

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


        Log.d(TAG, "getView: Position: " + position);
        TextView textView = (TextView) rowView.findViewById(R.id.list_text_subject);
        textView.setText(grades[position].subject.name);


        return rowView;
    }
}
