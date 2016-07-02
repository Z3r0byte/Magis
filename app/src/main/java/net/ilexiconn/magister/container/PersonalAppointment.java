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

import net.ilexiconn.magister.container.type.AppointmentType;
import net.ilexiconn.magister.util.DateUtil;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.Date;

public class PersonalAppointment implements Serializable {
    @SerializedName("Id")
    public int id = 0;

    @SerializedName("Start")
    public String startDate;

    @SerializedName("Einde")
    public String endDate;

    @SerializedName("DuurtHeleDag")
    public boolean takesAllDay;

    @SerializedName("Lokatie")
    public String location;

    @SerializedName("InfoType")
    public int infoType = 6;

    @SerializedName("WeergaveType")
    public int displayType = 1;

    @SerializedName("Type")
    public int appointmentType = 1;

    @SerializedName("Afgerond")
    public boolean finished = false;

    @SerializedName("Inhoud")
    public String content;

    @SerializedName("Omschrijving")
    public String description;

    @SerializedName("Status")
    public int classState = 2;

    @SerializedName("OpdrachtId")
    public int asignmentId = 0;

    public PersonalAppointment(String title, String content, String location, AppointmentType type, Date start, Date end) throws ParseException, InvalidParameterException {
        this.location = location;
        if (content == null || content == "") {
            content = " ";
        }
        this.content = content;
        if (title == null || "".equals(title)) {
            throw new InvalidParameterException("The appointment's title must be set!");
        }
        description = title;
        if (start.after(end) || start.equals(end)) {
            throw new InvalidParameterException("The appointment's start date must be before and not equal to the end date!");
        }
        startDate = DateUtil.dateToString(start);
        endDate = DateUtil.dateToString(end);
        if (!(type == AppointmentType.PERSONAL || type == AppointmentType.PLANNING)) {
            throw new InvalidParameterException("The AppointmentType must be PERSONAL or PLANNING!");
        }
        appointmentType = type.getID();
    }
}
