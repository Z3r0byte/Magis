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

import net.ilexiconn.magister.container.sub.Link;

import java.io.Serializable;

public class SingleStudyGuide implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("Links")
    public Link[] links;

    @SerializedName("Van")
    public String from;

    @SerializedName("TotEnMet")
    public String to;

    @SerializedName("Titel")
    public String title;

    @SerializedName("IsZichtbaar")
    public boolean isVisible;

    @SerializedName("InLeerlingArchief")
    public boolean isArchived;

    @SerializedName("VakCodes")
    public String[] courses;

    public StudyGuideItem[] items;
}
