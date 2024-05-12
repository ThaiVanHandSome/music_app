package com.example.music_app.helpers;
import com.example.music_app.models.Artist;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistHelper {
    public static void getArtistBySongId(int songId, final ArtistCallback callback) {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getArtistsBySongId(songId).enqueue(new Callback<GenericResponse<List<Artist>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Artist>>> call, Response<GenericResponse<List<Artist>>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    Artist artist = response.body().getData().get(0);
                    callback.onSuccess(artist);
                } else {
                    callback.onFailure(new Exception("No artist found for songId: " + songId));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Artist>>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public interface ArtistCallback {
        void onSuccess(Artist artist);
        void onFailure(Throwable t);
    }
}