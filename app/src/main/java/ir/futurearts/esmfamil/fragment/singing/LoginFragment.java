package ir.futurearts.esmfamil.fragment.singing;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.util.Objects;

import ir.futurearts.esmfamil.activity.MainActivity;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.interfaces.LoginInterface;
import ir.futurearts.esmfamil.module.UserM;
import ir.futurearts.esmfamil.network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.network.Responses.LoginResponse;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CustomProgress;
import ir.futurearts.esmfamil.utils.DialogActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText username,password;
    private Button loginbtn;
    private SharedPreferences.Editor editor;

    private LoginInterface LI;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_login, container, false);
        username=v.findViewById(R.id.login_username_txt);
        password=v.findViewById(R.id.login_password_txt);
        loginbtn=v.findViewById(R.id.login_btn);
        TextView forgetPassword = v.findViewById(R.id.login_forget_btn);
        TextView createAccount = v.findViewById(R.id.login_create_btn);

        loginbtn.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.disable_btn));
        loginbtn.setEnabled(false);

        SharedPreferences mPref = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        editor= mPref.edit();

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Check();
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Check();
            }
        });


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO foreget Password
                Intent intent= new Intent(getContext(), DialogActivity.class);
                intent.putExtra("type", "singleS");
                intent.putExtra("title", "به زودی");
                intent.putExtra("text", "به زودی...");
                startActivity(intent);
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LI.Change(2);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomProgress customProgress=new CustomProgress();
                customProgress.showProgress(getContext(),false);

                Call<LoginResponse> call= RetrofitClient
                        .getInstance()
                        .getUserApi()
                        .LogIn(username.getText().toString().trim(),password.getText().toString().trim());

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        customProgress.hideProgress();
                        if(response.code() == 200){
                            LoginResponse lr= response.body();
                            assert lr != null;
                            UserM u= lr.getUser();
                            editor.putString("name", u.getName());
                            editor.putString("username", u.getUsername());
                            editor.putString("id", u.getId());
                            editor.putString("email", u.getEmail());
                            editor.putString("online", "1");
                            editor.putString("img", u.getImg());
                            editor.putString("score", u.getScore());
                            editor.putString("coin", u.getCoin());
                            editor.putString("token", u.getToken());
                            editor.putBoolean("login", true);

                            editor.apply();

                            CurrentUser.setId(u.getId());
                            CurrentUser.setName(u.getName());
                            CurrentUser.setUsername(u.getUsername());
                            CurrentUser.setEmail(u.getEmail());
                            CurrentUser.setImg(u.getImg());
                            CurrentUser.setOnline(u.getOnline()+"");
                            CurrentUser.setScore(u.getScore());
                            CurrentUser.setCoin(u.getCoin());
                            CurrentUser.setLogin(true);
                            CurrentUser.setToken(u.getToken());

                            Intent intent= new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            LI.Finish();
                        }
                        else {
                            try {
                                assert response.errorBody() != null;
                                DefaultResponse er= new DefaultResponse(response.errorBody().string());

                                Intent intent= new Intent(getContext(), DialogActivity.class);
                                intent.putExtra("type", "singleE");
                                intent.putExtra("title", "خطا");
                                intent.putExtra("text",er.getMessage());

                                startActivity(intent);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        customProgress.hideProgress();
                        FancyToast.makeText(getContext(), getString(R.string.systemError),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            }
        });
        return v;
    }

    private void Check() {
        if(username.getText().toString().trim().length()<3)
            return;

        if(password.getText().toString().trim().length()<8)
            return;

        loginbtn.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.accent_button_bg));
        loginbtn.setEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        LI=(LoginInterface)context;
    }
}
