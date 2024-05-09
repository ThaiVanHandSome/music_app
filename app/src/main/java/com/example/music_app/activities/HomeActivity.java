package com.example.music_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.music_app.R;
import com.example.music_app.adapters.SongHomeAdapter;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongResponse;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rvTopThinhHanh;
    SongHomeAdapter songHomeAdapter;
    APIService apiService;
    List<Song> songList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AnhXa();
        GetTopThinhHanh();
        Log.e("DataRes", "Code chay vao day");
    }

    private void AnhXa() {
        // Ánh xạ
        rvTopThinhHanh = findViewById(R.id.rv_topthinhhanh);
    }
    private void GetTopThinhHanh(){
        Log.e("DataRes", "Code chay vao ham get Thinh Hanh");
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("DataRes", "Code chay vao Res");
                    songList = response.body().getData();
                    Log.e("DataRes", songList.toString());
//                    songHomeAdapter = new SongHomeAdapter(HomeActivity.this, songList);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//                    rvTopThinhHanh.setHasFixedSize(true);
//                    rvTopThinhHanh.setLayoutManager(layoutManager);
//                    rvTopThinhHanh.setAdapter(songHomeAdapter);
//                    songHomeAdapter.notifyDataSetChanged();
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