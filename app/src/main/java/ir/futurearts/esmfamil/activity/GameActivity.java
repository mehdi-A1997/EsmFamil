package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.module.ItemsM;
import ir.futurearts.esmfamil.network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CustomProgress;
import ir.futurearts.esmfamil.utils.DialogActivity;
import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellAd;
import ir.tapsell.sdk.TapsellAdRequestListener;
import ir.tapsell.sdk.TapsellAdRequestOptions;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.accounts.AccountManager.ERROR_CODE_NETWORK_ERROR;

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
    private boolean isConnect = false;
    private boolean isFirst = true;
    private boolean surended = false;
    private ItemsM itemsM;
    private TapsellAd gameAd;
    private final int RESULT_CODE = 1001;
    private final int EXIT_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
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

        TapsellAdRequestOptions options= new TapsellAdRequestOptions();
        options.setCacheType(TapsellAdRequestOptions.CACHE_TYPE_CACHED);
        Tapsell.requestAd(this, "5d14f4cd40878d000135e688", options, new TapsellAdRequestListener() {
            @Override
            public void onError (String error)
            {
            }

            @Override
            public void onAdAvailable (TapsellAd ad)
            {
                gameAd= ad;
            }

            @Override
            public void onNoAdAvailable ()
            {
            }

            @Override
            public void onNoNetwork ()
            {
            }

            @Override
            public void onExpiring (TapsellAd ad)
            {
            }
        });
    }



    private void setSocket() {
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

    private void prepareResult() {
        Log.d("MM", "PREPARE FOR RESULT CALLED");
        cdt.cancel();
        String name= tname.getText().toString().trim();
        String family= tfamily.getText().toString().trim();
        String city= tcity.getText().toString().trim();
        String country= tcountry.getText().toString().trim();
        String food= tfood.getText().toString().trim();
        String animal= tanimal.getText().toString().trim();
        String flower= tflower.getText().toString().trim();
        String fruit= tfruit.getText().toString().trim();
        String color= tcolor.getText().toString().trim();
        String obj= tobject.getText().toString().trim();

        if(surended){
             name= "";
             family= "";
             city= "";
             country= "";
             food= "";
             animal= "";
             flower= "";
             fruit= "";
             color= "";
             obj= "";
        }

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
        String time= timer.getText().toString();
        if(surended)
            time = "0";
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getGameApi().setGameResult(GID, CurrentUser.getId(), object, Integer.parseInt(time));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,@NonNull Response<ResponseBody> response) {
                customProgress.hideProgress();
                if(response.code() == 200){
                   int rand= new Random().nextInt(20);
                   if(rand %2 == 0 && rand>=10){
                      gameAd.show(GameActivity.this, null);
                   }
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
                        assert response.errorBody() != null;
                        DefaultResponse df= new DefaultResponse(response.errorBody().string());
                        FancyToast.makeText(GameActivity.this, df.getMessage(),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
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
                Intent intent= new Intent();
                setResult(RESULT_OK, intent);
                finish();
        }

        if(requestCode == EXIT_CODE){
            if(resultCode == RESULT_OK){
                type = 0;
                surended= true;
                prepareResult();
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
                        isFirst= false;
                        prepareResult();
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

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(this, DialogActivity.class);
        intent.putExtra("type", "error");
        intent.putExtra("title", "هشدار");
        intent.putExtra("text", "خروج به منزله انصراف است، مایل به خروج هستید؟");
        startActivityForResult(intent, EXIT_CODE);
    }
}
