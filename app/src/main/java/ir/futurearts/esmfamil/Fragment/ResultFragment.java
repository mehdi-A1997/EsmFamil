package ir.futurearts.esmfamil.Fragment;


import android.content.Intent;
import android.os.Bundle;
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
import ir.futurearts.esmfamil.Activity.GameActivity;
import ir.futurearts.esmfamil.Activity.GameResultActivity;
import ir.futurearts.esmfamil.Adapter.GameAdapter;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Interface.GameInterface;
import ir.futurearts.esmfamil.Module.GameM;
import ir.futurearts.esmfamil.Network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.Network.Responses.GamesResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.DialogActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements GameInterface {

    private RecyclerView list;
    private GameAdapter adapter;
    private LinkedList<GameM> data;
    private LinearLayout empty;
    private NewtonCradleLoading progress;
    private Call<GamesResponse> call;

    private final int MANAGE_CODE= 1001;
    int lastpos=0;
    //for manage game
    private int pos;
    private GameM game;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_result, container, false);
        list= v.findViewById(R.id.result_rv);
        empty= v.findViewById(R.id.result_empty);
        progress= v.findViewById(R.id.result_progress);

        data= new LinkedList<>();
        adapter= new GameAdapter(data, getContext(), this);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));


        progress.start();
        call = RetrofitClient.getInstance()
                .getGameApi().getMyGames(CurrentUser.getId());

        call.enqueue(new Callback<GamesResponse>() {
            @Override
            public void onResponse(Call<GamesResponse> call, Response<GamesResponse> response) {
                if(response.code() == 200){
                    LinkedList<GameM> dt= response.body().getGames();

                    for(GameM g: dt){
                        if(g.getStatus() == 1) {
                            if ((g.getUid() + "").equals(CurrentUser.getId()) && g.getUscore() == -1) {
                                data.addFirst(g);
                                lastpos++;
                            } else if ((g.getOid() + "").equals(CurrentUser.getId()) && g.getOscore() == -1) {
                                data.addFirst(g);
                                lastpos++;
                            }
                            else
                                data.add(lastpos, g);

                        }
                        else  if(CurrentUser.getId().equals(g.getOid()+"") && g.getStatus() == 9){
                            data.addFirst(g);
                            lastpos++;
                        }
                        else{
                            data.add(lastpos, g);
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
            public void onFailure(Call<GamesResponse> call, Throwable t) {
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
        return v;
    }

    @Override
    public void Play(GameM g, int pos) {
        Intent intent= new Intent(getContext(), GameActivity.class);
        intent.putExtra("items", g.getItems());
        intent.putExtra("id", g.getId());
        intent.putExtra("letter", g.getLetter());
        intent.putExtra("type", 0);

        startActivity(intent);
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
            else {
                acceptGame(6);
            }
        }
    }

    private void acceptGame(final int status) {
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getGameApi().manageGame(game.getId(), status);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                        DefaultResponse df= new DefaultResponse(response.errorBody().string());
                        FancyToast.makeText(getContext(), df.getMessage(),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
