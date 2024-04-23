package com.example.music_app.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8989/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
