package ir.futurearts.esmfamil.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Module.ItemsM;
import ir.futurearts.esmfamil.Network.Responses.CreateGameResponse;
import ir.futurearts.esmfamil.Network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;
import ir.futurearts.esmfamil.Utils.DialogActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {

    private EditText tname, tfamily, tcity, tcountry, tfood,
            tanimal, tcolor, tfruit, tobject, tflower;

    private CardView  sname, sfamily, scity, scountry, sfood,
            sanimal, scolor, sfruit, sobject, sflower;

    private TextView timer, letter;

    private Button sendbtn;

    private int GID;

    private CountDownTimer cdt;
    private int type= 0;
    private CustomProgress customProgress;
    private OkHttpClient client;
    private WebSocket ws;
    private Request request;
    private boolean isConnect = false;
    private boolean isFirst = true;
    private ItemsM itemsM;

    private final int RESULT_CODE= 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tname= findViewById(R.id.category_name);
        tfamily= findViewById(R.id.category_family);
        tcity= findViewById(R.id.category_city);
        tcountry= findViewById(R.id.category_country);
        tfood= findViewById(R.id.category_food);
        tanimal= findViewById(R.id.category_animal);
        tcolor= findViewById(R.id.category_color);
        tfruit= findViewById(R.id.category_fruit);
        tobject= findViewById(R.id.category_object);
        tflower= findViewById(R.id.category_flower);

        timer= findViewById(R.id.category_timer);
        letter= findViewById(R.id.gamep_letter);

        sname= findViewById(R.id.category_name_sec);
        sfamily= findViewById(R.id.category_family_sec);
        scity= findViewById(R.id.category_city_sec);
        scountry= findViewById(R.id.category_country_sec);
        sfood= findViewById(R.id.category_food_sec);
        sanimal= findViewById(R.id.category_animal_sec);
        scolor= findViewById(R.id.category_color_sec);
        sfruit= findViewById(R.id.category_fruit_sec);
        sobject= findViewById(R.id.category_object_sec);
        sflower= findViewById(R.id.category_flower_sec);

        sendbtn= findViewById(R.id.game_btn);

        itemsM= (ItemsM) getIntent().getSerializableExtra("items");
        GID= getIntent().getIntExtra("id", 0);
        letter.setText(getIntent().getStringExtra("letter"));
        type= getIntent().getIntExtra("type", 0);

        customProgress= new CustomProgress();

        if(GID == 0 )
            finish();

        if(itemsM.getName().equals("0"))
            sname.setVisibility(View.VISIBLE);
        else
            sname.setVisibility(View.GONE);

        if(itemsM.getFamily().equals("0"))
            sfamily.setVisibility(View.VISIBLE);
        else
            sfamily.setVisibility(View.GONE);

        if(itemsM.getCity().equals("0"))
            scity.setVisibility(View.VISIBLE);
        else
            scity.setVisibility(View.GONE);

        if(itemsM.getCountry().equals("0"))
            scountry.setVisibility(View.VISIBLE);
        else
            scountry.setVisibility(View.GONE);

        if(itemsM.getFood().equals("0"))
            sfood.setVisibility(View.VISIBLE);
        else
            sfood.setVisibility(View.GONE);

        if(itemsM.getAnimal().equals("0"))
            sanimal.setVisibility(View.VISIBLE);
        else
            sanimal.setVisibility(View.GONE);

        if(itemsM.getColor().equals("0"))
            scolor.setVisibility(View.VISIBLE);
        else
            scolor.setVisibility(View.GONE);

        if(itemsM.getFruit().equals("0"))
            sfruit.setVisibility(View.VISIBLE);
        else
            sfruit.setVisibility(View.GONE);

        if(itemsM.getFlower().equals("0"))
            sflower.setVisibility(View.VISIBLE);
        else
            sflower.setVisibility(View.GONE);

        if(itemsM.getObject().equals("0"))
            sobject.setVisibility(View.VISIBLE);
        else
            sobject.setVisibility(View.GONE);


        cdt= new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                long time= millisUntilFinished / 1000;
                timer.setText(time+"");

                if(time<=25 && time>10)
                    timer.setTextColor(ContextCompat.getColor(GameActivity.this, R.color.orange));
                else if(time<=10)
                    timer.setTextColor(ContextCompat.getColor(GameActivity.this, R.color.red));
                else
                    timer.setTextColor(ContextCompat.getColor(GameActivity.this, R.color.white));
            }

            public void onFinish() {
                prepareResult();
            }

        }.start();

        if(type == 1) {
            cdt.cancel();
           setSocket();
            setSocket();
        }
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(type == 1)
               {
                   customProgress.showProgress(GameActivity.this,false);
                   JSONObject object= new JSONObject();
                   try {
                       object.put("id", GID+"");
                       object.put("status", "4");
                       ws.send(object.toString());
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
               else
                   prepareResult();
            }
        });
    }


    private void setSocket() {
        client =  new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
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

    private void prepareResult() {
        cdt.cancel();
        String name= tname.getText().toString();
        String family= tfamily.getText().toString();
        String city= tcity.getText().toString();
        String country= tcountry.getText().toString();
        String food= tfood.getText().toString();
        String animal= tanimal.getText().toString();
        String flower= tflower.getText().toString();
        String fruit= tfruit.getText().toString();
        String color= tcolor.getText().toString();
        String obj= tobject.getText().toString();

        if(!itemsM.getName().equals("0"))
            name= "-1";

        if(!itemsM.getFamily().equals("0"))
           family= "-1";

        if(!itemsM.getCity().equals("0"))
           city= "-1";

        if(!itemsM.getCountry().equals("0"))
            country= "-1";

        if(!itemsM.getFood().equals("0"))
            food= "-1";

        if(!itemsM.getAnimal().equals("0"))
            animal= "-1";

        if(!itemsM.getColor().equals("0"))
            color= "-1";

        if(!itemsM.getFruit().equals("0"))
            fruit= "-1";

        if(!itemsM.getFlower().equals("0"))
            flower= "-1";

        if(!itemsM.getObject().equals("0"))
            obj= "-1";


        JSONObject object= new JSONObject();
        try {
            object.put("name", name);
            object.put("family", family);
            object.put("city", city);
            object.put("country", country);
            object.put("food",food);
            object.put("animal", animal);
            object.put("color", color);
            object.put("flower", flower);
            object.put("fruit", fruit);
            object.put("object", obj);

            Log.d("MM", object.toString());

            sendResult(object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendResult(JSONObject object) {
        Log.d("MM", object.toString());
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getGameApi().setGameResult(GID, CurrentUser.getId(), object, Integer.parseInt(timer.getText().toString()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                customProgress.hideProgress();
                if(response.code() == 200){
                    if(type == 0){
                        Intent intent= new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else{
                        openDialog();
                    }
                }
                else {
                    try {
                        DefaultResponse df= new DefaultResponse(response.errorBody().string());
                        FancyToast.makeText(GameActivity.this, df.getMessage(),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                customProgress.hideProgress();
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(GameActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }

    private void openDialog() {
        ws.close(1000, null);
        client.dispatcher().executorService().shutdown();
        Intent intent= new Intent(this, DialogActivity.class);
        intent.putExtra("type", "singleS");
        intent.putExtra("title", "پیام");
        intent.putExtra("text", "بازی پایان یافت");

        startActivityForResult(intent, RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_CODE){
            if(resultCode == RESULT_OK){
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cdt.cancel();
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            Log.d("MM", "Open:"+response.message());
            isConnect= true;
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.d("MM", "Message: "+ text);
            try {
                JSONObject object= new JSONObject(text);
                if(object.getString("id").equals(GID+"") &&
                        object.getString("status").equals("4"))
                    if(isFirst){
                        prepareResult();
                        isFirst= false;
                    }

            } catch (JSONException e) {
                e.printStackTrace();
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
            if(!isConnect)
                setSocket();
        }
    }
}
