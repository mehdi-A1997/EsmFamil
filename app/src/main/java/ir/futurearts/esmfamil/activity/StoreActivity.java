package ir.futurearts.esmfamil.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.futurearts.esmfamil.adapter.StoreAdapter;
import ir.futurearts.esmfamil.constant.CurrentUser;
import ir.futurearts.esmfamil.interfaces.StoreInterface;
import ir.futurearts.esmfamil.module.StoreM;
import ir.futurearts.esmfamil.network.RetrofitClient;
import ir.futurearts.esmfamil.R;
import ir.futurearts.esmfamil.utils.CustomProgress;
import ir.futurearts.esmfamil.utils.billing.util.IabHelper;
import ir.futurearts.esmfamil.utils.billing.util.IabResult;
import ir.futurearts.esmfamil.utils.billing.util.Inventory;
import ir.futurearts.esmfamil.utils.billing.util.Purchase;
import ir.futurearts.esmfamil.utils.billing.util.SkuDetails;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity implements StoreInterface {

    private StoreAdapter adapter;
    private List<StoreM> data;
    private NewtonCradleLoading mprog;
    private IabHelper mHelper;
    private CustomProgress progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_store);

        RecyclerView list = findViewById(R.id.store_rv);
        mprog= findViewById(R.id.store_progress);

        data= new ArrayList<>();
        adapter= new StoreAdapter(data, this, this);
        list.setAdapter(adapter);
        list.setLayoutManager(new GridLayoutManager(this, 2));

        progress= new CustomProgress();
        mprog.start();
        String base64EncodedPublicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUCAEdZmvQw+w1tCvc2yY5wr+dbDjwxzqeCHyqK1hSi3G79s+4EOW185fSEvcevTWh0K1Jglj9FGDPzmU0+mUV2bEWUjjbD3p6RoeQuNn2N2DoMtt3Ol1IC220Nj6gHP2SQXQ4z+gdg0P0Jh0lm2Ia2HdTSBVLxjWWZy9hWyYNPQIDAQAB";
        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()  {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d("MM", result.getMessage()+"");
                    FancyToast.makeText(StoreActivity.this, getString(R.string.systemError),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mprog.stop();
                    mprog.setVisibility(View.GONE);
                }
                getItems();
            } });
    }

    private void getItems() {
        IabHelper.QueryInventoryFinishedListener    mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener()  {
            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                if (result.isFailure()) {
                    // handle error
                    Log.d("MM", result.getMessage());
                    FancyToast.makeText(StoreActivity.this, getString(R.string.systemError),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    mprog.stop();
                    mprog.setVisibility(View.GONE);
                    return;
                }
                SkuDetails c100 = inventory.getSkuDetails("futurearts_esmfamil_100");
                SkuDetails c200 = inventory.getSkuDetails("futurearts_esmfamil_200");
                SkuDetails c500 = inventory.getSkuDetails("futurearts_esmfamil_500");
                SkuDetails c1000 = inventory.getSkuDetails("futurearts_esmfamil_1000");
                //TODO Add TO list
                data.add(new StoreM(c100.getTitle(), c100.getSku(), c100.getPrice()));
                data.add(new StoreM(c200.getTitle(), c200.getSku(), c200.getPrice()));
                data.add(new StoreM(c500.getTitle(), c500.getSku(), c500.getPrice()));
                data.add(new StoreM(c1000.getTitle(), c1000.getSku(), c1000.getPrice()));
                adapter.notifyDataSetChanged();
                mprog.stop();
                mprog.setVisibility(View.GONE);
            }
        };
        List<String> additionalSkuList = new ArrayList<>();
        additionalSkuList.add("futurearts_esmfamil_100");
        additionalSkuList.add("futurearts_esmfamil_200");
        additionalSkuList.add("futurearts_esmfamil_500");
        additionalSkuList.add("futurearts_esmfamil_1000");
        List<String>additionalSkuList1 = new ArrayList<>();
        additionalSkuList1.add("futurearts_esmfamil_100");
        additionalSkuList1.add("futurearts_esmfamil_200");
        additionalSkuList1.add("futurearts_esmfamil_500");
        additionalSkuList1.add("futurearts_esmfamil_1000");
        try {
            if(mHelper.isSetupDone() && !mHelper.isAsyncInProgress()) {
                mHelper.queryInventoryAsync(true, additionalSkuList, additionalSkuList1, mQueryFinishedListener);
            }
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
            Log.d("MM", e.getMessage()+"");
            FancyToast.makeText(StoreActivity.this, getString(R.string.systemError),
                    FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            mprog.stop();
            mprog.setVisibility(View.GONE);
        }
    }

    private void purchase(StoreM s) {
        try {
            mHelper.launchPurchaseFlow (this, s.getSku(), 10001, mPurchaseFinishedListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
            progress.hideProgress();
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d("MM", "Purchase finished: " + result + ", purchase: " + purchase);
            // if we were disposed of in the meantime, quit.
            if (mHelper == null) {
                progress.hideProgress();
                return;}

            if (result.isFailure()) {
                Log.d("MM", "Error purchasing: " + result);
                FancyToast.makeText(StoreActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                progress.hideProgress();
                return;
            }

            try {
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            } catch (IabHelper.IabAsyncInProgressException ignored) {
                progress.hideProgress();
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            FancyToast.makeText(StoreActivity.this,"خرید با موفقیت انجام شد",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
            if(result.isSuccess())
            {
                String sk=purchase.getSku();
                switch (sk) {
                    case "futurearts_esmfamil_100":
                        buyCredit(110);
                        break;
                    case "futurearts_esmfamil_200":
                        buyCredit(225);
                        break;
                    case "futurearts_esmfamil_500":
                        buyCredit(600);
                        break;
                    case "futurearts_esmfamil_1000":
                        buyCredit(1500);
                        break;
                }
            }
        }
    };

    private void buyCredit(final int count) {
        Call<ResponseBody> call= RetrofitClient.getInstance()
                .getUserApi().addCoin(CurrentUser.getId(), count);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                progress.hideProgress();
                if(response.code() == 200){
                    Intent intent= new Intent();
                    intent.putExtra("count", count);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Log.d("MM", t.getMessage()+"");
                FancyToast.makeText(StoreActivity.this, getString(R.string.systemError),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                progress.hideProgress();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MM", "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null){
            progress.hideProgress();
            return;
        }

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d("MM", "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public void Selected(StoreM s) {
        progress.showProgress(this, false);
        purchase(s);
    }
}
