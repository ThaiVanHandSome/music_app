package com.example.music_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.models.NotificationFirebase;
import com.example.music_app.models.UserFirebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class NotificationActivity extends AppCompatActivity {
    public static final String ID = "push_notification_id";
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
//        getToken();
        btn = (Button) findViewById(R.id.buttonFirebase);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

    }

    void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                Log.e("My Token",token);
            }
        });
    }

    void submit() {
        // Khởi tạo Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://music-app-967da-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference usersRef = database.getReference("user");

        // Thêm dữ liệu người dùng vào Firebase Realtime Database

        UserFirebase user2 = new UserFirebase(2);
//        usersRef.child("3").setValue(user2);

        DatabaseReference usersRef1 = database.getReference("user");
        NotificationFirebase noti = new NotificationFirebase(2,"Hello World","Thanh");
//        usersRef1.child("1").child("notification").child("4").setValue(noti);


        FirebaseDatabase database1 = FirebaseDatabase.getInstance("https://music-app-967da-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference usersRef2 = database.getReference("user").child("2").child("notification");

        usersRef2.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NotificationFirebase noti = dataSnapshot.getValue(NotificationFirebase.class);
                    Log.e("NotificationFirebase", noti.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NotificationActivity.this, "Call API fall", Toast.LENGTH_SHORT).show();
            }
        });
    }
}