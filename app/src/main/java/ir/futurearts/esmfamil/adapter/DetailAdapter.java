package ir.futurearts.esmfamil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.futurearts.esmfamil.module.DetailM;
import ir.futurearts.esmfamil.R;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.myHolder> {
    private List<DetailM> data;
    private Context context;

    public DetailAdapter(List<DetailM> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail, parent, false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        DetailM dt= data.get(position);
        holder.set(dt);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myHolder extends RecyclerView.ViewHolder{
        private TextView title, utext, uscore, otext, oscore;
         myHolder(@NonNull View itemView) {
            super(itemView);
             utext= itemView.findViewById(R.id.detail_utext);
             uscore= itemView.findViewById(R.id.detail_uscore);
             otext= itemView.findViewById(R.id.detail_otext);
             oscore= itemView.findViewById(R.id.detail_oscore);
             title= itemView.findViewById(R.id.detail_title);
        }

        public void set(DetailM dt){
             title.setText(dt.getTitle());
             utext.setText(dt.getUtext());
             uscore.setText(dt.getUscore());
             otext.setText(dt.getOtext());
             oscore.setText(dt.getOscore());
        }
    }
}
