package com.example.music_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.adapters.NotificationAdapter;
import com.example.music_app.adapters.SongHomeAdapter;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.NotificationFirebase;
import com.example.music_app.models.Song;
import com.example.music_app.models.UserFirebase;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

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
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView = (RecyclerView) findViewById(R.id.rv_notification);
        title = (TextView) findViewById(R.id.title_appbar_home);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        title.setText("Thông báo");

//        getToken();
        getNotification();
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
        usersRef1.child("6").child("notification").child("1").setValue(noti);


        FirebaseDatabase database1 = FirebaseDatabase.getInstance("https://music-app-967da-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference usersRef2 = database.getReference("user").child("3").child("notification");

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

    private void getNotification(){
        Log.e("DataRes", "Code chay vao ham get Thinh Hanh");
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<GenericResponse<List<Song>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Song>>> call, Response<GenericResponse<List<Song>>> response) {
                if (response.isSuccessful()) {
                    listSong = response.body().getData();
                    adapter = new NotificationAdapter(getApplicationContext(), listSong);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false );
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("DataRes", "No Res");

                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Song>>> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }

        });
    }
}