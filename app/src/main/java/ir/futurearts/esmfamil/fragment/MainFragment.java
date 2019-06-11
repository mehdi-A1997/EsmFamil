package ir.futurearts.esmfamil.fragment;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.activity.CompleteGameCreationActivity;
import ir.futurearts.esmfamil.activity.FriendsActivity;
import ir.futurearts.esmfamil.activity.GameRequestActivity;
import ir.futurearts.esmfamil.activity.RankActivity;
import ir.futurearts.esmfamil.activity.SelectFriendActivity;
import ir.futurearts.esmfamil.activity.StoreActivity;
import ir.futurearts.esmfamil.activity.HelpActivity;
import ir.futurearts.esmfamil.activity.UserResultActivity;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.network.Responses.CreateGameResponse;
import ir.futurearts.esmfamil.network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CustomProgress;
import ir.futurearts.esmfamil.utils.DialogActivity;
import ir.futurearts.esmfamil.utils.OptionsActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private TextView coin,score;
    private CircleImageView uimg;
    private SharedPreferences.Editor userEditor;

    private final int NORMAL_CODE = 1001;
    private final int NORMALRANDOM_GAME = 1002;
    private final int COIN_CODE = 1003;


    public MainFragment() {
        // Required empty public constructor
    }


    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_main, container, false);

        ConstraintLayout competgame = v.findViewById(R.id.main_startcompet);
        ConstraintLayout rank = v.findViewById(R.id.main_rank);
        ConstraintLayout addfriend = v.findViewById(R.id.main_addfriend);
        ConstraintLayout normalgame = v.findViewById(R.id.main_startnormal);
        coin= v.findViewById(R.id.coin_text);
        score= v.findViewById(R.id.main_uscore);
        uimg= v.findViewById(R.id.main_uimg);
        ImageView requests = v.findViewById(R.id.main_requests);
        CircleImageView coin_ic = v.findViewById(R.id.coin_ic);
        ImageView utility = v.findViewById(R.id.main_help);

        SharedPreferences userPref = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        userEditor= userPref.edit();
        try {
            uimg.setImageDrawable(getDrawableByName(CurrentUser.getImg()));
        }
        catch (Exception ignored){

        }
        competgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFriendCompete();
            }
        });

        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RankActivity.class)
                ,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FriendsActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        normalgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OptionsActivity.class);
                String[] data = new String[]{"بازی تصادفی", "بازی با دوستان"};
                intent.putExtra("list", data);

                startActivityForResult(intent, NORMAL_CODE,
                        ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

            }
        });

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), GameRequestActivity.class));
            }
        });

        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStore();
            }
        });

        coin_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStore();
            }
        });

        utility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HelpActivity.class));
            }
        });

        uimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserResult();
            }
        });

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserResult();
            }
        });
        getmyScore();

        SharedPreferences firstPref = getActivity().getSharedPreferences("first", Context.MODE_PRIVATE);
        SharedPreferences.Editor firstEditor= firstPref.edit();
        if(firstPref.getBoolean("main", true)){
            Intent intent= new Intent(getContext(), HelpActivity.class);
            intent.putExtra("type", 0);
            startActivity(intent);

            firstEditor.putBoolean("main", false);
            firstEditor.apply();
        }


        return v;
    }

    private void openUserResult() {
        Intent intent= new Intent(getContext(), UserResultActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()),
                        uimg,
                        Objects.requireNonNull(ViewCompat.getTransitionName(uimg)));
        startActivity(intent, options.toBundle());
    }

    private void openStore() {
        startActivityForResult(new Intent(getContext(), StoreActivity.class), COIN_CODE);
    }

    private void getmyScore() {
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getUserApi().getScore(CurrentUser.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.code() == 200){
                    try {
                        assert response.body() != null;
                        JSONObject object= new JSONObject(response.body().string());
                        coin.setText(object.getString("coin"));
                        score.setText(object.getString("score"));

                        userEditor.putString("coin", object.getString("coin"));
                        userEditor.putString("score", object.getString("score"));
                        CurrentUser.setCoin(object.getString("coin"));
                        CurrentUser.setScore(object.getString("score"));
                        userEditor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NORMAL_CODE){
            if(resultCode==RESULT_OK){
                int item=Integer.parseInt(data.getStringExtra("item"));
                switch (item){
                    case 0:
                        createRandomGameNormal();
                        break;

                    case 1:
                        selectFriendNormal();
                        break;
                }
            }
        }

        if(requestCode == NORMALRANDOM_GAME){
            if(resultCode == RESULT_OK){
                Intent intent =new Intent(getContext(), DialogActivity.class);
                intent.putExtra("title", "پیام");
                intent.putExtra("type", "singleS");
                intent.putExtra("text", "بازی ایجاد شد. در انتظار حریف تصادفی...");

                startActivity(intent);
            }
        }

        if(requestCode == COIN_CODE){
            if(resultCode == RESULT_OK){
                getmyScore();
            }
        }

    }


    private void selectFriendNormal() {
        Intent intent= new Intent(getContext(), SelectFriendActivity.class);
        intent.putExtra("type", 0);
        startActivity(intent);
    }

    private void createRandomGameNormal() {
        final CustomProgress customProgress= new CustomProgress();
        customProgress.showProgress(getContext(), false);
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Call<CreateGameResponse> call= RetrofitClient.getInstance()
                .getGameApi().CreateRandomGame(CurrentUser.getId(), 0, timeStamp);

        call.enqueue(new Callback<CreateGameResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateGameResponse> call, @NonNull Response<CreateGameResponse> response) {
                customProgress.hideProgress();
                if(response.code() == 201){
                    CreateGameResponse cgr= response.body();
                    assert cgr != null;
                    if(cgr.getGame().getLetter().equals("N")){
                        Intent intent= new Intent(getContext(), CompleteGameCreationActivity.class);
                        intent.putExtra("type", 2);
                        intent.putExtra("id", cgr.getGame().getId());
                        startActivityForResult(intent, NORMALRANDOM_GAME);
                    }
                    else {
                        Intent intent =new Intent(getContext(), DialogActivity.class);
                        intent.putExtra("title", "پیام");
                        intent.putExtra("type", "singleS");
                        intent.putExtra("text", "حریف تصادقی در مقابل شما قرار گرفت. به بخش بازی مراجعه کنید.");
                        startActivity(intent);
                    }
                }
                else {
                    try {
                        assert response.errorBody() != null;
                        DefaultResponse df= new DefaultResponse(response.errorBody().string());
                        FancyToast.makeText(getContext(), df.getMessage(),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateGameResponse> call, @NonNull Throwable t) {
                customProgress.hideProgress();
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(getContext(), getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }


    private void selectFriendCompete() {
        if(Integer.parseInt(CurrentUser.getCoin())<20) {
            Intent intent= new Intent(getContext(), DialogActivity.class);
            intent.putExtra("type", "singleE");
            intent.putExtra("title", "سکه کم است");
            intent.putExtra("text", "سکه شما کافی نیست، لطفا به فروشگاه مراجعه کنید");
            startActivity(intent);
            return;
        }
        Intent intent= new Intent(getContext(), SelectFriendActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    private Drawable getDrawableByName(String name){
        Resources resources = getResources();
        final int resourceId = resources.getIdentifier(name, "drawable", Objects.requireNonNull(getContext()).getPackageName());
        return ContextCompat.getDrawable(getContext(),resourceId);
    }

}
