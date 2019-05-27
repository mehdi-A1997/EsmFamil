package ir.futurearts.esmfamil.Adapter;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import ir.futurearts.esmfamil.Fragment.stepper.SelectItemsFragment;
import ir.futurearts.esmfamil.Fragment.stepper.SelectLetterFragment;
import ir.futurearts.esmfamil.R;

public  class StepperAdapter extends AbstractFragmentStepAdapter {


    public StepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {

        switch (position){
            case 0:
                return new SelectLetterFragment();

            case 1:
                return new SelectItemsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        StepViewModel.Builder builder = new StepViewModel.Builder(context);

        switch (position){
            case 0:
                builder.setTitle("انتخاب حرف");
                builder.setEndButtonLabel("ادامه");
                break;

            case 1:
                builder.setTitle("انتخاب موضوعات");
                builder.setEndButtonLabel("ایجاد");
                builder.setBackButtonLabel("قبلی");
                break;
        }

        return builder.create();
    }
}
