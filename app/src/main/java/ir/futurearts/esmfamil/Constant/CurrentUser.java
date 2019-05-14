package ir.futurearts.esmfamil.Constant;

import android.content.Context;
import android.content.SharedPreferences;

import ir.futurearts.esmfamil.Module.UserM;

public  class CurrentUser {
    private static String id, name, email, username, img, online, score;
    private static boolean login= false;

    private static SharedPreferences mPref;
    private static SharedPreferences.Editor editor;

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

    public static void LoadUser(Context context){
        mPref= context.getSharedPreferences("user",Context.MODE_PRIVATE);
        editor= mPref.edit();
        if(!mPref.getBoolean("login", false)){
            login= false;
            return;
        }

        login= true;

        id= mPref.getString("id", "-1");
        name= mPref.getString("name", "-");
        username= mPref.getString("username", "-");
        email= mPref.getString("email", "-");
        img= mPref.getString("img", "default");
        online= mPref.getString("online", "1");
        score= mPref.getString("score", "0");
    }

    public static void SaveUser(UserM u){
        editor.putString("name", u.getName());
        editor.putString("username", u.getUsername());
        editor.putString("id", u.getId());
        editor.putString("email", u.getEmail());
        editor.putString("online", "1");
        editor.putString("img", u.getImg());
        editor.putString("score", u.getScore());
        editor.putBoolean("login", true);

        editor.commit();
    }

    public static void Logout(){
        editor.clear();
        editor.commit();
    }
}
