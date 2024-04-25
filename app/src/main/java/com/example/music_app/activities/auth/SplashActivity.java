package com.example.music_app.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.music_app.R;

public class SplashActivity extends AppCompatActivity {

    private static final long DELAY_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, GetStartedActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME);
    }
}