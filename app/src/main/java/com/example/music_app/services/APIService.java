package com.example.music_app.services;

import com.example.music_app.models.RegisterModel;
import com.example.music_app.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterModel registerModel);

    @GET("auth/register/confirm")
    Call<RegisterResponse> verifyOtp(@Query("token") String token);

}
