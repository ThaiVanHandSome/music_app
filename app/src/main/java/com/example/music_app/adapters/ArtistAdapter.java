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

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder> {
    private final Context context;
    private final List<Song> songList;

    public ArtistAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Song song = songList.get(position);
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
        public CircleImageView image;
        public TextView tenNgheSi;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_artist_home);
            tenNgheSi = itemView.findViewById(R.id.tv_artist_home);
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
