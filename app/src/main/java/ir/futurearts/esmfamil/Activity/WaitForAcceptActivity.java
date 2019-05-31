package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;
import ir.futurearts.esmfamil.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WaitForAcceptActivity extends AppCompatActivity {

    private ImageView img;
    private Button btn;
    private String gid;
    private OkHttpClient client;
    private WebSocket ws;
    private Request request;

    private boolean isConnect = false;
    private boolean isFirst = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_accept);

        img= findViewById(R.id.wait_img);
        btn= findViewById(R.id.wait_exit);
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
        request = new Request.Builder().url("ws://connect.websocket.in/futurearts_esmfamil?room_id=1375").build();
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
        Intent intent= new Intent();
        setResult(RESULT_OK, intent);
        ws.close(1000, null);
        client.dispatcher().executorService().shutdown();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.dispatcher().executorService().shutdown();
        ws.close(1000, null);
    }

}
