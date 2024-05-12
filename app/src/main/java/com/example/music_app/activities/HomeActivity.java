package com.example.music_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.music_app.R;
import com.example.music_app.adapters.ArtistAdapter;
import com.example.music_app.adapters.NewSongHomeAdapter;
import com.example.music_app.adapters.SongAddToLibraryAdapter;
import com.example.music_app.adapters.SongHomeAdapter;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongResponse;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rvTopTrend, rvFavoriteSong, rvTopArtist, rvNewSong;
    SongHomeAdapter songHomeAdapter;
    ArtistAdapter artistAdapter;
    NewSongHomeAdapter newSongHomeAdapter;

    APIService apiService;
    List<Song> trendSongs, favoriteSongs, NewSongs;
    List<User> Artists;

    TextView title, xtt_topthinhhanh, xtt_moinguoiyeuthich, xtt_nghesihangdau, xtt_moiramat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        User user = SharePrefManagerUser.getInstance(this).getUser();
        AnhXa();
        title.setText("Chào " + user.getFirstName() + " " + user.getLastName() + " 👋");
        GetTopTrend();
        GetFavoriteSong();
        GetTopArtist();
        GetNewSong();

        xtt_topthinhhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HomeSongsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Keyy", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        xtt_moinguoiyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HomeSongsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Keyy", 2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        xtt_nghesihangdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HomeSongsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Keyy", 3);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        xtt_moiramat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HomeSongsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Keyy", 4);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void AnhXa() {
        rvTopTrend = findViewById(R.id.rv_toptrend);
        rvFavoriteSong = findViewById(R.id.rv_topfavorite);
        rvTopArtist = findViewById(R.id.rv_topartists);
        rvNewSong = findViewById(R.id.rv_newsongs);
        title = findViewById(R.id.title_appbar_home);
        xtt_topthinhhanh = findViewById(R.id.xemtatca_topthinhhanh);
        xtt_moinguoiyeuthich = findViewById(R.id.xemtatca_moinguoiyeuthich);
        xtt_nghesihangdau = findViewById(R.id.xemtatca_nghesihangdau);
        xtt_moiramat = findViewById(R.id.xemtatca_moiramat);
    }
    private void GetTopTrend(){
        Log.e("DataRes", "Code chay vao ham get Thinh Hanh");
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful()) {
                    trendSongs = response.body().getData();
                    songHomeAdapter = new SongHomeAdapter(getApplicationContext(), trendSongs);
                    rvTopTrend.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false );
                    rvTopTrend.setLayoutManager(layoutManager);
                    rvTopTrend.setAdapter(songHomeAdapter);
                } else {
                    Log.e("DataRes", "No Res");

                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }

        });
    }
    private void GetFavoriteSong(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful()) {
                    favoriteSongs = response.body().getData();
                    songHomeAdapter = new SongHomeAdapter(getApplicationContext(), favoriteSongs);
                    rvFavoriteSong.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false );
                    rvFavoriteSong.setLayoutManager(layoutManager);
                    rvFavoriteSong.setAdapter(songHomeAdapter);
                } else {
                    Log.e("DataRes", "No Res");

                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }
        });
    }
    private void GetTopArtist(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful()) {
                    NewSongs = response.body().getData();
                    artistAdapter = new ArtistAdapter(getApplicationContext(), NewSongs);
                    rvTopArtist.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false );
                    rvTopArtist.setLayoutManager(layoutManager);
                    rvTopArtist.setAdapter(artistAdapter);
                } else {
                    Log.e("DataRes", "No Res");

                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }
        });
    }
    private void GetNewSong(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful()) {
                    NewSongs = response.body().getData();
                    newSongHomeAdapter = new NewSongHomeAdapter(getApplicationContext(), NewSongs);
                    rvNewSong.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false );
                    rvNewSong.setLayoutManager(layoutManager);
                    rvNewSong.setAdapter(newSongHomeAdapter);
                } else {
                    Log.e("DataRes", "No Res");

                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }
        });
    }
}