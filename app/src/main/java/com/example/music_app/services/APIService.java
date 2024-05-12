package com.example.music_app.services;

import com.example.music_app.models.Artist;
import com.example.music_app.models.ForgotPassword;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.LoginRequest;
import com.example.music_app.models.LoginResponse;
import com.example.music_app.models.OtpResponse;
import com.example.music_app.models.RegisterRequest;
import com.example.music_app.models.RegisterResponse;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongResponse;

import java.util.List;

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
    Call<RegisterResponse> sendOtp(@Body ForgotPassword forgotPassword);

    @PATCH("user/forgot-password")
    Call<RegisterResponse> changePassword(@Body LoginRequest loginRequest);


    // Songs API
    @GET("song/most-views")
    Call<GenericResponse<SongResponse>> getMostViewSong(@Query("page") int page, @Query("size") int size);

    @GET("song/most-likes")
    Call<GenericResponse<SongResponse>> getMostLikeSong(@Query("page") int page, @Query("size") int size);

    @GET("song/new-released")
    Call<GenericResponse<SongResponse>> getSongNewReleased(@Query("page") int page, @Query("size") int size);

    @GET("song/{songId}/artists")
    Call<GenericResponse<List<Artist>>> getArtistsBySongId(@Path("songId") int songId);
}
