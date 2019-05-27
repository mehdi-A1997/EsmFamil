package ir.futurearts.esmfamil.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
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
        setContentView(R.layout.activity_profile);

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
        int changed =0;
        if(!username.getText().toString().equals(CurrentUser.getUsername())){
            changed= 1;
        }
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getUserApi().updateUser(CurrentUser.getId(), name.getText().toString().trim(),
                        username.getText().toString().trim(), img_name, changed);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    CurrentUser.setImg(img_name);
                    CurrentUser.setUsername(username.getText().toString());
                    CurrentUser.setName(name.getText().toString());

                    SharedPreferences.Editor editor= getSharedPreferences("user", MODE_PRIVATE).edit();

                    editor.putString("name", name.getText().toString());
                    editor.putString("username", username.getText().toString());
                    editor.putString("img", img_name);

                    editor.commit();

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
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FancyToast.makeText(ProfileActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
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
                String image = data.getStringExtra("img");
                img.setImageDrawable(getDrawableByName(image));
                img_name= image;
            }
        }
    }
}
