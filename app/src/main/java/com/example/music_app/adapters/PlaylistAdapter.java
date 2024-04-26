package com.example.music_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.models.Playlist;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private final Context context;
    private final List<Playlist> playlists;

    public PlaylistAdapter(Context context, List<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, null);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        Glide.with(context)
                .load(playlist.getImage())
                .into(holder.playlistImage);
        holder.playlistName.setText(playlist.getName());
        String playlistSongCount = context.getString(R.string.label_songs, playlist.getPlaylistSongs().size());
        holder.playlistSongCount.setText(playlistSongCount);

    }

    @Override
    public int getItemCount() {
        return playlists == null ? 0 : playlists.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder{
        ImageView playlistImage;
        TextView playlistName;
        TextView playlistSongCount;
        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistImage = itemView.findViewById(R.id.imv_user_playlist);
            playlistName = itemView.findViewById(R.id.tv_user_playlist);
            playlistSongCount = itemView.findViewById(R.id.tv_song_count);
        }
    }
}
