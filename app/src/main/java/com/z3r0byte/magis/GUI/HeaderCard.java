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

import com.z3r0byte.magis.R;

import it.gmariotti.cardslib.library.internal.Card;


public class HeaderCard extends Card {
    private static final String TAG = "HeaderCard";
    TextView contentTextView;

    Boolean ready = false;

    public HeaderCard(Context context) {
        this(context, R.layout.card_simple_inner_layout);
    }

    public HeaderCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    private void init() {
    }

    public void setDescription(final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
                contentTextView.setText(content);
            }
        }).start();
    }

    public void setPeriod(final String period) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
            }
        }).start();
    }

    public void setTeacher(final String Teacher) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
            }
        }).start();
    }

    public void setTime(final String time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
            }
        }).start();
    }

    public void setLocation(final String location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
            }
        }).start();
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        contentTextView = (TextView) parent.findViewById(R.id.card_simple_title);
        ready = true;
        Log.d(TAG, "setupInnerViewElements: Done setting up");
    }
}
