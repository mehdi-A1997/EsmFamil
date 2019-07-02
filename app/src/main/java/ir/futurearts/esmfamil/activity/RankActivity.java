package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.victor.loading.newton.NewtonCradleLoading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.futurearts.esmfamil.adapter.RankAdapter;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.interfaces.AddFriendInterface;
import ir.futurearts.esmfamil.module.UserM;
import ir.futurearts.esmfamil.network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.network.Responses.FriendsResponse;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CustomProgress;
import ir.futurearts.esmfamil.utils.DialogActivity;
import ir.tapsell.sdk.bannerads.TapsellBannerType;
import ir.tapsell.sdk.bannerads.TapsellBannerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankActivity extends AppCompatActivity implements AddFriendInterface {

    private RecyclerView list;
    private List<UserM>data;
    private RankAdapter adapter;
    private NewtonCradleLoading progress;
    private LinearLayout empty;

    private final int SEND_CODE=1001;

    private String uid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setupTransition();
        setContentView(R.layout.activity_rank);
        list= findViewById(R.id.rank_rv);
        progress= findViewById(R.id.rank_progress);
        empty= findViewById(R.id.rank_empty);
        data=new ArrayList<>();
        adapter=new RankAdapter(data,this,this);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        progress.start();

        final Call<FriendsResponse> call= RetrofitClient
                .getInstance()
                .getUserApi()
                .getRank();

        call.enqueue(new Callback<FriendsResponse>() {
            @Override
            public void onResponse(@NonNull Call<FriendsResponse> call, @NonNull Response<FriendsResponse> response) {

                FriendsResponse fr= response.body();
                try {
                    assert fr != null;
                    data.addAll(fr.getUsers());
                    progress.stop();
                    progress.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    list.setVisibility(View.VISIBLE);
                }
                catch (Exception ignored){

                }

                if(data.size() == 0){
                    empty.setVisibility(View.VISIBLE);
                }
                else {
                    empty.setVisibility(View.GONE);
                }
                progress.stop();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<FriendsResponse> call, @NonNull Throwable t) {
                progress.stop();
                progress.setVisibility(View.GONE);
                if(data.size() == 0){
                    empty.setVisibility(View.VISIBLE);
                }
                else {
                    empty.setVisibility(View.GONE);
                }
            }
        });

        TapsellBannerView banner1 = findViewById(R.id.banner1);

        banner1.loadAd(this, "5d14ef92d4d0e90001a00e2d", TapsellBannerType.BANNER_320x50);
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
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        customProgress.hideProgress();
                        if(response.code() == 200) {
                            try {
                                assert response.body() != null;
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
                                assert response.body() != null;
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
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        customProgress.hideProgress();
                        FancyToast.makeText(RankActivity.this, getString(R.string.systemError),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
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
