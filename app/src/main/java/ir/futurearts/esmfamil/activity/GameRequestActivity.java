package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ir.futurearts.esmfamil.adapter.GameRequestAdapter;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.interfaces.GameRequestInterface;
import ir.futurearts.esmfamil.module.GameM;
import ir.futurearts.esmfamil.network.Responses.GamesResponse;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CustomProgress;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRequestActivity extends AppCompatActivity implements GameRequestInterface {

    private GameRequestAdapter adapter;
    private List<GameM> data;
    private CustomProgress customProgress;
    private OkHttpClient client;
    private WebSocket ws;
    private GameM gameM;
    private boolean isFirst= true;
    private NewtonCradleLoading progress;
    private LinearLayout empty;
    private int position;

    private final int START_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_request);

        RecyclerView list = findViewById(R.id.gamerequest_rv);
        progress= findViewById(R.id.gamerequest_pb);
        empty= findViewById(R.id.gamerequest_empty);
        data= new ArrayList<>();
        adapter= new GameRequestAdapter(data, this, this);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        customProgress= new CustomProgress();
        progress.start();

        Call<GamesResponse> call= RetrofitClient.getInstance()
                .getGameApi().getGameRequest(CurrentUser.getId());

        call.enqueue(new Callback<GamesResponse>() {
            @Override
            public void onResponse(@NonNull Call<GamesResponse> call, @NonNull Response<GamesResponse> response) {
                if(response.code() == 200){
                    assert response.body() != null;
                    data.addAll(response.body().getGames());
                    adapter.notifyDataSetChanged();
                }
                if(data.size() == 0){
                    empty.setVisibility(View.VISIBLE);
                }
                else {
                    empty.setVisibility(View.GONE);
                }
                progress.stop();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<GamesResponse> call, @NonNull Throwable t) {
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



        setSocket();
        setSocket();
    }

    private void setSocket(){
        client =  new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url("ws://connect.websocket.in/futurearts_esmfamil?room_id=1375").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);
        JSONObject object= new JSONObject();
        try {
            object.put("id", "test");
            object.put("status", "3");
            ws.send(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            Log.d("MM", "Open:"+response.message());
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.d("MM", "Message: "+ text);
            if(gameM != null) {
                customProgress.hideProgress();
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("id").equals(gameM.getId()+"") &&
                            object.getString("status").equals("3"))
                        if(isFirst){
                            gotoGame(gameM);
                            isFirst = false;
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {

        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            Log.d("MM", "Socket Close");
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            Log.d("MM", t.getMessage()+"");
        }
    }

    @Override
    public void Accept(final GameM g,final int pos) {
        gameM= g;
        position= pos;
        customProgress.showProgress(this, false);

        JSONObject object= new JSONObject();
        try {
            object.put("id", g.getId()+"");
            object.put("status", "3");
            Log.d("MM", object.toString());
            ws.send(object.toString());
            ws.send(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void gotoGame(GameM g) {
        ws.close(1000, null);
        client.dispatcher().executorService().shutdown();
        Intent intent= new Intent(this, GameActivity.class);
        intent.putExtra("items", g.getItems());
        intent.putExtra("letter", g.getLetter());
        intent.putExtra("id", g.getId());
        intent.putExtra("type", 1);

        startActivityForResult(intent, START_CODE);
    }

    @Override
    public void Decline(final GameM g, final int pos) {
        customProgress.showProgress(this, false);
        gameM= g;
        JSONObject object= new JSONObject();
        try {
            object.put("id", g.getId()+"");
            object.put("status", "2");
            ws.send(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        data.remove(pos);
        adapter.notifyDataSetChanged();
        ws.close(1000, null);
        client.dispatcher().executorService().shutdown();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent dt) {
        super.onActivityResult(requestCode, resultCode, dt);

        if(requestCode == START_CODE){
            data.remove(position);
            adapter.notifyDataSetChanged();
        }
    }
}
