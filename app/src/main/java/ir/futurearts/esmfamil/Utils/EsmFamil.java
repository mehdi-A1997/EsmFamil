package ir.futurearts.esmfamil.Utils;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseUser;

public class EsmFamil extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("6NLO51kRttd6ZLazamem57BFdhCuk7i6O7DBWM7A")
                .clientKey("qJ7fjTJWmlujYt3DZPMvKNwgmmfUzNsisGg4XlWf")
                .server("https://parseapi.back4app.com")
                .build()
        );
        Foreground.init(this);
        ParseUser user=ParseUser.getCurrentUser();
        if(user!=null){
        user.put("online",1);
        user.saveInBackground();
        }
    }


}
