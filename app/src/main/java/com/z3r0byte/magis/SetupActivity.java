/*
 * Copyright 2016 Bas van den Boom 'Z3r0byte'
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.z3r0byte.magis;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

public class SetupActivity extends IntroActivity {

    private static final String TAG = "SetupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomActivityOnCrash.install(this);

        setSkipEnabled(false);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.setup_title_1)
                .description(R.string.setup_desc_1)
                .image(R.drawable.magis512)
                .background(R.color.setup_color_1)
                .backgroundDark(R.color.setup_color_1)
                .build());

        final Slide permissionsSlide;
        permissionsSlide = new SimpleSlide.Builder()
                .title(R.string.setup_title_2)
                .description(R.string.setup_desc_2)
                .background(R.color.setup_color_2)
                .backgroundDark(R.color.setup_color_2)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .build();
        addSlide(permissionsSlide);

        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {
                View contentView = findViewById(android.R.id.content);
                Slide slide = getSlide(position);

                if (slide == permissionsSlide) {
                    Snackbar.make(contentView, R.string.snackbar_no_permissions, Snackbar.LENGTH_LONG).show();
                }
            }
        });


    }
}
