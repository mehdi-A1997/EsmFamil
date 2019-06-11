package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;

import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.module.UserM;
import ir.futurearts.esmfamil.network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.network.Responses.LoginResponse;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CustomProgress;
import ir.futurearts.esmfamil.utils.DialogActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserActivity extends AppCompatActivity {

    private EditText username;

    private static final int SEND_CODE= 1001;
    private String uid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        username= findViewById(R.id.search_user_txt);
        Button searchbtn = findViewById(R.id.search_user_btn);


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().length()<3) {
                    username.setError(getString(R.string.usernameError));
                    return;
                }
                if(username.getText().toString().trim().equals(CurrentUser.getUsername().trim())){
                    username.setError("نام کاربری خود را وارد کردید :|");
                    return;
                }

                final CustomProgress customProgress= new CustomProgress();
                customProgress.showProgress(SearchUserActivity.this, false);

                Call<LoginResponse> call= RetrofitClient.getInstance()
                        .getUserApi().searchUser(username.getText().toString());

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call,@NonNull Response<LoginResponse> response) {
                        LoginResponse lr= response.body();
                        customProgress.hideProgress();
                        try {
                            if(lr.getUser() != null && lr.getUser().getUsername().equals(username.getText().toString().trim())){
                                SendRequest(lr.getUser());
                            }
                            else {
                                Intent intent=new Intent(SearchUserActivity.this, DialogActivity.class);
                                intent.putExtra("type","singleE");
                                intent.putExtra("title","پیام");
                                intent.putExtra("text","کاربر یافت نشد");
                                startActivity(intent);
                            }
                        }
                        catch (Exception ignored){
                            Intent intent=new Intent(SearchUserActivity.this, DialogActivity.class);
                            intent.putExtra("type","singleE");
                            intent.putExtra("title","پیام");
                            intent.putExtra("text","کاربر یافت نشد");
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        customProgress.hideProgress();
                        FancyToast.makeText(SearchUserActivity.this, getString(R.string.systemError),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });

            }
        });
    }

    public void SendRequest(UserM u) {
        uid=u.getId();
        Intent intent=new Intent(SearchUserActivity.this, DialogActivity.class);
        intent.putExtra("type","success");
        intent.putExtra("title","سوال");
        intent.putExtra("text","مایل به ارسال درخواست به "+u.getName()+" هستید؟");
        startActivityForResult(intent,SEND_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SEND_CODE){
            if(resultCode==RESULT_OK){
                final CustomProgress customProgress=new CustomProgress();
                customProgress.showProgress(SearchUserActivity.this,false);

                Call<ResponseBody> call= RetrofitClient
                        .getInstance()
                        .getUserApi()
                        .sendFriendRequest(CurrentUser.getId(), uid);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        customProgress.hideProgress();
                        if(response.code() == 200) {
                            try {
                                assert response.body() != null;
                                DefaultResponse dr = new DefaultResponse(response.body().string());

                                Intent intent=new Intent(SearchUserActivity.this, DialogActivity.class);
                                intent.putExtra("type","singleS");
                                intent.putExtra("title","پیام");
                                intent.putExtra("text",dr.getMessage());
                                startActivity(intent);

                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else{
                            try {
                                assert response.body() != null;
                                DefaultResponse dr = new DefaultResponse(response.body().string());

                                Intent intent=new Intent(SearchUserActivity.this, DialogActivity.class);
                                intent.putExtra("type","singleE");
                                intent.putExtra("title","پیام");
                                intent.putExtra("text",dr.getMessage());
                                startActivity(intent);

                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        customProgress.hideProgress();
                        FancyToast.makeText(SearchUserActivity.this, getString(R.string.systemError),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            }
        }
    }
}
