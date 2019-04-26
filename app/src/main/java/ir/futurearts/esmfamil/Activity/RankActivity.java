package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import ir.futurearts.esmfamil.Adapter.RankAdapter;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.R;

public class RankActivity extends AppCompatActivity {

    private RecyclerView list;
    private List<UserM>data;
    private RankAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        list=findViewById(R.id.rank_rv);
        data=new ArrayList<>();
        adapter=new RankAdapter(data,this);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        ParseQuery<ParseUser> q=ParseUser.getQuery();
        q.orderByDescending("score");

        q.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for(ParseUser p:objects){
                    UserM u=new UserM(p.getObjectId(),p.getString("name"),p.getUsername()
                    ,p.getEmail(),p.getString("score"));

                    ParseFile img=p.getParseFile("image");
                    u.setImg(img);
                    data.add(u);

                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
