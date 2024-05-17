package com.example.music_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app.R;
import com.example.music_app.activities.auth.LoginActivity;
import com.example.music_app.adapters.SongHomeAdapter;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.Song;
import com.example.music_app.services.APIService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class SettingActivity extends AppCompatActivity {
    TextView changePassword, logout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mapping();
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                SharePrefManagerUser.getInstance(getApplicationContext()).logout();
                finish();
            }
        });
    }
    void mapping() {
        changePassword = (TextView) findViewById(R.id.ChangePasswordTxt);
        logout = (TextView) findViewById(R.id.LogoutTxt);
    }

    void removeToken(int id) {
        String userId = String.valueOf(id);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://music-app-967da-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference tokenRef = database.getReference("tokenPhone");
        tokenRef.child(String.valueOf(id)).removeValue();
    }
}
