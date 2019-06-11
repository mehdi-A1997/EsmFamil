package ir.futurearts.esmfamil.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.Objects;

import ir.futurearts.esmfamil.R;

public class CustomProgress {
    public static CustomProgress customProgress = null;
    private Dialog mDialog;

    public static CustomProgress getInstance() {
        if (customProgress == null) {
            customProgress = new CustomProgress();
        }
        return customProgress;
    }

    public void showProgress(Context context, boolean cancelable) {
        mDialog = new Dialog(context);
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        // no tile for the dialog
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.progress_dialog);
        NewtonCradleLoading p=mDialog.findViewById(R.id.newton_cradle_loading);
        //p.setLoadingColor(R.color.colorAccent);
        p.start();
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
