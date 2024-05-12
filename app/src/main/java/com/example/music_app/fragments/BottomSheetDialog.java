package com.example.music_app.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.activities.AddSongToPlaylistActivity;
import com.example.music_app.databinding.BottomSheetBinding;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.SongLikedResponse;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    BottomSheetBinding binding;
    private Long songId;
    private String imageUrl;
    private String songName;
    private String artistName;
    private APIService apiService;
    private final User user = SharePrefManagerUser.getInstance(getContext()).getUser();
    /*private OnItemFavouriteChangeListener listener;*/

    public BottomSheetDialog() { }

    /*public void setOnItemFavouriteChangedListener(OnItemFavouriteChangeListener listener) {
        this.listener = listener;
    }*/

    public void setContent(Long songId, String imageUrl, String songName, String artistName) {
        this.songId = songId;
        this.imageUrl = imageUrl;
        this.songName = songName;
        this.artistName = artistName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        binding = BottomSheetBinding.bind(view);
        binding.itemSong.btnSongAction.setVisibility(View.GONE);

        if (songId != null && imageUrl != null && songName != null && artistName != null) {
            // Set content for item song
            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(binding.itemSong.imvSongImage);
            binding.itemSong.tvSongTitle.setText(songName);
            binding.itemSong.tvSongArtist.setText(artistName);
            apiService = RetrofitClient.getRetrofit().create(APIService.class);

            // Change menu item favourite for navigation view if song liked
            apiService.isUserLikedSong(songId, (long) user.getId()).enqueue(new Callback<SongLikedResponse>() {
                @Override
                public void onResponse(Call<SongLikedResponse> call, Response<SongLikedResponse> response) {
                    if (response.isSuccessful() && response.body().isData()) {
                        binding.navigationView.getMenu()
                                .findItem(R.id.menu_item_favourite)
                                .setTitle(getText(R.string.menu_item_remove_favourite))
                                .setIcon(R.drawable.ic_24dp_filled_favorite);
                    }
                }

                @Override
                public void onFailure(Call<SongLikedResponse> call, Throwable t) { }
            });

            // Handle navigation view item click
            binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_favourite:
                            apiService.toggleLike(songId, (long) user.getId()).enqueue(new Callback<SongLikedResponse>() {
                                @Override
                                public void onResponse(Call<SongLikedResponse> call, Response<SongLikedResponse> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().isData()) {
                                            Toast.makeText(getContext(), getText(R.string.toast_added_song_to_favourite), Toast.LENGTH_SHORT).show();
                                            binding.navigationView.getMenu()
                                                    .findItem(R.id.menu_item_favourite)
                                                    .setTitle(getText(R.string.menu_item_remove_favourite))
                                                    .setIcon(R.drawable.ic_24dp_filled_favorite);
                                        } else {
                                            Toast.makeText(getContext(), getText(R.string.toast_removed_song_to_favourite), Toast.LENGTH_SHORT).show();
                                            binding.navigationView.getMenu()
                                                    .findItem(R.id.menu_item_favourite)
                                                    .setTitle(getText(R.string.menu_item_add_favourite))
                                                    .setIcon(R.drawable.ic_24dp_outline_favorite);
                                        }
                                        BottomSheetDialog.this.dismiss();

                                    }
                                }

                                @Override
                                public void onFailure(Call<SongLikedResponse> call, Throwable t) { }
                            });

                            break;
                        case R.id.menu_item_add_playlist:
                            Intent intent = new Intent(getContext(), AddSongToPlaylistActivity.class);
                            intent.putExtra("SongId", songId);
                            startActivity(intent);
                            break;
                    }
                    return false;
                }

            });
        }

        return view;
    }

    /*@Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onItemFavouriteChanged();
        }
    }*/

    /*public interface OnItemFavouriteChangeListener {
        void onItemFavouriteChanged();
    }*/
}
