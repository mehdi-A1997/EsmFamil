package ir.futurearts.esmfamil.Fragment.singing;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class SignUpFragment extends Fragment {

    private EditText username,password,email;
    private Button signupbtn;
    private TextView backbtn;
    private ImageView showpass;

    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;

    private boolean visible=false;

    private LoginInterface LI;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_sign_up, container, false);

        username=v.findViewById(R.id.signup_username_txt);
        password=v.findViewById(R.id.signup_password_txt);
        email=v.findViewById(R.id.signup_email_txt);
        signupbtn=v.findViewById(R.id.signup_btn);
        backbtn=v.findViewById(R.id.signup_back_btn);
        showpass=v.findViewById(R.id.signup_show);

        mPref= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        editor= mPref.edit();

        signupbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.disable_btn));
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
                        .SingUp(email.getText().toString(), username.getText().toString(), password.getText().toString());

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        customProgress.hideProgress();
                        if(response.code() == 201){
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


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LI.Change(1);
            }
        });
        return v;
    }

    public boolean Check(){

        if(username.getText().length()<3){
            username.setError(getString(R.string.usernameError),
                    ContextCompat.getDrawable(getContext(),R.drawable.ic_error));
            signupbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.disable_btn));
            signupbtn.setEnabled(false);
            return false;
        }

        if(!hasLowerCase(password.getText().toString())||!hasUpperCase(password.getText().toString())){
            password.setError(getString(R.string.passwordErrorType),
                    ContextCompat.getDrawable(getContext(),R.drawable.ic_error));
            signupbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.disable_btn));
            signupbtn.setEnabled(false);
            return false;
        }
        else if(!hasLength(password.getText().toString())){
            password.setError(getString(R.string.passwordErrorCount),
                    ContextCompat.getDrawable(getContext(),R.drawable.ic_error));
            signupbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.disable_btn));
            signupbtn.setEnabled(false);
            return false;
        }

        if(!isEmailValid(email.getText().toString())){
            email.setError(getString(R.string.emailError),
                    ContextCompat.getDrawable(getContext(),R.drawable.ic_error));
            signupbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.disable_btn));
            signupbtn.setEnabled(false);
            return false;
        }

        signupbtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.accent_button_bg));
        signupbtn.setEnabled(true);
        return true;
    }
    private void changeShow() {
        if(visible){
            password.setTransformationMethod(null);
            showpass.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_visibility_off));
        }
        else {
            password.setTransformationMethod(new PasswordTransformationMethod());
            showpass.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_visibility));
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

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
