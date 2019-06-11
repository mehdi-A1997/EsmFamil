package ir.futurearts.esmfamil.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ir.futurearts.esmfamil.R;

public class OptionsActivity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        setupTransition();
        initComponent();
        loadComponent();
        setOnClick();
    }

    private void initComponent() {
        lv=findViewById(R.id.selectoption_list);
    }

    private void loadComponent() {
        String []data=getIntent().getStringArrayExtra("list");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.layout_adapter,data);

        lv.setAdapter(arrayAdapter);
    }

    private void setOnClick() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("item",position+"");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    private void setupTransition() {
        Slide explode=new Slide();
        explode.setSlideEdge(Gravity.BOTTOM);
        explode.setDuration(500);
        getWindow().setEnterTransition(explode);

        Fade fade= new Fade();
        fade.setDuration(300);
        getWindow().setReturnTransition(fade);
    }
}
