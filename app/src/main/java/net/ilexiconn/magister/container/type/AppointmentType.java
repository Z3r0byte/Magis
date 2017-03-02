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

package net.ilexiconn.magister.container.type;

import java.io.Serializable;

public enum AppointmentType implements Serializable {
    PERSONAL(1),
    GENERAL(2),
    SCHOOLWIDE(3),
    INTERNSHIP(4),
    INTAKE(5),
    SHEDULE_FREE(6),
    KWT(7),
    STANDBY(8),
    BLOCK(9),
    MISCELLANEOUS(10),
    LOCAL_BLOCK(11),
    CLASS_BLOCK(12),
    LESSON(13),
    STUDYHOUSE(14),
    SHEDULE_FREE_STUDY(15),
    PLANNING(16),
    ACTIONS(101),
    PRESENCES(102),
    EXAM_SHUDULE(103);

    private int id;

    AppointmentType(int i) {
        id = i;
    }

    public static AppointmentType getTypeById(int i) {
        for (AppointmentType type : values()) {
            if (type.getID() == i) {
                return type;
            }
        }
        return null;
    }

    public int getID() {
        return id;
    }
}
