package ir.futurearts.esmfamil.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.Activity.AboutActivity;
import ir.futurearts.esmfamil.Activity.MainActivity;
import ir.futurearts.esmfamil.Activity.ProfileActivity;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.DialogActivity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private CircleImageView uimg;
    private TextView uname;
    private CardView profile, about, score, exit;


    //RETURN CODES
    private final int PROFILE_CODE= 1001;
    private final int EXIT_CODE= 1002;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_setting, container, false);

        uimg= v.findViewById(R.id.setting_img);
        uname= v.findViewById(R.id.setting_name);
        profile= v.findViewById(R.id.setting_profile);
        about= v.findViewById(R.id.setting_about);
        score= v.findViewById(R.id.setting_score);
        exit= v.findViewById(R.id.setting_exit);

        uimg.setImageDrawable(getDrawableByName(CurrentUser.getImg()));
        uname.setText(CurrentUser.getUsername());

        setonClicks();
        return v;
    }

    private void setonClicks() {
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), ProfileActivity.class), PROFILE_CODE);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutActivity.class));
            }
        });


        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), DialogActivity.class);
                intent.putExtra("type", "error");
                intent.putExtra("title", "هشدار");
                intent.putExtra("text", "مایل به خروج هستید؟");

                startActivityForResult(intent, EXIT_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PROFILE_CODE){
            if(resultCode == RESULT_OK){
                uimg.setImageDrawable(getDrawableByName(CurrentUser.getImg()));
            }
        }

        if(requestCode == EXIT_CODE){
            if(resultCode == RESULT_OK){
                CurrentUser.setLogin(false);
                getContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().commit();

                Intent intent= new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    private Drawable getDrawableByName(String name){
        Resources resources = getResources();
        final int resourceId = resources.getIdentifier(name, "drawable", getContext().getPackageName());
        return ContextCompat.getDrawable(getContext(),resourceId);
    }
}
