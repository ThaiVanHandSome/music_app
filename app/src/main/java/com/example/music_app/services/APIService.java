package com.example.music_app.services;

import com.example.music_app.models.ForgotPassword;
import com.example.music_app.models.LoginRequest;
import com.example.music_app.models.LoginResponse;
import com.example.music_app.models.OtpResponse;
import com.example.music_app.models.RegisterRequest;
import com.example.music_app.models.RegisterResponse;
import com.example.music_app.models.ResponseMessage;
import com.example.music_app.models.Playlist;
import com.example.music_app.models.PlaylistResponse;
import com.example.music_app.models.SongResponse;
  
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;

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
  
    @GET("user/{idUser}/playlists")
    Call<PlaylistResponse> getPlaylistByIdUser (@Path("idUser") Long idUser);

    @GET("user/{idUser}/liked-songs")
    Call<SongResponse> getSongLikedByIdUser (@Path("idUser") Long idUser);
}
