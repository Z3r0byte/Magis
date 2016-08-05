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

import com.google.gson.Gson;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.adapter.StudyAdapter;
import net.ilexiconn.magister.container.Study;
import net.ilexiconn.magister.util.GsonUtil;
import net.ilexiconn.magister.util.HttpUtil;

import java.io.IOException;

/**
 * Created by z3r0byte on 5-8-16.
 */

public class StudyHandler {
    private Gson gson = GsonUtil.getGsonWithAdapter(Study[].class, new StudyAdapter());
    private Magister magister;

    public StudyHandler(Magister magister) {
        this.magister = magister;
    }


    public Study[] getStudies(Boolean noFuture, String date) throws IOException {
        return gson.fromJson(HttpUtil.httpGet(magister.schoolUrl.getApiUrl() + "personen/" + magister.profile.id + "/aanmeldingen?geenToekomstige=" + noFuture + "&peildatum=" + date), Study[].class);
    }
}
