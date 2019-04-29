package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ir.futurearts.esmfamil.Adapter.UserAdapter;
import ir.futurearts.esmfamil.Interface.UserInterface;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.R;

public class FriendsActivity extends AppCompatActivity implements UserInterface {

    private RecyclerView list;
    private List<UserM> data;
    private UserAdapter adapter;
    private ConstraintLayout searchbtn;
    private ConstraintLayout requestbtn;
    private NewtonCradleLoading progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        list = findViewById(R.id.frineds_rv);
        searchbtn= findViewById(R.id.friends_add);
        requestbtn= findViewById(R.id.friends_request);
        progress= findViewById(R.id.friends_progress);
        progress.start();
        data = new ArrayList<>();
        adapter = new UserAdapter(data, this,this);
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
                progress.stop();
                progress.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                list.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void userSelected(UserM u) {
        //TODO DELETE USER
        Toast.makeText(this, "DELETE USER", Toast.LENGTH_SHORT).show();
    }
}
