package ir.futurearts.esmfamil.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ir.futurearts.esmfamil.Adapter.UserAdapter;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Interface.UserInterface;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.Network.Responses.FriendsResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.DialogActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectFriendActivity extends AppCompatActivity implements UserInterface {

    private RecyclerView list;
    private List<UserM> data;
    private UserAdapter adapter;
    private NewtonCradleLoading progress;
    private LinearLayout empty;

    private Call<FriendsResponse> call;

    private int type=0;

    private final int CREATENORMAL_CODE = 1001;
    private final int RESULT_CODE= 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);

        list = findViewById(R.id.select_user_rv);
        progress= findViewById(R.id.select_user_proress);
        empty= findViewById(R.id.selectfriend_empty);

        data = new ArrayList<>();
        adapter = new UserAdapter(data, this,this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        type= getIntent().getIntExtra("type", 0);

        if(type == 0){
            call= RetrofitClient.getInstance()
                    .getUserApi().getFriends(CurrentUser.getId());
        }
        else {
            call= RetrofitClient.getInstance()
                    .getUserApi().getOnlineFriends(CurrentUser.getId());
        }
        progress.start();
        call.enqueue(new Callback<FriendsResponse>() {
            @Override
            public void onResponse(Call<FriendsResponse> call, Response<FriendsResponse> response) {
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
            public void onFailure(Call<FriendsResponse> call, Throwable t) {
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
