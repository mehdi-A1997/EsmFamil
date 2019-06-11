package ir.futurearts.esmfamil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.lguipeng.library.animcheckbox.AnimCheckBox;

import java.util.List;

import ir.futurearts.esmfamil.activity.CompleteGameCreationActivity;
import ir.futurearts.esmfamil.interfaces.SelectItemInterface;
import ir.futurearts.esmfamil.module.ItemM;
import ir.futurearts.esmfamil.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.myHolder> {

    private List<ItemM> data;
    private Context context;
    private SelectItemInterface sii;
    private int lastPosition= -1;

    public ItemAdapter(List<ItemM> data, Context context, SelectItemInterface sii) {
        this.data = data;
        this.context = context;
        this.sii = sii;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        ItemM i= data.get(position);
        holder.setIsRecyclable(false);
        if(context instanceof CompleteGameCreationActivity)
            holder.setCreation(i, position);
        else
            holder.setDetails(i);

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myHolder extends RecyclerView.ViewHolder{
        private View v;
        private TextView name;
        private AnimCheckBox checkBox;
         myHolder(@NonNull View itemView) {
            super(itemView);

            v= itemView;
            name= v.findViewById(R.id.item_name);
            checkBox= v.findViewById(R.id.item_checkbox);
        }

        private void setCreation(final ItemM i, final int pos){
            name.setText(i.getName());

            checkBox.setOnCheckedChangeListener(new AnimCheckBox.OnCheckedChangeListener() {
                @Override
                public void onChange(AnimCheckBox view, boolean checked) {
                    i.setActive(!i.getActive());
                    sii.selectItem(i, pos);
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(!checkBox.isChecked());
                }
            });
         }

        private void setDetails(ItemM i) {
            name.setText(i.getName());
            checkBox.setEnabled(false);
            checkBox.setChecked(i.getActive());
        }
    }
}
