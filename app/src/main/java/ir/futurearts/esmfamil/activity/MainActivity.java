package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.util.Objects;
import java.util.Random;

import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.fragment.MainFragment;
import ir.futurearts.esmfamil.fragment.ResultFragment;
import ir.futurearts.esmfamil.fragment.SettingFragment;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CurvedBottomView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.ads.AdRequest.ERROR_CODE_INTERNAL_ERROR;
import static com.google.android.gms.ads.AdRequest.ERROR_CODE_INVALID_REQUEST;
import static com.google.android.gms.ads.AdRequest.ERROR_CODE_NETWORK_ERROR;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private VectorMasterView fab1,fab2,fab3;
    private PathModel outline;
    private RelativeLayout lin_id;
    private float diff=0, origin=0;
    public CurvedBottomView nav;

    private static String TOPIC_GLOBAL= "global";
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTransition();

        nav= findViewById(R.id.main_bootomview);
        fab1= findViewById(R.id.fab1);
        fab2= findViewById(R.id.fab2);
        fab3= findViewById(R.id.fab3);
        lin_id= findViewById(R.id.lin_id);


        nav.setOnNavigationItemSelectedListener(this);
        changeFragment(1);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MM", "getInstanceId failed", task.getException());
                            return;
                        }
                        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
                        // Get new Instance ID token
                        String token = Objects.requireNonNull(task.getResult()).getToken();
                        if(!CurrentUser.getToken().equals(token)){
                            SharedPreferences.Editor editor= getSharedPreferences("user", MODE_PRIVATE).edit();
                            editor.putString("token", token);
                            CurrentUser.setToken(token);
                            editor.apply();
                            Call<ResponseBody> call= RetrofitClient.getInstance().getUserApi()
                                    .setToken(CurrentUser.getId(), token);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                                }
                            });
                        }
                    }
                });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1398806565081490/7847049989");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                int n= new Random().nextInt(20);
                Log.d("MM", n+"");
                if(n%2 == 0 && n>=10)
                    mInterstitialAd.show();

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                switch (errorCode) {
                    case ERROR_CODE_INTERNAL_ERROR:
                        Log.d("MM", "Something happened internally; for instance, an invalid response was received from the ad server. ");
                        break;

                    case ERROR_CODE_INVALID_REQUEST:
                        Log.d("MM", "The ad request was invalid; for instance, the ad unit ID was incorrect. ");
                        break;

                    case ERROR_CODE_NETWORK_ERROR:
                        Log.d("MM", "The ad request was unsuccessful due to network connectivity. ");
                        break;

                    default:
                        Log.d("MM", "Faild to Load:"+ errorCode);
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        });

    }

    private void draw() {
        // get width and height of navigation bar
        // Navigation bar bounds (width & height)
        //mNavigationBarHeight = getHeight();
        //mNavigationBarWidth = getWidth();
        // the coordinates (x,y) of the start point before curve
        nav.mFirstCurveStartPoint.set((nav.mNavigationBarWidth * 10/12) - (nav.CURVE_CIRCLE_RADIUS * 2) - (nav.CURVE_CIRCLE_RADIUS / 3), 0);
        // the coordinates (x,y) of the end point after curve
        nav.mFirstCurveEndPoint.set(nav.mNavigationBarWidth  * 10/12, nav.CURVE_CIRCLE_RADIUS + (nav.CURVE_CIRCLE_RADIUS / 4));
        // same thing for the second curve
        nav.mSecondCurveStartPoint = nav.mFirstCurveEndPoint;
        nav.mSecondCurveEndPoint.set((nav.mNavigationBarWidth  * 10/12) + (nav.CURVE_CIRCLE_RADIUS * 2) + (nav.CURVE_CIRCLE_RADIUS / 3), 0);

        // the coordinates (x,y)  of the 1st control point on a cubic curve
        nav.mFirstCurveControlPoint1.set(nav.mFirstCurveStartPoint.x + nav.CURVE_CIRCLE_RADIUS + (nav.CURVE_CIRCLE_RADIUS / 4), nav.mFirstCurveStartPoint.y);
        // the coordinates (x,y)  of the 2nd control point on a cubic curve
        nav.mFirstCurveControlPoint2.set(nav.mFirstCurveEndPoint.x - (nav.CURVE_CIRCLE_RADIUS * 2) + nav.CURVE_CIRCLE_RADIUS, nav.mFirstCurveEndPoint.y);

        nav.mSecondCurveControlPoint1.set(nav.mSecondCurveStartPoint.x + (nav.CURVE_CIRCLE_RADIUS * 2) - nav.CURVE_CIRCLE_RADIUS, nav.mSecondCurveStartPoint.y);
        nav.mSecondCurveControlPoint2.set(nav.mSecondCurveEndPoint.x - (nav.CURVE_CIRCLE_RADIUS + (nav.CURVE_CIRCLE_RADIUS / 4)), nav.mSecondCurveEndPoint.y);
    }

    private void draw(int i){
        // get width and height of navigation bar
        // Navigation bar bounds (width & height)
        //mNavigationBarHeight = getHeight();
        //mNavigationBarWidth = getWidth();
        // the coordinates (x,y) of the start point before curve
        nav.mFirstCurveStartPoint.set((nav.mNavigationBarWidth / i) - (nav.CURVE_CIRCLE_RADIUS * 2) - (nav.CURVE_CIRCLE_RADIUS / 3), 0);
        // the coordinates (x,y) of the end point after curve
        nav.mFirstCurveEndPoint.set(nav.mNavigationBarWidth / i, nav.CURVE_CIRCLE_RADIUS + (nav.CURVE_CIRCLE_RADIUS / 4));
        // same thing for the second curve
        nav.mSecondCurveStartPoint = nav.mFirstCurveEndPoint;
        nav.mSecondCurveEndPoint.set((nav.mNavigationBarWidth / i) + (nav.CURVE_CIRCLE_RADIUS * 2) + (nav.CURVE_CIRCLE_RADIUS / 3), 0);

        // the coordinates (x,y)  of the 1st control point on a cubic curve
        nav.mFirstCurveControlPoint1.set(nav.mFirstCurveStartPoint.x + nav.CURVE_CIRCLE_RADIUS + (nav.CURVE_CIRCLE_RADIUS / 4), nav.mFirstCurveStartPoint.y);
        // the coordinates (x,y)  of the 2nd control point on a cubic curve
        nav.mFirstCurveControlPoint2.set(nav.mFirstCurveEndPoint.x - (nav.CURVE_CIRCLE_RADIUS * 2) + nav.CURVE_CIRCLE_RADIUS, nav.mFirstCurveEndPoint.y);

        nav.mSecondCurveControlPoint1.set(nav.mSecondCurveStartPoint.x + (nav.CURVE_CIRCLE_RADIUS * 2) - nav.CURVE_CIRCLE_RADIUS, nav.mSecondCurveStartPoint.y);
        nav.mSecondCurveControlPoint2.set(nav.mSecondCurveEndPoint.x - (nav.CURVE_CIRCLE_RADIUS + (nav.CURVE_CIRCLE_RADIUS / 4)), nav.mSecondCurveEndPoint.y);



    }
    private void drawAnimation(final VectorMasterView fab1) {
        outline= fab1.getPathModelByName("outline");
        outline.setStrokeColor(Color.WHITE);
        outline.setTrimPathEnd(0.0f);

        final ValueAnimator valueAnimator= ValueAnimator.ofFloat(0.0f,1.0f);
        valueAnimator.setDuration(1000);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                outline.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                fab1.update();
            }
        });
        valueAnimator.start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_result:

                //animation
                draw(6);
                if(origin==0) {
                    origin = lin_id.getX();
                    diff= nav.mFirstCurveControlPoint1.x-origin;
                }
                //Find CorrectPath Using name

                lin_id.setX(nav.mFirstCurveControlPoint1.x-diff);
                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.GONE);
                drawAnimation(fab1);
                changeFragment(0);
                break;

            case R.id.menu_home:

                //animation
                draw(2);
                if(origin==0) {
                    origin = lin_id.getX();
                    diff= nav.mFirstCurveControlPoint1.x-origin;
                    Log.d("MM", origin+"");
                    Log.d("MM", diff+"");
                }
                //Find CorrectPath Using name

                lin_id.setX(nav.mFirstCurveControlPoint1.x-diff);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.GONE);
                drawAnimation(fab2);
                changeFragment(1);
                break;

            case R.id.menu_setting:

                //animation
                draw();
                if(origin==0) {
                    origin = lin_id.getX();
                    diff= nav.mFirstCurveControlPoint1.x-origin;
                }
                //Find CorrectPath Using name
                lin_id.setX(nav.mFirstCurveControlPoint1.x-diff);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.VISIBLE);
                drawAnimation(fab3);
                changeFragment(2);
                break;
        }

        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(!CurrentUser.isLogin())
            gotoLogin();
    }

    public void gotoLogin(){
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void changeFragment(int pos){
        FragmentManager fg=getSupportFragmentManager();
        FragmentTransaction ft=fg.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in,
                R.anim.fade_out);

        Fragment f = new MainFragment();
        switch (pos){
            case 0:
                f =new ResultFragment();
                break;

            case 1:
                f =new MainFragment();
                break;

            case 2:
                f =new SettingFragment();
                break;

        }
        ft.replace(R.id.main_frame, f);
        ft.commit();
    }

    private void setupTransition() {
        Explode explode=new Explode();
        explode.setDuration(1000);
        getWindow().setEnterTransition(explode);
    }

}
