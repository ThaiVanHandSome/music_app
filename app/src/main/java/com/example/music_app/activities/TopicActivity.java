package com.example.music_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentTransaction;
import androidx.media3.common.MediaMetadata;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.adapters.SongAdapter;
import com.example.music_app.databinding.ActivityTopicBinding;
import com.example.music_app.decorations.GradientHelper;
import com.example.music_app.fragments.SongDetailFragment;
import com.example.music_app.helpers.SongToMediaItemHelper;
import com.example.music_app.listeners.PaginationScrollListener;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.Playlist;
import com.example.music_app.models.PlaylistResponse;
import com.example.music_app.models.ResponseMessage;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongResponse;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.services.ExoPlayerQueue;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicActivity extends BaseActivity implements SongAdapter.OnItemClickListener {

//    Hoang Phuc
    private String topic;
    private List<Song> songList;
    private SongAdapter songAdapter;
    private int page, size, totalPages;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean isShuffle = false;
    private ExoPlayerQueue exoPlayerQueue;
    private SongDetailFragment songDetailFragment;
//    ----------------------------------

//    Trong Phuc
    ActivityTopicBinding binding;
    APIService apiService;
    RecyclerView recyclerView;
    List<Song> songs;
    SongAdapter adapter;
    ImageView playlistImage;
    TextView playlistName;
    TextView playlistSongCount;
    MaterialButton deletePlaylistButton;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        initMiniPlayer();
        exoPlayerQueue = ExoPlayerQueue.getInstance();
        topic = getIntent().getStringExtra("topic");

        View includeTopPlaylist = findViewById(R.id.included_top_playlist);
        ImageView coverPic = includeTopPlaylist.findViewById(R.id.imCoverPicture);
        TextView tvPlaylistTitle = includeTopPlaylist.findViewById(R.id.tvPlaylistTitle);
        TextView tvPlaylistIntro = includeTopPlaylist.findViewById(R.id.tvPlaylistIntro);
        TextView tvPlaySongCount = includeTopPlaylist.findViewById(R.id.tvPlaylistSongCount);
        tvPlaySongCount.setVisibility(View.GONE);

        View includeOption = findViewById(R.id.included_top_playlist_option);

        View includeListSong = findViewById(R.id.included_list_song);
        RecyclerView rvListSong = includeListSong.findViewById(R.id.rvListSong);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvListSong.setLayoutManager(layoutManager);
//        RecyclerView.ItemDecoration itemDecoration = new VerticalSpaceItemDecoration(R.dimen.spacing_4dp);
//        rvListSong.addItemDecoration(itemDecoration);
        rvListSong.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                loadNextPage(topic);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        View containerFragment = findViewById(R.id.fragment_container);
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        songList = new ArrayList<>();
        songAdapter = new SongAdapter(getApplicationContext(), songList, this);
        switch (topic) {
            case "trending":
                songList.clear();
                page = 0;
                size = 10;
                containerFragment.setBackgroundResource(R.drawable.linear_background_trending);
                coverPic.setImageResource(R.drawable.trending_cover);
                tvPlaylistTitle.setText(R.string.trending_title);
                tvPlaylistIntro.setText(R.string.trending_intro);

                fetchSongs(apiService.getMostViewSong(page, size));
                rvListSong.setAdapter(songAdapter);
                break;
            case "favorite":
                songList.clear();
                page = 0;
                size = 10;
                containerFragment.setBackgroundResource(R.drawable.linear_background_favorite);
                coverPic.setImageResource(R.drawable.favorite_cover);
                tvPlaylistTitle.setText(R.string.favorite_title);
                tvPlaylistIntro.setText(R.string.favorite_intro);

                fetchSongs(apiService.getMostLikeSong(page, size));
                rvListSong.setAdapter(songAdapter);
                break;
            case "topArtist":
                containerFragment.setBackgroundResource(R.drawable.linear_background_topartist);
                coverPic.setImageResource(R.drawable.top_artist_cover);
                tvPlaylistTitle.setText(R.string.topartist_title);
                tvPlaylistIntro.setText(R.string.topartist_intro);
                includeOption.setVisibility(View.GONE);
                break;
            case "newReleased":

        }

        MaterialButton btnShuffle = includeOption.findViewById(R.id.btnTopPlaylistOptionShuffle);
        MaterialButton btnPlayAll = includeOption.findViewById(R.id.btnTopPlaylistOptionPlay);

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShuffle = !isShuffle;
                if (isShuffle) {
                    btnShuffle.setIconTint(getResources().getColorStateList(R.color.primary));
                } else {
                    btnShuffle.setIconTint(getResources().getColorStateList(R.color.neutral3));
                }
            }
        });

        btnPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayPlaylistClick(songList);
            }
        });

//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                getSupportFragmentManager().beginTransaction().hide(songDetailFragment).commit();
//                includeMiniPlayer.setVisibility(View.VISIBLE);
//
//            }
//        });

