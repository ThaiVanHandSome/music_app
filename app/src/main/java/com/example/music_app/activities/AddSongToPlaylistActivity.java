package com.example.music_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app.R;
import com.example.music_app.adapters.PlaylistAddToLibraryAdapter;
import com.example.music_app.databinding.ActivityAddSongToPlaylistBinding;
import com.example.music_app.decorations.BottomOffsetDecoration;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.ListPlaylistResponse;
import com.example.music_app.models.Playlist;
import com.example.music_app.models.ResponseMessage;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSongToPlaylistActivity extends AppCompatActivity {
    ActivityAddSongToPlaylistBinding binding;
    List<Playlist> playlists;
    RecyclerView recyclerView;
    PlaylistAddToLibraryAdapter adapter;
    APIService apiService;
    User user = SharePrefManagerUser.getInstance(this).getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSongToPlaylistBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        recyclerView = binding.rvYourPlaylists;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        getUserPlaylists();
        addSongToPlaylist();
        clearCheckedSongsIfNeeded();
    }

    private void getUserPlaylists() {
        apiService.getPlaylistByIdUser(user.getId()).enqueue(new Callback<ListPlaylistResponse>() {
            @Override
            public void onResponse(Call<ListPlaylistResponse> call, Response<ListPlaylistResponse> response) {
                if (response.isSuccessful()) {
                    playlists = response.body().getData();
                    adapter = new PlaylistAddToLibraryAdapter(getApplicationContext(), playlists);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ListPlaylistResponse> call, Throwable t) {

            }
        });
    }
    private void addSongToPlaylist() {
        binding.fabAddToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Log.d("AddSongToPlaylist", "SongId: " + String.valueOf(intent.getLongExtra("SongId", 0)));
                Long songId = intent.getLongExtra("SongId", 0);
                for (int i = 0; i < adapter.getCheckedPlaylistIds().size(); i++) {
                    Log.d("AddSongToPlaylist", "PlaylistId: " + String.valueOf(adapter.getCheckedPlaylistIds().get(i)));
                    apiService.addSongToPlaylist(adapter.getCheckedPlaylistIds().get(i), songId).enqueue(new Callback<ResponseMessage>() {
                        @Override
                        public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(AddSongToPlaylistActivity.this, LibraryActivity.class);
                                startActivity(intent);
                                Toast.makeText(AddSongToPlaylistActivity.this, getText(R.string.toast_added_song_to_playlist), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseMessage> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void clearCheckedSongsIfNeeded() {
        binding.tvClearChoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clearAllCheckedPlaylists();
            }
        });
    }
}