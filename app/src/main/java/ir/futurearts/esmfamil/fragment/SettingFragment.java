package ir.futurearts.esmfamil.fragment;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.futurearts.esmfamil.activity.AboutActivity;
import ir.futurearts.esmfamil.activity.MainActivity;
import ir.futurearts.esmfamil.activity.ProfileActivity;
import ir.futurearts.esmfamil.activity.StoreActivity;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.DialogActivity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private CircleImageView uimg;
    private CardView profile, about, store, score, exit;


    //RETURN CODES
    private final int PROFILE_CODE= 1001;
    private final int EXIT_CODE= 1002;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_setting, container, false);

        uimg= v.findViewById(R.id.setting_img);
        TextView uname = v.findViewById(R.id.setting_name);
        profile= v.findViewById(R.id.setting_profile);
        about= v.findViewById(R.id.setting_about);
        store= v.findViewById(R.id.setting_store);
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
                startActivityForResult(new Intent(getContext(), ProfileActivity.class), PROFILE_CODE
                ,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), AboutActivity.class);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), StoreActivity.class));
            }
        });


        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url= "myket://comment?id="+ Objects.requireNonNull(getContext()).getPackageName();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
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
                Objects.requireNonNull(getContext()).getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().apply();

                Intent intent= new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    private Drawable getDrawableByName(String name){
        Resources resources = getResources();
        final int resourceId = resources.getIdentifier(name, "drawable", Objects.requireNonNull(getContext()).getPackageName());
        return ContextCompat.getDrawable(getContext(),resourceId);
    }
}
