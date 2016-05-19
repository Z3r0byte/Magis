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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MagisterAccount {

    @SerializedName("Username")
    String Username;

    @SerializedName("Password")
    String Passsword;

    @SerializedName("School")
    MagisterSchool School;

    @SerializedName("Id")
    int id;

    @SerializedName("Roepnaam")
    String Name;

    @SerializedName("Tussenvoegsel")
    String Insertion;

    @SerializedName("Achternaam")
    String Surname;

    @SerializedName("Geboortedatum")
    String DateOfBirth;

    public String getUsername() {

        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public MagisterSchool getSchool() {
        return School;
    }

    public void setSchool(MagisterSchool school) {
        School = school;
    }

    public String getPasssword() {
        return Passsword;
    }

    public void setPasssword(String passsword) {
        Passsword = passsword;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getInsertion() {
        return Insertion;
    }

    public String getSurname() {
        return Surname;
    }

    public String getDateOfBirth() {
        DateFormat parseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        try {
            date = parseDateFormat.parse(DateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateFormat.format(date);
    }

    public String getFullName() {
        String Fullname;
        if (Insertion == null || Insertion == "null") {
            Fullname = Name + " " + Surname;
        } else {
            Fullname = Name + " " + Insertion + " " + Surname;
        }
        return Fullname;
    }

    public String getApiUrl() {
        return School.getUrl() + "/api/";

    }

    @Override
    public String toString() {
        return "MagisterAccount{" +
                "Username='" + Username + '\'' +
                ", Passsword='" + Passsword + '\'' +
                ", School=" + School +
                ", id=" + id +
                ", Name='" + Name + '\'' +
                ", Insertion='" + Insertion + '\'' +
                ", Surname='" + Surname + '\'' +
                ", DateOfBirth='" + DateOfBirth + '\'' +
                '}';
    }
}
