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

import net.ilexiconn.magister.container.PresencePeriod;
import net.ilexiconn.magister.util.DateUtil;
import net.ilexiconn.magister.util.GsonUtil;
import net.ilexiconn.magister.util.LogUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class PresencePeriodAdapter extends TypeAdapter<PresencePeriod[]> {
    public Gson gson = GsonUtil.getGson();

    @Override
    public void write(JsonWriter out, PresencePeriod[] value) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PresencePeriod[] read(JsonReader in) throws IOException {
        JsonObject object = gson.getAdapter(JsonElement.class).read(in).getAsJsonObject();
        JsonArray array = object.get("Items").getAsJsonArray();
        List<PresencePeriod> presencePeriodList = new ArrayList<PresencePeriod>();
        for (JsonElement element : array) {
            JsonObject object1 = element.getAsJsonObject();
            PresencePeriod presencePeriod = gson.fromJson(object1, PresencePeriod.class);
            try {
                presencePeriod.startDate = DateUtil.stringToDate(presencePeriod.start);
                presencePeriod.endDate = DateUtil.stringToDate(presencePeriod.end);
            } catch (ParseException e) {
                LogUtil.printError("Failed to parse date.", e);
            }
            presencePeriodList.add(presencePeriod);
        }
        return presencePeriodList.toArray(new PresencePeriod[presencePeriodList.size()]);
    }
}
