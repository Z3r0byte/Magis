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

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.codevscolor.materialpreference.activity.MaterialPreferenceActivity;
import com.codevscolor.materialpreference.callback.MaterialPreferenceCallback;
import com.codevscolor.materialpreference.util.MaterialPrefUtil;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.z3r0byte.magis.Utils.ConfigUtil;

public class SettingsActivity extends MaterialPreferenceActivity implements MaterialPreferenceCallback {

    private static final String TAG = "SettingsActivity";
    ConfigUtil configUtil;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        configUtil = new ConfigUtil(this);
        //register this class as listener for preference change
        setPreferenceChangedListener(this);

        //use dark theme or not . Default is light theme
        useDarkTheme(false);

        //set toolbar title
        setToolbarTitle(getString(R.string.drawer_settings));

        //set primary color
        setPrimaryColor(MaterialPrefUtil.COLOR_BLUE);

        //default secondary color for tinting widgets, if no secondary color is used yet
        setDefaultSecondaryColor(this, MaterialPrefUtil.COLOR_ORANGE);

        //set application package name and xml resource name of preference
        setAppPackageName("com.z3r0byte.magis");
        //set xml resource name
        setXmlResourceName("settings");
    }


    /**
     * callback for preference changes
     *
     * @param sharedPreferences
     * @param name
     */
    @Override
    public void onPreferenceSettingsChanged(SharedPreferences sharedPreferences, String name) {
        if (name.equals("libraries")) {
            new LibsBuilder()
                    .withAboutAppName("Magis")
                    .withLicenseShown(true)
                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .withActivityTitle("Over")
                    .start(this);
            configUtil.removePreferencesValue(name);
        } else if (name.equals("website")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.magis-app.nl"));
            startActivity(browserIntent);
            configUtil.removePreferencesValue(name);
        } else {
            Toast.makeText(this, R.string.msg_restart_to_apply, Toast.LENGTH_SHORT).show();
        }
    }
}
