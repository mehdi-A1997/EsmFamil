package ir.futurearts.esmfamil.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.module.UserM;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CustomProgress;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.myHolder> {

    private List<UserM> data;
    private Context context;
    private int lastPosition= -1;

    public FriendRequestAdapter(List<UserM> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friendrequest, parent, false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        UserM u= data.get(position);
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

        private CircleImageView uimg;
        private TextView username,uname;
        private ImageView okbtn,nobtn;
        public myHolder(@NonNull View itemView) {
            super(itemView);

            uimg= itemView.findViewById(R.id.friendrequest_row_img);
            username= itemView.findViewById(R.id.friendrequest_row_username);
            uname= itemView.findViewById(R.id.friendrequest_row_name);
            okbtn= itemView.findViewById(R.id.friendrequest_row_ok_btn);
            nobtn= itemView.findViewById(R.id.friendrequest_row_no_btn);
        }

        public void set(final UserM u) {

            uimg.setImageDrawable(getDrawableByName(u.getImg()));

            username.setText(u.getUsername());
            uname.setText(u.getName());

            okbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CustomProgress customProgress= new CustomProgress();
                    customProgress.showProgress(context, false);
                    Call<ResponseBody> call= RetrofitClient.getInstance()
                            .getUserApi().addFriend(CurrentUser.getId(), u.getId());

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            customProgress.hideProgress();
                            if(response.code() == 200){
                                okbtn.setVisibility(View.GONE);
                                nobtn.setVisibility(View.GONE);

                            }
                            else {
                                FancyToast.makeText(context, context.getString(R.string.systemError),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            customProgress.hideProgress();
                            FancyToast.makeText(context, context.getString(R.string.systemError),
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    });
                }
            });

            nobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CustomProgress customProgress= new CustomProgress();
                    customProgress.showProgress(context, false);
                    Call<ResponseBody> call= RetrofitClient.getInstance()
                            .getUserApi().declineFriend(CurrentUser.getId(), u.getId());

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            customProgress.hideProgress();
                            if(response.code() == 200){
                                okbtn.setVisibility(View.GONE);
                                nobtn.setVisibility(View.GONE);

                            }
                            else {
                                FancyToast.makeText(context, context.getString(R.string.systemError),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            customProgress.hideProgress();
                            FancyToast.makeText(context, context.getString(R.string.systemError),
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    });
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
