package com.example.music_app.services;

import com.example.music_app.models.ChangePasswordRequest;
import com.example.music_app.models.ForgotPassword;
import com.example.music_app.models.ListPlaylistResponse;
import com.example.music_app.models.LoginRequest;
import com.example.music_app.models.LoginResponse;
import com.example.music_app.models.OAuthLogin;
import com.example.music_app.models.OtpResponse;
import com.example.music_app.models.PlaylistRequest;
import com.example.music_app.models.PlaylistResponse;
import com.example.music_app.models.RegisterRequest;
import com.example.music_app.models.RegisterResponse;
import com.example.music_app.models.ResetPasswordRequest;
import com.example.music_app.models.ResponseMessage;
import com.example.music_app.models.SongLikedRequest;
import com.example.music_app.models.SongResponse;
import com.example.music_app.models.UpdateProfileRequest;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerModel);

    @POST("auth/authenticate")
    Call<LoginResponse> authenticate(@Body LoginRequest loginRequest);

    @POST("auth/authenticate-oauth")
    Call<LoginResponse> authenticateOAuth(@Body RegisterRequest registerRequest);

    @GET("auth/register/confirm")
    Call<OtpResponse> verifyOtp(@Query("token") String token, @Query("type") String type);

    @POST("auth/send-email")
    Call<ResponseMessage> sendOtp(@Body ForgotPassword forgotPassword);

    @PATCH("user/forgot-password")
    Call<ResponseMessage> changePassword(@Body ResetPasswordRequest resetPasswordRequest);

    @GET("user/{id_user}/playlists")
    Call<ListPlaylistResponse> getPlaylistByIdUser(@Path("id_user") int id_user);


    @GET("/user/{id_user}/liked-songs")
    Call<SongResponse> getSongLikedByIdUser(@Path("id_user") Long id_user);

    @GET("user/{id_user}/liked-songs")
    Call<SongResponse> getSongLikedByIdUser(@Path("id_user") int id_user);

    @GET("songs")
    Call<SongResponse> getAllSongs();

    @POST("playlist")
    Call<PlaylistResponse> createPlaylist(@Body PlaylistRequest playlistRequest);

    @GET("playlist/{id_playlist}")
    Call<PlaylistResponse> getPlaylistById(@Path("id_playlist") int id_playlist);

    @DELETE("playlist/{id_playlist}")
    Call<ResponseMessage> deletePlaylist(@Path("id_playlist") int id_playlist);

    @GET("user/{id_user}/not-liked-songs")
    Call<SongResponse> getNotLikedSongsByIdUser(@Path("id_user") int id_user);

    @POST("songLiked/songs")
    Call<ResponseMessage> addSongsToFavourite(@Body SongLikedRequest songLikedRequest);

    @PATCH("user/{id_user}/change-password")
    Call<ResponseMessage> changePasswordWithIdUser(@Path("id_user") int id_user, @Body ChangePasswordRequest changePasswordRequest);

    @PATCH("user/{id_user}")
    Call<ResponseMessage> updateProfile(@Path("id_user") String id_user, @Body UpdateProfileRequest updateProfileRequest);

    @Multipart
    @POST("user/upload")
    Call<ResponseMessage> uploadAvatar(@Part MultipartBody.Part imageFile, @Part("idUser") String idUser
    );
}
