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
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class SignUpFragment extends Fragment {

    private EditText username,password,email;
    private Button signupbtn;
    private ImageView showpass;

    private SharedPreferences.Editor editor;

    private boolean visible=false;

    private LoginInterface LI;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_sign_up, container, false);

        username=v.findViewById(R.id.signup_username_txt);
        password=v.findViewById(R.id.signup_password_txt);
        email=v.findViewById(R.id.signup_email_txt);
        signupbtn=v.findViewById(R.id.signup_btn);
        TextView backbtn = v.findViewById(R.id.signup_back_btn);
        showpass=v.findViewById(R.id.signup_show);

        SharedPreferences mPref = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        editor= mPref.edit();

        signupbtn.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.disable_btn));
        signupbtn.setEnabled(false);

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

        email.addTextChangedListener(new TextWatcher() {
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

        showpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                visible=!visible;

                changeShow();
            }
        });


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomProgress customProgress=new CustomProgress();
                customProgress.showProgress(getContext(),false);

                Call<LoginResponse> call= RetrofitClient
                        .getInstance()
                        .getUserApi()
                        .SingUp(email.getText().toString().trim(), username.getText().toString().trim(), password.getText().toString().trim());

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        customProgress.hideProgress();
                        if(response.code() == 201){
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
                            CurrentUser.setToken(u.getToken());
                            CurrentUser.setLogin(true);

                            Intent intent= new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
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
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        customProgress.hideProgress();
                        FancyToast.makeText(getContext(), getString(R.string.systemError),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            }
        });


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LI.Change(1);
            }
        });
        return v;
    }

    private void Check(){

        if(username.getText().length()<3){
            username.setError(getString(R.string.usernameError),
                    ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.ic_error));
            signupbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.disable_btn));
            signupbtn.setEnabled(false);
            return ;
        }

        if(!hasLowerCase(password.getText().toString())||!hasUpperCase(password.getText().toString())){
            password.setError(getString(R.string.passwordErrorType),
                    ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.ic_error));
            signupbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.disable_btn));
            signupbtn.setEnabled(false);
            return ;
        }
        else if(!hasLength(password.getText().toString().trim())){
            password.setError(getString(R.string.passwordErrorCount),
                    ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.ic_error));
            signupbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.disable_btn));
            signupbtn.setEnabled(false);
            return ;
        }

        if(!isEmailValid(email.getText().toString())){
            email.setError(getString(R.string.emailError),
                    ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.ic_error));
            signupbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.disable_btn));
            signupbtn.setEnabled(false);
            return ;
        }

        signupbtn.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.accent_button_bg));
        signupbtn.setEnabled(true);
    }
    private void changeShow() {
        if(visible){
            password.setTransformationMethod(null);
            showpass.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.ic_visibility_off));
        }
        else {
            password.setTransformationMethod(new PasswordTransformationMethod());
            showpass.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.ic_visibility));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LI=(LoginInterface)context;
    }

    private static boolean hasLength(CharSequence data) {
        return String.valueOf(data).length() >= 8;
    }


    private static boolean hasUpperCase(CharSequence data) {
        String password = String.valueOf(data);
        return !password.equals(password.toLowerCase());
    }

    private static boolean hasLowerCase(CharSequence data) {
        String password = String.valueOf(data);
        return !password.equals(password.toUpperCase());
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
