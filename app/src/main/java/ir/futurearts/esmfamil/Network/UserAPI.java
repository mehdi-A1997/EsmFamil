package ir.futurearts.esmfamil.Network;

import ir.futurearts.esmfamil.Network.Responses.FreindsResponse;
import ir.futurearts.esmfamil.Network.Responses.LoginResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPI {

    //SignUp User

    @FormUrlEncoded
    @POST("signup")
    Call<LoginResponse> SingUp(
          @Field("email") String Email,
          @Field("username") String Username,
          @Field("pass") String Password
    );


    //Login User

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> LogIn(
          @Field("username") String Username,
          @Field("pass") String Password
    );

    //Get getFriends

    @GET("user.php/friends/{id}")
    Call<FreindsResponse> getFriends(
            @Path("id") String id
    );

    //Get getRank

    @GET("user.php/rank")
    Call<FreindsResponse> getRank();

    //Get Friend Request

    @GET("user.php/friendrequest/{id}")
    Call<FreindsResponse> friendRequests(
            @Path("id") String id
    );

    //Search User By Username

    @GET("user.php/searchuser/{username}")
    Call<LoginResponse> searchUser(
            @Path("username") String Username
    );

    //Send Friend Request

    @FormUrlEncoded
    @POST("user.php/sendfriendrequest")
    Call<ResponseBody> sendFriendRequest(
            @Field("cid") String Cid,
            @Field("uid") String Uid
    );

    //Add Requested Friend

    @FormUrlEncoded
    @POST("user.php/addfriend")
    Call<ResponseBody> addFriend(
            @Field("cid") String Cid,
            @Field("uid") String Uid
    );

    //Decline Requested Friend

    @FormUrlEncoded
    @DELETE("user.php/declinefriend")
    Call<ResponseBody> declineFriend(
            @Field("cid") String Cid,
            @Field("uid") String Uid
    );

    //Update Username

    @PUT("user.php/updateusername")
    Call<ResponseBody> updateUsername(
            @Field("id") String id,
            @Field("username") String Username
    );

    //Update Name

    @PUT("user.php/updatename")
    Call<ResponseBody> updateName(
            @Field("id") String id,
            @Field("name") String name
    );

    //Change Online

    @PUT("user.php/changeonline")
    Call<ResponseBody> changeOnline(
            @Field("id") String id,
            @Field("online") int online
    );
}
