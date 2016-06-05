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

package net.ilexiconn.magister.util;

import net.ilexiconn.magister.container.School;

public class SchoolUrl {
    private School school;

    public SchoolUrl(School school) {
        this.school = school;
    }

    public String getMagisterUrl() {
        return school.url + "/";
    }

    public String getApiUrl() {
        return getMagisterUrl() + "api/";
    }

    public String getVersionUrl() {
        return getApiUrl() + "versie/";
    }

    public String getSessionUrl() {
        return getApiUrl() + "sessies/";
    }

    public String getCurrentSessionUrl() {
        return getSessionUrl() + "huidige/";
    }

    public String getAccountUrl() {
        return getApiUrl() + "account/";
    }

    public String getStudiesUrl(int profileId) {
        return getApiUrl() + "personen/" + profileId + "/aanmeldingen";
    }
}
