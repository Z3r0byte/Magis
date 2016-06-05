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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.ilexiconn.magister.adapter.sub.PrivilegeAdapter;
import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.sub.Privilege;
import net.ilexiconn.magister.util.GsonUtil;

import java.io.IOException;

public class ProfileAdapter extends TypeAdapter<Profile> {
    public Gson gson = GsonUtil.getGsonWithAdapter(Privilege[].class, new PrivilegeAdapter());

    @Override
    public void write(JsonWriter out, Profile value) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Profile read(JsonReader in) throws IOException {
        JsonObject object = gson.getAdapter(JsonElement.class).read(in).getAsJsonObject();
        Profile profile = gson.fromJson(object.getAsJsonObject("Persoon"), Profile.class);
        profile.privileges = gson.getAdapter(Privilege[].class).fromJsonTree(object.get("Groep"));
        return profile;
    }
}
