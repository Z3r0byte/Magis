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

import android.app.Application;
import android.support.compat.BuildConfig;

import com.github.stkent.bugshaker.BugShaker;
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType;

/**
 * Created by bas on 11-9-16.
 */

public final class Magis extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BugShaker.get(this)
                .setEmailAddresses("z3r0byte.apps@gmail.com")
                .setEmailSubjectLine("Magis Bug Report")
                .setLoggingEnabled(BuildConfig.DEBUG)
                .setAlertDialogType(AlertDialogType.APP_COMPAT)
                .assemble()
                .start();
    }

}