package ir.futurearts.esmfamil.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.Activity.CompleteGameCreationActivity;
import ir.futurearts.esmfamil.Activity.FriendsActivity;
import ir.futurearts.esmfamil.Activity.GameRequestActivity;
import ir.futurearts.esmfamil.Activity.RankActivity;
import ir.futurearts.esmfamil.Activity.SelectFriendActivity;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Network.Responses.CreateGameResponse;
import ir.futurearts.esmfamil.Network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;
import ir.futurearts.esmfamil.Utils.DialogActivity;
import ir.futurearts.esmfamil.Utils.OptionsActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

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
    private ImageView requests;

    private final int NORMAL_CODE = 1001;
    private final int NORMALRANDOM_GAME= 1002;
    private final int COMPETE_CODE = 1003;


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
        requests= v.findViewById(R.id.main_requests);

        competgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OptionsActivity.class);
                String[] data = new String[]{"بازی تصادفی", "بازی با دوستان"};
                intent.putExtra("list", data);

                startActivityForResult(intent, COMPETE_CODE);
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
                Intent intent = new Intent(getContext(), OptionsActivity.class);
                String[] data = new String[]{"بازی تصادفی", "بازی با دوستان"};
                intent.putExtra("list", data);

                startActivityForResult(intent, NORMAL_CODE);

            }
        });

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), GameRequestActivity.class));
            }
        });

        getmyScore();
        return v;
    }
    private void getmyScore() {
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getUserApi().getScore(CurrentUser.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    try {
                        JSONObject object= new JSONObject(response.body().string());
                        coin.setText(object.getString("coin"));
                        score.setText(object.getString("score"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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

        if(requestCode == COMPETE_CODE){
            if(resultCode==RESULT_OK){
                int item=Integer.parseInt(data.getStringExtra("item"));
                switch (item){
                    case 0:
                        createRandomGameCompete();
                        break;

                    case 1:
                        selectFriendCompete();
                        break;
                }
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

        Call<CreateGameResponse> call= RetrofitClient.getInstance()
                .getGameApi().CreateRandomGame(CurrentUser.getId(), 0);

        call.enqueue(new Callback<CreateGameResponse>() {
            @Override
            public void onResponse(Call<CreateGameResponse> call, Response<CreateGameResponse> response) {
                customProgress.hideProgress();
                if(response.code() == 201){
                    CreateGameResponse cgr= response.body();
                    if(cgr.getGame().getLetter().equals("")){
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
                        DefaultResponse df= new DefaultResponse(response.errorBody().string());
                        FancyToast.makeText(getContext(), df.getMessage(),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateGameResponse> call, Throwable t) {
                customProgress.hideProgress();
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(getContext(), getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }


    private void selectFriendCompete() {
        Intent intent= new Intent(getContext(), SelectFriendActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    private void createRandomGameCompete() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");

        // The query will search for a ParseObject, given its objectId.
        // When the query finishes running, it will invoke the GetCallback
        // with either the object, or the exception thrown
        query.getInBackground("lKsxj89agL", new GetCallback<ParseObject>() {
            public void done(ParseObject result, ParseException e) {
                if (e == null) {
                    Toast.makeText(getContext(), result.getString("Status"), Toast.LENGTH_SHORT).show();
                } else {
                    // something went wrong
                    Log.d("MM", e.getMessage());
                }
            }
        });
    }


}
