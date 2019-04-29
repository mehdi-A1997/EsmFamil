package ir.futurearts.esmfamil.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.List;

import ir.futurearts.esmfamil.Adapter.RankAdapter;
import ir.futurearts.esmfamil.Interface.AddFriendInterface;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;
import ir.futurearts.esmfamil.Utils.DialogActivity;

public class RankActivity extends AppCompatActivity implements AddFriendInterface {

    private RecyclerView list;
    private List<UserM>data;
    private RankAdapter adapter;
    private NewtonCradleLoading progress;

    private ParseUser current_user;
    private final int SEND_CODE=1001;

    private String uid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        list=findViewById(R.id.rank_rv);
        progress=findViewById(R.id.rank_progress);
        data=new ArrayList<>();
        adapter=new RankAdapter(data,this,this);

        current_user=ParseUser.getCurrentUser();
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        progress.start();
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
                progress.stop();
                progress.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                list.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void AlreadyFriend() {
        Intent intent=new Intent(RankActivity.this, DialogActivity.class);
        intent.putExtra("type","singleS");
        intent.putExtra("title","پیام");
        intent.putExtra("text","کاربر"+" در لیست دوستان شما حضور دارد");
        startActivity(intent);
    }

    @Override
    public void AlreadySent() {
        Intent intent=new Intent(RankActivity.this, DialogActivity.class);
        intent.putExtra("type","singleS");
        intent.putExtra("title","پیام");
        intent.putExtra("text","درخواست قبلا ارسال شده");
        startActivity(intent);
    }

    @Override
    public void SendRequest(String id) {
        uid=id;
        Intent intent=new Intent(RankActivity.this, DialogActivity.class);
        intent.putExtra("type","success");
        intent.putExtra("title","سوال");
        intent.putExtra("text","مایل به اضافه کردن به لیست دوستان هستید؟");
        startActivityForResult(intent,SEND_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SEND_CODE){
            if(resultCode==RESULT_OK){
                final CustomProgress customProgress=new CustomProgress();
                customProgress.showProgress(RankActivity.this,false);
                ParseObject req=new ParseObject("FriendRequests");
                req.put("Cid",current_user.getObjectId());
                req.put("Uid",uid);

                req.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            customProgress.hideProgress();
                            Intent intent=new Intent(RankActivity.this, DialogActivity.class);
                            intent.putExtra("type","singleS");
                            intent.putExtra("title","پیام");
                            intent.putExtra("text","با موفقیت ارسال شد");
                            startActivity(intent);
                        }
                        else{
                            //TODO Fancy Toast
                        }
                    }
                });
            }
        }
    }
}
