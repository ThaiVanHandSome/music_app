package com.example.music_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_app.R;
import com.example.music_app.adapters.PlaylistAdapter;
import com.example.music_app.databinding.FragmentUserPlaylistBinding;
import com.example.music_app.models.Playlist;
import com.example.music_app.models.PlaylistResponse;
import com.example.music_app.services.APIService;
import com.example.music_app.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPlaylistFragment extends Fragment {
    FragmentUserPlaylistBinding binding;
    RecyclerView recyclerView;
    PlaylistAdapter adapter;
    List<Playlist> playlists = new ArrayList<>();
    public UserPlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserPlaylistBinding.inflate(inflater, container, false);

        // Set text for tvAddToLibrary in include layout
        binding.linearLayoutAddToLibary.tvAddToLibrary.setText(R.string.label_add_new_playlist);

        // Bind recyclerView and adapter
        recyclerView = binding.rvUserPlaylist;
        adapter = new PlaylistAdapter(getContext(), playlists);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        getPlaylistByIdUser();
        return binding.getRoot();
    }

    private void getPlaylistByIdUser() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        //TODO: get idUser later
        apiService.getPlaylistByIdUser(1L).enqueue(new Callback<PlaylistResponse>() {
            @Override
            public void onResponse(Call<PlaylistResponse> call, Response<PlaylistResponse> response) {
                if (response.isSuccessful()) {
                    playlists.addAll(response.body().getData());
                    Log.d("APIService", "JSON response: " + new Gson().toJson(response.body()));
                    Log.d("APIService", "getPlaylistByIdUser: " + playlists.size());
                    adapter = new PlaylistAdapter(getContext(), playlists);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("RetrofitError", String.valueOf(response.code() + ": " + response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<PlaylistResponse> call, Throwable t) {
                Log.d("RetrofitError", t.getMessage());
            }
        });
    }
}