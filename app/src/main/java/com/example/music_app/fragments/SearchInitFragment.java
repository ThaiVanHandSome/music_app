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

import com.example.music_app.R;
import com.example.music_app.activities.SongDetailActivity;
import com.example.music_app.adapters.SongAdapter;
import com.example.music_app.adapters.SongAddToLibraryAdapter;
import com.example.music_app.decorations.BottomOffsetDecoration;
import com.example.music_app.helpers.SongToMediaItemHelper;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.Song;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.services.ExoPlayerQueue;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchInitFragment extends Fragment {
    APIService apiService;
    List<Song> songs;
    SongAdapter adapter;
    RecyclerView rvDeXuat;
    ExoPlayerQueue exoPlayerQueue;

    public SearchInitFragment() {
        // Required empty public constructor
    }

    public static SearchInitFragment newInstance() {
        SearchInitFragment fragment = new SearchInitFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_init, container, false);
        // Ánh xạ các view
        initView(view);
        exoPlayerQueue = ExoPlayerQueue.getInstance();
        getAllSongs();

        return view;
    }

    private void initView(View view) {
        rvDeXuat = view.findViewById(R.id.rc_dexuat_seach);

    }
    private void getAllSongs() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<GenericResponse<List<Song>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Song>>> call, Response<GenericResponse<List<Song>>> response) {
                if (response.isSuccessful()) {
                    songs = response.body().getData();
                    Collections.shuffle(songs);
                    adapter = new SongAdapter(requireContext(), songs, new SongAdapter.OnItemClickListener() {
                        @Override
                        public void onSongClick(int position) {
                            exoPlayerQueue.clear();
                            exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(songs));
                            exoPlayerQueue.setCurrentPosition(position);
                            Intent intent = new Intent(getContext(), SongDetailActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onPlayPlaylistClick(List<Song> songList) {

                        }
                    });
                    rvDeXuat.setAdapter(adapter);
                    rvDeXuat.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                    rvDeXuat.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
                    rvDeXuat.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Song>>> call, Throwable t) {

            }
        });

    }
}