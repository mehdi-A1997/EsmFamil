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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.interfaces.AddFriendInterface;
import ir.futurearts.esmfamil.module.UserM;
import ir.futurearts.esmfamil.R;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.myHolder> {

    private List<UserM> data;
    private Context context;
    private AddFriendInterface adi;
    private int lastPosition= -1;
    public RankAdapter(List<UserM> data, Context context,AddFriendInterface adi) {
        this.data = data;
        this.context = context;
        this.adi=adi;
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
        holder.set(u,position);
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
        private ConstraintLayout main;
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
            main=v.findViewById(R.id.user_row_card);
        }

        public void set(final UserM u,int p){


            uimg.setImageBitmap(null);
            uimg.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.ic_nouser));

            switch (p){
                case 0:
                    main.setBackground(ContextCompat.getDrawable(context,R.drawable.gold_bg));
                    username.setTextColor(ContextCompat.getColor(context,R.color.white));
                    break;

                case 1:
                    main.setBackground(ContextCompat.getDrawable(context,R.drawable.silver_bg));
                    username.setTextColor(ContextCompat.getColor(context,R.color.white));
                    break;

                case 2:
                    main.setBackground(ContextCompat.getDrawable(context,R.drawable.boronze_bg));
                    username.setTextColor(ContextCompat.getColor(context,R.color.white));
                    break;
                    default:
                        main.setBackground(ContextCompat.getDrawable(context,R.drawable.game_notactive_bg));
                        username.setTextColor(ContextCompat.getColor(context,R.color.gray));

            }

            uimg.setImageDrawable(getDrawableByName(u.getImg()));

            online.setVisibility(View.GONE);
            name.setText(u.getName());
            username.setText(u.getUsername());
            score.setText(u.getScore());

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequest(u.getId());
                }
            });
        }
    }

    private void sendRequest(final String id) {

        if(id.equals(CurrentUser.getId()))
            return;
        adi.SendRequest(id);
    }

    private Drawable getDrawableByName(String name){
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(name, "drawable", context.getPackageName());
        return ContextCompat.getDrawable(context,resourceId);
    }
}
