package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Locale;

import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CustomProgress;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText pass, newpass, newpassc;
    private Button btnsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_change_password);

        pass= findViewById(R.id.profile_pass);
        newpass= findViewById(R.id.profile_newpass);
        newpassc= findViewById(R.id.profile_newpassconfirm);
        btnsave= findViewById(R.id.profile_savepass);

        btnsave.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this,R.drawable.disable_btn));
        btnsave.setEnabled(false);

        newpass.addTextChangedListener(new TextWatcher() {
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

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String npass= newpass.getText().toString().trim();
                String npassc= newpassc.getText().toString().trim();

                if(npass.equals(npassc) || npass.equals("")){
                    changePass(pass.getText().toString().trim(), npass);
                }
                else {
                    newpassc.setError("رمز عبور شده مطابقت ندارد");
                }
            }
        });
    }

    private void Check() {
        if(!hasLowerCase(newpass.getText().toString())||!hasUpperCase(newpass.getText().toString())){
            newpass.setError(getString(R.string.passwordErrorType));
            btnsave.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this,R.drawable.disable_btn));
            btnsave.setEnabled(false);
            return ;
        }
        else if(!hasLength(newpass.getText().toString())){
            newpass.setError(getString(R.string.passwordErrorCount));
            btnsave.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this,R.drawable.disable_btn));
            btnsave.setEnabled(false);
            return ;
        }
        btnsave.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this,R.drawable.btn_green));
        btnsave.setEnabled(true);
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

    private void changePass( String spass, String npass ) {
        final CustomProgress progress=  new CustomProgress();
        progress.showProgress(this, false);
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getUserApi().changePassword(CurrentUser.getId(), spass, npass);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,@NonNull Response<ResponseBody> response) {
                if(response.code() == 200){
                    FancyToast.makeText(ChangePasswordActivity.this,  "تغییرات انجام شد", FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS, false).show();

                    finish();
                }
                else if(response.code() == 409){
                    pass.setError("رمز عبور وارد شده نادرست است");
                }
                else {
                    FancyToast.makeText(ChangePasswordActivity.this, getString(R.string.systemError),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
                progress.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                FancyToast.makeText(ChangePasswordActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                progress.hideProgress();
            }
        });
    }
}
