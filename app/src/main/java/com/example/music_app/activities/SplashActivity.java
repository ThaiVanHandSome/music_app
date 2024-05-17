package com.example.music_app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.music_app.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        if(getIntent().getExtras()!=null){
//            //from notification
//            String songId = getIntent().getExtras().getString("songId");
//            // Xử lý getSong
//            .get().addOnCompleteListener(task -> {
//                        if(task.isSuccessful()){
//                            Intent intent = new Intent(this, DetailSongActivity.class);
//                            intent.putExtra("songModel", songModel);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//
//
//        }else{
//            // Nothing
//        }
    }

}