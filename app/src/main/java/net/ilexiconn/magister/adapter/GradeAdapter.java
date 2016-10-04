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

import net.ilexiconn.magister.adapter.type.RowTypeAdapter;
import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.container.type.RowType;
import net.ilexiconn.magister.util.DateUtil;
import net.ilexiconn.magister.util.GsonUtil;
import net.ilexiconn.magister.util.LogUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GradeAdapter extends TypeAdapter<Grade[]> {
    public Gson gson = GsonUtil.getGsonWithAdapter(RowType.class, new RowTypeAdapter());

    @Override
    public void write(JsonWriter out, Grade[] value) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Grade[] read(JsonReader in) throws IOException {
        JsonObject object = gson.getAdapter(JsonElement.class).read(in).getAsJsonObject();
        JsonArray array = null;

        if (object.has("Items")) {
            array = object.get("Items").getAsJsonArray();
        } else {
            return new Grade[]{};
        }

        List<Grade> gradeList = new ArrayList<Grade>();
        for (JsonElement element : array) {
            JsonObject object1 = element.getAsJsonObject();
            Grade grade = gson.fromJson(object1, Grade.class);
            if (grade.filledInDateString != null) {
                try {
                    grade.filledInDate = DateUtil.stringToDate(grade.filledInDateString);
                } catch (ParseException e) {
                    LogUtil.printError("Unable to parse date", e);
                }
            }
            gradeList.add(grade);
        }
        return gradeList.toArray(new Grade[gradeList.size()]);
    }
}
