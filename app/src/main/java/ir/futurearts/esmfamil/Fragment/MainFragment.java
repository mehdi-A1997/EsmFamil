package ir.futurearts.esmfamil.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.Activity.FriendsActivity;
import ir.futurearts.esmfamil.Activity.MainActivity;
import ir.futurearts.esmfamil.Activity.RankActivity;
import ir.futurearts.esmfamil.Activity.SelectUserActivity;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private ConstraintLayout competgame;
    private ConstraintLayout rank;
    private ConstraintLayout addfriend;
    private ConstraintLayout normalgame;
    private TextView coin,score;
    private CircleImageView uimg;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_main, container, false);

        competgame= v.findViewById(R.id.main_startcompet);
        rank= v.findViewById(R.id.main_rank);
        addfriend= v.findViewById(R.id.main_addfriend);
        normalgame= v.findViewById(R.id.main_startnormal);
        coin= v.findViewById(R.id.coin_text);
        score= v.findViewById(R.id.main_uscore);
        uimg= v.findViewById(R.id.main_uimg);

        competgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelectUserActivity.class));
            }
        });

        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RankActivity.class));
            }
        });

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FriendsActivity.class));
            }
        });

        normalgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    JSONObject object= new JSONObject();
                    object.put("name", "mehdi");
                    object.put("lastname", "allahyari");
                    Call<ResponseBody> call= RetrofitClient.getInstance()
                            .getGameApi().test(1,2,0,object);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                Log.d("MM", response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return v;
    }

}
