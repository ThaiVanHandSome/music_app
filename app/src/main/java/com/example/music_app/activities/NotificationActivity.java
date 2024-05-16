package com.example.music_app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.example.music_app.R;
import com.example.music_app.adapters.NotificationAdapter;
import com.example.music_app.adapters.SongHomeAdapter;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.NotificationFirebase;
import com.example.music_app.models.Song;
import com.example.music_app.models.User;
import com.example.music_app.models.UserFirebase;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.services.NotificationService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    public static final String ID = "push_notification_id";
    private Button btn;
    RecyclerView recyclerView;
    APIService apiService;

    List<Song> listSong;

    NotificationAdapter adapter;

    TextView title;

    User user;
    List<NotificationFirebase> notificationList = new ArrayList<>();
    private boolean isFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView = (RecyclerView) findViewById(R.id.rv_notification);
        title = (TextView) findViewById(R.id.title_appbar_home);
        title.setText("Thông báo");
        user = SharePrefManagerUser.getInstance(this).getUser();
        getNotification();
    }


//    void submit() {
//        // Khởi tạo Firebase
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://music-app-967da-default-rtdb.asia-southeast1.firebasedatabase.app/");
//        DatabaseReference usersRef = database.getReference("user");
//
//        // Thêm dữ liệu người dùng vào Firebase Realtime Database
//
//        UserFirebase user2 = new UserFirebase(2);
////        usersRef.child("3").setValue(user2);
//
//        DatabaseReference usersRef1 = database.getReference("user");
//        NotificationFirebase noti = new NotificationFirebase(2,"Hello World","Thanh");
//        usersRef1.child("6").child("notification").child("1").setValue(noti);
//
//
//        FirebaseDatabase database1 = FirebaseDatabase.getInstance("https://music-app-967da-default-rtdb.asia-southeast1.firebasedatabase.app/");
//        DatabaseReference usersRef2 = database.getReference("user").child("3").child("notification");
//
//        usersRef2.addValueEventListener(new ValueEventListener(){
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    NotificationFirebase noti = dataSnapshot.getValue(NotificationFirebase.class);
//                    Log.e("NotificationFirebase", noti.toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(NotificationActivity.this, "Call API fall", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void getNotification() {
        Log.e("DataRes", "Code chạy vào hàm get Thông Báo");
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://music-app-967da-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference notiRef = database.getReference("user").child(String.valueOf(user.getId())).child("notification");

        // Khởi tạo adapter với một danh sách trống
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(getApplicationContext(), notificationList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        // Lấy dữ liệu ban đầu và tất cả dữ liệu mới
        notiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Set recycleView
                notificationList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NotificationFirebase notification = dataSnapshot.getValue(NotificationFirebase.class);
                    if (notification != null) {
                        notificationList.add(notification);
                    }
                }

                if (isFirstLoad && notificationList.size() > 0) {
                    isFirstLoad = false;
                } else if (!isFirstLoad) {
                        NotificationFirebase notification = notificationList.get(notificationList.size() - 1);
                        sendNotification(notification);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NotificationActivity.this, "Call API fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification(NotificationFirebase notificationFirebase) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationService.CHANNEL_ID)
                .setContentTitle("Bài hát mới")
                .setContentText(notificationFirebase.getUserName() + " vừa phát hành ca khúc " + notificationFirebase.getSongName())
                .setSmallIcon(R.mipmap.ic_launcher);

        Glide.with(this)
                .asBitmap()
                .load(notificationFirebase.getCover())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        builder.setLargeIcon(resource);
                        // Hiển thị thông báo sau khi tải ảnh thành công
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(1, builder.build());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Không cần xử lý gì thêm
                    }
                });
    }
}