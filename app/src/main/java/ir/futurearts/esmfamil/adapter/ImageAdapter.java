package ir.futurearts.esmfamil.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.interfaces.ImageInterface;
import ir.futurearts.esmfamil.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.myHolder> {
    private List<String> data;
    private Context context;
    private ImageInterface ii;
    private int lastPosition= -1;

    public ImageAdapter(List<String> data, Context context, ImageInterface ii) {
        this.data = data;
        this.context = context;
        this.ii = ii;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_img, parent, false);
        Log.d("MM","created");
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        String  name= data.get(position);

        holder.set(name);
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
        private CircleImageView img;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.image_img);
        }

        public void set(final String name){
            img.setImageDrawable(getDrawableByName(name));

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ii.imageSelected(name);
                }
            });
        }

        private Drawable getDrawableByName(String name){
            Resources resources = context.getResources();
            final int resourceId = resources.getIdentifier(name, "drawable", context.getPackageName());
            return ContextCompat.getDrawable(context,resourceId);
        }
    }
}
