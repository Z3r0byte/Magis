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

package com.z3r0byte.magis.Listeners;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bas on 3-10-16.
 */

public class FinishInitiator {
    private static final String TAG = "FinishInitiator";

    private List<FinishListener> listeners = new ArrayList<FinishListener>();

    public void addListener(FinishListener toAdd) {
        listeners.add(toAdd);
    }

    public void finished() {
        Log.d(TAG, "finished: Sending Event...");
        for (FinishListener hl : listeners)
            hl.applyFinish();
    }
}