//        binding = ActivityTopicBinding.inflate(getLayoutInflater(), null, false);
//        setContentView(binding.getRoot());
//        //Binding
//        deletePlaylistButton = binding.includedTopPlaylistOption.btnDeletePlaylist;
//        playlistImage = binding.includedTopPlaylist.imCoverPicture;
//        playlistName = binding.includedTopPlaylist.tvPlaylistTitle;
//        playlistSongCount = binding.includedTopPlaylist.tvPlaylistSongCount;
//        binding.includedTopPlaylist.tvPlaylistIntro.setVisibility(View.GONE);
//        recyclerView = binding.includedListSong.rvListSong;
//        adapter = new SongAdapter(this, songs, null);
//        recyclerView.setAdapter(adapter);
////---------------------------------------------------
//        //Get idPlaylist from intent
//        Intent intent = getIntent();
//
//        int idPlaylist = intent.getIntExtra("SelectedPlaylist", 0);
//        if (idPlaylist == 0) {
//            Log.d("Intent", "No idPlaylist found");
//            return;
//        }
//        apiService = RetrofitClient.getRetrofit().create(APIService.class);
//        apiService.getPlaylistById(idPlaylist).enqueue(new Callback<PlaylistResponse>() {
//            @Override
//            public void onResponse(Call<PlaylistResponse> call, Response<PlaylistResponse> response) {
//                if (response.isSuccessful()) {
//                    Playlist playlist = response.body().getData();
//                    if (playlist != null) {
//                        loadTopPlaylist(playlist);
//                        loadPlaylistSongs(playlist.getSongs());
//                    } else {
//                        Log.d("API Response", "Playlist is null");
//                    }
//
//                } else {
//                    Log.d("API Response", "Request not successful");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PlaylistResponse> call, Throwable t) {
//
//            }
//        });
//
//        APIService finalApiService = apiService;
//        deletePlaylistButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finalApiService.deletePlaylist(idPlaylist).enqueue(new Callback<ResponseMessage>() {
//                    @Override
//                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
//                        if (response.isSuccessful()) {
//                            Log.d("API Response", "Delete playlist successful");
//                            Intent intent = new Intent(TopicActivity.this, LibraryActivity.class);
//                            startActivity(intent);
//                            Toast.makeText(TopicActivity.this, getText(R.string.toast_deleted_playlist), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseMessage> call, Throwable t) {
//                    }
//                });
//            }
//        });
    }

    private void fetchSongs(Call<GenericResponse<SongResponse>> call) {
        call.enqueue(new Callback<GenericResponse<SongResponse>>() {
            @Override
            public void onResponse(Call<GenericResponse<SongResponse>> call, Response<GenericResponse<SongResponse>> response) {
                if (response.isSuccessful()) {
                    List<Song> newList =  response.body().getData().getContent();
                    totalPages = response.body().getData().getTotalPages();
                    songList.addAll(newList);
                    page++;
                    songAdapter.notifyDataSetChanged();
                    Log.d("TopicFragment", "Total pages: " + response.body().getData().getTotalPages());
                }
            }
            @Override
            public void onFailure(Call<GenericResponse<SongResponse>> call, Throwable t) {
                Log.d("TopicFragment", "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadNextPage(String topic) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page < totalPages) {
                    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
                    Call<GenericResponse<SongResponse>> call = null;
                    switch (topic) {
                        case "trending":
                            call = apiService.getMostViewSong(page, size);
                            break;
                        case "favorite":
                            call = apiService.getMostLikeSong(page, size);
                            break;
                        case "newReleased":
                            call = apiService.getSongNewReleased(page, size);
                            break;
                    }
                    fetchSongs(call);
                }
                isLoading = false;
                if (page == totalPages) {
                    isLastPage = true;
                }
            }
        }, 500);
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void updateMiniplayer(MediaMetadata metadata) {
        super.updateMiniplayer(metadata);
        Log.d("TopicActivity", "updateMiniplayer: updated");
    }

    @Override
    public void onSongClick(int position) {
        exoPlayerQueue.clear();
        exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(songList));
        exoPlayerQueue.setCurrentPosition(position);
        if (isShuffle) {
            exoPlayerQueue.setShuffle(true);
        } else {
            exoPlayerQueue.setShuffle(false);
        }
        Log.d("SongClicked", "onSongClick: " + songList.get(position).getName() + " " + songList.get(position).getIdSong());
        Intent intent = new Intent(this, SongDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPlayPlaylistClick(List<Song> songList) {
        exoPlayerQueue.clear();
        exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(songList));
        exoPlayerQueue.setCurrentPosition(0);
        if (isShuffle) {
            exoPlayerQueue.setShuffle(true);
        } else {
            exoPlayerQueue.setShuffle(false);
        }

        Intent intent = new Intent(this, SongDetailActivity.class);
        startActivity(intent);
        // Trong Phuc

    }
    private void loadTopPlaylist(Playlist playlist) {
        Glide.with(TopicActivity.this)
                .load(playlist.getImage())
                .into(playlistImage);
        playlistName.setText(playlist.getName());
        playlistSongCount.setText(getString(R.string.label_songs, playlist.getSongCount()));

        GradientHelper.applyGradient(this, binding.includedTopPlaylist.getRoot(), playlist.getImage(), R.color.neutral2);
    }

    private void loadPlaylistSongs(List<Song> songs) {
        adapter = new SongAdapter(this, songs, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter.notifyDataSetChanged();
    }

}