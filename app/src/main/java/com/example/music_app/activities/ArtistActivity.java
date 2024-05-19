package com.example.music_app.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.adapters.SongAdapter;
import com.example.music_app.decorations.BottomOffsetDecoration;
import com.example.music_app.helpers.ArtistHelper;
import com.example.music_app.helpers.GradientHelper;
import com.example.music_app.helpers.SongToMediaItemHelper;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.listeners.PaginationScrollListener;
import com.example.music_app.models.Artist;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongResponse;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.services.ExoPlayerQueue;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistActivity extends BaseActivity implements SongAdapter.OnItemClickListener{

    private int artistId;
    ImageView coverPic;
    EditText tvPlaylistTitle;
    TextView tvPlaylistIntro;
    TextView tvPlaySongCount;
    RecyclerView rvListSong;
    View includeTopPlaylist, includeTopPlaylistOption, includeListSong, container;
    MaterialButton btnOption, btnShuffle;
    APIService apiService;
    List<Song> songList;
    SongAdapter songAdapter;
    int page = 0, totalPages;
    boolean isLoading = false, isLastPage = false, isShuffle = false;
    private ExoPlayerQueue exoPlayerQueue;
    boolean isUserFollowedArtist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        initMiniPlayer();

        artistId = getIntent().getIntExtra("artistId", -1);
        if(artistId == -1){
            finish();
        }

        exoPlayerQueue = ExoPlayerQueue.getInstance();

        container = findViewById(R.id.layout_container);

        includeTopPlaylist = findViewById(R.id.included_top_playlist);
        coverPic = includeTopPlaylist.findViewById(R.id.imCoverPicture);
        edtPlaylistTitle = includeTopPlaylist.findViewById(R.id.edtPlaylistTitle);
        tvPlaylistIntro = includeTopPlaylist.findViewById(R.id.tvPlaylistIntro);
        tvPlaySongCount = includeTopPlaylist.findViewById(R.id.tvPlaylistSongCount);
        includeTopPlaylist.findViewById(R.id.btn_edit_name).setVisibility(View.GONE);
        tvPlaylistIntro.setVisibility(View.GONE);
        includeTopPlaylist.findViewById(R.id.btn_edit_name).setEnabled(false);
        tvPlaylistTitle.setEnabled(false);
        includeTopPlaylistOption = findViewById(R.id.included_top_playlist_option);
        btnOption = includeTopPlaylistOption.findViewById(R.id.btn_delete_playlist);

        User user = SharePrefManagerUser.getInstance(this).getUser();
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        apiService.isFollowedArtist(user.getId(), artistId).enqueue(new Callback<GenericResponse<Boolean>>() {

            @Override
            public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getData()) {
                        btnOption.setText(R.string.followed);
                        isUserFollowedArtist = true;
                    } else {
                        btnOption.setText(R.string.follow);
                        isUserFollowedArtist = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {

            }
        });

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.followArtist(user.getId(), artistId).enqueue(new Callback<GenericResponse<Boolean>>() {

                    @Override
                    public void onResponse(Call<GenericResponse<Boolean>> call, Response<GenericResponse<Boolean>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getData()) {
                                btnOption.setText(R.string.followed);
                                isUserFollowedArtist = true;
                            } else {
                                btnOption.setText(R.string.follow);
                                isUserFollowedArtist = false;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<Boolean>> call, Throwable t) {

                    }
                });
            }
        });

        btnShuffle = includeTopPlaylistOption.findViewById(R.id.btnTopPlaylistOptionShuffle);
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShuffle) {
                    isShuffle = false;
                    btnShuffle.setIconTint(getResources().getColorStateList(R.color.primary));
                } else {
                    isShuffle = true;
                    btnShuffle.setIconTint(getResources().getColorStateList(R.color.neutral3));
                }
            }
        });

        includeListSong = findViewById(R.id.included_list_song);
        rvListSong = includeListSong.findViewById(R.id.rvListSong);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvListSong.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset));
        rvListSong.addItemDecoration(itemDecoration);

        setArtistInfo();
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        songList = new ArrayList<>();
        songAdapter = new SongAdapter(getApplicationContext(), songList, this);
        fetchSongs(apiService.getAllSongsByArtistId(artistId, page, 5));
        rvListSong.setAdapter(songAdapter);

        rvListSong.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                loadNextPage();
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
    }

    private void setArtistInfo() {
        ArtistHelper.getArtistById(artistId, new ArtistHelper.ArtistCallback() {
            @Override
            public void onSuccess(Artist artist) {
                edtPlaylistTitle.setText(artist.getNickname());
                Glide.with(getApplicationContext()).load(artist.getAvatar()).into(coverPic);
                GradientHelper.applyGradient(getApplicationContext(), includeTopPlaylist, String.valueOf(artist.getAvatar()));
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });

        ArtistHelper.getSongCountByArtistId(artistId, new ArtistHelper.SongCountCallback() {
            @Override
            public void onSuccess(int songCount) {
                tvPlaySongCount.setText(getString(R.string.label_songs, songCount));
            }

            @Override
            public void onFailure(Throwable t) {

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

    private void loadNextPage() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page < totalPages) {
                    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
                    fetchSongs(apiService.getAllSongsByArtistId(artistId, page, 5));
                }
                isLoading = false;
                if (page == totalPages) {
                    isLastPage = true;
                }
            }
        }, 500);
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
}