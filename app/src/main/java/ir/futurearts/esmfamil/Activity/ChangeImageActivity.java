package ir.futurearts.esmfamil.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ir.futurearts.esmfamil.Adapter.ImageAdapter;
import ir.futurearts.esmfamil.Interface.ImageInterface;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.R;

public class ChangeImageActivity extends AppCompatActivity implements ImageInterface {

    private RecyclerView list;
    private ImageAdapter adapter;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);

        list= findViewById(R.id.images_rv);
        data= new ArrayList<>();

        adapter= new ImageAdapter(data, this, this);
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
