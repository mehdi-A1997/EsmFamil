package ir.futurearts.esmfamil.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.activity.FriendsActivity;
import ir.futurearts.esmfamil.activity.SelectFriendActivity;
import ir.futurearts.esmfamil.interfaces.UserInterface;
import ir.futurearts.esmfamil.module.UserM;
import ir.futurearts.esmfamil.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.myHolder> {

    private List<UserM>data;
    private Context context;
    private UserInterface ui;
    private int lastPosition= -1;

    public UserAdapter(List<UserM> data, Context context,UserInterface ui) {
        this.data = data;
        this.context = context;
        this.ui=ui;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user,parent,false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        UserM u=data.get(position);
        holder.set(u);
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

    public class myHolder extends RecyclerView.ViewHolder{
        private View v;
        private CircleImageView uimg;
        private TextView name,username,score,online;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            v=itemView;
            uimg=v.findViewById(R.id.user_row_img);
            name=v.findViewById(R.id.user_row_name);
            username=v.findViewById(R.id.user_row_username);
            score=v.findViewById(R.id.user_row_score);
            online=v.findViewById(R.id.user_row_oline);
        }

        public void set(final UserM u){
            uimg.setImageBitmap(null);
            uimg.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.ic_nouser));

            uimg.setImageDrawable(getDrawableByName(u.getImg()));

            if(u.getOnline()!=1){
                online.setVisibility(View.GONE);
            }
            else {
                online.setVisibility(View.VISIBLE);
            }
            name.setText(u.getName());
            username.setText(u.getUsername());
            score.setText(u.getScore());
            if(context instanceof SelectFriendActivity)
                v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ui.userSelected(u);
                }
            });

            if(context instanceof FriendsActivity)
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ui.userSelected(u);
                        return true;
                    }
                });
        }
    }
    private Drawable getDrawableByName(String name){
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(name, "drawable", context.getPackageName());
        return ContextCompat.getDrawable(context,resourceId);
    }
}
