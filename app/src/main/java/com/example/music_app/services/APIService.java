package com.example.music_app.services;

import com.example.music_app.models.ForgotPassword;
import com.example.music_app.models.LoginRequest;
import com.example.music_app.models.LoginResponse;
import com.example.music_app.models.OtpResponse;
import com.example.music_app.models.PlaylistResponse;
import com.example.music_app.models.RegisterRequest;
import com.example.music_app.models.RegisterResponse;
import com.example.music_app.models.ResponseMessage;
import com.example.music_app.models.SongResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerModel);

    @POST("auth/authenticate")
    Call<LoginResponse> authenticate(@Body LoginRequest loginRequest);

    @GET("auth/register/confirm")
    Call<OtpResponse> verifyOtp(@Query("token") String token, @Query("type") String type);

    @POST("auth/send-email")
    Call<ResponseMessage> sendOtp(@Body ForgotPassword forgotPassword);

    @PATCH("user/forgot-password")
    Call<ResponseMessage> changePassword(@Body LoginRequest loginRequest);

    @GET("/user/{id_user}/liked-songs")
    Call<PlaylistResponse> getPlaylistByIdUser(@Path("id_user") Long id_user);

    @GET("/user/{id_user}/liked-songs")
    Call<SongResponse> getSongLikedByIdUser(@Path("id_user") Long id_user);

    @GET("/songs")
    Call<SongResponse> getAllSongs();
}
