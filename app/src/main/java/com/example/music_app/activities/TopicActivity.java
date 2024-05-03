package com.example.music_app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.music_app.R;
import com.example.music_app.adapters.SongAdapter;
import com.example.music_app.databinding.ActivityTopicBinding;
import com.example.music_app.decorations.GradientHelper;
import com.example.music_app.models.Playlist;
import com.example.music_app.models.PlaylistResponse;
import com.example.music_app.models.ResponseMessage;
import com.example.music_app.models.Song;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicActivity extends AppCompatActivity {

    ActivityTopicBinding binding;
    APIService apiService;
    RecyclerView recyclerView;
    List<Song> songs;
    SongAdapter adapter;
    ImageView playlistImage;
    TextView playlistName;
    TextView playlistSongCount;
    MaterialButton deletePlaylistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopicBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

        //Binding
        deletePlaylistButton = binding.includedTopPlaylistOption.btnDeletePlaylist;
        playlistImage = binding.includedTopPlaylist.imCoverPicture;
        playlistName = binding.includedTopPlaylist.tvPlaylistTitle;
        playlistSongCount = binding.includedTopPlaylist.tvPlaylistSongCount;
        binding.includedTopPlaylist.tvPlaylistIntro.setVisibility(View.GONE);
        recyclerView = binding.includedListSong.rvListSong;
        adapter = new SongAdapter(this, songs);
        recyclerView.setAdapter(adapter);

        //Get idPlaylist from intent
        Intent intent = getIntent();
        int idPlaylist = intent.getIntExtra("SelectedPlaylist", 0);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getPlaylistById(idPlaylist).enqueue(new Callback<PlaylistResponse>() {
            @Override
            public void onResponse(Call<PlaylistResponse> call, Response<PlaylistResponse> response) {
                if (response.isSuccessful()) {
                    Playlist playlist = response.body().getData();
                    if (playlist != null) {
                        loadTopPlaylist(playlist);
                        loadPlaylistSongs(playlist.getSongs());
                    } else {
                        Log.d("API Response", "Playlist is null");
                    }

                } else {
                    Log.d("API Response", "Request not successful");
                }
            }

            @Override
            public void onFailure(Call<PlaylistResponse> call, Throwable t) {

            }
        });

        deletePlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiService.deletePlaylist(idPlaylist).enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        if (response.isSuccessful()) {
                            Log.d("API Response", "Delete playlist successful");
                            Intent intent = new Intent(TopicActivity.this, LibraryActivity.class);
                            startActivity(intent);
                            Toast.makeText(TopicActivity.this, getText(R.string.toast_deleted_playlist), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    }
                });
            }
        });



    }
    private void loadTopPlaylist(Playlist playlist) {
        Glide.with(TopicActivity.this)
                .load(playlist.getImage())
                .into(playlistImage);
        playlistName.setText(playlist.getName());
        playlistSongCount.setText(getString(R.string.label_songs, playlist.getSongCount()));

        GradientHelper.applyGradient(this, binding.includedTopPlaylist.getRoot(), playlist.getImage(), R.color.neutral2);
    }

    private void loadPlaylistSongs(List<Song> songs) {
        adapter = new SongAdapter(this, songs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter.notifyDataSetChanged();
    }

}