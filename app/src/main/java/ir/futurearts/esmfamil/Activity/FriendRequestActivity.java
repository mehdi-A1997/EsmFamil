package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import ir.futurearts.esmfamil.Adapter.FriendRequestAdapter;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.Network.Responses.FreindsResponse;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        list= findViewById(R.id.friendrequest_rv);

        data= new ArrayList<>();
        adapter= new FriendRequestAdapter(data, this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        final CustomProgress customProgress= new CustomProgress();
        customProgress.showProgress(this, false);

        Call<FreindsResponse> call= RetrofitClient.getInstance()
                .getApi().friendRequests(CurrentUser.getId());

        call.enqueue(new Callback<FreindsResponse>() {
            @Override
            public void onResponse(Call<FreindsResponse> call, Response<FreindsResponse> response) {
                FreindsResponse fr= response.body();

                for(UserM u:fr.getUsers()){
                    data.add(u);
                }
                customProgress.hideProgress();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<FreindsResponse> call, Throwable t) {
                customProgress.hideProgress();
                FancyToast.makeText(FriendRequestActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }
}
