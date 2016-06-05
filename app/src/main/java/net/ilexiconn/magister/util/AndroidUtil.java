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

import java.lang.reflect.InvocationTargetException;

public class AndroidUtil {
    private static boolean runningOnAndroid = false;
    private static boolean androidSupportCache = false;

    /**
     * Check if the API is running on Android.
     */
    public static void checkAndroid() {
        try {
            Class.forName("android.view.View");
            runningOnAndroid = true;
            isCacheAvailableOnAndroid();
        } catch (ClassNotFoundException e) {
            runningOnAndroid = false;
        }
    }

    /**
     * Check if the API is running on Android, and if it can use the Android caching utilities.
     *
     * @return true if the API is running on Android and can use the Android caching utilities.
     */
    public static boolean isCacheAvailableOnAndroid() {
        try {
            Class<?> cache = Class.forName("android.net.http.HttpResponseCache");
            cache.getMethod("getInstalled").invoke(null);
            androidSupportCache = true;
        } catch (ClassNotFoundException e) {
            LogUtil.printError("Could not find Class: android.net.http.HttpResponseCache", e.getCause());
            androidSupportCache = false;
        } catch (NoSuchMethodException e) {
            LogUtil.printError("Could not find Method: getInstalled", e.getCause());
            androidSupportCache = false;
        } catch (IllegalAccessException e) {
            LogUtil.printError("Could not access Method: getInstalled", e.getCause());
            androidSupportCache = false;
        } catch (InvocationTargetException e) {
            LogUtil.printError("Failed to invoke Method: getInstalled", e.getCause());
            androidSupportCache = false;
        }
        return androidSupportCache;
    }

    public static boolean getRunningOnAndroid() {
        return runningOnAndroid;
    }

    public static boolean getAndroidSupportCache() {
        return androidSupportCache;
    }
}
