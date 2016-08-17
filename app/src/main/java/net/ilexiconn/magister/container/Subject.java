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

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Subject implements Serializable {
    @SerializedName("afkorting")
    public String abbreviation;

    @SerializedName("begindatum")
    public String startDateString;

    public Date startDate;

    @SerializedName("dispensatie")
    public boolean dispensation;

    @SerializedName("docent")
    public String teacher;

    @SerializedName("einddatum")
    public String endDateString;

    public Date endDate;

    @SerializedName("hogerNiveau")
    public boolean higherLevel;

    @SerializedName("id")
    public int id;

    @SerializedName("omschrijving")
    public String description;

    @SerializedName("studieId")
    public int studyId;

    @SerializedName("studieVakId")
    public int studySubjectId;

    @SerializedName("volgnr")
    public int followId;

    @SerializedName("vrijstelling")
    public boolean dispensation2;
}
