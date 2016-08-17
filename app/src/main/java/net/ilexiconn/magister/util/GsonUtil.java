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

package net.ilexiconn.magister.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;

import java.util.HashMap;
import java.util.Map;

public class GsonUtil {
    private static Gson gson = new Gson();
    private static JsonParser parser = new JsonParser();

    public static Gson getGson() {
        return gson;
    }

    public static JsonElement getFromJson(String json, String key) {
        return parser.parse(json).getAsJsonObject().get(key);
    }

    public static Gson getGsonWithAdapter(Class<?> type, TypeAdapter<?> adapter) {
        Map<Class<?>, TypeAdapter<?>> map = new HashMap<Class<?>, TypeAdapter<?>>();
        map.put(type, adapter);
        return getGsonWithAdapters(map);
    }

    public static Gson getGsonWithAdapters(Map<Class<?>, TypeAdapter<?>> map) {
        GsonBuilder builder = new GsonBuilder();
        for (Map.Entry<Class<?>, TypeAdapter<?>> entry : map.entrySet()) {
            builder.registerTypeAdapter(entry.getKey(), entry.getValue());
        }
        return builder.create();
    }
}
