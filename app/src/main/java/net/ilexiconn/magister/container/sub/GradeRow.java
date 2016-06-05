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

package net.ilexiconn.magister.container.sub;

import com.google.gson.annotations.SerializedName;

import net.ilexiconn.magister.container.type.RowType;

import java.io.Serializable;

public class GradeRow implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("KolomNaam")
    public String rowName;

    @SerializedName("KolomVolgNummer")
    public String rowSerialNumber;

    @SerializedName("KolomNummer")
    public String rowNumber;

    @SerializedName("KolomOmschrijving")
    public String rowDiscription; //TODO: See if this works

    @SerializedName("KolomKop")
    public String rowTitle;

    @SerializedName("KolomSoort")
    public RowType rowSort;

    @SerializedName("IsDocentKolom")
    public boolean isTeacherOnly;

    @SerializedName("IsHerkansingKolom")
    public boolean isResitRow;

    @SerializedName("HeeftOnderliggendeKolommen")
    public boolean hasSubRows;

    @SerializedName("IsPTAKolom")
    public boolean isPTARow;
}
