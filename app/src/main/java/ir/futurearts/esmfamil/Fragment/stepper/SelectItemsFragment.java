package ir.futurearts.esmfamil.Fragment.stepper;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.futurearts.esmfamil.Activity.CompleteGameCreationActivity;
import ir.futurearts.esmfamil.Adapter.ItemAdapter;
import ir.futurearts.esmfamil.Interface.SelectItemInterface;
import ir.futurearts.esmfamil.Module.ItemM;
import ir.futurearts.esmfamil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectItemsFragment extends Fragment implements Step, SelectItemInterface {

    private RecyclerView list;
    private ItemAdapter adapter;
    private List<ItemM> data;

    private CompleteGameCreationActivity game;
    private int active=10;

    public SelectItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_select_items, container, false);
        list= v.findViewById(R.id.items_rv);
        data= new ArrayList<>();
        adapter= new ItemAdapter(data, getContext(),this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        String [] items= getResources().getStringArray(R.array.items);

        for(String d: items)
            data.add(new ItemM(d,true));

        adapter.notifyDataSetChanged();
        game= (CompleteGameCreationActivity) getActivity();

        return v;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(active <3){
            return new VerificationError("حداقل 3 مورد را انتخاب کنید");
        }
        JSONObject object= new JSONObject();
        try {
            object.put("name", data.get(0).getActive1());
            object.put("family", data.get(1).getActive1());
            object.put("city", data.get(2).getActive1());
            object.put("country", data.get(3).getActive1());
            object.put("food", data.get(4).getActive1());
            object.put("animal", data.get(5).getActive1());
            object.put("color", data.get(6).getActive1());
            object.put("flower", data.get(7).getActive1());
            object.put("fruit", data.get(8).getActive1());
            object.put("object", data.get(9).getActive1());

            Log.d("MM", object.toString());
            game.setItems(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void selectItem(ItemM i, int pos) {
        if(data.get(pos).getActive())
            active++;
        else
            active--;
        data.get(pos).setActive(i.getActive());
    }
}
