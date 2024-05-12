package com.example.music_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app.models.Song;
import com.example.music_app.R;
import java.util.List;

public class SongHomeAdapter extends RecyclerView.Adapter<SongHomeAdapter.MyViewHolder> {
    private final Context context;
    private final List<Song> songList;

    public SongHomeAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_trend, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.tenBaiHat.setText(song.getName());
        holder.tenNgheSi.setText(song.getName());

        Glide.with(context)
                .load(song.getImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
       return   songList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public ImageView image;
        public TextView tenBaiHat;
        public TextView tenNgheSi;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imv_user_playlist);
            tenBaiHat = itemView.findViewById(R.id.tv_user_playlist);
            tenNgheSi = itemView.findViewById(R.id.tv_song_count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Song song = songList.get(getAdapterPosition());
                    Toast.makeText(context.getApplicationContext(), "Bạn đã click vào bài hát" + song.getName(), Toast.LENGTH_SHORT).show();

                }
            });


        }
    }
}
