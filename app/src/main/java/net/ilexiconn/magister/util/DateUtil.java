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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    /**
     * Parse a String date into a Date object.
     *
     * @param date the String date.
     * @return the new Date instance.
     * @throws ParseException if the date String can't be parsed.
     */
    public static Date stringToDate(String date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT-0"));
        return format.parse(date);
    }

    /**
     * Parse a Date object into a String.
     *
     * @param date the Date instance.
     * @return the date as String.
     * @throws ParseException if the Date object can't be parsed.
     */
    public static String dateToString(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        format.setTimeZone(TimeZone.getTimeZone("GMT-0"));
        return format.format(date) + "Z";
    }
}
