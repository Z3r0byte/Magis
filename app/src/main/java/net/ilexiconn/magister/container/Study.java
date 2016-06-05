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

import net.ilexiconn.magister.container.sub.Group;
import net.ilexiconn.magister.container.sub.Link;

import java.io.Serializable;
import java.util.Date;

public class Study implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("Links")
    public Link[] links;

    @SerializedName("LeerlingId")
    public int pupilId;

    @SerializedName("Start")
    public String startDateString;

    public Date startDate;

    @SerializedName("Einde")
    public String endDateString;

    public Date endDate;

    @SerializedName("Lesperiode")
    public String classPeriod;

    @SerializedName("Groep")
    public Group group;

    public int studyId;

    public String description;
}
