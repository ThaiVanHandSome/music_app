package com.example.music_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.models.Song;

import java.util.List;

public class NewSongHomeAdapter extends RecyclerView.Adapter<NewSongHomeAdapter.MyViewHolder> {
    private final Context context;
    private final List<Song> songList;

    public NewSongHomeAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_song_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.tenBaiHat.setText(song.getName());
        holder.tenNgheSi.setText(song.getName());
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

        public TextView thoiGian;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imv_new_song_home);
            tenBaiHat = itemView.findViewById(R.id.tv_name_new_song_home);
            tenNgheSi = itemView.findViewById(R.id.tv_artist_new_song_home);
            thoiGian = itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Song song = songList.get(getAdapterPosition());
                    //Goi item

                }
            });


        }
    }
}
