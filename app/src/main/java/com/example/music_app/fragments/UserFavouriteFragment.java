package com.example.music_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_app.R;
import com.example.music_app.adapters.SongAdapter;
import com.example.music_app.databinding.FragmentUserFavouriteBinding;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongResponse;
import com.example.music_app.services.APIService;
import com.example.music_app.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFavouriteFragment extends Fragment {
    FragmentUserFavouriteBinding binding;
    RecyclerView recyclerView;
    SongAdapter adapter;
    List<Song> favouriteSongs;
    public UserFavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserFavouriteBinding.inflate(inflater, container, false);

        // Set text for tvAddToLibrary in include layout
        binding.linearLayoutAddToLibary.tvAddToLibrary.setText(R.string.label_add_new_favourite);

        // Bind recyclerView and adapter
        recyclerView = binding.rvUserFavourites;
        adapter = new SongAdapter(getContext(), favouriteSongs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getSongLikedByIdUser();
        return binding.getRoot();

    }

    private void getSongLikedByIdUser() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        //TODO: get idUser later
        apiService.getSongLikedByIdUser(1L).enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful()) {
                    favouriteSongs = response.body().getData();
                    Log.d("APIService", "JSON response: " + new Gson().toJson(response.body()));
                    Log.d("APIService", "getSongLikedByIdUser: " + favouriteSongs.size());
                    adapter = new SongAdapter(getContext(), favouriteSongs);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.d("RetrofitError", String.valueOf(response.code() + ": " + response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
                Log.d("RetrofitError", t.getMessage());
            }
        });
    }
}