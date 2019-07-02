package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.futurearts.esmfamil.adapter.UserAdapter;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.interfaces.UserInterface;
import ir.futurearts.esmfamil.module.UserM;
import ir.futurearts.esmfamil.network.Responses.FriendsResponse;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.DialogActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectFriendActivity extends AppCompatActivity implements UserInterface {

    private List<UserM> data;
    private UserAdapter adapter;
    private NewtonCradleLoading progress;
    private LinearLayout empty;

    private int type=0;

    private final int CREATENORMAL_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_select_friend);

        RecyclerView list = findViewById(R.id.select_user_rv);
        progress= findViewById(R.id.select_user_proress);
        empty= findViewById(R.id.selectfriend_empty);

        data = new ArrayList<>();
        adapter = new UserAdapter(data, this,this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        type= getIntent().getIntExtra("type", 0);

        Call<FriendsResponse> call;
        if(type == 0){
            call = RetrofitClient.getInstance()
                    .getUserApi().getFriends(CurrentUser.getId());
        }
        else {
            call = RetrofitClient.getInstance()
                    .getUserApi().getOnlineFriends(CurrentUser.getId());
        }
        progress.start();
        call.enqueue(new Callback<FriendsResponse>() {
            @Override
            public void onResponse(@NonNull Call<FriendsResponse> call, @NonNull Response<FriendsResponse> response) {
                if(response.code()==200) {
                    FriendsResponse fr = response.body();

                    assert fr != null;
                    data.addAll(fr.getUsers());
                }
                progress.stop();
                progress.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                if(data.size() == 0){
                    empty.setVisibility(View.VISIBLE);
                }
                else {
                    empty.setVisibility(View.GONE);
                }
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
    }

    @Override
    public void userSelected(UserM u) {

        if(type == 0) {
            Intent intent = new Intent(this, CompleteGameCreationActivity.class);
            intent.putExtra("oid", u.getId());
            intent.putExtra("type", type);
            startActivityForResult(intent, CREATENORMAL_CODE);
        }
        else {
            Intent intent = new Intent(this, CompleteGameCreationActivity.class);
            intent.putExtra("oid", u.getId());
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int RESULT_CODE = 1002;
        if(requestCode == CREATENORMAL_CODE){
            if(resultCode == RESULT_OK){
                Intent intent =new Intent(SelectFriendActivity.this, DialogActivity.class);
                intent.putExtra("title", "پیام");
                intent.putExtra("type", "singleS");
                intent.putExtra("text", "درخواست بازی ارسال شد");

                startActivityForResult(intent, RESULT_CODE);
            }
        }

        if(requestCode == RESULT_CODE){
            if(resultCode == RESULT_OK){
                finish();
            }
        }

    }
}
