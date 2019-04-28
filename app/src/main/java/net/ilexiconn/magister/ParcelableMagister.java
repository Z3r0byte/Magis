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

package net.ilexiconn.magister;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.Session;
import net.ilexiconn.magister.container.Study;
import net.ilexiconn.magister.container.Subject;
import net.ilexiconn.magister.container.User;
import net.ilexiconn.magister.container.Version;
import net.ilexiconn.magister.util.AndroidUtil;
import net.ilexiconn.magister.util.HttpUtil;
import net.ilexiconn.magister.util.LogUtil;
import net.ilexiconn.magister.util.LoginUrl;
import net.ilexiconn.magister.util.SchoolUrl;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

/**
 * A special Magister class implementing Parcelable.
 * {@inheritDoc}
 */
public class ParcelableMagister extends Magister implements Parcelable {
    public static final Creator<ParcelableMagister> CREATOR = new Creator<ParcelableMagister>() {
        @Override
        public ParcelableMagister createFromParcel(Parcel in) {
            return new ParcelableMagister(in);
        }

        @Override
        public ParcelableMagister[] newArray(int size) {
            return new ParcelableMagister[size];
        }
    };

    private static final Random random = new Random();

    protected ParcelableMagister() {
        super();
    }

    protected ParcelableMagister(Parcel in) {
        super();
        school = (School) in.readSerializable();
        schoolUrl = new SchoolUrl(school);
        user = (User) in.readSerializable();
        version = (Version) in.readSerializable();
        session = (Session) in.readSerializable();
        profile = (Profile) in.readSerializable();
        subjects = new Subject[in.readInt()];
        for (int i = 0; i < subjects.length; i++) {
            subjects[i] = (Subject) in.readSerializable();
        }
        currentStudy = (Study) in.readSerializable();
        studies = new Study[in.readInt()];
        for (int i = 0; i < studies.length; i++) {
            studies[i] = (Study) in.readSerializable();
        }
    }

