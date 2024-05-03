package com.example.music_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.adapters.SongAddToLibraryAdapter;
import com.example.music_app.databinding.ActivityAddFavouriteSongsBinding;
import com.example.music_app.decorations.BottomOffsetDecoration;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.ResponseMessage;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongLikedRequest;
import com.example.music_app.models.SongResponse;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFavouriteSongsActivity extends AppCompatActivity {
    ActivityAddFavouriteSongsBinding binding;
    RecyclerView recyclerView;
    SongAddToLibraryAdapter adapter;
    List<Song> songs;
    APIService apiService;
    User user = SharePrefManagerUser.getInstance(this).getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFavouriteSongsBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        
        // Binding views
        recyclerView = binding.rvRecommendedSongs;
        adapter = new SongAddToLibraryAdapter(this, songs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
        getSongNotInFavourite();
        addSongToFavourite();
    }

    private void getSongNotInFavourite() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getNotLikedSongsByIdUser(user.getId()).enqueue(new Callback<SongResponse>() {
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

    public void addSongToFavourite() {
        binding.fabAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongLikedRequest songLikedRequest = new SongLikedRequest();
                songLikedRequest.setIdUser((long) user.getId());
                songLikedRequest.setSongIds(adapter.getCheckedSongIds());
                apiService = RetrofitClient.getRetrofit().create(APIService.class);
                apiService.addSongsToFavourite(songLikedRequest).enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(AddFavouriteSongsActivity.this, LibraryActivity.class);
                            startActivity(intent);
                            Toast.makeText(AddFavouriteSongsActivity.this, getText(R.string.toast_added_song_to_favourite), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {

                    }
                });
            }
        });
    }
}