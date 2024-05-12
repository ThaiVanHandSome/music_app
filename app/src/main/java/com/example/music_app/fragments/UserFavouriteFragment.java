package com.example.music_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.music_app.R;
import com.example.music_app.activities.AddFavouriteSongsActivity;
import com.example.music_app.adapters.SongAdapter;
import com.example.music_app.databinding.FragmentUserFavouriteBinding;
import com.example.music_app.decorations.BottomOffsetDecoration;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongResponse;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFavouriteFragment extends Fragment {
    FragmentUserFavouriteBinding binding;
    RecyclerView songRecyclerView;
    SongAdapter songAdapter;
    List<Song> favouriteSongs;
    LinearLayout linearLayoutAddToLibrary;
    APIService apiService;
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
        songRecyclerView = binding.rvUserFavourites;
        songRecyclerView.setAdapter(songAdapter);
        songRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        songRecyclerView.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        getSongLikedByIdUser();
        refreshFavouriteSongsIfNeeded();
        return binding.getRoot();
    }

    private void getSongLikedByIdUser() {
        apiService.getSongLikedByIdUser(user.getId()).enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful()) {
                    favouriteSongs = response.body().getData();
                    songAdapter = new SongAdapter(getContext(), favouriteSongs);
                    songRecyclerView.setAdapter(songAdapter);
                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
            }
        });
    }

    private void refreshFavouriteSongsIfNeeded() {
        binding.swipeRefreshLayoutUserFavourites.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSongLikedByIdUser();
                binding.swipeRefreshLayoutUserFavourites.setRefreshing(false);
            }
        });
    }
}