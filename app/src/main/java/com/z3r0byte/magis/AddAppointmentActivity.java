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

package com.z3r0byte.magis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class AddAppointmentActivity extends AppCompatActivity {

    Toolbar mToolbar;
    EditText mEditTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        mToolbar = (Toolbar) findViewById(R.id.Toolbar);
        mToolbar.setTitle(getString(R.string.title_new_appointment));
        setSupportActionBar(mToolbar);

        mEditTitle = (EditText) findViewById(R.id.edit_text_title);
        mEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    mToolbar.setTitle(charSequence.toString());
                } else {
                    mToolbar.setTitle(getString(R.string.title_new_appointment));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
