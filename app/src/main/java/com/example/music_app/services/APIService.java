package com.example.music_app.services;

import com.example.music_app.models.Playlist;
import com.example.music_app.models.PlaylistResponse;
import com.example.music_app.models.SongResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {
    @GET("user/{idUser}/playlists")
    Call<PlaylistResponse> getPlaylistByIdUser (@Path("idUser") Long idUser);

    @GET("user/{idUser}/liked-songs")
    Call<SongResponse> getSongLikedByIdUser (@Path("idUser") Long idUser);
}
