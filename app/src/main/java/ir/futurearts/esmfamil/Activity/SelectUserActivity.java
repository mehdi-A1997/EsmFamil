package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ir.futurearts.esmfamil.Adapter.UserAdapter;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.R;

public class SelectUserActivity extends AppCompatActivity {

    private RecyclerView list;
    private List<UserM> data;
    private UserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        list = findViewById(R.id.select_user_rv);
        data = new ArrayList<>();
        adapter = new UserAdapter(data, this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        ParseUser c = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> q = new ParseQuery<>("Friends");
        q.whereEqualTo("Cid", c.getObjectId());
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject p : objects) {
                    ParseQuery<ParseUser> p1 = ParseUser.getQuery();
                    try {
                        final ParseUser user = p1.get(p.getString("Uid"));
                        final UserM u = new UserM(user.getObjectId(), user.getString("name"),
                                user.getUsername(), user.getEmail(), user.getString("score"));
                        ParseFile img = user.getParseFile("image");
                        u.setImg(img);
                        u.setOnline(user.getInt("online"));
                        data.add(u);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data.sort(new Comparator<UserM>() {
                @Override
                public int compare(UserM o1, UserM o2) {
                    return o1.getOnline()<o2.getOnline()?-1:0;
                }
            });
        }
        adapter.notifyDataSetChanged();
        ParseLiveQueryClient parseLiveQueryClient = null;

        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://esmfamil.back4app.io/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (parseLiveQueryClient != null) {
            ParseQuery<ParseObject> parseQuery = new ParseQuery<>("Test");
            SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

            subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE, new SubscriptionHandling.HandleEventCallback<ParseObject>() {
                @Override
                public void onEvent(ParseQuery<ParseObject> query, final ParseObject object) {
                    Log.d("MM",object.getString("name"));

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(SelectUserActivity.this, object.getString("name"), Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            });
        }
    }
}
