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

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.adapter.AppointmentAdapter;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.PersonalAppointment;
import net.ilexiconn.magister.container.PresencePeriod;
import net.ilexiconn.magister.exeption.PrivilegeException;
import net.ilexiconn.magister.util.GsonUtil;
import net.ilexiconn.magister.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppointmentHandler implements IHandler {
    private Gson gson = GsonUtil.getGsonWithAdapter(Appointment[].class, new AppointmentAdapter());
    private Magister magister;

    public AppointmentHandler(Magister magister) {
        this.magister = magister;
    }

    private static final String TAG = "AppointmentHandler";

    /**
     * Get an array with all the {@link Appointment}s of this {@link PresencePeriod}. If no appointments can be found,
     * an empty array will be returned instead.
     *
     * @param period the {@link PresencePeriod} of the {@link Appointment}s.
     * @return an array with the {@link Appointment}s of this period.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public Appointment[] getAppointments(PresencePeriod period) throws IOException {
        return getAppointments(period.startDate, period.endDate);
    }

    /**
     * Get an array with all the {@link Appointment}s. If no appointments can be found, an empty array will be returned
     * instead.
     *
     * @param from  the start date.
     * @param until the end date.
     * @return an array with the {@link Appointment}s of this date period.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public Appointment[] getAppointments(Date from, Date until) throws IOException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String dateNow = format.format(from);
        String dateFrom = format.format(until);
        return gson.fromJson(HttpUtil.httpGet(magister.school.url + "/api/personen/" + magister.profile.id + "/afspraken?status=1&van=" + dateNow + "&tot=" + dateFrom), Appointment[].class);
    }

    /**
     * Get an array with all the {@link Appointment}s of today. If no appointments can be found, an empty array will be
     * returned
     * instead.
     *
     * @return an array with the {@link Appointment}s of this date period.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public Appointment[] getAppointmentsOfToday() throws IOException {
        Date now = new Date();
        return getAppointments(now, now);
    }

    /**
     * Adds an appointment to magister. It wil return the Url of this Appointment, if it fails null will be
     * returned
     *
     * @param personalAppointment the personal appointment instance.
     * @return the appointment instance with more info.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public Appointment createAppointment(PersonalAppointment personalAppointment) throws IOException {
        String data = gson.toJson(personalAppointment);
        BufferedReader reader = new BufferedReader(HttpUtil.httpPostRaw(magister.school.url + "/api/personen/" + magister.profile.id + "/afspraken", data));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }
        String url = GsonUtil.getFromJson(responseBuilder.toString(), "Url").getAsString();
        return gson.fromJson(HttpUtil.httpGet(magister.school.url + url), Appointment.class);
    }

    public Boolean finishAppointment(Appointment appointment) throws IOException, JSONException {
        String rawAppointment = getRawAppointment(appointment.id);
        Log.d(TAG, "finishAppointment: " + rawAppointment);
        JSONObject jsonObject = new JSONObject(rawAppointment);
        jsonObject.put("Afgerond", appointment.finished);


        String data = jsonObject.toString();
        Log.d(TAG, "finishAppointment: data: " + data);
        BufferedReader reader = new BufferedReader(HttpUtil.httpPut(magister.school.url + "/api/personen/" + magister.profile.id
                + "/afspraken/" + appointment.id, data));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }
        String result = responseBuilder.toString();
        if (result.equals("{\"Url\":\"/api/personen/" + magister.profile.id + "/afspraken/" + appointment.id + "\",\"UriKind\":0}")) {
            return true;
        } else {
            return false;
        }
    }

    public String getRawAppointment(int id) throws IOException {
        BufferedReader reader = new BufferedReader(HttpUtil.httpGet(magister.school.url + "/api/personen/"
                + magister.profile.id + "/afspraken/" + id));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }
        return responseBuilder.toString();
    }


    /**
     * Deletes an appointment from magister.
     *
     * @param appointment the appointment instance.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public void removeAppointment(Appointment appointment) throws IOException {
        removeAppointment(appointment.id);
    }

    /**
     * Deletes an appointment from magister.
     *
     * @param id the appointment id.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public void removeAppointment(int id) throws IOException {
        if (id <= 0) {
            throw new InvalidParameterException("Id can't be 0 or lower!");
        }
        HttpUtil.httpDelete(magister.school.url + "/api/personen/" + magister.profile.id + "/afspraken/" + id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrivilege() {
        return "Afspraken";
    }
}
