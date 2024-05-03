package com.example.music_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.models.Playlist;
import com.example.music_app.models.Song;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

public class PlaylistAddToLibraryAdapter extends RecyclerView.Adapter<PlaylistAddToLibraryAdapter.SongAddToLibraryViewHolder> {
    private final Context context;
    private final List<Playlist> playlists;

    public PlaylistAddToLibraryAdapter(Context context, List<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public SongAddToLibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_song_library, parent, false);
        return new SongAddToLibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAddToLibraryViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        Glide.with(context)
                .load(playlist.getImage())
                .into(holder.playlistImage);
        holder.playlistTitle.setText(playlist.getName());
        holder.playlistSongCount.setText(playlist.getSongCount());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //TODO: add chosen song to playlist

            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists == null ? 0 : playlists.size();
    }

    public static class SongAddToLibraryViewHolder extends RecyclerView.ViewHolder {
        ImageView playlistImage;
        TextView playlistTitle;
        TextView playlistSongCount;
        MaterialCheckBox checkBox;
        public SongAddToLibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistImage = itemView.findViewById(R.id.imv_add_playlist_image);
            playlistTitle = itemView.findViewById(R.id.tv_add_playlist_title);
            playlistSongCount = itemView.findViewById(R.id.tv_add_playlist_song_count);
            checkBox = itemView.findViewById(R.id.cb_add_playlist);
        }
    }

}
