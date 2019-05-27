package ir.futurearts.esmfamil.Fragment.stepper;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import ir.futurearts.esmfamil.Activity.CompleteGameCreationActivity;
import ir.futurearts.esmfamil.Adapter.LetterAdapter;
import ir.futurearts.esmfamil.Interface.SelectLetterInterface;
import ir.futurearts.esmfamil.Module.LetterM;
import ir.futurearts.esmfamil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectLetterFragment extends Fragment implements Step, SelectLetterInterface {

    private RecyclerView list;
    private LetterAdapter adapter;
    private List<LetterM> data;

    private CompleteGameCreationActivity game;

    public SelectLetterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_select_letter, container, false);
        list= v.findViewById(R.id.selectletter_rv);
        data= new ArrayList<>();
        adapter= new LetterAdapter(data, getContext(), this);

        list.setAdapter(adapter);
        list.setLayoutManager(new GridLayoutManager(getContext(),3));

        String[] arr= getResources().getStringArray(R.array.letters);

        for(String s: arr)
            data.add(new LetterM(s));

        adapter.notifyDataSetChanged();

        game= (CompleteGameCreationActivity) getActivity();
        return v;

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(game.getLetter().equals("")){
            VerificationError v= new VerificationError("حرف را انتخاب کنید");
            return v;
        }

        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        FancyToast.makeText(getContext(), error.getErrorMessage(), FancyToast.LENGTH_LONG,
                FancyToast.ERROR, false).show();
    }

    @Override
    public void letterSelected(LetterM letter) {
        assert game != null;
        game.setLetter(letter.getLetter());
        verifyStep();
    }
}
