package net.winnerawan.openmadiun.helper;

import net.winnerawan.openmadiun.response.Response;
import net.winnerawan.openmadiun.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by winnerawan on 2/9/17.
 */

public interface AppInterface {

    @GET("/places")
    Call<Response> getPlaces();

    @GET("/place/tour")
    Call<Response> getPlaceTour();

    @GET("/place/kost")
    Call<Response> getKost();

    @POST("/signin")
    Call<UserResponse> signIn(@Query("email") String email, @Query("password") String password);

    @POST("/signup")
    Call<UserResponse> signUp(@Query("username") String username, @Query("email") String email, @Query("password") String password);

}
