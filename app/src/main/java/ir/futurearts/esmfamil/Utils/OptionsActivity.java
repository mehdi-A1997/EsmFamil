package ir.futurearts.esmfamil.Utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ir.futurearts.esmfamil.R;

public class OptionsActivity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

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
}
