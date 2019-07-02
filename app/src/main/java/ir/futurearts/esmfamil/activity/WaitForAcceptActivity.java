package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;

public class WaitForAcceptActivity extends AppCompatActivity {

    private String gid;
    private OkHttpClient client;
    private WebSocket ws;

    private boolean isConnect = false;
    private boolean isFirst = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_wait_for_accept);

        ImageView img = findViewById(R.id.wait_img);
        Button btn = findViewById(R.id.wait_exit);
        Glide.with(this).asGif().load(R.raw.load).into(img);
        client =  new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        gid= getIntent().getStringExtra("gid");


        setSocket();
        setSocket();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setSocket() {
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
        public void onOpen(WebSocket webSocket, Response response) {
            Log.d("MM", "Open:"+response.message());
            isConnect = true;
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            try {
                JSONObject object= new JSONObject(text);
                if(object.getString("id").equals(gid+"") &&
                    object.getString("status").equals("3")) {
                    if (isFirst) {
                        accepted();
                        isFirst = false;
                    }
                }
                else if(object.getString("id").equals(gid+"") &&
                        object.getString("status").equals("2")) {
                    if (isFirst) {
                        finish();
                        isFirst = false;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("MM", "Message: "+ text);

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
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.d("MM", t.getMessage()+"");
            if(!isConnect)
                setSocket();
        }
    }


    private void accepted() {
        ws.close(1000, null);
        client.dispatcher().executorService().shutdown();

        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getUserApi().consumeCoin(CurrentUser.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                if(response.code() == 200){
                    Intent intent= new Intent();
                    setResult(RESULT_OK, intent);

                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(WaitForAcceptActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.dispatcher().executorService().shutdown();
        ws.close(1000, null);
    }

}
