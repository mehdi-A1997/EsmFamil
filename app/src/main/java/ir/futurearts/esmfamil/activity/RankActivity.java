package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.victor.loading.newton.NewtonCradleLoading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.ads.AdRequest.ERROR_CODE_INTERNAL_ERROR;
import static com.google.android.gms.ads.AdRequest.ERROR_CODE_INVALID_REQUEST;
import static com.google.android.gms.ads.AdRequest.ERROR_CODE_NETWORK_ERROR;
import static com.google.android.gms.ads.AdRequest.ERROR_CODE_NO_FILL;

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

        setupTransition();
        setContentView(R.layout.activity_rank);
        list= findViewById(R.id.rank_rv);
        progress= findViewById(R.id.rank_progress);
        empty= findViewById(R.id.rank_empty);
        AdView mAdView = findViewById(R.id.rankadView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("MM", "LOADED");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d("MM", "Faild to Load:"+ errorCode);
                switch (errorCode) {
                    case ERROR_CODE_INTERNAL_ERROR:
                        Log.d("MM", "Something happened internally; for instance, an invalid response was received from the ad server. ");
                        break;

                    case ERROR_CODE_INVALID_REQUEST:
                        Log.d("MM", "The ad request was invalid; for instance, the ad unit ID was incorrect. ");
                        break;

                    case ERROR_CODE_NETWORK_ERROR:
                        Log.d("MM", "The ad request was unsuccessful due to network connectivity. ");
                        break;

                    case ERROR_CODE_NO_FILL:
                        Log.d("MM", "The ad request was successful, but no ad was returned due to lack of ad inventory.");
                    default:
                        Log.d("MM", "Faild to Load:"+ errorCode);
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d("MM", "opened");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d("MM", "clicked");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d("MM", "left");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d("MM", "closed");
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
