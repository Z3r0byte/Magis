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

package com.z3r0byte.magis.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by z3r0byte on 21-8-16.
 */

public class ConfigUtil {
    Context context;

    public ConfigUtil(Context context) {
        this.context = context;
    }

    public void removePreferencesValue(String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.z3r0byte.magis_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(value);
        editor.apply();
    }

    public Boolean getBoolean(String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.z3r0byte.magis_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(name, false);
    }
}
