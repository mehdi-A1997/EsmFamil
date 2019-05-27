package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Module.ItemsM;
import ir.futurearts.esmfamil.Network.Responses.CreateGameResponse;
import ir.futurearts.esmfamil.Network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
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

        ItemsM itemsM= (ItemsM) getIntent().getSerializableExtra("items");
        GID= getIntent().getIntExtra("id", 0);
        letter.setText(getIntent().getStringExtra("letter"));
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

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareResult();
            }
        });
    }

    private void prepareResult() {
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

        Call<CreateGameResponse> call= RetrofitClient.getInstance()
                .getGameApi().setGameResult(GID, CurrentUser.getId(), object, Integer.parseInt(timer.getText().toString()));

        call.enqueue(new Callback<CreateGameResponse>() {
            @Override
            public void onResponse(Call<CreateGameResponse> call, Response<CreateGameResponse> response) {
                if(response.code() == 200){
                    CreateGameResponse cgr= response.body();

                    Intent intent= new Intent();
                    intent.putExtra("game", cgr.getGame());
                    setResult(RESULT_OK, intent);
                    finish();
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
            public void onFailure(Call<CreateGameResponse> call, Throwable t) {
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(GameActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cdt.cancel();
    }
}
