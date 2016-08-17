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

public class User implements Serializable {
    @SerializedName("Gebruikersnaam")
    public String username;

    @SerializedName("Wachtwoord")
    public String password;

    @SerializedName("IngelogdBlijven")
    public boolean stayLoggedIn;

    public User(String username, String password, boolean stayLoggedIn) {
        this.username = username;
        this.password = password;
        this.stayLoggedIn = stayLoggedIn;
    }
}
