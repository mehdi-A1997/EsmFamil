package ir.futurearts.esmfamil.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

import java.io.IOException;

import ir.futurearts.esmfamil.Adapter.StepperAdapter;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Module.GameM;
import ir.futurearts.esmfamil.Network.Responses.CreateGameResponse;
import ir.futurearts.esmfamil.Network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.Network.Responses.GamesResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteGameCreationActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    private StepperLayout mStepperLayout;

    //Game Variable
    private int type;
    String letter="";
    JSONObject items= null;

    Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_game_creation);

        data= getIntent().getExtras();
        type= data.getInt("type", 0);

        mStepperLayout =  findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);
    }

    @Override
    public void onCompleted(View completeButton) {
        if(type ==0 || type == 1)
            CreateGame();
        else
            SetRandomGame();
    }

    @Override
    public void onError(VerificationError verificationError) {
        FancyToast.makeText(CompleteGameCreationActivity.this, verificationError.getErrorMessage(),
                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }


    public void setLetter(String letter) {
        this.letter = letter;
    }

    public void setItems(JSONObject items) {
        this.items = items;
    }

    public String getLetter() {
        return letter;
    }

    public JSONObject getItems() {
        return items;
    }

    private void CreateGame() {

        String oid= data.getString("oid");
        String uid= CurrentUser.getId();

        Call<CreateGameResponse> call= RetrofitClient.getInstance()
                .getGameApi().CreateGameWithFriends(uid, oid, type, letter, items);

        call.enqueue(new Callback<CreateGameResponse>() {
            @Override
            public void onResponse(Call<CreateGameResponse> call, Response<CreateGameResponse> response) {
                if(response.code() == 201){
                    Intent intent= new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    try {
                        DefaultResponse df= new DefaultResponse(response.errorBody().string());
                        FancyToast.makeText(CompleteGameCreationActivity.this, df.getMessage(),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateGameResponse> call, Throwable t) {
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(CompleteGameCreationActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }

    private void SetRandomGame() {

        int id= data.getInt("id", 0);
        String uid= CurrentUser.getId();
        Log.d("MM", id+"");
        Call<CreateGameResponse> call= RetrofitClient.getInstance()
                .getGameApi().setGameStuff(id,uid,letter, items);

        call.enqueue(new Callback<CreateGameResponse>() {
            @Override
            public void onResponse(Call<CreateGameResponse> call, Response<CreateGameResponse> response) {
                if(response.code() == 201){
                    Intent intent= new Intent();
                    intent.putExtra("type", type);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    try {
                        DefaultResponse df= new DefaultResponse(response.errorBody().string());
                        FancyToast.makeText(CompleteGameCreationActivity.this, df.getMessage(),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateGameResponse> call, Throwable t) {
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(CompleteGameCreationActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }



}
