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

package net.ilexiconn.magister.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.ilexiconn.magister.adapter.type.AppointmentTypeAdapter;
import net.ilexiconn.magister.adapter.type.DisplayTypeAdapter;
import net.ilexiconn.magister.adapter.type.InfoTypeAdapter;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.type.AppointmentType;
import net.ilexiconn.magister.container.type.DisplayType;
import net.ilexiconn.magister.container.type.InfoType;
import net.ilexiconn.magister.util.DateUtil;
import net.ilexiconn.magister.util.GsonUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentAdapter extends TypeAdapter<Appointment[]> {
    private static Gson gson;

    static {
        Map<Class<?>, TypeAdapter<?>> map = new HashMap<Class<?>, TypeAdapter<?>>();
        map.put(AppointmentType.class, new AppointmentTypeAdapter());
        map.put(DisplayType.class, new DisplayTypeAdapter());
        map.put(InfoType.class, new InfoTypeAdapter());
        gson = GsonUtil.getGsonWithAdapters(map);
    }

    @Override
    public void write(JsonWriter out, Appointment[] value) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Appointment[] read(JsonReader in) throws IOException {
        JsonObject object = gson.getAdapter(JsonElement.class).read(in).getAsJsonObject();
        if (object.has("Items")) {
            JsonArray array = object.get("Items").getAsJsonArray();
            List<Appointment> appointmentList = new ArrayList<Appointment>();
            for (JsonElement element : array) {
                JsonObject object1 = element.getAsJsonObject();
                Appointment appointment = gson.fromJson(object1, Appointment.class);
                try {
                    appointment.startDate = DateUtil.stringToDate(appointment.startDateString);
                    appointment.endDate = DateUtil.stringToDate(appointment.endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                appointment.type = gson.getAdapter(AppointmentType.class).fromJsonTree(object1.getAsJsonPrimitive("Type"));
                appointment.displayType = gson.getAdapter(DisplayType.class).fromJsonTree(object1.getAsJsonPrimitive("WeergaveType"));
                appointment.infoType = gson.getAdapter(InfoType.class).fromJsonTree(object1.getAsJsonPrimitive("InfoType"));
                appointmentList.add(appointment);
            }
            return appointmentList.toArray(new Appointment[appointmentList.size()]);
        } else {
            Appointment appointment = gson.fromJson(object, Appointment.class);
            try {
                appointment.startDate = DateUtil.stringToDate(appointment.startDateString);
                appointment.endDate = DateUtil.stringToDate(appointment.endDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            appointment.type = gson.getAdapter(AppointmentType.class).fromJsonTree(object.getAsJsonPrimitive("Type"));
            appointment.displayType = gson.getAdapter(DisplayType.class).fromJsonTree(object.getAsJsonPrimitive("WeergaveType"));
            appointment.infoType = gson.getAdapter(InfoType.class).fromJsonTree(object.getAsJsonPrimitive("InfoType"));
            return new Appointment[]{appointment};
        }
    }
}
