package ir.futurearts.esmfamil.Network;


import org.json.JSONObject;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GameAPI {

    @POST("game.php/creategamewithfriend")
    @FormUrlEncoded
    Call<ResponseBody> test(
            @Field("uid") int uid,
            @Field("oid") int oid,
            @Field("type") int type,
            @Field("items") JSONObject items
            );
}
