package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.victor.loading.newton.NewtonCradleLoading;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import ir.futurearts.esmfamil.adapter.UserAdapter;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.interfaces.UserInterface;
import ir.futurearts.esmfamil.module.UserM;
import ir.futurearts.esmfamil.network.Responses.FriendsResponse;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsActivity extends AppCompatActivity implements UserInterface {

    private RecyclerView list;
    private List<UserM> data;
    private UserAdapter adapter;
    private NewtonCradleLoading progress;
    private LinearLayout empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_friends);

        setupTransition();
        list = findViewById(R.id.frineds_rv);
        ConstraintLayout searchbtn = findViewById(R.id.friends_add);
        ConstraintLayout requestbtn = findViewById(R.id.friends_request);
        progress= findViewById(R.id.friends_progress);
        empty= findViewById(R.id.friends_empty);

        progress.start();
        data = new ArrayList<>();
        adapter = new UserAdapter(data, this,this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

         //TODO GET FRIENDS
        Call<FriendsResponse> call= RetrofitClient
                .getInstance()
                .getUserApi()
                .getFriends(CurrentUser.getId());

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
                list.setVisibility(View.VISIBLE);

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
                FancyToast.makeText(FriendsActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                if(data.size() == 0){
                    empty.setVisibility(View.VISIBLE);
                }
                else {
                    empty.setVisibility(View.GONE);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data.sort(new Comparator<UserM>() {
                @Override
                public int compare(UserM o1, UserM o2) {
                    return o1.getOnline()<o2.getOnline()?-1:0;
                }
            });
        }
        adapter.notifyDataSetChanged();

        requestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendsActivity.this, FriendRequestActivity.class));
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendsActivity.this, SearchUserActivity.class));
            }
        });
    }

    @Override
    public void userSelected(UserM u) {
        //TODO DELETE USER
        Toast.makeText(this, "DELETE USER", Toast.LENGTH_SHORT).show();
    }

    private void setupTransition() {
        Slide explode=new Slide();
        explode.setSlideEdge(Gravity.BOTTOM);
        explode.setDuration(800);
        getWindow().setEnterTransition(explode);
        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.main_bg));
    }
}
