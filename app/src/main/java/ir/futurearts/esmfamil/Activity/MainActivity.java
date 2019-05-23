package ir.futurearts.esmfamil.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;
import ir.futurearts.esmfamil.Constant.CurrentUser;
import ir.futurearts.esmfamil.Fragment.MainFragment;
import ir.futurearts.esmfamil.Fragment.ResultFragment;
import ir.futurearts.esmfamil.Fragment.SettingFragment;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.Utils.CurvedBottomView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

    private VectorMasterView fab1,fab2,fab3;
    private PathModel outline;
    private RelativeLayout lin_id;
    private float diff=0, origin=0;
    public CurvedBottomView nav;
    private Fragment f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nav= findViewById(R.id.main_bootomview);
        fab1= findViewById(R.id.fab1);
        fab2= findViewById(R.id.fab2);
        fab3= findViewById(R.id.fab3);
        lin_id= findViewById(R.id.lin_id);

        //nav.inflateMenu(R.menu.main);
        nav.setOnNavigationItemSelectedListener(this);
        changeFragment(1);

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

        f=new MainFragment();
        switch (pos){
            case 0:
                f=new ResultFragment();
                break;

            case 1:
                f=new MainFragment();
                break;

            case 2:
                f=new SettingFragment();
                break;

        }
        ft.replace(R.id.main_frame,f);
        ft.commit();
    }
}
