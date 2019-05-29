package ir.futurearts.esmfamil.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.Interface.GameRequestInterface;
import ir.futurearts.esmfamil.Module.GameM;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.Network.Responses.LoginResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRequestAdapter extends RecyclerView.Adapter<GameRequestAdapter.myHolder> {

    private List<GameM> data;
    private Context context;
    private GameRequestInterface gri;

    public GameRequestAdapter(List<GameM> data, Context context, GameRequestInterface gri) {
        this.data = data;
        this.context = context;
        this.gri = gri;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friendrequest, parent, false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        GameM g= data.get(position);
        holder.set(g, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myHolder extends RecyclerView.ViewHolder{
        private CircleImageView uimg;
        private TextView username,uname;
        private ImageView okbtn,nobtn;

         myHolder(@NonNull View itemView) {
            super(itemView);

            uimg= itemView.findViewById(R.id.friendrequest_row_img);
            username= itemView.findViewById(R.id.friendrequest_row_username);
            uname= itemView.findViewById(R.id.friendrequest_row_name);
            okbtn= itemView.findViewById(R.id.friendrequest_row_ok_btn);
            nobtn= itemView.findViewById(R.id.friendrequest_row_no_btn);
        }

        public void set(final GameM g, final int pos) {
            getUserInfo(g.getUid());

            okbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gri.Accept(g, pos);
                }
            });

            nobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gri.Decline(g, pos);
                }
            });
        }

        private void getUserInfo(int uid) {
            Call<LoginResponse> call= RetrofitClient.getInstance()
                    .getUserApi().getUser(uid+"");

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.code() == 200){
                        UserM u= response.body().getUser();

                        username.setText(u.getUsername());
                        uname.setText(u.getName());
                        uimg.setImageDrawable(getDrawableByName(u.getImg()));
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

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
