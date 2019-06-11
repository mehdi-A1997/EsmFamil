package ir.futurearts.esmfamil.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.futurearts.esmfamil.adapter.ImageAdapter;
import ir.futurearts.esmfamil.interfaces.ImageInterface;
import ir.futurearts.esmfamil.R;

public class ChangeImageActivity extends AppCompatActivity implements ImageInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);

        RecyclerView list = findViewById(R.id.images_rv);
        List<String> data = new ArrayList<>();

        ImageAdapter adapter = new ImageAdapter(data, this, this);
        list.setAdapter(adapter);
        list.setLayoutManager(new GridLayoutManager(this,3));
        String []images= getResources().getStringArray(R.array.images);

        data.clear();
        Collections.addAll(data, images);
        adapter.notifyDataSetChanged();
        //Log.d("MM", data.get(0));
    }

    @Override
    public void imageSelected(String name) {
        Intent intent= new Intent();
        intent.putExtra("img", name);
        setResult(RESULT_OK, intent);
        finish();
    }
}
