package ir.futurearts.esmfamil.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.Activity.GameDetailsActivity;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Interface.GameInterface;
import ir.futurearts.esmfamil.Module.GameM;
import ir.futurearts.esmfamil.Network.Responses.LoginResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.myHolder> {
    private LinkedList<GameM> data;
    private Context context;
    private GameInterface gi;

    public GameAdapter(LinkedList<GameM> data, Context context, GameInterface gi) {
        this.data = data;
        this.context = context;
        this.gi = gi;
    }
    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_game, parent, false);
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
        private CircleImageView uimg, oimg;
        private TextView uname, oname, uscore, oscore, result, letter;
        private View v, indicator;
        private LinearLayout sec;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            v= itemView;
            uimg= itemView.findViewById(R.id.game_userimg);
            oimg= itemView.findViewById(R.id.game_opponentimg);
            uname= itemView.findViewById(R.id.game_username);
            oname= itemView.findViewById(R.id.game_opponentname);
            uscore= itemView.findViewById(R.id.game_userscore);
            oscore= itemView.findViewById(R.id.game_opponentscore);
            result= itemView.findViewById(R.id.game_result);
            letter= itemView.findViewById(R.id.game_letter);
            indicator= itemView.findViewById(R.id.game_indicator);
            sec= itemView.findViewById(R.id.game_sec);
        }

        public void set(final GameM g, final int pos) {

            if((g.getUid()+"").equals(CurrentUser.getId())){
                uname.setText(CurrentUser.getUsername());

                if(g.getOid() != 0) {
                    Call<LoginResponse> call = RetrofitClient.getInstance()
                            .getUserApi().getUser(g.getOid() + "");

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.code() == 200)
                                oname.setText(response.body().getUser().getUsername());
                            else {
                                Log.d("MM", "ERROR");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.d("MM", "ERROR");
                        }
                    });
                }
                else
                    oname.setText("نا مشخص");
            }
            else{
                oname.setText(CurrentUser.getUsername());

                if(g.getUid() != 0) {
                    Call<LoginResponse> call = RetrofitClient.getInstance()
                            .getUserApi().getUser(g.getUid() + "");

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.code() == 200)
                                uname.setText(response.body().getUser().getUsername());
                            else {
                                Log.d("MM", "ERROR");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.d("MM", "ERROR");
                        }
                    });
                }
                else {
                    uname.setText("نا مشخص");
                }
            }

            uscore.setText("-");
            oscore.setText("-");
            letter.setText(g.getLetter());

           setStatus(g, pos);


           v.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   Intent intent= new Intent(context, GameDetailsActivity.class);
                   intent.putExtra("item", g.getItems());
                   context.startActivity(intent);
                   return true;
               }
           });

           v.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   setClicks(g, pos);
               }
           });
        }
        private void setStatus(GameM g, int pos) {
            if(g.getStatus() == 1){
                if((g.getUid()+"").equals(CurrentUser.getId()) && g.getUscore() != -1){
                    result.setText("در انتظار حریف");
                    result.setTextColor(ContextCompat.getColor(context,R.color.orange));
                    indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));
                    v.setElevation(2);
                    sec.setBackground(ContextCompat.getDrawable(context, R.drawable.game_notactive_bg));
                }
                else if((g.getOid()+"").equals(CurrentUser.getId()) && g.getOscore() != -1){
                    result.setText("در انتظار حریف");
                    result.setTextColor(ContextCompat.getColor(context,R.color.orange));
                    indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));
                    v.setElevation(2);
                    sec.setBackground(ContextCompat.getDrawable(context, R.drawable.game_notactive_bg));
                }
                else {
                    result.setText("شروع");
                    result.setTextColor(ContextCompat.getColor(context,R.color.blue));
                    indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.blue));
                    v.setElevation(8);
                    sec.setBackground(ContextCompat.getDrawable(context, R.drawable.game_active_bg));

                }
            }
            else if(g.getStatus() == 6){
                result.setText("تایید نشد");
                result.setTextColor(ContextCompat.getColor(context,R.color.red));
                indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.red));
                v.setElevation(2);
                sec.setBackground(ContextCompat.getDrawable(context, R.drawable.game_notactive_bg));
            }
            else if(g.getStatus() == 7){
                result.setText("منقضی شده");
                result.setTextColor(ContextCompat.getColor(context,R.color.gray));
                indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.gray));
                v.setElevation(2);
                sec.setBackground(ContextCompat.getDrawable(context, R.drawable.game_notactive_bg));
            }
            else if(g.getStatus() == 8){
                result.setText("در انتظار حریف");
                result.setTextColor(ContextCompat.getColor(context,R.color.orange));
                indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));
                v.setElevation(2);
                sec.setBackground(ContextCompat.getDrawable(context, R.drawable.game_notactive_bg));
            }
            else if(g.getStatus() == 9){
                if(CurrentUser.getId().equals(g.getOid()+"")){
                    result.setText("در انتظار تایید");
                    result.setTextColor(ContextCompat.getColor(context,R.color.blue));
                    indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.blue));
                    v.setElevation(8);
                    sec.setBackground(ContextCompat.getDrawable(context, R.drawable.game_active_bg));

                }
                else{
                    result.setText("در انتظار تایید");
                    result.setTextColor(ContextCompat.getColor(context,R.color.orange));
                    indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));
                    v.setElevation(2);
                    sec.setBackground(ContextCompat.getDrawable(context, R.drawable.game_notactive_bg));
                }
            }
            else{
                v.setElevation(2);
                sec.setBackground(ContextCompat.getDrawable(context, R.drawable.game_notactive_bg));
                uscore.setText(g.getUscore()+"");
                oscore.setText(g.getOscore()+"");
                if((g.getUid()+"").equals(CurrentUser.getId())){
                    if(g.getUscore()>g.getOscore()){
                        result.setText("برد");
                        result.setTextColor(ContextCompat.getColor(context,R.color.green));
                        indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
                    }
                    else if(g.getUscore()==g.getOscore()){
                        result.setText("مساوی");
                        result.setTextColor(ContextCompat.getColor(context,R.color.gray));
                        indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.gray));
                    }
                    else {
                        result.setText("باخت");
                        result.setTextColor(ContextCompat.getColor(context,R.color.red));
                        indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.red));
                    }
                }
                else {
                    if(g.getUscore()>g.getOscore()){
                        result.setText("باخت");
                        result.setTextColor(ContextCompat.getColor(context,R.color.red));
                        indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.red));
                    }
                    else if(g.getUscore()==g.getOscore()){
                        result.setText("مساوی");
                        result.setTextColor(ContextCompat.getColor(context,R.color.gray));
                        indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.gray));
                    }
                    else {
                        result.setText("برد");
                        result.setTextColor(ContextCompat.getColor(context,R.color.green));
                        indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
                    }
                }
            }
        }

        private void setClicks(GameM g, int pos) {
            if(g.getStatus() == 1 ){
                if(CurrentUser.getId().equals(g.getUid()+"") && g.getUscore() == -1)
                    gi.Play(g, pos);

                if(CurrentUser.getId().equals(g.getOid()+"") && g.getOscore() == -1)
                    gi.Play(g, pos);
            }
            else if(g.getStatus() == 2){
                gi.CompleteDetail(g);
            }
            else if(g.getStatus() == 9 && CurrentUser.getId().equals(g.getOid()+"")){
                gi.ManageRequest(g, pos);
            }
        }
    }




}
