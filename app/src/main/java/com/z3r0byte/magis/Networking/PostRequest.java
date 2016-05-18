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
import java.util.Collections;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class PostRequest {

    private static final String TAG = "PostRequest";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String postRequest(String url, String cookie, String postData) throws IOException {
        Log.d(TAG, "postRequest() called with: " + "url = [" + url + "], cookie = [" + cookie + "], postData =" +
                " [" + postData.substring(0, 10) + "*****************" + "]");//hiding password for security reasons

        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .build();

        RequestBody postDataBody = RequestBody.create(JSON, postData);

        Request request;
        if (cookie == null) {
            request = new Request.Builder()
                    .url(url)
                    .post(postDataBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .header("Cookie", cookie)
                    .post(postDataBody)
                    .build();
        }


        Response response = client.newCall(request).execute();

        return response.header("Set-Cookie");
    }

}
