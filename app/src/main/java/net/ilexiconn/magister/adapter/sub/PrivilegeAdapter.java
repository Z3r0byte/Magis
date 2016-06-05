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

package net.ilexiconn.magister.adapter.sub;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.ilexiconn.magister.container.sub.Privilege;
import net.ilexiconn.magister.util.GsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrivilegeAdapter extends TypeAdapter<Privilege[]> {
    public Gson gson = GsonUtil.getGson();

    @Override
    public void write(JsonWriter out, Privilege[] value) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Privilege[] read(JsonReader in) throws IOException {
        JsonArray array = gson.getAdapter(JsonElement.class).read(in).getAsJsonArray();
        List<Privilege> privilegeList = new ArrayList<Privilege>();
        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            String group = object.get("Naam").getAsString();
            JsonArray array1 = object.get("Privileges").getAsJsonArray();
            for (JsonElement element1 : array1) {
                Privilege privilege = gson.fromJson(element1, Privilege.class);
                privilege.group = group;
                privilegeList.add(privilege);
            }
        }
        return privilegeList.toArray(new Privilege[privilegeList.size()]);
    }
}
