package com.example.music_app.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.music_app.R;

public class ExoService extends Service {

    private ExoPlayer exoPlayer;
    private ExoPlayerQueue exoPlayerQueue;
//    private ExoPlayerQueue exoPlayerQueue;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "com.example.music_app.services.ExoService";
    private static final String CHANNEL_NAME = "ExoService";

    private final IBinder iBinder = new MusicBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        exoPlayerQueue = ExoPlayerQueue.getInstance();
        exoPlayer = new ExoPlayer.Builder(this).build();
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED && exoPlayer.getPlayWhenReady()) {
                    startForeground(NOTIFICATION_ID, createNotification());
                } else if (playbackState == Player.STATE_READY && exoPlayer.getPlayWhenReady()) {
                    startForeground(NOTIFICATION_ID, createNotification());
                } else if (playbackState == Player.STATE_IDLE) {
                    stopForeground(true);
                }
            }
        });
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public ExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class MusicBinder extends Binder {
        public ExoService getService() {
            return ExoService.this;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification() {
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }

        MediaItem currentMediaItem = exoPlayerQueue.getCurrentMediaItem();
        String title;
        String artist;

        if (currentMediaItem != null && currentMediaItem.mediaMetadata != null) {
            MediaMetadata metadata = currentMediaItem.mediaMetadata;
            title = metadata.title.toString();
            artist = metadata.extras.getString("artist").toString();
        } else {
            title = "Unknown";
            artist = "Unknown";
        }

        builder.setContentTitle(title)
                .setContentText(artist)
                .setSmallIcon(R.drawable.ic_32dp_outline_graphic_eq)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setShowWhen(false)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_32dp_filled_skip_previous, "Previous", getPendingIntent("PREVIOUS"))
                .addAction(R.drawable.ic_32dp_filled_pause, "Pause", getPendingIntent("PAUSE"))
                .addAction(R.drawable.ic_32dp_filled_skip_next, "Next", getPendingIntent("NEXT"))
                .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2));

        return builder.build();
    }

    private PendingIntent getPendingIntent(String action) {
        Intent intent = new Intent(this, ExoService.class);
        intent.setAction(action);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }
}
