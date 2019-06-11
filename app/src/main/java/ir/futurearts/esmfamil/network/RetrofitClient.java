package ir.futurearts.esmfamil.network;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //private static final String Auth= "Basic "+ Base64.encodeToString(("esmfamil:cb9aee2c7cd211121276d1fc0c940b55fcf5246c22b245ff8e42d85e743e9381").getBytes(), Base64.NO_WRAP);
    private static final String BASE_URL= "https://future-arts.ir/EsmFamil/public/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    public RetrofitClient() {
       /* OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original= chain.request();

                        Request.Builder builder= original.newBuilder()
                                .addHeader("Authorization", Auth)
                                .method(original.method(), original.body());

                        Request request= builder.build();
                        return  chain.proceed(request);
                    }
                }).build();*/
        retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if(mInstance == null)
            mInstance= new RetrofitClient();

        return mInstance;
    }

    public UserAPI getUserApi(){
        return  retrofit.create(UserAPI.class);
    }

    public GameAPI getGameApi(){
        return  retrofit.create(GameAPI.class);
    }
}
