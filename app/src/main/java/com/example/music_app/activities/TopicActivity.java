package com.example.music_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.media3.common.MediaMetadata;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.adapters.SongAdapter;
import com.example.music_app.decorations.BottomOffsetDecoration;
import com.example.music_app.fragments.SongDetailFragment;
import com.example.music_app.helpers.DialogHelper;
import com.example.music_app.helpers.GradientHelper;
import com.example.music_app.helpers.SongToMediaItemHelper;
import com.example.music_app.listeners.DialogClickListener;
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
    private String topic;
    private List<Song> songList;
    private SongAdapter songAdapter;
    private int page, size, totalPages;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean isShuffle = false;
    private ExoPlayerQueue exoPlayerQueue;
    private SongDetailFragment songDetailFragment;
    private APIService apiService;
    ImageView coverPic;
    TextView tvPlaylistTitle;
    TextView tvPlaylistIntro;
    TextView tvPlaySongCount;
    MaterialButton deletePlaylistButton;
    RecyclerView rvListSong;
    View includeTopPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        initMiniPlayer();
        exoPlayerQueue = ExoPlayerQueue.getInstance();
        topic = getIntent().getStringExtra("topic");

        includeTopPlaylist = findViewById(R.id.included_top_playlist);
        coverPic = includeTopPlaylist.findViewById(R.id.imCoverPicture);
        tvPlaylistTitle = includeTopPlaylist.findViewById(R.id.tvPlaylistTitle);
        tvPlaylistIntro = includeTopPlaylist.findViewById(R.id.tvPlaylistIntro);
        tvPlaySongCount = includeTopPlaylist.findViewById(R.id.tvPlaylistSongCount);
        tvPlaySongCount.setVisibility(View.GONE);

        View includeOption = findViewById(R.id.included_top_playlist_option);

        View includeListSong = findViewById(R.id.included_list_song);
        deletePlaylistButton = includeOption.findViewById(R.id.btn_delete_playlist);
        rvListSong = includeListSong.findViewById(R.id.rvListSong);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvListSong.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset));
        rvListSong.addItemDecoration(itemDecoration);
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
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        songList = new ArrayList<>();
        songAdapter = new SongAdapter(getApplicationContext(), songList, this);
        switch (topic) {
            case "trending":
                songList.clear();
                page = 0;
                size = 10;
                coverPic.setImageResource(R.drawable.trending_cover);
                GradientHelper.applyGradient(this, includeTopPlaylist, R.drawable.trending_cover);
                tvPlaylistTitle.setText(R.string.trending_title);
                tvPlaylistIntro.setText(R.string.trending_intro);

                fetchSongs(apiService.getMostViewSong(page, size));
                rvListSong.setAdapter(songAdapter);
                break;
            case "favorite":
                songList.clear();
                page = 0;
                size = 10;
                coverPic.setImageResource(R.drawable.favorite_cover);
                GradientHelper.applyGradient(this, includeTopPlaylist, R.drawable.favorite_cover);
                tvPlaylistTitle.setText(R.string.favorite_title);
                tvPlaylistIntro.setText(R.string.favorite_intro);

                fetchSongs(apiService.getMostLikeSong(page, size));
                rvListSong.setAdapter(songAdapter);
                break;
            case "topArtist":
                coverPic.setImageResource(R.drawable.top_artist_cover);
                GradientHelper.applyGradient(this, includeTopPlaylist, R.drawable.top_artist_cover);
                tvPlaylistTitle.setText(R.string.topartist_title);
                tvPlaylistIntro.setText(R.string.topartist_intro);
                includeOption.setVisibility(View.GONE);
                break;
            case "newReleased":
                songList.clear();
                page = 0;
                size = 10;
                coverPic.setImageResource(R.drawable.new_released);
                GradientHelper.applyGradient(this, includeTopPlaylist, R.drawable.new_released);
                tvPlaylistTitle.setText(R.string.newreleased_title);
                tvPlaylistIntro.setText(R.string.newreleased_intro);
                fetchSongs(apiService.getSongNewReleased(page, size));
                rvListSong.setAdapter(songAdapter);
                break;

            default:
                tvPlaySongCount.setVisibility(View.VISIBLE);
                getPlaylistById(Integer.parseInt(topic));
                rvListSong.setAdapter(songAdapter);
                deletePlaylistIfNeeded(Integer.parseInt(topic));
                break;
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

    }
    private void loadTopPlaylist(Playlist playlist) {
        tvPlaylistIntro.setVisibility(View.GONE);
        Glide.with(TopicActivity.this)
                .load(playlist.getImage())
                .into(coverPic);
        tvPlaylistTitle.setText(playlist.getName());
        tvPlaySongCount.setText(getString(R.string.label_songs, playlist.getSongCount()));

        GradientHelper.applyGradient(this, includeTopPlaylist, playlist.getImage());
    }

    private void loadPlaylistSongs(List<Song> songs) {
        songAdapter = new SongAdapter(this, songs, null);
        rvListSong.setAdapter(songAdapter);
        rvListSong.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }


    private void getPlaylistById(int idPlaylist) {
        apiService.getPlaylistById(Integer.parseInt(topic)).enqueue(new Callback<PlaylistResponse>() {
            @Override
            public void onResponse(Call<PlaylistResponse> call, Response<PlaylistResponse> response) {
                if (response.isSuccessful()) {
                    Playlist playlist = response.body().getData();
                    if (playlist != null) {
                        loadTopPlaylist(playlist);
                        loadPlaylistSongs(playlist.getSongs());
                    }
                }
            }

            @Override
            public void onFailure(Call<PlaylistResponse> call, Throwable t) {

            }
        });
    }
    private void deletePlaylistIfNeeded(int idPlaylist) {
        deletePlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper dialog = new DialogHelper(view.getContext(), new DialogClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        apiService.deletePlaylist(Integer.parseInt(topic)).enqueue(new Callback<ResponseMessage>() {
                            @Override
                            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                if (response.isSuccessful()) {
                                    Log.d("API Response", "Delete playlist successful");
                                    Intent intent = new Intent(TopicActivity.this, LibraryActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(TopicActivity.this, getText(R.string.toast_deleted_playlist), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                            }
                        });
                    }

                    @Override
                    public void onNegativeButtonClick() { }
                });
                dialog.show();
                dialog.setTitle(getString(R.string.dialog_title_confirm_delete));
                dialog.setMessage(getString(R.string.dialog_message_confirm_delete));
                dialog.setPositiveButtonContent(getString(R.string.button_delete));
                dialog.setNegativeButtonContent(getString(R.string.button_cancel));
                dialog.setPositiveButtonColor(R.color.error);

            }
        });
    }

}