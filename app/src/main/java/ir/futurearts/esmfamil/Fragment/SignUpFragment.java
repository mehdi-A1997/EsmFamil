package ir.futurearts.esmfamil.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.futurearts.esmfamil.Activity.MainActivity;
import ir.futurearts.esmfamil.Interface.LoginInterface;
import ir.futurearts.esmfamil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private EditText username,password,email;
    private Button signupbtn;
    private TextView backbtn;
    private ImageView showpass;

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
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.setEmail(email.getText().toString());
                user.put("score","0");
                user.put("name","");
                user.put("online",1);

                // Other fields can be set just like any other ParseObject,
                // using the "put" method, like this: user.put("attribute", "its value");
                // If this field does not exists, it will be automatically created

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent=new Intent(getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Log.d("MM",e.getMessage());
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
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
