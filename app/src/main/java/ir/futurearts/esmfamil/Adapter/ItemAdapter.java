package ir.futurearts.esmfamil.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.lguipeng.library.animcheckbox.AnimCheckBox;

import java.util.List;

import ir.futurearts.esmfamil.Activity.CompleteGameCreationActivity;
import ir.futurearts.esmfamil.Interface.SelectItemInterface;
import ir.futurearts.esmfamil.Module.ItemM;
import ir.futurearts.esmfamil.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.myHolder> {

    private List<ItemM> data;
    private Context context;
    private SelectItemInterface sii;

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
        if(context instanceof CompleteGameCreationActivity)
            holder.setCreation(i, position);
        else
            holder.setDetails(i);
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
