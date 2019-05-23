package ir.futurearts.esmfamil.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL= "http://192.168.137.1:8080/EsmFamil/public/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    public RetrofitClient() {
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
