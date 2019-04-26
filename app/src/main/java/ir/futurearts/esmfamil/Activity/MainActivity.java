package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;

public class MainActivity extends AppCompatActivity  {

    private ConstraintLayout newgamge;
    private ConstraintLayout rank;
    private ConstraintLayout addfriend;
    private ConstraintLayout setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newgamge=findViewById(R.id.main_newgame);
        rank=findViewById(R.id.main_rank);
        addfriend=findViewById(R.id.main_addfriend);
        setting=findViewById(R.id.main_setting);

        newgamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SelectUserActivity.class));
            }
        });

        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RankActivity.class));
            }
        });

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomProgress customProgress=new CustomProgress();
                customProgress.showProgress(MainActivity.this,false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        customProgress.hideProgress();
                    }
                },5000);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        gotoLogin();
                    }
                });
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
        } else {
           gotoLogin();
        }
    }

    public void gotoLogin(){
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
