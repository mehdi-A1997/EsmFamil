package ir.futurearts.esmfamil.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.shashank.sony.fancytoastlib.FancyToast;
import com.victor.loading.newton.NewtonCradleLoading;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

import ir.futurearts.esmfamil.activity.GameActivity;
import ir.futurearts.esmfamil.activity.GameResultActivity;
import ir.futurearts.esmfamil.activity.HelpActivity;
import ir.futurearts.esmfamil.adapter.GameAdapter;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.interfaces.GameInterface;
import ir.futurearts.esmfamil.module.GameM;
import ir.futurearts.esmfamil.network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.network.Responses.GamesResponse;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.DialogActivity;
import ir.tapsell.sdk.bannerads.TapsellBannerType;
import ir.tapsell.sdk.bannerads.TapsellBannerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements GameInterface {

    private GameAdapter adapter;
    private LinkedList<GameM> data;
    private LinearLayout empty;
    private NewtonCradleLoading progress;
    private Call<GamesResponse> call;

    private final int MANAGE_CODE= 1001;
    private final int PLAY_CODE= 1002;
    private int middlepos=0;
    //for manage game
    private int pos;
    private GameM game;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_result, container, false);
        RecyclerView list = v.findViewById(R.id.result_rv);
        empty= v.findViewById(R.id.result_empty);
        progress= v.findViewById(R.id.result_progress);


        data= new LinkedList<>();
        adapter= new GameAdapter(data, getContext(), this);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        loadGames();
        SharedPreferences firstPref = Objects.requireNonNull(getActivity()).getSharedPreferences("first", Context.MODE_PRIVATE);
        SharedPreferences.Editor firstEditor= firstPref.edit();
        if(firstPref.getBoolean("game", true)){
            Intent intent= new Intent(getContext(), HelpActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);

            firstEditor.putBoolean("game", false);
            firstEditor.apply();
        }

        TapsellBannerView banner1 = v.findViewById(R.id.banner2);

        banner1.loadAd(getContext(), "5d14ef92d4d0e90001a00e2d", TapsellBannerType.BANNER_320x50);
        return v;
    }

    private void loadGames() {
        middlepos=0;
        progress.start();
        progress.setVisibility(View.VISIBLE);
        data.clear();
        call = RetrofitClient.getInstance()
                .getGameApi().getMyGames(CurrentUser.getId());

        call.enqueue(new Callback<GamesResponse>() {
            @Override
            public void onResponse(@NonNull Call<GamesResponse> call, @NonNull Response<GamesResponse> response) {
                if(response.code() == 200){
                    assert response.body() != null;
                    LinkedList<GameM> dt= response.body().getGames();

                    for(GameM g: dt){
                        if(g.getStatus() == 1) {
                            if ((g.getUid() + "").equals(CurrentUser.getId()) && g.getUscore() == -1) {
                                data.add(middlepos,g);
                                middlepos++;
                            } else if ((g.getOid() + "").equals(CurrentUser.getId()) && g.getOscore() == -1) {
                                data.add(middlepos,g);
                                middlepos++;
                            }
                            else
                                data.addLast(g);

                        }
                        else  if(CurrentUser.getId().equals(g.getOid()+"") && g.getStatus() == 9){
                            data.add(middlepos,g);
                            middlepos++;
                        }
                        else{
                            data.addLast(g);
                        }
                    }

                    adapter.notifyDataSetChanged();
                }
                progress.stop();
                progress.setVisibility(View.GONE);
                if(data.size() == 0){
                    empty.setVisibility(View.VISIBLE);
                }
                else {
                    empty.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<GamesResponse> call, @NonNull Throwable t) {
                progress.stop();
                progress.setVisibility(View.GONE);
                Log.d("MM", t.getMessage()+"");

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
    public void Play(GameM g, int pos) {
        Intent intent= new Intent(getContext(), GameActivity.class);
        intent.putExtra("items", g.getItems());
        intent.putExtra("id", g.getId());
        intent.putExtra("letter", g.getLetter());
        intent.putExtra("type", 0);

        startActivityForResult(intent, PLAY_CODE);
    }

    @Override
    public void ManageRequest(GameM g, int pos) {
        this.pos= pos;
        this.game= g;

        Intent intent= new Intent(getContext(), DialogActivity.class);
        intent.putExtra("type", "success");
        intent.putExtra("title", "سوال");
        intent.putExtra("text", "درخواست بازی را می پذیرید؟");
        startActivityForResult(intent, MANAGE_CODE);
    }

    @Override
    public void CompleteDetail(GameM g) {
        Intent intent= new Intent(getContext(), GameResultActivity.class);
        intent.putExtra("id", g.getId()+"");
        intent.putExtra("letter", g.getLetter());
        intent.putExtra("uid", g.getUid()+"");
        intent.putExtra("oid", g.getOid()+"");

        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dt) {
        super.onActivityResult(requestCode, resultCode, dt);

        if(requestCode == MANAGE_CODE){
            if(resultCode == RESULT_OK){
                acceptGame(1);

            }
            else if(resultCode == RESULT_FIRST_USER){
                    acceptGame(6);
            }
        }

        if(requestCode == PLAY_CODE){
            if(resultCode == RESULT_OK){
                loadGames();
            }
        }
    }

    private void acceptGame(final int status) {
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getGameApi().manageGame(game.getId(), status);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.code() == 200){
                    if(status == 1){
                        data.get(pos).setStatus(1);
                        adapter.notifyItemChanged(pos);
                    }
                    else {
                        data.get(pos).setStatus(6);
                        adapter.notifyItemChanged(pos);
                    }
                }
                else{
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
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(getContext(), getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        call.cancel();
    }
}
