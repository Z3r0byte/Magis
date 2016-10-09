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
import net.ilexiconn.magister.util.SchoolUrl;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.Date;

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
        magister.logout();
        String data = magister.gson.toJson(magister.user);
        String session = LogUtil.getStringFromInputStream(HttpUtil.httpPost(url.getSessionUrl(), data)); //logging
        if (session.startsWith("{\"message\"")) {
            return null;
        }
        magister.session = magister.gson.fromJson(session, Session.class);
        if (!magister.session.state.equals("active")) {
            LogUtil.printError("Invalid credentials", new InvalidParameterException());
            return null;
        }
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
