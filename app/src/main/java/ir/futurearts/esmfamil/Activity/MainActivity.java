package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.R;

public class MainActivity extends AppCompatActivity  {

    private ConstraintLayout newgamge;
    private ConstraintLayout rank;
    private ConstraintLayout addfriend;
    private ConstraintLayout setting;
    private TextView coin,score;
    private CircleImageView uimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newgamge=findViewById(R.id.main_newgame);
        rank=findViewById(R.id.main_rank);
        addfriend=findViewById(R.id.main_addfriend);
        setting=findViewById(R.id.main_setting);
        coin=findViewById(R.id.coin_text);
        score=findViewById(R.id.main_uscore);
        uimg=findViewById(R.id.main_uimg);

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
                startActivity(new Intent(MainActivity.this,FriendsActivity.class));
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CurrentUser.Logout();
               gotoLogin();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        if(!CurrentUser.isLogin())
            gotoLogin();
    }

    public void gotoLogin(){
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
