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
import android.widget.TextView;

import com.z3r0byte.magis.R;

import net.ilexiconn.magister.container.PresencePeriod;

/**
 * Created by bas on 18-9-16.
 */

public class PresencePeriodAdapter extends ArrayAdapter<PresencePeriod> {
    private final Context context;
    private final PresencePeriod[] presencePeriods;

    public PresencePeriodAdapter(Context context, PresencePeriod[] presencePeriods) {
        super(context, -1, presencePeriods);
        this.context = context;
        this.presencePeriods = presencePeriods;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_studies, parent,
                false);

        TextView textView = (TextView) row.findViewById(R.id.list_text);
        textView.setTextColor(context.getResources().getColor(R.color.md_white_1000));
        textView.setText(presencePeriods[position].description);
        return row;
    }


    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_studies, parent,
                false);

        TextView textView = (TextView) row.findViewById(R.id.list_text);
        textView.setText(presencePeriods[position].description);
        return row;
    }
}
