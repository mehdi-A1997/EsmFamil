package ir.futurearts.esmfamil.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.util.Locale;

import ir.futurearts.esmfamil.R;

public class HelpActivity extends AppCompatActivity {

    private final String help1 ="    <h2><u>بازی رقابتی: </u></h2>\n" +
            "    <p>بازی همزمان با دوستان، به صورت آنلاین و همزمان با دوستانتان رقابت کنید<br/> توجه کنید که برای بازی رقابتی دوست شما باید انلاین باشد</p>\n" +
            "    <hr/>\n" +
            "    <h2><u>بازی معمولی:</u></h2>\n" +
            "    <p>در بازی معمولی نیازی به انلاین بودن دوست نیست و هرکس جداگانه نوبت خود را بازی میکند اما توجه داشته باشید که هرکس 1 دقیقه زمان دارد\n" +
            "          و کسی که در زمان کمتری نتایج را ثبت کند 10 امتیاز به مجموع امتیازات او افزوده میشود</p>\n" +
            "    <hr/>\n" +
            "    <h2><u> محاسبه امتیاز: </u></h2>\n" +
            "    <p>پس از پایان بازی ، داور بازی نتایج شمارا بررسی میکند در صورت وجود پاسخ مشترک 5 امتیاز در غیر اینصورت 10 امتیاز به شما تعلق میگیرد<br/> \n" +
            "           دقت کنید که اگر کلمه وارد شده نا معتبر باشد به شما امتیازی تعلق نمیگیرد <br/> <br/>\n" +
            "        در صورت برد 2 سکه و 10 امتیاز به شما تعلق میگیرد <br/>\n" +
            "    در صورت تساوی فقط 5 امتیاز به شما تعلق میگیرد <br/>\n" +
            " در صورت باخت 5 امتیاز از شما کسر میگردد</p>\n" +
            "        <hr/>"
            +"";

    private final String help2= "<h2><u>راهنما</u></h2>\n" +
            "    <p>برای مشاهده جزییات بازی بازی مورد نظر را لمس کرده و نگه دارید.<br/>\n" +
            "    اگر بازی تمام شده باشد با لمس بازی وارد بخش نتایج بازی و امتیازات میشوید</p>\n" +
            "    <h2><u>بازی های فعال</u></h2>\n" +
            "    <p>اگر رنگ بازی ابی کمرنگ باشد به معنی فعال بودن بازی است، یعنی یا نوبت بازی شما و یا تایید بازی است.</p>\n" +
            "\n" +
            "    <h2><u>هشدار</u></h2>\n" +
            "    <p>هر بازی به مدت 24 ساعت فعال است .اگر در مدت 24 ساعت نوبت خود را بازی نکنید امتیاز از دست خواهید داد</p>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_help);

        int type= getIntent().getIntExtra("type", 0);
        TextView helpt = findViewById(R.id.help_text);
        helpt.setMovementMethod(new ScrollingMovementMethod());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           if(type == 0){
               helpt.setText(Html.fromHtml(help1, Html.FROM_HTML_MODE_COMPACT));
           }
           else{
               helpt.setText(Html.fromHtml(help2, Html.FROM_HTML_MODE_COMPACT));
           }
        } else {
            if(type == 0){
                helpt.setText(Html.fromHtml(help1));
            }
            else{
                helpt.setText(Html.fromHtml(help2));
            }

        }
    }
}
