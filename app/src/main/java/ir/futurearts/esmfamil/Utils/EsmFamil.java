package ir.futurearts.esmfamil.Utils;

import android.app.Application;
import android.content.SharedPreferences;

import ir.futurearts.esmfamil.Constant.CurrentUser;

public class EsmFamil extends Application {
    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;
    @Override
    public void onCreate() {
        super.onCreate();

        mPref=getSharedPreferences("user",MODE_PRIVATE);
        editor=mPref.edit();

        String id=mPref.getString("id","-1");

        if(id.equals("-1")){
            CurrentUser.setLogin(false);
        }
        else {
            CurrentUser.setLogin(true);
            CurrentUser.setId(mPref.getString("id", "-1"));
            CurrentUser.setName(mPref.getString("name", "-"));
            CurrentUser.setUsername(mPref.getString("username", "-"));
            CurrentUser.setEmail(mPref.getString("email", "-"));
            CurrentUser.setImg(mPref.getString("img", "-"));
            CurrentUser.setOnline(mPref.getString("online", "-"));
            CurrentUser.setScore(mPref.getString("score", "0"));
            CurrentUser.setCoin(mPref.getString("coin", "0"));
        }
    }


}
