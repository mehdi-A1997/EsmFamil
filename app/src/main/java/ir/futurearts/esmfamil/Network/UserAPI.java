package ir.futurearts.esmfamil.Network;

import ir.futurearts.esmfamil.Network.Responses.FriendsResponse;
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
    @POST("user.php/signup")
    Call<LoginResponse> SingUp(
          @Field("email") String Email,
          @Field("username") String Username,
          @Field("pass") String Password
    );


    //Login User

    @FormUrlEncoded
    @POST("user.php/login")
    Call<LoginResponse> LogIn(
          @Field("username") String Username,
          @Field("pass") String Password
    );

    //Get getFriends

    @GET("user.php/friends/{id}")
    Call<FriendsResponse> getFriends(
            @Path("id") String id
    );

    //Get getFriends

    @GET("user.php/onlinefriends/{id}")
    Call<FriendsResponse> getOnlineFriends(
            @Path("id") String id
    );

    //Get getRank

    @GET("user.php/rank")
    Call<FriendsResponse> getRank();

    //Get Friend Request

    @GET("user.php/friendrequest/{id}")
    Call<FriendsResponse> friendRequests(
            @Path("id") String id
    );

    //Search User By Username

    @GET("user.php/searchuser/{username}")
    Call<LoginResponse> searchUser(
            @Path("username") String Username
    );

    //Search User By Username

    @GET("user.php/getuser/{id}")
    Call<LoginResponse> getUser(
            @Path("id") String id
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

    //Update User

    @PUT("user.php/updateuser")
    @FormUrlEncoded
    Call<ResponseBody> updateUser(
            @Field("id") String id,
            @Field("name") String name,
            @Field("username") String username,
            @Field("img") String img,
            @Field("changed") int changed
    );

    //Change Online

    @PUT("user.php/changeonline")
    @FormUrlEncoded
    Call<ResponseBody> changeOnline(
            @Field("id") String id,
            @Field("online") int online
    );

    //Change Password

    @PUT("user.php/changepassword")
    @FormUrlEncoded
    Call<ResponseBody> changePassword(
            @Field("id") String id,
            @Field("oldpass") String  pass,
            @Field("newpass") String  newpass
    );

    //Get Score

    @GET("user.php/getscore/{id}")
    Call<ResponseBody> getScore(
            @Path("id") String id
    );
}
