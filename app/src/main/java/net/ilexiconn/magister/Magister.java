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

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.ilexiconn.magister.adapter.ProfileAdapter;
import net.ilexiconn.magister.adapter.StudyAdapter;
import net.ilexiconn.magister.adapter.SubjectAdapter;
import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.Session;
import net.ilexiconn.magister.container.Study;
import net.ilexiconn.magister.container.Subject;
import net.ilexiconn.magister.container.User;
import net.ilexiconn.magister.container.Version;
import net.ilexiconn.magister.container.sub.Privilege;
import net.ilexiconn.magister.exeption.PrivilegeException;
import net.ilexiconn.magister.handler.AppointmentHandler;
import net.ilexiconn.magister.handler.ContactHandler;
import net.ilexiconn.magister.handler.ELOHandler;
import net.ilexiconn.magister.handler.GradeHandler;
import net.ilexiconn.magister.handler.Handler;
import net.ilexiconn.magister.handler.IHandler;
import net.ilexiconn.magister.handler.MessageHandler;
import net.ilexiconn.magister.handler.PresenceHandler;
import net.ilexiconn.magister.util.AndroidUtil;
import net.ilexiconn.magister.util.HttpUtil;
import net.ilexiconn.magister.util.LogUtil;
import net.ilexiconn.magister.util.SchoolUrl;
import net.ilexiconn.magister.util.android.ImageContainer;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * The main API class. You can get a new instance by running {@link Magister#login(School, String, String)}.
 *
 * @author iLexiconn
 * @since 0.1.0
 */
public class Magister {
    private static final String TAG = "Magister";
    public static final String VERSION = "0.1.2";

    public static final int SESSION_TIMEOUT = 1200000;

    public Gson gson = new GsonBuilder()
            .registerTypeAdapter(Profile.class, new ProfileAdapter())
            .registerTypeAdapter(Study[].class, new StudyAdapter())
            .registerTypeAdapter(Subject[].class, new SubjectAdapter())
            .create();

    public School school;
    public SchoolUrl schoolUrl;
    public User user;

    public Version version;
    public Session session;
    public Profile profile;
    public Subject[] subjects;
    public Study[] studies;
    public Study currentStudy;

    protected long loginTime = 0L;

    private List<IHandler> handlerList = new ArrayList<IHandler>();

    protected Magister() {
        handlerList.add(new GradeHandler(this));
        handlerList.add(new PresenceHandler(this));
        handlerList.add(new ContactHandler(this));
        handlerList.add(new MessageHandler(this));
        handlerList.add(new AppointmentHandler(this));
        handlerList.add(new ELOHandler(this));
    }

    /**
     * Create a new {@link Magister} instance by logging in. Will return null if login fails.
     *
     * @param school   the {@link School} instance. Can't be null.
     * @param username the username of the profile. Can't be null.
     * @param password the password of the profile. Can't be null.
     * @return the new {@link Magister} instance, null if login fails.
     * @throws IOException               if there is no active internet connection.
     * @throws ParseException            if parsing the date fails.
     * @throws InvalidParameterException if one of the arguments is null.
     */
    public static Magister login(School school, String username, String password) throws IOException, ParseException, InvalidParameterException {
        if (school == null || username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidParameterException("Parameters can't be null or empty, dumbass!");
        }
        Magister magister = new Magister();
        AndroidUtil.checkAndroid();
        magister.school = school;
        SchoolUrl url = magister.schoolUrl = new SchoolUrl(school);
        magister.version = magister.gson.fromJson(HttpUtil.httpGet(url.getVersionUrl()), Version.class);
        magister.user = new User(username, password, false);
        magister.logout();
        String data = magister.gson.toJson(magister.user);
        String session = LogUtil.getStringFromInputStream(HttpUtil.httpPost(url.getSessionUrl(), data)); //logging
        Log.d(TAG, "login: onLogin (session): " + session);
        if (session.contains("Ongeldig account of verkeerde combinatie van gebruikersnaam en wachtwoord")) {
            LogUtil.printError("Invalid credentials", new InvalidParameterException());
            return null;
        } else if (session.startsWith("{\"message\"")) {
            throw new InvalidParameterException(session);
        }
        magister.session = magister.gson.fromJson(session, Session.class);
        if (!magister.session.state.equals("active")) {
            LogUtil.printError("Invalid credentials", new InvalidParameterException());
            return null;
        }
        magister.loginTime = System.currentTimeMillis();
        HttpUtil.httpGet(url.getCurrentSessionUrl()); // more mimicing the magister client
        String profile = LogUtil.getStringFromInputStream(HttpUtil.httpGet(url.getAccountUrl())); //Logging
        Log.d(TAG, "login: Profile: " + profile);
        magister.profile = magister.gson.fromJson(profile, Profile.class);
        magister.studies = magister.gson.fromJson(HttpUtil.httpGet(url.getStudiesUrl(magister.profile.id)), Study[].class);
        Date now = new Date();
        for (Study study : magister.studies) {
            if (study.endDate.before(now)) {
                magister.currentStudy = study;
            }
        }
        if (magister.currentStudy != null) {
            magister.subjects = magister.getSubjectsOfStudy(magister.currentStudy);
        }
        return magister;
    }

    /**
     * Refresh the session of this magister instance by logging in again.
     *
     * @return the current session.
     * @throws IOException if there is no active internet connection.
     */
    public Session login() throws IOException {
        logout();
        Map<String, String> nameValuePairMap = gson.fromJson(gson.toJson(user), new TypeToken<Map<String, String>>() {
        }.getType());
        String data = gson.toJson(user);
        session = gson.fromJson(HttpUtil.httpPost(schoolUrl.getSessionUrl(), data), Session.class);
        loginTime = System.currentTimeMillis();
        return session;
    }

    /**
     * Logout the current session. You have to wait a few seconds before logging in again.
     *
     * @throws IOException if there is no active internet connection.
     */
    public void logout() throws IOException {
        HttpUtil.httpDelete(schoolUrl.getCurrentSessionUrl());
        loginTime = 0L;
    }

    /**
     * Check if the current session is still active.
     *
     * @return true if the current session is still active.
     */
    public boolean isExpired() {
        return loginTime + (SESSION_TIMEOUT - 1000) < System.currentTimeMillis();
    }

    /**
     * Check if this user has the following privilege.
     *
     * @param privilege the privilege name.
     * @return true if the profile has the privilege.
     */
    public boolean hasPrivilege(String privilege) {
        for (Privilege p : profile.privileges) {
            if (p.name.equals(privilege)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the current profile picture in the default size.
     *
     * @return the current profile picture in the default size.
     * @throws IOException if there is no active internet connection.
     */
    public ImageContainer getImage() throws IOException {
        return getImage(42, 64, false);
    }

    /**
     * Get the current profile picture.
     *
     * @param width  the width.
     * @param height the height.
     * @param crop   true if not in default ratio.
     * @return the current profile picture.
     * @throws IOException if there is no active internet connection.
     */
    public ImageContainer getImage(int width, int height, boolean crop) throws IOException {
        String url = school.url + "/api/personen/" + profile.id + "/foto" + (width != 42 || height != 64 || crop ? "?width=" + width + "&height=" + height + "&crop=" + crop : "");
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", HttpUtil.getCurrentCookies());
        connection.connect();
        try {
            return new ImageContainer(connection.getInputStream());
        } catch (ClassNotFoundException e) {
            LogUtil.printError("Unable to load image class.", e);
            return null;
        }
    }

    /**
     * Change the password of the current profile.
     *
     * @param oldPassword  the current password.
     * @param newPassword  the new password.
     * @param newPassword2 the new password.
     * @return a String with the response. 'Successful' if the password changed successfully.
     * @throws IOException               if there is no active internet connection.
     * @throws InvalidParameterException if one of the parameters is null or empty, or when the two new passwords aren't
     *                                   the same.
     * @throws PrivilegeException        if the profile doesn't have the privilege to perform this action.
     * @deprecated use the password handler instead.
     */
    @Deprecated
    public String changePassword(String oldPassword, String newPassword, String newPassword2) throws IOException, InvalidParameterException, PrivilegeException {
        return getHandler(Handler.PASSWORD).changePassword(oldPassword, newPassword, newPassword2);
    }

    public Subject[] getSubjectsOfStudy(Study study) throws IOException {
        return gson.fromJson(HttpUtil.httpGet(schoolUrl.getApiUrl() + "personen/" + profile.id + "/aanmeldingen/" + study.id + "/vakken"), Subject[].class);
    }

    /**
     * Get a handler instance from this magister instance.
     *
     * @param type the class of the handler.
     * @param <T>  the handler class type.
     * @return the {@link IHandler} instance, null if it can't be found.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public <T extends IHandler> T getHandler(Class<T> type) throws PrivilegeException {
        for (IHandler handler : handlerList) {
            if (handler.getClass() == type) {
                if (!hasPrivilege(handler.getPrivilege())) {
                    throw new PrivilegeException();
                }
                return type.cast(handler);
            }
        }
        return null;
    }

    public <T extends IHandler> T getHandler(Handler<T> handler) {
        for (IHandler h : handlerList) {
            if (h.getClass() == handler.getHandler()) {
                if (!hasPrivilege(h.getPrivilege())) {
                    throw new PrivilegeException();
                }
                return handler.getHandler().cast(h);
            }
        }
        return null;
    }
}
