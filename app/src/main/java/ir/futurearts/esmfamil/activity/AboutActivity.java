package ir.futurearts.esmfamil.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.Locale;

import ir.futurearts.esmfamil.R;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_aboutus);

        setupTransition();
        TextView phone = findViewById(R.id.about_phone);
        TextView email = findViewById(R.id.about_emil);
        TextView site = findViewById(R.id.about_site);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel","09147176112",null)));
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL,"futurearts.ir@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT,"قرمه سرا");
                startActivity(Intent.createChooser(intent,"ارسال ایمیل:"));
            }
        });

        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://future-arts.ir")));
            }
        });

        SharedPreferences firstPref = getSharedPreferences("first", Context.MODE_PRIVATE);
        SharedPreferences.Editor firstEditor= firstPref.edit();
        if(firstPref.getBoolean("about", true)){
            setTapTarget();
            firstEditor.putBoolean("about", false);
            firstEditor.apply();
        }

    }

    private void setTapTarget() {
        new TapTargetSequence(this).targets(
                TapTarget.forView(findViewById(R.id.about_phone), "تماس با ما", "برای تماس با ما.شماره زیر را لمس کنید")
                        // All options below are optional
                        .outerCircleColor(R.color.blue)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
                        .titleTextSize(22)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.orange)      // Specify the color of the title text
                        .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.white)  // Specify the color of the description text
                        // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.gray)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60)
                ,
                TapTarget.forView(findViewById(R.id.about_emil), "تماس با ما", "جهت ارائه پیشنهاد یا انتقاد . نشانی الکترونیکی را لمس کنید")
                        // All options below are optional
                        .outerCircleColor(R.color.blue)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.99f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
                        .titleTextSize(22)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.orange)      // Specify the color of the title text
                        .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.white)  // Specify the color of the description text
                        // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.gray)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60)
                ,
                TapTarget.forView(findViewById(R.id.about_site), "سایر محصولات", "برای مشاهده سایر محصولات و اطلاع از اخبار لینک زیر را لمس کنید")
                        // All options below are optional
                        .outerCircleColor(R.color.blue)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.99f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
                        .titleTextSize(22)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.orange)      // Specify the color of the title text
                        .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.white)  // Specify the color of the description text
                        // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.gray)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        // Specify a custom drawable to draw as the target
                        .targetRadius(60)
        ) .listener(new TapTargetSequence.Listener() {
            // This listener will tell us when interesting(tm) events happen in regards
            // to the sequence
            @Override
            public void onSequenceFinish() {
                // Yay
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

            }


            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                // Boo
            }
        }).start();
    }

    private void setupTransition() {
        Slide explode=new Slide();
        explode.setSlideEdge(Gravity.BOTTOM);
        explode.setDuration(800);
        getWindow().setEnterTransition(explode);
        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.main_bg));
    }
}
