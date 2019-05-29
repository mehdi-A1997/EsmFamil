package ir.futurearts.esmfamil.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import ir.futurearts.esmfamil.Adapter.StepperAdapter;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Module.GameM;
import ir.futurearts.esmfamil.Network.Responses.CreateGameResponse;
import ir.futurearts.esmfamil.Network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteGameCreationActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    private StepperLayout mStepperLayout;
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

        String oid= data.getString("oid");
        String uid= CurrentUser.getId();

        Call<CreateGameResponse> call= RetrofitClient.getInstance()
                .getGameApi().CreateGameWithFriends(uid, oid, type, letter, items);

        call.enqueue(new Callback<CreateGameResponse>() {
            @Override
            public void onResponse(Call<CreateGameResponse> call, Response<CreateGameResponse> response) {
                if(response.code() == 201){
                    if(type == 0){
                        customProgress.hideProgress();
                        Intent intent= new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        game= response.body().getGame();
                        CreateCompetGame(game.getId()+"");
                    }
                }
                else {
                    customProgress.hideProgress();
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
            public void onResponse(Call<CreateGameResponse> call, Response<CreateGameResponse> response) {
                customProgress.hideProgress();
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
                customProgress.hideProgress();
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(CompleteGameCreationActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }

    private void CreateCompetGame(String  id) {
        Log.d("MM", "Start");

        final String oid= data.getString("oid");

        ParseObject entity = new ParseObject("Games");

        entity.put("Uid", CurrentUser.getId());
        entity.put("Oid", oid);
        entity.put("Status", "0");
        entity.put("Letter", letter);
        entity.put("Gid", id);

        entity.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    sendGameRequest(oid);
                }
                else {
                    Log.d("MM",e.getMessage()+"/Game");
                    deleteGame(1);
                }
            }
        });
    }


    private String getGid(String gid) {
        ParseQuery<ParseObject> specificPostQuery = ParseQuery.getQuery("Games");
        specificPostQuery.whereEqualTo("Gid", gid);

        try {
            ParseObject object=specificPostQuery.getFirst();
            return object.getObjectId();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("MM",e.getMessage()+"/GID");
        }

        return null;
    }

    private void sendGameRequest(final String oid) {
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getGameApi().sendGameRequest(CurrentUser.getId(), oid, game.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
       intent.putExtra("gid", gid);
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
                cancleGame();
            }
        }
    }

    private void cancleGame() {
        String gid= getGid(game.getId()+"");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");

        // Retrieve the object by id
        query.getInBackground(gid, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                   entity.deleteInBackground();
                   deleteGame(1);
                }
                else {
                    customProgress.hideProgress();
                }
            }
        });

    }

    private void deleteGame(int t){
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getGameApi().deleteGame(game.getId(), t);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                customProgress.hideProgress();
                if(response.code() == 200){
                    //TODO Game Deleted And Finish
                }
                else{
                    FancyToast.makeText(CompleteGameCreationActivity.this,  getString(R.string.systemError),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                customProgress.hideProgress();
                FancyToast.makeText(CompleteGameCreationActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }
}
