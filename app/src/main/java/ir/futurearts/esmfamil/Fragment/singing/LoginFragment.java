package ir.futurearts.esmfamil.Fragment.singing;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import ir.futurearts.esmfamil.Activity.MainActivity;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Interface.LoginInterface;
import ir.futurearts.esmfamil.Module.UserM;
import ir.futurearts.esmfamil.Network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.Network.Responses.LoginResponse;
import ir.futurearts.esmfamil.Network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;
import ir.futurearts.esmfamil.Utils.DialogActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText username,password;
    private Button loginbtn;
    private TextView createAccount,forgetPassword;
    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;

    private LoginInterface LI;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_login, container, false);
        username=v.findViewById(R.id.login_username_txt);
        password=v.findViewById(R.id.login_password_txt);
        loginbtn=v.findViewById(R.id.login_btn);
        forgetPassword=v.findViewById(R.id.login_forget_btn);
        createAccount=v.findViewById(R.id.login_create_btn);

        loginbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.disable_btn));
        loginbtn.setEnabled(false);

        mPref= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
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
                        .LogIn(username.getText().toString(),password.getText().toString());

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        customProgress.hideProgress();
                        if(response.code() == 200){
                            LoginResponse lr= response.body();
                            UserM u= lr.getUser();
                            editor.putString("name", u.getName());
                            editor.putString("username", u.getUsername());
                            editor.putString("id", u.getId());
                            editor.putString("email", u.getEmail());
                            editor.putString("online", "1");
                            editor.putString("img", u.getImg());
                            editor.putString("score", u.getScore());
                            editor.putBoolean("login", true);

                            editor.commit();

                            CurrentUser.setId(u.getId());
                            CurrentUser.setName(u.getName());
                            CurrentUser.setUsername(u.getUsername());
                            CurrentUser.setEmail(u.getEmail());
                            CurrentUser.setImg(u.getImg());
                            CurrentUser.setOnline(u.getOnline()+"");
                            CurrentUser.setScore(u.getScore());
                            CurrentUser.setCoin(u.getCoin());
                            CurrentUser.setLogin(true);

                            startActivity(new Intent(getActivity(), MainActivity.class));
                        }
                        else {
                            try {
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
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
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
        if(username.getText().toString().length()<3)
            return;

        if(password.getText().toString().length()<8)
            return;

        loginbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.accent_button_bg));
        loginbtn.setEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        LI=(LoginInterface)context;
    }
}
