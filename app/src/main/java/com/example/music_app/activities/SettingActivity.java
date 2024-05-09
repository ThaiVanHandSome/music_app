package com.example.music_app.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app.R;
import com.example.music_app.adapters.SongHomeAdapter;
import com.example.music_app.models.Song;
import com.example.music_app.services.APIService;

import java.util.List;

public class SettingActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
//        AnhXa();
        Log.e("DataRes", "Code chay vao day");
    }
}
