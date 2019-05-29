package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import ir.futurearts.esmfamil.Adapter.GameRequestAdapter;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Interface.GameRequestInterface;
import ir.futurearts.esmfamil.Module.GameM;
import ir.futurearts.esmfamil.Network.Responses.GamesResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRequestActivity extends AppCompatActivity implements GameRequestInterface {

    private RecyclerView list;
    private GameRequestAdapter adapter;
    private List<GameM> data;
    private CustomProgress customProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_request);

        list= findViewById(R.id.gamerequest_rv);
        data= new ArrayList<>();
        adapter= new GameRequestAdapter(data, this, this);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        customProgress= new CustomProgress();

        Call<GamesResponse> call= RetrofitClient.getInstance()
                .getGameApi().getGameRequest(CurrentUser.getId());

        call.enqueue(new Callback<GamesResponse>() {
            @Override
            public void onResponse(Call<GamesResponse> call, Response<GamesResponse> response) {
                if(response.code() == 200){
                    data.addAll(response.body().getGames());
                    adapter.notifyDataSetChanged();
                }
                else{
                    FancyToast.makeText(GameRequestActivity.this, getString(R.string.systemError),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(Call<GamesResponse> call, Throwable t) {
                FancyToast.makeText(GameRequestActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }

    @Override
    public void Accept(final GameM g,final int pos) {
        customProgress.showProgress(this, false);
        Log.d("MM", g.getId()+"");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
        query.whereEqualTo("Gid", g.getId()+"");
        // Retrieve the object by id
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    entity.put("Status", "1");
                    entity.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                gotoGame(g);
                                data.remove(pos);
                                adapter.notifyDataSetChanged();
                                customProgress.hideProgress();
                                finish();
                            }
                            else {
                                customProgress.hideProgress();
                                Log.d("MM", "update Error");
                                Log.d("MM", e.getMessage());
                            }
                        }
                    });
                }
                else {
                    customProgress.hideProgress();
                    Log.d("MM", "get Error");
                    Log.d("MM", e.getMessage());
                }
            }
        });

    }

    private void gotoGame(GameM g) {
        Intent intent= new Intent(this, GameActivity.class);
        intent.putExtra("items", g.getItems());
        intent.putExtra("letter", g.getLetter());
        intent.putExtra("id", g.getId());
        intent.putExtra("type", 1);

        startActivity(intent);
    }

    @Override
    public void Decline(final GameM g, final int pos) {
        customProgress.showProgress(this, false);
        Log.d("MM", g.getId()+"");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
        query.whereEqualTo("Gid", g.getId()+"");
        // Retrieve the object by id
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    entity.put("Status", "0");
                    entity.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                gotoGame(g);
                                data.remove(pos);
                                adapter.notifyDataSetChanged();
                                customProgress.hideProgress();
                            }
                            else {
                                customProgress.hideProgress();
                                Log.d("MM", "update Error");
                                Log.d("MM", e.getMessage());
                            }
                        }
                    });
                }
                else {
                    customProgress.hideProgress();
                    Log.d("MM", "get Error");
                    Log.d("MM", e.getMessage());
                }
            }
        });
    }
}
