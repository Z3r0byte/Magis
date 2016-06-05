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

import net.ilexiconn.magister.container.Subject;
import net.ilexiconn.magister.util.DateUtil;
import net.ilexiconn.magister.util.GsonUtil;
import net.ilexiconn.magister.util.LogUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends TypeAdapter<Subject[]> {
    public Gson gson = GsonUtil.getGson();

    @Override
    public void write(JsonWriter out, Subject[] value) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Subject[] read(JsonReader in) throws IOException {
        JsonArray array = gson.getAdapter(JsonElement.class).read(in).getAsJsonArray();
        List<Subject> subjectList = new ArrayList<Subject>();
        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            Subject subject = gson.fromJson(object, Subject.class);
            try {
                subject.startDate = DateUtil.stringToDate(subject.startDateString);
                subject.endDate = DateUtil.stringToDate(subject.endDateString);
            } catch (ParseException e) {
                LogUtil.printError("Unable to parse date", e);
            }
            subjectList.add(subject);
        }
        return subjectList.toArray(new Subject[subjectList.size()]);
    }
}
