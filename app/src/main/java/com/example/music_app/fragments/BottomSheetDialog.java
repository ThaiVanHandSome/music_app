package com.example.music_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.databinding.BottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    BottomSheetBinding binding;
    private String imageUrl;
    private String songName;
    private String artistName;

    public void setContent(String imageUrl, String songName, String artistName) {
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

        if (imageUrl != null && songName != null && artistName != null) {
            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(binding.itemSong.imvSongImage);
            binding.itemSong.tvSongTitle.setText(songName);
            binding.itemSong.tvSongArtist.setText(artistName);
        }

        return view;
    }
}
