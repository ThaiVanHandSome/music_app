package com.example.music_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.User;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        User user = SharePrefManagerUser.getInstance(this).getUser();
        textView.setText(user.getAccessToken());
    }
}