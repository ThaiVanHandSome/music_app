package com.example.music_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.music_app.R;
import com.example.music_app.adapters.ArtistAdapter;
import com.example.music_app.adapters.NewSongHomeAdapter;
import com.example.music_app.adapters.SongHomeAdapter;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongResponse;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    RecyclerView rvTopTrend, rvFavoriteSong, rvTopArtist, rvNewSong;
    SongHomeAdapter songHomeAdapter;
    ArtistAdapter artistAdapter;
    NewSongHomeAdapter newSongHomeAdapter;

    APIService apiService;
    List<Song> trendSongs, favoriteSongs, NewSongs;
    List<User> Artists;

    TextView title, xtt_topthinhhanh, xtt_moinguoiyeuthich, xtt_nghesihangdau, xtt_moiramat;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initMiniPlayer();
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
                Intent intent = new Intent(HomeActivity.this, TopicActivity.class);
                intent.putExtra("topic", "trending");
                startActivity(intent);
            }
        });
        xtt_moinguoiyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TopicActivity.class);
                intent.putExtra("topic", "favorite");
                startActivity(intent);
            }
        });
        xtt_nghesihangdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TopicActivity.class);
                intent.putExtra("topic", "topArtist");
                startActivity(intent);
            }
        });
        xtt_moiramat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TopicActivity.class);
                intent.putExtra("topic", "newReleased");
                startActivity(intent);
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_search:
                        break;
                    case R.id.menu_item_library:
                        Intent intent2 = new Intent(HomeActivity.this, LibraryActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
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
        bottomNavigationView = findViewById(R.id.navigation);
    }
    private void GetTopTrend(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<GenericResponse<List<Song>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Song>>> call, Response<GenericResponse<List<Song>>> response) {
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
            public void onFailure(Call<GenericResponse<List<Song>>> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }

        });
    }
    private void GetFavoriteSong(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<GenericResponse<List<Song>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Song>>> call, Response<GenericResponse<List<Song>>> response) {
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
            public void onFailure(Call<GenericResponse<List<Song>>> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }
        });
    }
    private void GetTopArtist(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<GenericResponse<List<Song>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Song>>> call, Response<GenericResponse<List<Song>>> response) {
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
            public void onFailure(Call<GenericResponse<List<Song>>> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }
        });
    }
    private void GetNewSong(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<GenericResponse<List<Song>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Song>>> call, Response<GenericResponse<List<Song>>> response) {
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
            public void onFailure(Call<GenericResponse<List<Song>>> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}