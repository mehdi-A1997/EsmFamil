package ir.futurearts.esmfamil.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import ir.futurearts.esmfamil.Activity.MainActivity;
import ir.futurearts.esmfamil.Interface.LoginInterface;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CustomProgress;
import ir.futurearts.esmfamil.Utils.DialogActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText username,password;
    private Button loginbtn;
    private TextView createAccount,forgetPassword;

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
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent intent=new Intent(getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            customProgress.hideProgress();
                            getActivity().finish();
                        } else {
                            customProgress.hideProgress();
                            Log.d("MM",e.getMessage());
                            Intent intent=new Intent(getContext(), DialogActivity.class);
                            intent.putExtra("type","singleE");
                            intent.putExtra("title","خطا");
                            intent.putExtra("text","نام کاربری یا رمز عبور نادرست است");
                            startActivity(intent);
                        }
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
