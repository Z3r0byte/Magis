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

package com.z3r0byte.magis.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.z3r0byte.magis.Adapters.PresenceAdapter;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.MagisActivity;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Presence;
import net.ilexiconn.magister.container.PresencePeriod;
import net.ilexiconn.magister.handler.PresenceHandler;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Created by bas on 18-9-16.
 */

public class PresenceTask extends AsyncTask<Void, Void, Presence[]> {
    private static final String TAG = "PresenceTask";

    public MagisActivity activity;
    public Magister magister;
    PresencePeriod presencePeriod;

    public String error;


    public PresenceTask(MagisActivity activity, Magister magister, PresencePeriod period) {
        this.activity = activity;
        this.magister = magister;
        this.presencePeriod = period;
    }

    @Override
    protected void onPreExecute() {
        activity.mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected Presence[] doInBackground(Void... params) {
        try {
            PresenceHandler presenceHandler = new PresenceHandler(magister);
            Presence[] presences;
            if (presencePeriod == null || presencePeriod.start == null) {
                presences = presenceHandler.getPresence();
            } else {
                presences = presenceHandler.getPresence(presencePeriod);
            }

            Log.d(TAG, "doInBackground: " + presences.length);
            return presences;
        } catch (IOException e) {
            Log.e(TAG, "Unable to retrieve data", e);
            error = activity.getString(R.string.err_no_connection);
            return null;
        } catch (InvalidParameterException e) {
            Log.e(TAG, "Invalid Parameters", e);
            error = activity.getString(R.string.err_unknown);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Presence[] presences) {
        if (presences != null && presences.length > 0) {
            activity.mPresences = presences;
            activity.mSwipeRefreshLayout.setRefreshing(false);
            //activity.errorView.setVisibility(View.GONE);

            activity.mPresenceAdapter = new PresenceAdapter(activity, presences);
            activity.listView.setAdapter(activity.mHomeworkAdapter);
        } else {
            activity.mSwipeRefreshLayout.setRefreshing(false);
            //activity.errorView.setVisibility(View.VISIBLE);
            //activity.errorView.setConfig(ErrorViewConfigs.NoHomeworkConfig);
            Log.e(TAG, "Error: " + error);
        }
    }
}
