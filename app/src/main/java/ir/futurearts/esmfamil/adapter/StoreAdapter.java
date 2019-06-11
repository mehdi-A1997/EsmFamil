package ir.futurearts.esmfamil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.futurearts.esmfamil.interfaces.StoreInterface;
import ir.futurearts.esmfamil.module.StoreM;
import ir.futurearts.esmfamil.R;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.myHolder> {
    private List<StoreM> data;
    private Context context;
    private StoreInterface si;

    public StoreAdapter(List<StoreM> data, Context context, StoreInterface si) {
        this.data = data;
        this.context = context;
        this.si = si;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_store, parent, false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        StoreM s= data.get(position);
        holder.set(s);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView count, price;
        private View v;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.store_img);
            count = itemView.findViewById(R.id.store_count);
            price = itemView.findViewById(R.id.store_price);
            v = itemView;
        }

        public void set(final StoreM s) {
            switch (s.getSku()){
                case "futurearts_esmfamil_100":
                    img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.coin100));
                    break;

                case "futurearts_esmfamil_200":
                    img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.coin200));
                    break;

                case "futurearts_esmfamil_500":
                    img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.coin500));
                    break;

                case "futurearts_esmfamil_1000":
                    img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.coin1000));
                    break;
            }
            count.setText(s.getName());
            price.setText(s.getPrice());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    si.Selected(s);
                }
            });
        }

    }
}
