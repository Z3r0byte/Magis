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

package com.z3r0byte.magis.Magister;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bas on 23-5-16.
 */
public class Teacher {
    @SerializedName("Id")
    public int id;

    @SerializedName("Naam")
    public String name;

    @SerializedName("Docentcode")
    public String techerCode;

    public Teacher(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public String getTecherCode() {
        return techerCode;
    }

    public void setTecherCode(String techerCode) {
        techerCode = techerCode;
    }
}
