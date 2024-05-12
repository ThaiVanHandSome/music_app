package com.example.music_app.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.music_app.R;
import com.example.music_app.fragments.SongDetailFragment;

public class SongDetailActivity extends AppCompatActivity {
    private SongDetailFragment songDetailFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (songDetailFragment != null) {
            songDetailFragment.getExoPlayer().stop();
            fragmentTransaction.hide(songDetailFragment);
        }

        songDetailFragment = SongDetailFragment.newInstance();
        if (songDetailFragment.isAdded()) {
            fragmentTransaction.replace(R.id.fragment_container, songDetailFragment).addToBackStack(null);
        } else {
            fragmentTransaction.add(R.id.fragment_container, songDetailFragment).addToBackStack(null);
        }
        fragmentTransaction.commit();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (songDetailFragment != null) {
                    finish();
                }
            }
        });
    }

}