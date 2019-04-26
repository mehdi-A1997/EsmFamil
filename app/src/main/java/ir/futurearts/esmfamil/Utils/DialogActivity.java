package ir.futurearts.esmfamil.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ir.futurearts.esmfamil.R;

public class DialogActivity extends AppCompatActivity {

    private ImageView img;
    private TextView title,text;
    private String type;
    private Button btnNo;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        img=findViewById(R.id.dialogImg);
        title=findViewById(R.id.dialogTitle);
        text=findViewById(R.id.dialogText);
        btnNo=findViewById(R.id.dialogBtn2);
        btnOk=findViewById(R.id.dialogBtn);


        Bundle data=getIntent().getExtras();

        String mtitle=data.getString("title");
        String mtext=data.getString("text");
        type=data.getString("type");

        if(type.equals("error"))
        {
            Drawable dw= ContextCompat.getDrawable(this,R.drawable.ic_error);
            img.setImageDrawable(dw);
            title.setBackgroundColor(Color.parseColor("#ff2c2c"));
            btnNo.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setText("بله");
            btnNo.setText("خیر");
        }
        else if(type.equals("singleE")){
            btnNo.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.GONE);
            Drawable dw=ContextCompat.getDrawable(this,R.drawable.ic_error);
            img.setImageDrawable(dw);
            title.setBackgroundColor(Color.parseColor("#ff2c2c"));
            btnNo.setText("باشه");
        }
        else if(type.equals("singleS")){
            btnNo.setVisibility(View.GONE);
            btnOk.setVisibility(View.VISIBLE);
            Drawable dw=ContextCompat.getDrawable(this,R.mipmap.ic_ok);
            img.setImageDrawable(dw);
            title.setBackgroundColor(Color.parseColor("#8bc34a"));
            btnOk.setText("باشه");
        }else {
            btnNo.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.VISIBLE);
            Drawable dw=ContextCompat.getDrawable(this,R.mipmap.ic_ok);
            img.setImageDrawable(dw);
            title.setBackgroundColor(Color.parseColor("#8bc34a"));
            btnOk.setText("بله");
            btnNo.setText("خیر");
        }
        text.setText(mtext);
        title.setText(mtitle);
    }
    public void okDialog(View view){
        Intent i=new Intent();
        i.putExtra("type",type);
        setResult(RESULT_OK,i);
        finish();
    }
    public void noDialog(View view){
        finish();
    }
}
