package ir.futurearts.esmfamil.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.futurearts.esmfamil.adapter.ItemAdapter;
import ir.futurearts.esmfamil.module.ItemM;
import ir.futurearts.esmfamil.module.ItemsM;
import ir.futurearts.esmfamil.R;

public class GameDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_game_details);

        RecyclerView list = findViewById(R.id.gamedetails_rv);
        List<ItemM> data = new ArrayList<>();
        ItemAdapter adapter = new ItemAdapter(data, this, null);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        ItemsM itemsM= (ItemsM) getIntent().getSerializableExtra("item");
        Log.d("MM", itemsM.getCity());
        data.add(new ItemM("اسم", Integer.parseInt(itemsM.getName())));
        data.add(new ItemM("فامیل", Integer.parseInt(itemsM.getFamily())));
        data.add(new ItemM("شهر", Integer.parseInt(itemsM.getCity())));
        data.add(new ItemM("کشور", Integer.parseInt(itemsM.getCountry())));
        data.add(new ItemM("غذا", Integer.parseInt(itemsM.getFood())));
        data.add(new ItemM("حیوان", Integer.parseInt(itemsM.getAnimal())));
        data.add(new ItemM("رنگ", Integer.parseInt(itemsM.getColor())));
        data.add(new ItemM("گل", Integer.parseInt(itemsM.getFlower())));
        data.add(new ItemM("میوه", Integer.parseInt(itemsM.getFruit())));
        data.add(new ItemM("اشیاء", Integer.parseInt(itemsM.getObject())));

        adapter.notifyDataSetChanged();
    }
}