    /**
     * Create a new {@link ParcelableMagister} instance by logging in. Will return null if login fails.
     *
     * @param school   the {@link School} instance. Can't be null.
     * @param username the username of the profile. Can't be null.
     * @param password the password of the profile. Can't be null.
     * @return the new {@link ParcelableMagister} instance, null if login fails.
     * @throws IOException               if there is no active internet connection.
     * @throws ParseException            if parsing the date fails.
     * @throws InvalidParameterException if one of the arguments is null.
     */
    public static ParcelableMagister login(School school, String username, String password) throws IOException, ParseException, InvalidParameterException {
        if (school == null || username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidParameterException("Parameters can't be null or empty!");
        }
        ParcelableMagister magister = new ParcelableMagister();
        AndroidUtil.checkAndroid();
        magister.school = school;
        SchoolUrl url = magister.schoolUrl = new SchoolUrl(school);
        magister.version = magister.gson.fromJson(HttpUtil.httpGet(url.getVersionUrl()), Version.class);
        magister.user = new User(username, password, false);

        //The new secret magister login method.
        HttpsURLConnection con = HttpUtil.httpGetConnection((LoginUrl.getAuthorizeUrl() +
                "?client_id=" + url.getClientId() +
                "&redirect_uri=" + url.getRedirectUrl() +
                "&response_type=id_token token" +
                "&scope=openid profile magister.ecs.legacy magister.mdv.broker.read magister.dnn.roles.read" +
                "&nonce=" + random.nextLong())
                .replace(" ", "%20")
        );

        //The request is redirected and there is some information in the final redirected url.
        con.getInputStream().close();
        String query = con.getURL().getQuery();
        query = query.replaceFirst("\\?", "");

        String sessionId = "";
        String returnUrl = "";
        for (String entry : query.split("&")) {
            String[] values = entry.split("=");
            switch (values[0]) {
                case "sessionId":
                    sessionId = values[1];
                    break;
                case "returnUrl":
                    returnUrl = URLDecoder.decode(values[1], "UTF-8");
                    break;
                default:
                    break;
            }
        }
        if (sessionId.isEmpty() || returnUrl.isEmpty()) {
            LogUtil.printError("Invalid credentials", new InvalidParameterException());
            return null;
        }

        //XSRF-TOKEN needs to be a header so we get that here.
        String xrsfToken = "";
        String[] cookies = HttpUtil.getCurrentCookies().split(";");
        for (String cookie : cookies) {
            String[] values = cookie.split("=");
            if (values[0].equalsIgnoreCase("XSRF-TOKEN")) {
                xrsfToken = values[1];
                break;
            }
        }
        if (xrsfToken.isEmpty()) {
            LogUtil.printError("Invalid credentials", new InvalidParameterException());
            return null;
        }

        //We get the authCode from another site, because magister likes to changes thing around for "security" reasons.
        String authCode = magister.gson.fromJson(LogUtil.getStringFromInputStream(HttpUtil.httpGet(LoginUrl.getAuthCodeUrl())), LoginUrl.AuthCode.class).authCode;
        LoginUrl.ChallengeData challengeData = new LoginUrl.ChallengeData(authCode, returnUrl, sessionId);

        Map<String, String> header = Collections.singletonMap("X-XSRF-TOKEN", xrsfToken);

        //Send the data to magister, don't know why it can't be one request.
        String data = magister.gson.toJson(challengeData);
        String response =  LogUtil.getStringFromInputStream(HttpUtil.httpPost(LoginUrl.getCurrentUrl(), data, header));

        String error = magister.gson.fromJson(response, LoginUrl.Error.class).error;
        if (error != null && !error.isEmpty()) {
            LogUtil.printError("Invalid credentials", new InvalidParameterException());
            return null;
        }

        challengeData.setUsername(username);
        data = magister.gson.toJson(challengeData);
        response =  LogUtil.getStringFromInputStream(HttpUtil.httpPost(LoginUrl.getUsernameUrl(), data, header));

        error = magister.gson.fromJson(response, LoginUrl.Error.class).error;
        if (error != null && !error.isEmpty()) {
            LogUtil.printError("Invalid credentials", new InvalidParameterException());
            return null;
        }
        challengeData.setPassword(password);
        data = magister.gson.toJson(challengeData);
        response =  LogUtil.getStringFromInputStream(HttpUtil.httpPost(LoginUrl.getPasswordUrl(), data, header));

        error = magister.gson.fromJson(response, LoginUrl.Error.class).error;
        if (error != null && !error.isEmpty()) {
            LogUtil.printError("Invalid credentials", new InvalidParameterException());
            return null;
        }

        //Now we need to get a bearer token and we are done :D
        con = HttpUtil.httpGetConnection(LoginUrl.getMainUrl() + returnUrl);

        //The token is hidden in the redirected location so we get that url.
        con.getInputStream().close();
        String uri = con.getURL().toString();
        String hash = uri.split("#", 2)[1];

        String accessToken = "";
        for (String entry : hash.split("&")) {
            String[] values = entry.split("=");
            if (!values[0].equalsIgnoreCase("access_token")) continue;
            accessToken = values[1];
        }

        if (accessToken.isEmpty()) {
            LogUtil.printError("Invalid credentials", new InvalidParameterException());
            return null;
        }

        HttpUtil.accesToken = accessToken;

        magister.loginTime = System.currentTimeMillis();
        magister.profile = magister.gson.fromJson(HttpUtil.httpGet(url.getAccountUrl()), Profile.class);
        magister.studies = magister.gson.fromJson(HttpUtil.httpGet(url.getStudiesUrl(magister.profile.id)), Study[].class);
        Date now = new Date();
        for (Study study : magister.studies) {
            if (study.startDate.before(now) && study.endDate.after(now)) {
                magister.currentStudy = study;
            }
        }
        if (magister.currentStudy != null) {
            magister.subjects = magister.getSubjectsOfStudy(magister.currentStudy);
        }
        return magister;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(school);
        dest.writeSerializable(user);
        dest.writeSerializable(version);
        dest.writeSerializable(session);
        dest.writeSerializable(profile);
        dest.writeInt(subjects.length);
        for (Subject subject : subjects) {
            dest.writeSerializable(subject);
        }
        dest.writeSerializable(currentStudy);
        dest.writeInt(studies.length);
        for (Study study : studies) {
            dest.writeSerializable(study);
        }
    }
}
