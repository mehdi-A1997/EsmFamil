package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.victor.loading.newton.NewtonCradleLoading;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import ir.futurearts.esmfamil.Adapter.UserAdapter;
import ir.futurearts.esmfamil.Interface.UserInterface;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.R;

public class SelectUserActivity extends AppCompatActivity implements UserInterface {

    private RecyclerView list;
    private List<UserM> data;
    private UserAdapter adapter;
    private NewtonCradleLoading progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        list = findViewById(R.id.select_user_rv);
        progress= findViewById(R.id.select_user_proress);
        data = new ArrayList<>();
        adapter = new UserAdapter(data, this,this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        progress.start();
        //TODO GET FRIENDS LIST
                progress.stop();
                progress.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                list.setVisibility(View.VISIBLE);

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
        //TODO User Selected
    }
}
