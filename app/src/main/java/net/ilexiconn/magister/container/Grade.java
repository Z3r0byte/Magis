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

import net.ilexiconn.magister.container.sub.GradePeriod;
import net.ilexiconn.magister.container.sub.GradeRow;
import net.ilexiconn.magister.container.sub.SubSubject;

import java.io.Serializable;
import java.util.Date;

public class Grade implements Serializable {
    @SerializedName("CijferId")
    public int id;

    @SerializedName("CijferStr")
    public String grade;

    @SerializedName(value = "IsCijferVoldoende", alternate = "IsVoldoende")
    public boolean isSufficient;

    @SerializedName("IngevoerdDoor")
    public String filledInBy;

    @SerializedName("DatumIngevoerd")
    public String filledInDateString;

    public Date filledInDate;

    @SerializedName("CijferPeriode")
    public GradePeriod gradePeriod;

    @SerializedName("Inhalen")
    public boolean doAtLaterDate;

    @SerializedName("Vrijstelling")
    public boolean dispensation;

    @SerializedName("TeltMee")
    public boolean doesCount;

    @SerializedName("CijferKolom")
    public GradeRow gradeRow;

    @SerializedName("Docent")
    public String teacherAbbreviation;

    @SerializedName("CijferKolomIdEloOpdracht")
    public String gradeRowIdOfElo;

    @SerializedName("Vak")
    public SubSubject subject;

    @SerializedName("VakDispensatie")
    public String dispensationForCourse;

    @SerializedName("VakVrijstelling")
    public String dispensationForCourse2;

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", grade='" + grade + '\'' +
                ", isSufficient=" + isSufficient +
                ", filledInBy='" + filledInBy + '\'' +
                ", filledInDateString='" + filledInDateString + '\'' +
                ", filledInDate=" + filledInDate +
                ", gradePeriod=" + gradePeriod +
                ", doAtLaterDate=" + doAtLaterDate +
                ", dispensation=" + dispensation +
                ", doesCount=" + doesCount +
                ", gradeRow=" + gradeRow +
                ", teacherAbbreviation='" + teacherAbbreviation + '\'' +
                ", gradeRowIdOfElo='" + gradeRowIdOfElo + '\'' +
                ", subject=" + subject +
                ", dispensationForCourse='" + dispensationForCourse + '\'' +
                ", dispensationForCourse2='" + dispensationForCourse2 + '\'' +
                '}';
    }
}
