package ir.futurearts.esmfamil.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.myHolder> {

    private List<UserM> data;
    private Context context;
    private Drawable defultbg=null;
    public RankAdapter(List<UserM> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row,parent,false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        UserM u=data.get(position);
        holder.set(u,position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{
        private View v;
        private CardView main;
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

            if(defultbg==null)
                defultbg=main.getBackground();

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
                        main.setBackground(defultbg);
                        username.setTextColor(ContextCompat.getColor(context,R.color.gray));

            }

            if(u.getImg()!=null){
                u.getImg().getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        uimg.setImageDrawable(null);
                        uimg.setImageBitmap(bitmap);
                    }
                });
            }
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
        final ParseUser c=ParseUser.getCurrentUser();

        if(id.equals(c.getObjectId()))
            return;

        final CustomProgress customProgress=new CustomProgress();
        customProgress.showProgress(context,false);
        ParseQuery<ParseObject> q1=new ParseQuery<>("Friends");
        q1.whereEqualTo("Uid",id);
        q1.whereEqualTo("Cid",c.getObjectId());

       final ParseQuery<ParseObject> q2=new ParseQuery<>("FriendRequests");
        q2.whereEqualTo("Cid",c.getObjectId());
        q2.whereEqualTo("Uid",id);
        q1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()==0){
                    q2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(objects.size()==0){
                                ParseObject req=new ParseObject("FriendRequests");
                                req.put("Cid",c.getObjectId());
                                req.put("Uid",id);

                                req.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e==null){
                                            //TODO SENT
                                            customProgress.hideProgress();
                                            Toast.makeText(context, "Sent Succesfully", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else{
                                //TODO ALREADY SENT
                                customProgress.hideProgress();
                                Toast.makeText(context, "Already Sent", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    //TODO ALREADY FRIENDS
                    customProgress.hideProgress();
                    Toast.makeText(context, "Already Friends", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
