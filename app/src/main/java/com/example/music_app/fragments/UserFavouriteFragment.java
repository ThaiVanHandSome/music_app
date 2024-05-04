package com.example.music_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.music_app.R;
import com.example.music_app.activities.AddFavouriteSongsActivity;
import com.example.music_app.activities.CreatePlaylistActivity;
import com.example.music_app.adapters.SongAdapter;
import com.example.music_app.databinding.FragmentUserFavouriteBinding;
import com.example.music_app.decorations.BottomOffsetDecoration;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongResponse;
import com.example.music_app.models.User;
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

    LinearLayout linearLayoutAddToLibrary;
    User user = SharePrefManagerUser.getInstance(this.getContext()).getUser();
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

        // Set onClickListener for linearLayoutAddToLibrary
        linearLayoutAddToLibrary = binding.linearLayoutAddToLibary.getRoot();
        linearLayoutAddToLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddFavouriteSongsActivity.class);
                startActivity(intent);
            }
        });

        // Bind recyclerView and adapter
        recyclerView = binding.rvUserFavourites;
        adapter = new SongAdapter(getContext(), favouriteSongs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
        getSongLikedByIdUser();
        return binding.getRoot();

    }

    private void getSongLikedByIdUser() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getSongLikedByIdUser(user.getId()).enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful()) {
                    favouriteSongs = response.body().getData();
                    adapter = new SongAdapter(getContext(), favouriteSongs);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
            }
        });
    }
}