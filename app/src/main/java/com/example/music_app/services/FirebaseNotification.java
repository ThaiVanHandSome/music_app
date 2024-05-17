package com.example.music_app.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.music_app.R;
import com.example.music_app.activities.MainActivity;
import com.example.music_app.activities.NotificationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FirebaseNotification extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "MY_CHANNEL";

    public static void sendNotificationToUser(String messageBody, String receiverToken) {
        new Thread(() -> {
            try {
                URL url = new URL("https://fcm.googleapis.com/v1/projects/music-app-967da/messages:send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + "ya29.c.c0AY_VpZhLd2dn8r_Cs8yZPrtRi6IpfdpI84JL-iFZcUBeYDq4gJhwxO88aRWW5MramCbDAlNOFBJgH_MK4OBLln1SYocW4d3kG4rjgKLHaaNV62S_aUAhfa9MZ7lbNH8_MmuzIN9HvDnwjC_fC2fVbj4y8QfQcpwSkAx3Pf97K3naUxs6riMMIePzdCDd2PJCzIcEJ1oVD-5M443mkePl6UbFW_Lp9rfzzZ9miGO1Fn_dxGfc7VFXorIG0nS1_iU5jWquyDv1YsKmykxnMw1_HlYDFAlthWuuHIUGiBCVNlnf2LHeinP0KJs2e7U4U2v7lhatt0Jsc3cdqORhUZEVaRvqZkUcPUikcRvSWzybOkXe30Do3gNOqxBuL385D7d1xodyc4FeV6jQus6Qcy0OY9MgiyiZOf6ug4w4sQsirnWFk_5595OphhmhnhlBydp0cIe6z1vOIbFOZgO_6Y6-O8WOY_l2FF65_4ZJiiSjtb3l-iSJ3enj2zW_aWx9VW3_SIwlaRlss74gQS2oSiq9l1kZw-sUiB43FYhwkcavasIuwXe9vlR2bj1sQlzctpBvt1QiVt1lgxzR-ol4u2Mgy3zg1Ye4pF4sh1uWSsn5J6byeosgU2u631rUFwVtFgsSayxZ6Uaxw2i2rX1S9B1iiahw1rBtx0yBYMWOhxbpjiss3pJqn_7n_-0Urs92nn0e_0BcQfVIbtJma3Fkt17u058_y4miYl38So_6d_JR4RQ__Ugle4BdWW0uedF0mVdYzlcym0WwlfpxkInQjVma-1_u35wmctf22zhs1OJnShRkSuvvBhVIXS-outVVWyQ7mcyqg76bffSUpi0_mdRkciavsaI5hJ6_6jXShIBi7waFgvWMIunSU__hxjQVQf1X9MpZeRZfIOMffuRzkpR6we4Ms34_vZX7R90qYkys57Q_X7bzB71jJpYvgZoOgy25O1OkmIxrU7k0yyldjJaoueOqfxhmJg4BtxughB1ba3SdqVIqiwOUkFa");
                conn.setRequestProperty("Content-Type", "application/json; UTF-8");

                String payload = "{\n" +
                        "  \"message\": {\n" +
                        "    \"token\": \"" + receiverToken + "\",\n" +
                        "    \"notification\": {\n" +
                        "      \"body\": \"" + messageBody + "\",\n" +
                        "      \"title\": \"Bài hát mới\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";

                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = payload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        Log.d("FCM123", "Response: " + response.toString());
                    }
                } else {
                    Log.e("FCM123", "Failed to send notification, response code: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages here.
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Music App Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

}
