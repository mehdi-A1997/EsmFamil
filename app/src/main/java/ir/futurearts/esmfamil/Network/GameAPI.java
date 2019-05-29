package ir.futurearts.esmfamil.Network;


import org.json.JSONObject;


import ir.futurearts.esmfamil.Network.Responses.CreateGameResponse;
import ir.futurearts.esmfamil.Network.Responses.DefaultResponse;
import ir.futurearts.esmfamil.Network.Responses.GamesResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GameAPI {

    @POST("game.php/creategamewithfriend")
    @FormUrlEncoded
    Call<CreateGameResponse> CreateGameWithFriends(
            @Field("uid") String  uid,
            @Field("oid") String  oid,
            @Field("type") int type,
            @Field("letter") String letter,
            @Field("items") JSONObject items
    );

    @POST("game.php/createrandomgame")
    @FormUrlEncoded
    Call<CreateGameResponse> CreateRandomGame(
            @Field("uid") String uid,
            @Field("type") int type
    );

    @GET("game.php/getmygames/{uid}")
    Call<GamesResponse> getMyGames(
            @Path("uid") String  uid
    );

    @POST("game.php/setgamestuff")
    @FormUrlEncoded
    Call<CreateGameResponse> setGameStuff(
            @Field("id") int id,
            @Field("uid") String uid,
            @Field("letter") String letter,
            @Field("items") JSONObject items
    );

    @PUT("game.php/setgameresult")
    @FormUrlEncoded
    Call<CreateGameResponse> setGameResult(
            @Field("id") int id,
            @Field("uid") String uid,
            @Field("items") JSONObject items,
            @Field("time") int time
    );

    @PUT("game.php/managegame")
    @FormUrlEncoded
    Call<ResponseBody> manageGame(
            @Field("id") int id,
            @Field("status") int status
    );

    @POST("game.php/sendgamerequest")
    @FormUrlEncoded
    Call<ResponseBody> sendGameRequest(
            @Field("uid") String uid,
            @Field("oid") String oid,
            @Field("gid") int gid
    );

    @GET("game.php/getgamerequest/{uid}")
    Call<GamesResponse> getGameRequest(
            @Path("uid") String  uid
    );

    @POST("game.php/deletegame")
    @FormUrlEncoded
    Call<ResponseBody> deleteGame(
            @Field("gid") int gid,
            @Field("type") int type
    );


}
