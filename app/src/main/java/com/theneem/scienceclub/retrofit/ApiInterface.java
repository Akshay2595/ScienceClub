package com.theneem.scienceclub.retrofit;

import com.google.gson.JsonObject;
import com.theneem.scienceclub.model.checkTheSubscription;
import com.theneem.scienceclub.response.ClubListResponse;
import com.theneem.scienceclub.response.ForgotPassResponse;
import com.theneem.scienceclub.response.JoiningClubResponse;
import com.theneem.scienceclub.response.RegisterResponse;
import com.theneem.scienceclub.response.SocialLoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("clublist.php")
    Call<ClubListResponse> getClubList();

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterResponse> getRegister(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<RegisterResponse> getLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("forgotPasswordOtp.php")
    Call<ForgotPassResponse> getForgotPassword(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("resetPassword.php")
    Call<RegisterResponse> getResetPassword(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("socialLogin.php")
    Call<SocialLoginResponse> getSocialLogin(
            @Field("isSocial") String isSocial,
            @Field("username") String username,
            @Field("email") String email,
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("joinTheClub.php")
    Call<JoiningClubResponse> getJoinClub(
            @Field("id") String id,
            @Field("clubid") String clubid

    );

    @FormUrlEncoded
    @POST("checkTheSubscription.php")
    Call<checkTheSubscription> getClubSubscription(
            @Field("id") String id,
            @Field("clubid") String clubid
    );
}
