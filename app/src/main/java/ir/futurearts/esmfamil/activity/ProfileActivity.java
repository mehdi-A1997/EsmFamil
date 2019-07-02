package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CustomProgress;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView img;
    private TextView img_title;
    private EditText username, name;
    private Button btnsave, btnpass;

    private String img_name;

    private final int IMAGE_CODE= 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_profile);

        setupTransition();
        img= findViewById(R.id.profile_img);
        img_title= findViewById(R.id.profile_label);
        username= findViewById(R.id.profile_username);
        name= findViewById(R.id.profile_name);
        btnsave= findViewById(R.id.profile_save);
        btnpass= findViewById(R.id.profile_changepass);

        username.setText(CurrentUser.getUsername());
        name.setText(CurrentUser.getName());
        if(CurrentUser.getImg().startsWith("user")){
            img.setImageDrawable(getDrawableByName(CurrentUser.getImg()));
            img_name= CurrentUser.getImg();
        }
        else{
            img.setImageDrawable(getDrawableByName("user_1"));
            img_name= "user_1";
        }

        setonClicks();

    }

    private void setonClicks() {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });

        img_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        btnpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
            }
        });
    }

    private void saveChanges() {
        if(username.getText().toString().length()<3){
            username.setError("نام کاربری نباید کمتر از 3 کاراکتر باشد.");
            return;
        }
        if(name.getText().toString().length()<3){
            name.setError("نام نمیتواند بدون مقدار باشد");
            return;
        }
        final CustomProgress progress= new CustomProgress();
        progress.showProgress(this, false);
        int changed =0;

        if(!username.getText().toString().trim().equals(CurrentUser.getUsername())){
            changed= 1;
        }
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getUserApi().updateUser(CurrentUser.getId(), name.getText().toString().trim(),
                        username.getText().toString().trim(), img_name, changed);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.code() == 200){
                    CurrentUser.setImg(img_name);
                    CurrentUser.setUsername(username.getText().toString());
                    CurrentUser.setName(name.getText().toString().trim());

                    SharedPreferences.Editor editor= getSharedPreferences("user", MODE_PRIVATE).edit();

                    editor.putString("name", name.getText().toString().trim());
                    editor.putString("username", username.getText().toString().trim());
                    editor.putString("img", img_name);

                    editor.apply();

                    Intent intent= new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                    FancyToast.makeText(ProfileActivity.this,  "تغییرات انجام شد", FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS, false).show();
                }
                else if(response.code() == 409){
                    username.setError("نام کاربری رزرو شده است.");
                }
                else {
                    FancyToast.makeText(ProfileActivity.this, getString(R.string.systemError),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
                progress.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                FancyToast.makeText(ProfileActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                progress.hideProgress();
            }
        });
    }

    private void changeImage() {
        Intent intent= new Intent(this, ChangeImageActivity.class);
        startActivityForResult(intent, IMAGE_CODE);
    }

    private Drawable getDrawableByName(String name){
        Resources resources = getResources();
        final int resourceId = resources.getIdentifier(name, "drawable", getPackageName());
        return ContextCompat.getDrawable(this,resourceId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_CODE){
            if(resultCode == RESULT_OK){
                assert data != null;
                String image = data.getStringExtra("img");
                img.setImageDrawable(getDrawableByName(image));
                img_name= image;
            }
        }
    }

    private void setupTransition() {
        Slide explode=new Slide();
        explode.setSlideEdge(Gravity.BOTTOM);
        explode.setDuration(800);
        getWindow().setEnterTransition(explode);
        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.main_bg));
    }
}
