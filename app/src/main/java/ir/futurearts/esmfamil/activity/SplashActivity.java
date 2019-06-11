package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONObject;

import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView img = findViewById(R.id.splash_img);
        LinearLayout footer = findViewById(R.id.splash_footer);

        Animation ai= AnimationUtils.loadAnimation(this, R.anim.splash_img);
        img.setAnimation(ai);

        Animation af= AnimationUtils.loadAnimation(this, R.anim.splash_footer);
        footer.setAnimation(af);

        SharedPreferences mpref = getSharedPreferences("user", MODE_PRIVATE);
        editor= mpref.edit();

        RetrofitClient.getInstance()
                .getUserApi().getToken(CurrentUser.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if(response.code() == 200){
                            try {
                                assert response.body() != null;
                                JSONObject object= new JSONObject(response.body().string());
                                editor.putString("token", object.getString("token"));
                                editor.apply();
                                finishAll();
                            } catch (Exception e) {
                                e.printStackTrace();
                                finishAll();
                            }
                        }
                        finishAll();

                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                           finishAll();
                    }
                });
    }

    void finishAll(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },1800);
    }
}
