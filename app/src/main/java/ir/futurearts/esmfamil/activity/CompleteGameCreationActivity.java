package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import ir.futurearts.esmfamil.adapter.StepperAdapter;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.module.GameM;
import ir.futurearts.esmfamil.network.Responses.CreateGameResponse;
import ir.futurearts.esmfamil.network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CustomProgress;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteGameCreationActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    public StepperLayout mStepperLayout;
    private Bundle data;

    //Game Variable
    private int type;
    String letter="";
    JSONObject items= null;

    //Return Codes
    private final int ACCEPT_CODE= 1001;

    //Created Game ID
    private GameM game;
    private CustomProgress customProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_game_creation);

        data= getIntent().getExtras();
        assert data != null;
        type= data.getInt("type", 0);

        mStepperLayout =  findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);
        customProgress= new CustomProgress();
    }

    @Override
    public void onCompleted(View completeButton) {
        customProgress.showProgress(this, false);
        if(type == 0 || type ==1)
            CreateNormalGame();
        else if(type ==2)
            SetNormalRandomGame();
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

    private void CreateNormalGame() {

        final String oid= data.getString("oid");
        String uid= CurrentUser.getId();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Call<CreateGameResponse> call= RetrofitClient.getInstance()
                .getGameApi().CreateGameWithFriends(uid, oid, type, letter, items, timeStamp);

        call.enqueue(new Callback<CreateGameResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateGameResponse> call,@NonNull Response<CreateGameResponse> response) {
                if(response.code() == 201){
                    if(type == 0){
                        customProgress.hideProgress();
                        Intent intent= new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        assert response.body() != null;
                        game= response.body().getGame();
                        sendGameRequest(oid);
                    }
                }
                else {
                    customProgress.hideProgress();
                    try {
                        DefaultResponse df= new DefaultResponse(Objects.requireNonNull(response.errorBody()).string());
                        Log.d("MM", df.getMessage()+"");
                        FancyToast.makeText(CompleteGameCreationActivity.this, getString(R.string.systemError),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateGameResponse> call, @NonNull Throwable t) {
                customProgress.hideProgress();
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(CompleteGameCreationActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }

    private void SetNormalRandomGame() {

        int id= data.getInt("id", 0);
        String uid= CurrentUser.getId();
        Log.d("MM", id+"");
        Call<CreateGameResponse> call= RetrofitClient.getInstance()
                .getGameApi().setGameStuff(id,uid,letter, items);

        call.enqueue(new Callback<CreateGameResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateGameResponse> call, @NonNull Response<CreateGameResponse> response) {
                customProgress.hideProgress();
                if(response.code() == 201){
                    Intent intent= new Intent();
                    intent.putExtra("type", type);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    try {
                        assert response.errorBody() != null;
                        DefaultResponse df= new DefaultResponse(response.errorBody().string());
                        Log.d("MM", df.getMessage()+"");
                        FancyToast.makeText(CompleteGameCreationActivity.this, getString(R.string.systemError),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateGameResponse> call, @NonNull Throwable t) {
                customProgress.hideProgress();
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(CompleteGameCreationActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }



    private void sendGameRequest(final String oid) {
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getGameApi().sendGameRequest(CurrentUser.getId(), oid, game.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.code() == 200){
                    waitForAccept(game.getId()+"");
                }
                else {
                    customProgress.hideProgress();
                    FancyToast.makeText(CompleteGameCreationActivity.this, getString(R.string.systemError),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                customProgress.hideProgress();
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(CompleteGameCreationActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }

    private void waitForAccept(String  gid) {
        customProgress.hideProgress();
       Intent intent= new Intent(this, WaitForAcceptActivity.class);
       intent.putExtra("gid", gid+"");
        startActivityForResult(intent, ACCEPT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ACCEPT_CODE){
            if(resultCode == RESULT_OK){
                deleteGame(0);
                Intent intent= new Intent(CompleteGameCreationActivity.this, GameActivity.class);
                intent.putExtra("items", game.getItems());
                intent.putExtra("id", game.getId());
                intent.putExtra("letter", letter);
                intent.putExtra("type", 1);

                startActivity(intent);

            }
            else {
                customProgress.showProgress(this, false);
                deleteGame(1);
            }
        }
    }


    private void deleteGame(int t){
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getGameApi().deleteGame(game.getId(), t);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                customProgress.hideProgress();
                if(response.code() == 200){
                  finish();
                }
                else{
                    FancyToast.makeText(CompleteGameCreationActivity.this,  getString(R.string.systemError),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                customProgress.hideProgress();
                FancyToast.makeText(CompleteGameCreationActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }
}
