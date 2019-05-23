package ir.futurearts.esmfamil.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;

import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.Network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.Network.Responses.LoginResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;
import ir.futurearts.esmfamil.Utils.DialogActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserActivity extends AppCompatActivity {

    private EditText username;
    private Button searchbtn;

    private static final int SEND_CODE= 1001;
    private String uid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        username= findViewById(R.id.search_user_txt);
        searchbtn= findViewById(R.id.search_user_btn);


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().length()<3) {
                    username.setError(getString(R.string.usernameError));
                    return;
                }
                if(username.equals(CurrentUser.getUsername())){
                    username.setError("نام کاربری خود را وارد کردید :|");
                    return;
                }

                final CustomProgress customProgress= new CustomProgress();
                customProgress.showProgress(SearchUserActivity.this, false);

                Call<LoginResponse> call= RetrofitClient.getInstance()
                        .getUserApi().searchUser(username.getText().toString());

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        LoginResponse lr= response.body();
                        customProgress.hideProgress();
                        if(lr.getUser().getUsername()!= null){
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

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
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
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        customProgress.hideProgress();
                        if(response.code() == 200) {
                            try {
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
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        customProgress.hideProgress();
                        FancyToast.makeText(SearchUserActivity.this, getString(R.string.systemError),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            }
        }
    }
}
