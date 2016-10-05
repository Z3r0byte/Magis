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

package net.ilexiconn.magister.handler;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.adapter.PresenceAdapter;
import net.ilexiconn.magister.adapter.PresencePeriodAdapter;
import net.ilexiconn.magister.container.Presence;
import net.ilexiconn.magister.container.PresencePeriod;
import net.ilexiconn.magister.exeption.PrivilegeException;
import net.ilexiconn.magister.util.GsonUtil;
import net.ilexiconn.magister.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PresenceHandler implements IHandler {
    private static final String TAG = "PresenceHandler";
    private Gson gson;
    private Magister magister;

    public PresenceHandler(Magister magister) {
        this.magister = magister;
        Map<Class<?>, TypeAdapter<?>> map = new HashMap<Class<?>, TypeAdapter<?>>();
        map.put(Presence[].class, new PresenceAdapter());
        map.put(PresencePeriod[].class, new PresencePeriodAdapter());
        gson = GsonUtil.getGsonWithAdapters(map);
    }

    /**
     * Get an array with all {@link Presence} data of the current study. If no data can be found, an empty array will
     * be returned instead.
     *
     * @return an array with all the {@link Presence} data.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public Presence[] getPresence() throws IOException, PrivilegeException {
        Log.d(TAG, "getPresence() called");
        Log.d(TAG, "getPresence: " + magister.currentStudy.startDateString);
        return getPresence(null);
    }

    /**
     * Get an array with all {@link Presence} data of a specific period. If no data can be found, an empty array will
     * be returned instead.
     *
     * @param period the {@link PresencePeriod}.
     * @return an array with all the {@link Presence} data.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public Presence[] getPresence(PresencePeriod period) throws IOException, PrivilegeException {
        Log.d(TAG, "getPresence() called with: period = [" + period + "]");
        try {
            return gson.fromJson(HttpUtil.httpGet(magister.schoolUrl.getApiUrl() + "personen/" + magister.profile.id + "/absenties?van=" + (period == null ? magister.currentStudy.startDateString.substring(0, 10) : period.start.substring(0, 10)) + "&tot=" + (period == null ? magister.currentStudy.endDateString.substring(0, 10) : period.end.substring(0, 10))), Presence[].class);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get an array with all the {@link PresencePeriod}s of this profile. If no data can be found, an empty array will
     * be returned instead.
     *
     * @return an array with all the {@link PresencePeriod}s.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public PresencePeriod[] getPresencePeriods() throws IOException, PrivilegeException {
        return gson.fromJson(HttpUtil.httpGet(magister.schoolUrl.getApiUrl() + "personen/" + magister.profile.id + "/absentieperioden"), PresencePeriod[].class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrivilege() {
        return "Absenties";
    }
}
