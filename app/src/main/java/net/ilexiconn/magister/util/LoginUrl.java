package net.ilexiconn.magister.util;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//
// Created by Under_Koen on 04/02/2019.
//
public class LoginUrl {
    public static String getMainUrl() {
        return "https://accounts.magister.net/";
    }

    public static String getConnectUrl() {
        return getMainUrl() + "connect/";
    }

    public static String getAuthorizeUrl() {
        return getConnectUrl() + "authorize";
    }

    public static String getChallengeUrl() {
        return getMainUrl() + "challenge/";
    }

    public static String getCurrentUrl() {
        return getChallengeUrl() + "current/";
    }

    public static String getUsernameUrl() {
        return getChallengeUrl() + "username/";
    }

    public static String getPasswordUrl() {
        return getChallengeUrl() + "password/";
    }

    public static String getAuthCodeUrl() {
        //Currently magister is still a little confused how to implement a auth code so they change it so and then instead to rewrite it to for every application I made a public http get request for it.
        return "https://macister.nl/authcode.php";
    }

    public static class AuthCode implements Serializable {
        @SerializedName("authCode")
        public String authCode = "";
    }

    public static class ChallengeData implements Serializable {
        @SerializedName("authCode")
        public String authCode = "";

        @SerializedName("returnUrl")
        public String returnUrl = "";

        @SerializedName("sessionId")
        public String sessionId = "";

        @SerializedName("username")
        public String username = "";

        @SerializedName("password")
        public String password = "";

        public ChallengeData(String authCode, String returnUrl, String sessionId) {
            this.authCode = authCode;
            this.returnUrl = returnUrl;
            this.sessionId = sessionId;
        }

        public void setUsername(String username) {
            this.username = username;
            password = "";
        }

        public void setPassword(String password) {
            this.password = password;
            username = "";
        }
    }

    public static class Error implements Serializable {
        @SerializedName("error")
        public String error = "";
    }
}
