package com.example.music_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.adapters.SongAddToLibraryAdapter;
import com.example.music_app.databinding.ActivityCreatePlaylistBinding;
import com.example.music_app.decorations.BottomOffsetDecoration;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.Playlist;
import com.example.music_app.models.PlaylistRequest;
import com.example.music_app.models.PlaylistResponse;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongResponse;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePlaylistActivity extends AppCompatActivity {
    ActivityCreatePlaylistBinding binding;
    RecyclerView recyclerView;
    SongAddToLibraryAdapter adapter;
    List<Song> songs;
    APIService apiService;

    User user = SharePrefManagerUser.getInstance(this).getUser();

    TextInputLayout playlistNameLayout;
    TextInputEditText edtPlaylistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_create_playlist);*/
        binding = ActivityCreatePlaylistBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

        // Binding views
        recyclerView = binding.rvRecommendedSongs;
        playlistNameLayout = binding.playlistNameLayout;
        edtPlaylistName = binding.edtPlaylistName;
        edtPlaylistName.requestFocus();

        adapter = new SongAddToLibraryAdapter(getApplicationContext(), songs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
        getAllSongs();
        createPlaylist();
        clearCheckedSongs();
    }

    private void getAllSongs() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful()) {
                    songs = response.body().getData();
                    Collections.shuffle(songs);
                    adapter = new SongAddToLibraryAdapter(getApplicationContext(), songs);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {

            }
        });

    }


    private void createPlaylist() {
        binding.fabCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaylistRequest playlistRequest = new PlaylistRequest();
                playlistRequest.setIdUser(user.getId());
                playlistRequest.setName(binding.edtPlaylistName.getText().toString());
                playlistRequest.setSongIds(adapter.getCheckedSongIds());
                apiService = RetrofitClient.getRetrofit().create(APIService.class);
                apiService.createPlaylist(playlistRequest).enqueue(new Callback<PlaylistResponse>() {
                    @Override
                    public void onResponse(Call<PlaylistResponse> call, Response<PlaylistResponse> response) {
                        if (response.isSuccessful()) {
                            Playlist playlist = response.body().getData();
                            Intent intent = new Intent(CreatePlaylistActivity.this, LibraryActivity.class);
                            startActivity(intent);
                            Toast.makeText(CreatePlaylistActivity.this, getText(R.string.toast_created_playlist), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlaylistResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void clearCheckedSongs() {
        binding.tvClearChoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clearAllCheckedSongs();
            }
        });
    }
}