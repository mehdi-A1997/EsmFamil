package ir.futurearts.esmfamil.Constant;

import android.content.Context;
import android.content.SharedPreferences;

import ir.futurearts.esmfamil.Module.UserM;

public  class CurrentUser {
    private static String id, name, email, username, img, online, score, coin, token="";
    private static boolean login= false;


    public CurrentUser() {

    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        CurrentUser.id = id;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        CurrentUser.name = name;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        CurrentUser.email = email;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        CurrentUser.username = username;
    }

    public static String getImg() {
        return img;
    }

    public static void setImg(String img) {
        CurrentUser.img = img;
    }

    public static String getOnline() {
        return online;
    }

    public static void setOnline(String online) {
        CurrentUser.online = online;
    }

    public static String getScore() {
        return score;
    }

    public static void setScore(String score) {
        CurrentUser.score = score;
    }

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean login) {
        CurrentUser.login = login;
    }

    public static String getCoin() {
        return coin;
    }

    public static void setCoin(String coin) {
        CurrentUser.coin = coin;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        CurrentUser.token = token;
    }

}
