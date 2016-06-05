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

public class Source implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("Links")
    public Link[] links;

    @SerializedName("BronSoort")
    public int sourceType;

    @SerializedName("Naam")
    public String name;

    @SerializedName("Referentie")
    public int reference;

    //Uri

    @SerializedName("Grootte")
    public int size;

    @SerializedName("Privilege")
    public int privelege;

    @SerializedName("Type")
    public int type;

    @SerializedName("ContentType")
    public String contentType;

    //GewijzigdOp

    //GeplaatstDoor

    //GemaaktOp

    @SerializedName("FileBlobId")
    public int fileBlobId;

    @SerializedName("ParentId")
    public int parentId;

    @SerializedName("UniqueId")
    public String uniqueId;

    @SerializedName("Volgnr")
    public int followId;

    @SerializedName("ModuleSoort")
    public int moduleType;
}
