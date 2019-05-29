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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;

import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.R;

public class WaitForAcceptActivity extends AppCompatActivity {

    ParseLiveQueryClient parseLiveQueryClient = null;
    private ImageView img;
    private Button btn;

    private String gid;
    private Handler handler = new Handler();
    private int delay = 5000; //milliseconds
    private boolean isDone= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_accept);

        img= findViewById(R.id.wait_img);
        btn= findViewById(R.id.wait_exit);
        Glide.with(this).asGif().load(R.raw.load).into(img);



        gid= getIntent().getStringExtra("gid");
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://esmfamil.back4app.io/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.d("MM", "Error Listen");
        }

        handler.postDelayed(new Runnable(){
            public void run(){
                checkStatus();
                handler.postDelayed(this, delay);
            }
        }, delay);

        if (parseLiveQueryClient != null) {
            ParseQuery parseQuery = new ParseQuery("Games");
            parseQuery.whereEqualTo("Gid", gid);
            Log.d("MM", "Start Listening");
            SubscriptionHandling subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

            subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE, new SubscriptionHandling.HandleEventCallback<ParseObject>() {
                @Override
                public void onEvent(ParseQuery<ParseObject> query, final ParseObject object) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            String status= object.getString("Status");
                            Log.d("MM", status);

                            if(status.equals("1") && !isDone){
                                accepted();
                            }
                            else {
                                finish();
                            }
                        }
                    });
                }
            });
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkStatus() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
        query.whereEqualTo("Gid", gid);
        // Retrieve the object by id
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    if(entity.getString("Status").equals("3") && !isDone){
                        accepted();
                    }
                    else {
                        finish();
                    }
                }
            }
        });
    }

    private void accepted() {
        handler.removeCallbacksAndMessages(null);
        isDone =true;
        Intent intent= new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
