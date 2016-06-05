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

package net.ilexiconn.magister.container;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import net.ilexiconn.magister.util.HttpUtil;
import net.ilexiconn.magister.util.LogUtil;

import java.io.IOException;
import java.io.Serializable;

public class School implements Serializable {
    @SerializedName("Id")
    public String id;

    @SerializedName("Name")
    public String name;

    @SerializedName("Url")
    public String url;

    public static School[] findSchool(String s) {
        if (s.length() <= 3) {
            return new School[]{};
        }

        try {
            return new Gson().fromJson(HttpUtil.httpGet("https://mijn.magister.net/api/schools?filter=" + s), School[].class);
        } catch (IOException e) {
            LogUtil.printError("Unable to finish request", e);
            return new School[]{};
        }
    }
}
