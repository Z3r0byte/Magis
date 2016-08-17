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

public class SingleGrade implements Serializable {
    @SerializedName("KolomKopnaam")
    public String headerName;

    @SerializedName("KolomNaam")
    public String name;

    @SerializedName("KolomNiveau")
    public String level;

    @SerializedName("KolomOmschrijving")
    public String description;

    @SerializedName("KolomSoortKolom")
    public int type;

    @SerializedName("Weging")
    public int wage;

    @SerializedName("WerkInformatieOmschrijving")
    public String workDescription;

    @SerializedName("WerkinformatieDatumIngevoerd")
    public String workDate;
}
