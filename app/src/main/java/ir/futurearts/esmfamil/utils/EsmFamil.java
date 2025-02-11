package ir.futurearts.esmfamil.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

import io.fabric.sdk.android.Fabric;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.tapsell.sdk.Tapsell;

public class EsmFamil extends Application {
    private SharedPreferences.Editor editor;
    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
        super.onCreate();
        Tapsell.initialize(this, "cfektnihpncqeigiqmqjihqoaeoqabqnsqdmcohftmqoqgkrmtaacgrpcolafrjeqlehil");
        FirebaseApp.initializeApp(this);
        Fabric.with(this, new Crashlytics());
        SharedPreferences mPref = getSharedPreferences("user", MODE_PRIVATE);
        editor= mPref.edit();

        String id= mPref.getString("id","-1");

        assert id != null;
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
            Foreground.init(this);
        }
    }




}
