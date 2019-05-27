package ir.futurearts.esmfamil.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.victor.loading.newton.NewtonCradleLoading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ir.futurearts.esmfamil.Adapter.RankAdapter;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Interface.AddFriendInterface;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.Network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.Network.Responses.FriendsResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;
import ir.futurearts.esmfamil.Utils.DialogActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankActivity extends AppCompatActivity implements AddFriendInterface {

    private RecyclerView list;
    private List<UserM>data;
    private RankAdapter adapter;
    private NewtonCradleLoading progress;

    private final int SEND_CODE=1001;

    private String uid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        list=findViewById(R.id.rank_rv);
        progress=findViewById(R.id.rank_progress);
        data=new ArrayList<>();
        adapter=new RankAdapter(data,this,this);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        progress.start();

        Call<FriendsResponse> call= RetrofitClient
                .getInstance()
                .getUserApi()
                .getRank();

        call.enqueue(new Callback<FriendsResponse>() {
            @Override
            public void onResponse(Call<FriendsResponse> call, Response<FriendsResponse> response) {

                FriendsResponse fr= response.body();
                Log.d("MM",fr.getMessage());
                Log.d("MM",fr.getUsers().size()+"");
                for(UserM u: fr.getUsers()){
                    data.add(u);
                }
                progress.stop();
                progress.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                list.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<FriendsResponse> call, Throwable t) {
                progress.stop();
                progress.setVisibility(View.GONE);
                FancyToast.makeText(RankActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }


    @Override
    public void SendRequest(String id) {
        uid=id;
        Intent intent=new Intent(RankActivity.this, DialogActivity.class);
        intent.putExtra("type","success");
        intent.putExtra("title","سوال");
        intent.putExtra("text","مایل به اضافه کردن به لیست دوستان هستید؟");
        startActivityForResult(intent,SEND_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SEND_CODE){
            if(resultCode==RESULT_OK){
                final CustomProgress customProgress=new CustomProgress();
                customProgress.showProgress(RankActivity.this,false);

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

                                Intent intent=new Intent(RankActivity.this, DialogActivity.class);
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

                                Intent intent=new Intent(RankActivity.this, DialogActivity.class);
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
                        FancyToast.makeText(RankActivity.this, getString(R.string.systemError),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            }
        }
    }
}
