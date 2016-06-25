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
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.z3r0byte.magis.R;

import it.gmariotti.cardslib.library.internal.Card;


public class AppointmentDetailCard extends Card {
    private static final String TAG = "AppointmentDetailCard";

    TextView descriptionTextView;
    TextView descriptionTextInput;
    ImageView descriptionImageView;
    TextView periodTextView;
    TextView periodTextInput;
    ImageView periodImageView;
    TextView teacherTextView;
    TextView teacherTextInput;
    ImageView teacherImageView;
    TextView durationTextView;
    TextView durationTextInput;
    ImageView durationImageView;
    TextView locationTextView;
    TextView locationTextInput;
    ImageView locationImageView;

    Boolean ready = false;

    public AppointmentDetailCard(Context context) {
        this(context, R.layout.card_appointments_details_layout);
    }

    public AppointmentDetailCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    private void init() {
    }

    public void setDescription(final Context context, final String description) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
                IconicsDrawable drawable = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_description);
                descriptionImageView.setVisibility(View.VISIBLE);
                descriptionTextView.setVisibility(View.VISIBLE);
                descriptionTextInput.setVisibility(View.VISIBLE);
                descriptionImageView.setImageDrawable(drawable);
                descriptionTextInput.setText(description);
            }
        }).start();
    }

    public void setPeriod(final Context context, final String period) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
                IconicsDrawable drawable = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_info);
                periodImageView.setVisibility(View.VISIBLE);
                periodTextView.setVisibility(View.VISIBLE);
                periodTextInput.setVisibility(View.VISIBLE);
                periodImageView.setImageDrawable(drawable);
                periodTextInput.setText(period);
            }
        }).start();
    }

    public void setTeacher(final Context context, final String teacher) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
                IconicsDrawable drawable = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_person);
                teacherImageView.setVisibility(View.VISIBLE);
                teacherTextView.setVisibility(View.VISIBLE);
                teacherTextInput.setVisibility(View.VISIBLE);
                teacherImageView.setImageDrawable(drawable);
                teacherTextInput.setText(teacher);
            }
        }).start();
    }

    public void setTime(final Context context, final String duration) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
                IconicsDrawable drawable = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_today);
                durationImageView.setVisibility(View.VISIBLE);
                durationTextView.setVisibility(View.VISIBLE);
                durationTextInput.setVisibility(View.VISIBLE);
                durationImageView.setImageDrawable(drawable);
                durationTextInput.setText(duration);
            }
        }).start();
    }

    public void setLocation(final Context context, final String location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ready != true) {
                }
                IconicsDrawable drawable = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_location_on);
                locationImageView.setVisibility(View.VISIBLE);
                locationTextView.setVisibility(View.VISIBLE);
                locationTextInput.setVisibility(View.VISIBLE);
                locationImageView.setImageDrawable(drawable);
                locationTextInput.setText(location);
            }
        }).start();
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        descriptionTextView = (TextView) view.findViewById(R.id.card_details_textview_description);
        descriptionTextInput = (TextView) view.findViewById(R.id.card_details_textview_description_input);
        descriptionImageView = (ImageView) view.findViewById(R.id.card_details_imageview_description);
        periodTextView = (TextView) view.findViewById(R.id.card_details_textview_period);
        periodTextInput = (TextView) view.findViewById(R.id.card_details_textview_period_input);
        periodImageView = (ImageView) view.findViewById(R.id.card_details_imageview_period);
        teacherTextView = (TextView) view.findViewById(R.id.card_details_textview_teacher);
        teacherTextInput = (TextView) view.findViewById(R.id.card_details_textview_teacher_input);
        teacherImageView = (ImageView) view.findViewById(R.id.card_details_imageview_teacher);
        durationTextView = (TextView) view.findViewById(R.id.card_details_textview_duration);
        durationTextInput = (TextView) view.findViewById(R.id.card_details_textview_duration_input);
        durationImageView = (ImageView) view.findViewById(R.id.card_details_imageview_duration);
        locationTextView = (TextView) view.findViewById(R.id.card_details_textview_location);
        locationTextInput = (TextView) view.findViewById(R.id.card_details_textview_location_input);
        locationImageView = (ImageView) view.findViewById(R.id.card_details_imageview_location);
        ready = true;
        Log.d(TAG, "setupInnerViewElements: Done setting up");
    }
}
