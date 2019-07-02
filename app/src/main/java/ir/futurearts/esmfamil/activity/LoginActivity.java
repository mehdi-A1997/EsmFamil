package ir.futurearts.esmfamil.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.Locale;

import ir.futurearts.esmfamil.fragment.singing.LoginFragment;
import ir.futurearts.esmfamil.fragment.singing.SignUpFragment;
import ir.futurearts.esmfamil.interfaces.LoginInterface;
import ir.futurearts.esmfamil.R;

public class LoginActivity extends AppCompatActivity implements LoginInterface {

    int p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_login);

        changeFragment(1);
    }

    void changeFragment(int pos){
        p=pos;
        if(pos==1){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            LoginFragment loginFragment=new LoginFragment() ;
            ft.replace(R.id.login_frame,loginFragment);
            ft.commit();
        }
        if(pos==2){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            SignUpFragment signupFragment=new SignUpFragment();
            ft.replace(R.id.login_frame,signupFragment);
            ft.commit();
        }
    }

    @Override
    public void Change(int p) {
        changeFragment(p);
    }

    @Override
    public void onBackPressed() {
        if(p==1)
            finish();
        else {
            changeFragment(1);
        }
    }

    @Override
    public void Finish(){
        finish();
    }
}
