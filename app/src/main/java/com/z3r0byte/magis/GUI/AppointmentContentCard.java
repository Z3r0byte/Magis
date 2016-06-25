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

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.z3r0byte.magis.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by z3r0byte on 25-6-16.
 */
public class AppointmentContentCard extends Card {
    private static final String TAG = "AppointmentContentCard";

    TextView ContentTextView;
    TextView ContentButton;
    Context Context;
    Boolean ready = false;

    public AppointmentContentCard(Context context) {
        this(context, R.layout.card_appointments_content_layout);
    }

    public AppointmentContentCard(Context context, int innerLayout) {
        super(context, innerLayout);
        Context = context;
        init();
    }

    private void init() {
    }

    public void setContent(final String description) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
                ContentTextView.setText(description);
            }
        }).start();
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        ContentTextView = (TextView) view.findViewById(R.id.card_content_textview);
        ContentButton = (TextView) view.findViewById(R.id.card_content_button);
        ContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Er is op de knop gedrukt.");
                Toast.makeText(Context, "Dit werkt nog niet.", Toast.LENGTH_SHORT).show();
            }
        });
        ready = true;
        Log.d(TAG, "setupInnerViewElements: Done setting up");
    }
}
