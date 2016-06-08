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

public enum DisplayType implements Serializable {
    AVAILABLE(1),
    PROVISIONALLY_SCHEDULED(2),
    SCHEDULED(3),
    ABSENT(4);

    private int id;

    DisplayType(int i) {
        id = i;
    }

    public static DisplayType getTypeById(int i) {
        for (DisplayType type : values()) {
            if (type.getID() == i) {
                return type;
            }
        }
        return null;
    }

    public int getID() {
        return id;
    }

    @Override
    public String toString() {
        return "DisplayType{" +
                "id=" + id +
                '}';
    }
}
