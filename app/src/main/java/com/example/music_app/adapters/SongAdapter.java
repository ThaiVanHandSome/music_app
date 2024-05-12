package com.example.music_app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
<<<<<<< HEAD
import com.example.music_app.models.Artist;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.Song;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private Context context;
    private List<Song> songList;

    private OnItemClickListener onItemClickListener;

    public SongAdapter(Context context, List<Song> songList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.songList = songList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<Song> songList) {
        this.songList = songList;
        notifyDataSetChanged();
=======
import com.example.music_app.fragments.BottomSheetDialog;
import com.example.music_app.models.Song;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private final Context context;
    private final List<Song> songs;

    public SongAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
>>>>>>> b04fcfb10aa465867953c8b1b63fe7e24b27494d
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
<<<<<<< HEAD
        Song song = songList.get(position);
        if (song == null) return;
        holder.tvSongTitle.setText(song.getName());
        Glide.with(context).load(song.getImage())
                .transform(new RoundedCornersTransformation(10, 0))
                .into(holder.imSongAvt);
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getArtistsBySongId(song.getIdSong()).enqueue(new Callback<GenericResponse<List<Artist>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Artist>>> call, Response<GenericResponse<List<Artist>>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    Artist artist = response.body().getData().get(0);
                    holder.tvSongArtist.setText(artist.getNickname());
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Artist>>> call, Throwable t) {
                holder.tvSongArtist.setText("");
                Log.d("SongAdapter", "onFailure: " + t.getMessage());
=======
        Song song = songs.get(position);
        Glide.with(context)
                .load(song.getImage())
                .into(holder.songImage);
        holder.songTitle.setText(song.getName());
        holder.artistName.setText(song.getArtistName());
        Log.d("ArtistName", song.getArtistName());
        holder.songActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                bottomSheetDialog.show(((androidx.fragment.app.FragmentActivity) context)
                        .getSupportFragmentManager(), "ModalBottomSheet");
                bottomSheetDialog.setContent(song.getIdSong(), song.getImage(), song.getName(), song.getArtistName());

>>>>>>> b04fcfb10aa465867953c8b1b63fe7e24b27494d
            }
        });
    }

    @Override
    public int getItemCount() {
<<<<<<< HEAD
        return songList.isEmpty() ? 0 : songList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imSongAvt;
        TextView tvSongTitle, tvSongArtist;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imSongAvt = itemView.findViewById(R.id.imSongAvt);
            tvSongTitle = itemView.findViewById(R.id.tvSongTitle);
            tvSongArtist = itemView.findViewById(R.id.tvSongArtist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onSongClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onSongClick(int position);
        void onPlayPlaylistClick(List<Song> songList);
    }
=======
        return songs == null ? 0 : songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView songImage;
        TextView songTitle;
        TextView artistName;
        MaterialButton songActionButton;
        public SongViewHolder(android.view.View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.imv_song_image);
            songTitle = itemView.findViewById(R.id.tv_song_title);
            artistName = itemView.findViewById(R.id.tv_song_artist);
            songActionButton = itemView.findViewById(R.id.btn_song_action);
        }
    }
>>>>>>> b04fcfb10aa465867953c8b1b63fe7e24b27494d
}
