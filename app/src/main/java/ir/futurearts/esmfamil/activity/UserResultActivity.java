package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserResultActivity extends AppCompatActivity {

    private TextView win, draw, lose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_result);

        win= findViewById(R.id.userresult_win);
        draw= findViewById(R.id.userresult_draw);
        lose= findViewById(R.id.userresult_lose);
        CircleImageView img = findViewById(R.id.userresult_img);

        img.setImageDrawable(getDrawableByName(CurrentUser.getImg()));

        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getUserApi().getUserResult(CurrentUser.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.code() == 200){
                    try {
                        assert response.body() != null;
                        JSONObject object= new JSONObject(response.body().string());

                        win.setText(object.getString("win"));
                        draw.setText(object.getString("draw"));
                        lose.setText(object.getString("lose"));
                    } catch (Exception ignored) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    private Drawable getDrawableByName(String name){
        Resources resources = getResources();
        final int resourceId = resources.getIdentifier(name, "drawable", getPackageName());
        return ContextCompat.getDrawable(this,resourceId);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
