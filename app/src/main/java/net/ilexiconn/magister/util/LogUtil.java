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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtil {
    private static String TAG = "MAGISTER.JAVA";

    /**
     * Print an error to the system error stream. Will use the Log class if running on Android.
     *
     * @param description the error's description.
     * @param throwable   the throwable.
     */
    public static void printError(String description, Throwable throwable) {
        String details = toPrettyString("Operating System", System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")") + toPrettyString("Java Version", System.getProperty("java.version"));
        String report = "---- Error Report ----\n" + description + "\n\n-- Crash Log --\n" + getStackTrace(throwable) + "\n-- System Details --\n" + details;
        if (AndroidUtil.getRunningOnAndroid()) {
            try {
                Class<?> logClass = Class.forName("android.util.Log");
                logClass.getMethod("e", String.class, String.class, Throwable.class).invoke(null, TAG, description, throwable);
                return;
            } catch (Exception e) {
                System.err.println(report);
            }
        }
        System.err.println(report);
    }

    private static String toPrettyString(String key, String value) {
        return key + "\n\t" + value + "\n";
    }

    private static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        throwable.printStackTrace(printWriter);
        String s = stringWriter.toString();

        try {
            stringWriter.close();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    /**
     * Set a custom Android logging tag.
     *
     * @param tag the new tag.
     */
    public static void setAndroidTag(String tag) {
        TAG = tag;
    }
}
