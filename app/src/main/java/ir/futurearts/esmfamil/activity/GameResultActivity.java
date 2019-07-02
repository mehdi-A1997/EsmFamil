package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.futurearts.esmfamil.adapter.DetailAdapter;
import ir.futurearts.esmfamil.module.DetailM;
import ir.futurearts.esmfamil.module.ItemsM;
import ir.futurearts.esmfamil.network.Responses.DetailResponse;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameResultActivity extends AppCompatActivity {

    private DetailAdapter adapter;
    private List<DetailM> data;
    private String letter= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_game_result);

        String gid= getIntent().getStringExtra("id");
        String uid= getIntent().getStringExtra("uid");
        String oid= getIntent().getStringExtra("oid");
        letter= getIntent().getStringExtra("letter");
        Log.d("MM", gid);

        RecyclerView list = findViewById(R.id.detail_rv);
        data= new ArrayList<>();
        adapter= new DetailAdapter(data, this);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        Call<DetailResponse> call= RetrofitClient.getInstance()
                .getGameApi().getDetail(gid, uid, oid);

        call.enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<DetailResponse> call, @NonNull Response<DetailResponse> response) {
                if(response.code() == 200){
                    DetailResponse dr= response.body();

                    assert dr != null;
                    addItemsToCollection(dr.getUitem(), dr.getOitem());
                }
                else {
                    try {
                        assert response.errorBody() != null;
                        Log.d("MM", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FancyToast.makeText(GameResultActivity.this, getString(R.string.systemError),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailResponse> call, @NonNull Throwable t) {
                Log.d("MM", t.getMessage());
                FancyToast.makeText(GameResultActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }

    private void addItemsToCollection(ItemsM uitem, ItemsM oitem) {
        if(!uitem.getName().equals("-1")){
            if(uitem.getName().equals(oitem.getName()) && !uitem.getName().equals("")){
                if(uitem.getName().startsWith(letter))
                    data.add(new DetailM("اسم", uitem.getName(), "+5", oitem.getName(), "+5"));
                else
                    data.add(new DetailM("اسم", uitem.getName(), "0", oitem.getName(), "0"));
            }
            else if(uitem.getName().equals("") && !oitem.getName().equals("")){
                if(oitem.getName().startsWith(letter))
                    data.add(new DetailM("اسم", "-", "0", oitem.getName(), "+10"));
                else
                    data.add(new DetailM("اسم", "-", "0", oitem.getName(), "0"));
            }
            else if(!uitem.getName().equals("") && oitem.getName().equals("")){
                if(uitem.getName().startsWith(letter))
                    data.add(new DetailM("اسم", uitem.getName(), "+10", "-", "0"));
                else
                    data.add(new DetailM("اسم", uitem.getName(), "0", "-", "0"));
            }
            else if(!uitem.getName().equals("") && !oitem.getName().equals("")) {
                if(uitem.getName().startsWith(letter) && oitem.getName().startsWith(letter))
                    data.add(new DetailM("اسم", uitem.getName(), "+10", oitem.getName(), "+10"));
                else{
                    if(uitem.getName().startsWith(letter))
                        data.add(new DetailM("اسم", uitem.getName(), "+10", oitem.getName(), "0"));
                    else if(oitem.getName().startsWith(letter))
                        data.add(new DetailM("اسم", uitem.getName(), "0", oitem.getName(), "+10"));
                }
            }
        }

        if(!uitem.getFamily().equals("-1")){
            if(uitem.getFamily().equals(oitem.getFamily()) && !uitem.getFamily().equals("")){
                if(uitem.getFamily().startsWith(letter))
                    data.add(new DetailM("فامیل", uitem.getFamily(), "+5", oitem.getFamily(), "+5"));
                else
                    data.add(new DetailM("فامیل", uitem.getFamily(), "0", oitem.getFamily(), "0"));
            }
            else if(uitem.getFamily().equals("") && !oitem.getFamily().equals("")){
                if(oitem.getFamily().startsWith(letter))
                    data.add(new DetailM("فامیل", "-", "0", oitem.getFamily(), "+10"));
                else
                    data.add(new DetailM("فامیل", "-", "0", oitem.getFamily(), "0"));
            }
            else if(!uitem.getFamily().equals("") && oitem.getFamily().equals("")){
                if(uitem.getFamily().startsWith(letter))
                    data.add(new DetailM("فامیل", uitem.getFamily(), "+10", "-", "0"));
                else
                    data.add(new DetailM("فامیل", uitem.getFamily(), "0", "-", "0"));
            }
            else if(!uitem.getFamily().equals("") && !oitem.getFamily().equals("")) {
                if(uitem.getFamily().startsWith(letter) && oitem.getFamily().startsWith(letter))
                    data.add(new DetailM("فامیل", uitem.getFamily(), "+10", oitem.getFamily(), "+10"));
                else{
                    if(uitem.getFamily().startsWith(letter))
                        data.add(new DetailM("فامیل", uitem.getFamily(), "+10", oitem.getFamily(), "0"));
                    else if(oitem.getFamily().startsWith(letter))
                        data.add(new DetailM("فامیل", uitem.getFamily(), "0", oitem.getFamily(), "+10"));
                }
            }
        }

        if(!uitem.getCity().equals("-1")){
            if(uitem.getCity().equals(oitem.getCity()) && !uitem.getCity().equals("")){
                if(uitem.getCity().startsWith(letter))
                    data.add(new DetailM("شهر", uitem.getCity(), "+5", oitem.getCity(), "+5"));
                else
                    data.add(new DetailM("شهر", uitem.getCity(), "0", oitem.getCity(), "0"));
            }
            else if(uitem.getCity().equals("") && !oitem.getCity().equals("")){
                if(oitem.getCity().startsWith(letter))
                    data.add(new DetailM("شهر", "-", "0", oitem.getCity(), "+10"));
                else
                    data.add(new DetailM("شهر", "-", "0", oitem.getCity(), "0"));
            }
            else if(!uitem.getCity().equals("") && oitem.getCity().equals("")){
                if(uitem.getCity().startsWith(letter))
                    data.add(new DetailM("شهر", uitem.getCity(), "+10", "-", "0"));
                else
                    data.add(new DetailM("شهر", uitem.getCity(), "0", "-", "0"));
            }
            else if(!uitem.getCity().equals("") && !oitem.getCity().equals("")) {
                if(uitem.getCity().startsWith(letter) && oitem.getCity().startsWith(letter))
                    data.add(new DetailM("شهر", uitem.getCity(), "+10", oitem.getCity(), "+10"));
                else{
                    if(uitem.getCity().startsWith(letter))
                        data.add(new DetailM("شهر", uitem.getCity(), "+10", oitem.getCity(), "0"));
                    else if(oitem.getCity().startsWith(letter))
                        data.add(new DetailM("شهر", uitem.getCity(), "0", oitem.getCity(), "+10"));
                }
            }
        }

        if(!uitem.getCountry().equals("-1")){
            if(uitem.getCountry().equals(oitem.getCountry()) && !uitem.getCountry().equals("")){
                if(uitem.getCountry().startsWith(letter))
                    data.add(new DetailM("کشور", uitem.getCountry(), "+5", oitem.getCountry(), "+5"));
                else
                    data.add(new DetailM("کشور", uitem.getCountry(), "0", oitem.getCountry(), "0"));
            }
            else if(uitem.getCountry().equals("") && !oitem.getCountry().equals("")){
                if(oitem.getCountry().startsWith(letter))
                    data.add(new DetailM("کشور", "-", "0", oitem.getCountry(), "+10"));
                else
                    data.add(new DetailM("کشور", "-", "0", oitem.getCountry(), "0"));
            }
            else if(!uitem.getCountry().equals("") && oitem.getCountry().equals("")){
                if(uitem.getCountry().startsWith(letter))
                    data.add(new DetailM("کشور", uitem.getCountry(), "+10", "-", "0"));
                else
                    data.add(new DetailM("کشور", uitem.getCountry(), "0", "-", "0"));
            }
            else if(!uitem.getCountry().equals("") && !oitem.getCountry().equals("")) {
                if(uitem.getCountry().startsWith(letter) && oitem.getCountry().startsWith(letter))
                    data.add(new DetailM("کشور", uitem.getCountry(), "+10", oitem.getCountry(), "+10"));
                else{
                    if(uitem.getCountry().startsWith(letter))
                        data.add(new DetailM("کشور", uitem.getCountry(), "+10", oitem.getCountry(), "0"));
                    else if(oitem.getCountry().startsWith(letter))
                        data.add(new DetailM("کشور", uitem.getCountry(), "0", oitem.getCountry(), "+10"));
                }
            }
        }

        if(!uitem.getFood().equals("-1")){
            if(uitem.getFood().equals(oitem.getFood()) && !uitem.getFood().equals("")){
                if(uitem.getFood().startsWith(letter))
                    data.add(new DetailM("غذا", uitem.getFood(), "+5", oitem.getFood(), "+5"));
                else
                    data.add(new DetailM("غذا", uitem.getFood(), "0", oitem.getFood(), "0"));
            }
            else if(uitem.getFood().equals("") && !oitem.getFood().equals("")){
                if(oitem.getFood().startsWith(letter))
                    data.add(new DetailM("غذا", "-", "0", oitem.getFood(), "+10"));
                else
                    data.add(new DetailM("غذا", "-", "0", oitem.getFood(), "0"));
            }
            else if(!uitem.getFood().equals("") && oitem.getFood().equals("")){
                if(uitem.getFood().startsWith(letter))
                    data.add(new DetailM("غذا", uitem.getFood(), "+10", "-", "0"));
                else
                    data.add(new DetailM("غذا", uitem.getFood(), "0", "-", "0"));
            }
            else if(!uitem.getFood().equals("") && !oitem.getFood().equals("")) {
                if(uitem.getFood().startsWith(letter) && oitem.getFood().startsWith(letter))
                    data.add(new DetailM("غذا", uitem.getFood(), "+10", oitem.getFood(), "+10"));
                else{
                    if(uitem.getFood().startsWith(letter))
                        data.add(new DetailM("غذا", uitem.getFood(), "+10", oitem.getFood(), "0"));
                    else if(oitem.getFood().startsWith(letter))
                        data.add(new DetailM("غذا", uitem.getFood(), "0", oitem.getFood(), "+10"));
                }
            }
        }

        if(!uitem.getAnimal().equals("-1")){
            if(uitem.getAnimal().equals(oitem.getAnimal()) && !uitem.getAnimal().equals("")){
                if(uitem.getAnimal().startsWith(letter))
                    data.add(new DetailM("حیوان", uitem.getAnimal(), "+5", oitem.getAnimal(), "+5"));
                else
                    data.add(new DetailM("حیوان", uitem.getAnimal(), "0", oitem.getAnimal(), "0"));
            }
            else if(uitem.getAnimal().equals("") && !oitem.getAnimal().equals("")){
                if(oitem.getAnimal().startsWith(letter))
                    data.add(new DetailM("حیوان", "-", "0", oitem.getAnimal(), "+10"));
                else
                    data.add(new DetailM("حیوان", "-", "0", oitem.getAnimal(), "0"));
            }
            else if(!uitem.getAnimal().equals("") && oitem.getAnimal().equals("")){
                if(uitem.getAnimal().startsWith(letter))
                    data.add(new DetailM("حیوان", uitem.getAnimal(), "+10", "-", "0"));
                else
                    data.add(new DetailM("حیوان", uitem.getAnimal(), "0", "-", "0"));
            }
            else if(!uitem.getAnimal().equals("") && !oitem.getAnimal().equals("")) {
                if(uitem.getAnimal().startsWith(letter) && oitem.getAnimal().startsWith(letter))
                    data.add(new DetailM("حیوان", uitem.getAnimal(), "+10", oitem.getAnimal(), "+10"));
                else{
                    if(uitem.getAnimal().startsWith(letter))
                        data.add(new DetailM("حیوان", uitem.getAnimal(), "+10", oitem.getAnimal(), "0"));
                    else if(oitem.getAnimal().startsWith(letter))
                        data.add(new DetailM("حیوان", uitem.getAnimal(), "0", oitem.getAnimal(), "+10"));
                }
            }
        }

        if(!uitem.getColor().equals("-1")){
            if(uitem.getColor().equals(oitem.getColor()) && !uitem.getColor().equals("")){
                if(uitem.getColor().startsWith(letter))
                    data.add(new DetailM("رنگ", uitem.getColor(), "+5", oitem.getColor(), "+5"));
                else
                    data.add(new DetailM("رنگ", uitem.getColor(), "0", oitem.getColor(), "0"));
            }
            else if(uitem.getColor().equals("") && !oitem.getColor().equals("")){
                if(oitem.getColor().startsWith(letter))
                    data.add(new DetailM("رنگ", "-", "0", oitem.getColor(), "+10"));
                else
                    data.add(new DetailM("رنگ", "-", "0", oitem.getColor(), "0"));
            }
            else if(!uitem.getColor().equals("") && oitem.getColor().equals("")){
                if(uitem.getColor().startsWith(letter))
                    data.add(new DetailM("رنگ", uitem.getColor(), "+10", "-", "0"));
                else
                    data.add(new DetailM("رنگ", uitem.getColor(), "0", "-", "0"));
            }
            else if(!uitem.getColor().equals("") && !oitem.getColor().equals("")) {
                if(uitem.getColor().startsWith(letter) && oitem.getColor().startsWith(letter))
                    data.add(new DetailM("رنگ", uitem.getColor(), "+10", oitem.getColor(), "+10"));
                else{
                    if(uitem.getColor().startsWith(letter))
                        data.add(new DetailM("رنگ", uitem.getColor(), "+10", oitem.getColor(), "0"));
                    else if(oitem.getColor().startsWith(letter))
                        data.add(new DetailM("رنگ", uitem.getColor(), "0", oitem.getColor(), "+10"));
                }
            }
        }

        if(!uitem.getFlower().equals("-1")){
            if(uitem.getFlower().equals(oitem.getFlower()) && !uitem.getFlower().equals("")){
                if(uitem.getFlower().startsWith(letter))
                    data.add(new DetailM("گل", uitem.getFlower(), "+5", oitem.getFlower(), "+5"));
                else
                    data.add(new DetailM("گل", uitem.getFlower(), "0", oitem.getFlower(), "0"));
            }
            else if(uitem.getFlower().equals("") && !oitem.getFlower().equals("")){
                if(oitem.getFlower().startsWith(letter))
                    data.add(new DetailM("گل", "-", "0", oitem.getFlower(), "+10"));
                else
                    data.add(new DetailM("گل", "-", "0", oitem.getFlower(), "0"));
            }
            else if(!uitem.getFlower().equals("") && oitem.getFlower().equals("")){
                if(uitem.getFlower().startsWith(letter))
                    data.add(new DetailM("گل", uitem.getFlower(), "+10", "-", "0"));
                else
                    data.add(new DetailM("گل", uitem.getFlower(), "0", "-", "0"));
            }
            else if(!uitem.getFlower().equals("") && !oitem.getFlower().equals("")) {
                if(uitem.getFlower().startsWith(letter) && oitem.getFlower().startsWith(letter))
                    data.add(new DetailM("گل", uitem.getFlower(), "+10", oitem.getFlower(), "+10"));
                else{
                    if(uitem.getFlower().startsWith(letter))
                        data.add(new DetailM("گل", uitem.getFlower(), "+10", oitem.getFlower(), "0"));
                    else if(oitem.getFlower().startsWith(letter))
                        data.add(new DetailM("گل", uitem.getFlower(), "0", oitem.getFlower(), "+10"));
                }
            }
        }

        if(!uitem.getFruit().equals("-1")){
            if(uitem.getFruit().equals(oitem.getFruit()) && !uitem.getFruit().equals("")){
                if(uitem.getFruit().startsWith(letter))
                    data.add(new DetailM("میوه", uitem.getFruit(), "+5", oitem.getFruit(), "+5"));
                else
                    data.add(new DetailM("میوه", uitem.getFruit(), "0", oitem.getFruit(), "0"));
            }
            else if(uitem.getFruit().equals("") && !oitem.getFruit().equals("")){
                if(oitem.getFruit().startsWith(letter))
                    data.add(new DetailM("میوه", "-", "0", oitem.getFruit(), "+10"));
                else
                    data.add(new DetailM("میوه", "-", "0", oitem.getFruit(), "0"));
            }
            else if(!uitem.getFruit().equals("") && oitem.getFruit().equals("")){
                if(uitem.getFruit().startsWith(letter))
                    data.add(new DetailM("میوه", uitem.getFruit(), "+10", "-", "0"));
                else
                    data.add(new DetailM("میوه", uitem.getFruit(), "0", "-", "0"));
            }
            else if(!uitem.getFruit().equals("") && !oitem.getFruit().equals("")) {
                if(uitem.getFruit().startsWith(letter) && oitem.getFruit().startsWith(letter))
                    data.add(new DetailM("میوه", uitem.getFruit(), "+10", oitem.getFruit(), "+10"));
                else{
                    if(uitem.getFruit().startsWith(letter))
                        data.add(new DetailM("میوه", uitem.getFruit(), "+10", oitem.getFruit(), "0"));
                    else if(oitem.getFruit().startsWith(letter))
                        data.add(new DetailM("میوه", uitem.getFruit(), "0", oitem.getFruit(), "+10"));
                }
            }
        }

        if(!uitem.getObject().equals("-1")){
            if(uitem.getObject().equals(oitem.getObject()) && !uitem.getObject().equals("")){
                if(uitem.getObject().startsWith(letter))
                    data.add(new DetailM("اشیاء", uitem.getObject(), "+5", oitem.getObject(), "+5"));
                else
                    data.add(new DetailM("اشیاء", uitem.getObject(), "0", oitem.getObject(), "0"));
            }
            else if(uitem.getObject().equals("") && !oitem.getObject().equals("")){
                if(oitem.getObject().startsWith(letter))
                    data.add(new DetailM("اشیاء", "-", "0", oitem.getObject(), "+10"));
                else
                    data.add(new DetailM("اشیاء", "-", "0", oitem.getObject(), "0"));
            }
            else if(!uitem.getObject().equals("") && oitem.getObject().equals("")){
                if(uitem.getObject().startsWith(letter))
                    data.add(new DetailM("اشیاء", uitem.getObject(), "+10", "-", "0"));
                else
                    data.add(new DetailM("اشیاء", uitem.getObject(), "0", "-", "0"));
            }
            else if(!uitem.getObject().equals("") && !oitem.getObject().equals("")) {
                if(uitem.getObject().startsWith(letter) && oitem.getObject().startsWith(letter))
                    data.add(new DetailM("اشیاء", uitem.getObject(), "+10", oitem.getObject(), "+10"));
                else{
                    if(uitem.getObject().startsWith(letter))
                        data.add(new DetailM("اشیاء", uitem.getObject(), "+10", oitem.getObject(), "0"));
                    else if(oitem.getObject().startsWith(letter))
                        data.add(new DetailM("اشیاء", uitem.getObject(), "0", oitem.getObject(), "+10"));
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}
