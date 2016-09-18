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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.z3r0byte.magis.R;

import net.ilexiconn.magister.container.Presence;

/**
 * Created by bas on 18-9-16.
 */

public class PresenceAdapter extends ArrayAdapter<Presence>

{
    private final Context context;
    private final Presence[] presences;
    String previousDay = "";

    public PresenceAdapter(Context context, Presence[] presences) {
        super(context, -1, presences);
        this.context = context;
        this.presences = presences;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_homework, parent, false);

        TextView period = (TextView) rowView.findViewById(R.id.text_list_period);
        TextView lesson = (TextView) rowView.findViewById(R.id.text_lesson);
        TextView homework = (TextView) rowView.findViewById(R.id.text_homework);
        TextView headerText = (TextView) rowView.findViewById(R.id.header_text);
        RelativeLayout header = (RelativeLayout) rowView.findViewById(R.id.header);

        String day = presences[position].start;
        if (position != 0) {
            previousDay = presences[position].start;
            if (!previousDay.equals(day)) {
                headerText.setText(presences[position].start);
            } else {
                header.setVisibility(android.view.View.GONE);
            }
        } else {
            headerText.setText(presences[position].start);
        }


        period.setText(presences[position].period + "");
        if (presences[position].period == 0) {
            period.setText("");
            rowView.findViewById(R.id.layout_list_calendar_period).setBackgroundResource(0);
        }
        lesson.setText(presences[position].description);
        CharSequence content = Html.fromHtml(presences[position].description);
        homework.setText(content);


        return rowView;
    }
}
