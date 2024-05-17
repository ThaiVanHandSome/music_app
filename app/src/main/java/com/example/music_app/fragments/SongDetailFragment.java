package com.example.music_app.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlayer;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.activities.BaseActivity;
import com.example.music_app.helpers.GradientHelper;
import com.example.music_app.services.ExoBuilderService;
import com.example.music_app.services.ExoPlayerQueue;
import com.example.music_app.services.ExoService;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Formatter;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class SongDetailFragment extends BottomSheetDialogFragment {
    private Handler handler;
    private ExoPlayer exoPlayer;
    SeekBar seekBar;
    TextView tvSongTitle, tvArtistName, tvCurrentTime, tvDuration;
    MaterialButton btnRepeat, btnShuffle, btnPrevious, btnPlay, btnNext;
    CircleImageView imSongAvt;
    View view;

    private ExoService exoService;
    private ExoBuilderService exoBuilderService;
    private ExoPlayerQueue exoPlayerQueue;
    private RotateAnimation rotateAnimation;

    public SongDetailFragment() {

    }

    public static SongDetailFragment newInstance() {
        SongDetailFragment fragment = new SongDetailFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        exoPlayerQueue = ExoPlayerQueue.getInstance();

        if (exoPlayerQueue.isShuffle()) {
            btnShuffle.setIconTint(getResources().getColorStateList(R.color.primary));
        } else {
            btnShuffle.setIconTint(getResources().getColorStateList(R.color.neutral3));
        }
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayerQueue.setShuffle(!exoPlayerQueue.isShuffle());
                if (exoPlayerQueue.isShuffle()) {
                    btnShuffle.setIconTint(getResources().getColorStateList(R.color.primary));
                } else {
                    btnShuffle.setIconTint(getResources().getColorStateList(R.color.neutral3));
                }
            }
        });


        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exoPlayer != null) {
                    exoPlayer.seekToPreviousMediaItem();
                }
            }
        });


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exoPlayer != null) {
                    if (exoPlayer.isPlaying()) {
                        exoPlayer.pause();
                        btnPlay.setIcon(getResources().getDrawable(R.drawable.ic_32dp_filled_play));
                        imSongAvt.clearAnimation();
                    } else {
                        exoPlayer.play();
                        btnPlay.setIcon(getResources().getDrawable(R.drawable.ic_32dp_filled_pause));
                        imSongAvt.startAnimation(rotateAnimation);
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exoPlayer != null) {
                    exoPlayer.seekToNextMediaItem();
                }
            }
        });


        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int repeatMode = exoPlayer.getRepeatMode();
                if (repeatMode == Player.REPEAT_MODE_OFF) {
                    btnRepeat.setIcon(getResources().getDrawable(R.drawable.ic_24dp_outline_repeat_on));
                    btnRepeat.setIconTint(getResources().getColorStateList(R.color.neutral3));
                } else if (repeatMode == Player.REPEAT_MODE_ALL) {
                    btnRepeat.setIcon(getResources().getDrawable(R.drawable.ic_24dp_outline_repeat_on));
                    btnRepeat.setIconTint(getResources().getColorStateList(R.color.primary));
                } else {
                    btnRepeat.setIcon(getResources().getDrawable(R.drawable.ic_24dp_filled_repeat_one));
                    btnRepeat.setIconTint(getResources().getColorStateList(R.color.neutral3));
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @OptIn(markerClass = UnstableApi.class) @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    exoPlayer.seekTo(progress);
                    tvCurrentTime.setText(Util.getStringForTime(new StringBuilder(), new Formatter(), progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (exoPlayer != null) {
                    exoPlayer.setPlayWhenReady(false);
                }
                btnPlay.setIcon(getResources().getDrawable(R.drawable.ic_32dp_filled_play));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (exoPlayer != null) {
                    exoPlayer.setPlayWhenReady(true);
                }
                btnPlay.setIcon(getResources().getDrawable(R.drawable.ic_32dp_filled_pause));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song_detail, container, false);
        tvSongTitle = view.findViewById(R.id.tvSongTitle);
        tvArtistName = view.findViewById(R.id.tvArtistName);
        tvCurrentTime = view.findViewById(R.id.tvSongCurrentTime);
        tvDuration = view.findViewById(R.id.tvSongDuration);
        seekBar = view.findViewById(R.id.sbSongProgress);
        imSongAvt = view.findViewById(R.id.imSongAvt);
        btnRepeat = view.findViewById(R.id.btnRepeat);
        btnShuffle = view.findViewById(R.id.btnShuffle);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnNext = view.findViewById(R.id.btnNext);

        handler = new Handler();
        if (getActivity() instanceof BaseActivity) {
            exoPlayer = ((BaseActivity) getActivity()).getExoPlayer();
            playPlaylist();
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (exoPlayer != null) {
//            if (exoPlayer.getCurrentMediaItem() != null) {
//                MediaItem currentSong = exoPlayer.getCurrentMediaItem();
//                MediaMetadata metadata = currentSong.mediaMetadata;
//                updateSongAsset(metadata);
//                handler.post(updateMediaRunable);
//            }
//        }
    }

    @OptIn(markerClass = UnstableApi.class)
    private void playPlaylist() {
        if (exoPlayerQueue == null) {
            exoPlayerQueue = ExoPlayerQueue.getInstance();
        }
        long currentSongId = -1;
        if (exoPlayer.getCurrentMediaItem() != null && exoPlayer.getCurrentMediaItem().mediaMetadata.extras != null) {
            currentSongId = exoPlayer.getCurrentMediaItem().mediaMetadata.extras.getLong("id");
        }
        long newSongId = exoPlayerQueue.getCurrentMediaItem().mediaMetadata.extras.getLong("id");
        if (currentSongId != newSongId) {
            exoPlayer.clearMediaItems();
            if (exoPlayerQueue.isShuffle()) exoPlayerQueue.shuffle();
            exoPlayer.setMediaItems(exoPlayerQueue.getCurrentQueue());
            exoPlayer.prepare();
            exoPlayer.seekTo(exoPlayerQueue.getCurrentPosition(), 0);
            exoPlayer.play();
        } else {
            if (!exoPlayer.isPlaying()) exoPlayer.play();
        }
        updateSongAsset(exoPlayerQueue.getCurrentMediaItem().mediaMetadata);

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    long duration = exoPlayer.getDuration();
                    seekBar.setMax((int) duration);
                    tvDuration.setText(Util.getStringForTime(new StringBuilder(), new Formatter(), duration));
                    handler.post(updateMediaRunable);
                } else {
                    handler.removeCallbacks(updateMediaRunable);
                }
            }
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                if (mediaItem != null) {
                    MediaMetadata metadata = mediaItem.mediaMetadata;
                    updateSongAsset(metadata);
                }
            }
        });
        btnPlay.setIcon(getResources().getDrawable(R.drawable.ic_32dp_filled_pause));
    }

    private Runnable updateMediaRunable = new Runnable() {
        @OptIn(markerClass = UnstableApi.class) @Override
        public void run() {
            if (exoPlayer != null && exoPlayer.isPlaying()) {
                long current = exoPlayer.getCurrentPosition();
                seekBar.setProgress((int) current);
                tvCurrentTime.setText(Util.getStringForTime(new StringBuilder(), new Formatter(), current));
                handler.postDelayed(this, 1000);
            }
        }
    };


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    // Thêm BottomSheetCallback để lắng nghe sự kiện kéo
                    behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            if (newState == BottomSheetBehavior.STATE_DRAGGING || newState == BottomSheetBehavior.STATE_SETTLING) {

                            } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                                dismiss();
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                        }
                    });
                    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                    if (layoutParams != null) {
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        bottomSheet.setLayoutParams(layoutParams);
                    }
                }
            }
        });
        return dialog;
    }



    @OptIn(markerClass = UnstableApi.class) private void updateSongAsset(MediaMetadata metadata) {
        if (isAdded()) {

            tvSongTitle.setText(metadata.title);
            tvArtistName.setText(metadata.extras.getString("artist"));
            tvDuration.setText(Util.getStringForTime(new StringBuilder(), new Formatter(), exoPlayer.getDuration()));
            tvCurrentTime.setText(Util.getStringForTime(new StringBuilder(), new Formatter(), exoPlayer.getCurrentPosition()));

            Glide.with(getContext())
                    .load(metadata.artworkUri)
                    .transform(new RoundedCornersTransformation(30, 0))
                    .into(imSongAvt);
            GradientHelper.applyGradient(getContext(), view, String.valueOf(metadata.artworkUri));

            rotateAnimation = new RotateAnimation(
                    0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            rotateAnimation.setDuration(20000);
            rotateAnimation.setRepeatCount(Animation.INFINITE);

            imSongAvt.startAnimation(rotateAnimation);
        }
    }

    public ExoPlayer getExoPlayer() {
        return exoPlayer;
    }
}