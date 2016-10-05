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

package com.z3r0byte.magis.Networking;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by basva on 14-5-2016.
 */
public class GetRequest {

    private static final String TAG = "GetRequest";

    public static String getRequest(String url, String cookie) throws IOException {
        Log.d(TAG, "getRequest() called with: " + "url = [" + url + "], cookie = [" + cookie + "]");

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Request request;
        if (cookie == null) {
            request = new Request.Builder()
                    .url(url)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .header("Cookie", cookie)
                    .build();
        }


        Response response = client.newCall(request).execute();

        String responseStr = response.body().string();

        Log.d(TAG, "getRequest: response: " + responseStr);
        return responseStr;
    }
}
