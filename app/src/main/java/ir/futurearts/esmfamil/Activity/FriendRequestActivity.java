package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.List;

import ir.futurearts.esmfamil.Adapter.FriendRequestAdapter;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.Network.Responses.FriendsResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestActivity extends AppCompatActivity {

    private RecyclerView list;
    private List<UserM> data;
    private FriendRequestAdapter adapter;
    private LinearLayout empty;
    private NewtonCradleLoading progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        list= findViewById(R.id.friendrequest_rv);
        empty= findViewById(R.id.friendsrequest_empty);
        progress= findViewById(R.id.friendrequest_progress);

        data= new ArrayList<>();
        adapter= new FriendRequestAdapter(data, this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        progress.start();

        Call<FriendsResponse> call= RetrofitClient.getInstance()
                .getUserApi().friendRequests(CurrentUser.getId());

        call.enqueue(new Callback<FriendsResponse>() {
            @Override
            public void onResponse(Call<FriendsResponse> call, Response<FriendsResponse> response) {
                FriendsResponse fr= response.body();
                try {
                    assert fr != null;
                    data.addAll(fr.getUsers());
                    progress.stop();
                    progress.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
                catch (Exception ignored){
                    progress.stop();
                    progress.setVisibility(View.GONE);
                }
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
}
