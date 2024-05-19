package com.example.music_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.music_app.R;
import com.example.music_app.activities.HomeActivity;
import com.example.music_app.activities.HomeSongsActivity;
import com.example.music_app.activities.LibraryActivity;
import com.example.music_app.activities.SongDetailActivity;
import com.example.music_app.activities.TopicActivity;
import com.example.music_app.adapters.ArtistAdapter;
import com.example.music_app.adapters.NewSongHomeAdapter;
import com.example.music_app.adapters.SongHomeAdapter;
import com.example.music_app.helpers.SongToMediaItemHelper;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.Song;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.services.ExoPlayerQueue;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView rvTopTrend, rvFavoriteSong, rvTopArtist, rvNewSong;
    SongHomeAdapter songHomeAdapter;
    ArtistAdapter artistAdapter;
    NewSongHomeAdapter newSongHomeAdapter;

    APIService apiService;
    List<Song> trendSongs, favoriteSongs, NewSongs;
    List<User> Artists;
    TextView title, xtt_topthinhhanh, xtt_moinguoiyeuthich, xtt_nghesihangdau, xtt_moiramat;
    private ExoPlayerQueue exoPlayerQueue = ExoPlayerQueue.getInstance();
    private final SongHomeAdapter.OnItemClickListener songTrendItemClick = new SongHomeAdapter.OnItemClickListener() {
        @Override
        public void onSongClick(int position) {
            exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(trendSongs));
            exoPlayerQueue.setCurrentPosition(position);
            Log.d("HomeActivity", "onSongClick: position " + exoPlayerQueue.getCurrentPosition() + "Recycler view tag: trend");
            Intent intent = new Intent(getContext(), SongDetailActivity.class);
            startActivity(intent);
        }

        @Override
        public void onPlayPlaylistClick(List<Song> songList) {

        }
    };

    private final SongHomeAdapter.OnItemClickListener songFavoriteItemClick = new SongHomeAdapter.OnItemClickListener() {
        @Override
        public void onSongClick(int position) {
            exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(favoriteSongs));
            exoPlayerQueue.setCurrentPosition(position);
            Log.d("HomeActivity", "onSongClick: position " + position + "Recycler view tag: favorite");
            Intent intent = new Intent(getContext(), SongDetailActivity.class);
            startActivity(intent);
        }

        @Override
        public void onPlayPlaylistClick(List<Song> songList) {

        }
    };

    private final NewSongHomeAdapter.OnItemClickListener songNewItemClick = new NewSongHomeAdapter.OnItemClickListener() {
        @Override
        public void onSongClick(int position, String tag) {
            exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(NewSongs));
            exoPlayerQueue.setCurrentPosition(position);
            Log.d("HomeActivity", "onSongClick: position " + position + "Recycler view tag: newReleased");
            Intent intent = new Intent(getContext(), SongDetailActivity.class);
            startActivity(intent);
        }

        @Override
        public void onPlayPlaylistClick(List<Song> songList) {

        }
    };
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("DataRes", "Code chay vao ham onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Ánh xạ các view
        initView(view);
        Log.e("DataRes", "Code chay vao ham onCreateView");

        User user = SharePrefManagerUser.getInstance(requireContext()).getUser();
        title.setText("Chào " + user.getFirstName() + " " + user.getLastName() + " 👋");

        GetTopTrend();
        GetFavoriteSong();
        GetTopArtist();
        GetNewSong();

        xtt_topthinhhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), TopicActivity.class);
                intent.putExtra("topic", "trending");
                startActivity(intent);
            }
        });
        xtt_moinguoiyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), HomeSongsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Keyy", 2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        xtt_nghesihangdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), HomeSongsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Keyy", 3);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        xtt_moiramat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), HomeSongsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Keyy", 4);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initView(View view) {
        rvTopTrend = view.findViewById(R.id.rv_toptrend);
        rvFavoriteSong = view.findViewById(R.id.rv_topfavorite);
        rvTopArtist = view.findViewById(R.id.rv_topartists);
        rvNewSong = view.findViewById(R.id.rv_newsongs);
        title = view.findViewById(R.id.title_appbar_home);
        xtt_topthinhhanh = view.findViewById(R.id.xemtatca_topthinhhanh);
        xtt_moinguoiyeuthich = view.findViewById(R.id.xemtatca_moinguoiyeuthich);
        xtt_nghesihangdau = view.findViewById(R.id.xemtatca_nghesihangdau);
        xtt_moiramat = view.findViewById(R.id.xemtatca_moiramat);
    }
    private void GetTopTrend(){
        Log.e("DataRes", "Code chay vao ham get Thinh Hanh");
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<GenericResponse<List<Song>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Song>>> call, Response<GenericResponse<List<Song>>> response) {
                if (response.isSuccessful() && isAdded()) {
                    Log.e("DataRes", "Code chay vao ham get Song Thinh Hanh Thanh Cong");
                    trendSongs = response.body().getData();
                    songHomeAdapter = new SongHomeAdapter(requireContext(), trendSongs, songTrendItemClick);
                    rvTopTrend.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false );
                    rvTopTrend.setLayoutManager(layoutManager);
                    rvTopTrend.setAdapter(songHomeAdapter);
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
    private void GetFavoriteSong(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<GenericResponse<List<Song>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Song>>> call, Response<GenericResponse<List<Song>>> response) {
                if (response.isSuccessful() && isAdded()) {
                    favoriteSongs = response.body().getData();
                    songHomeAdapter = new SongHomeAdapter(requireContext(), favoriteSongs, songFavoriteItemClick);
                    rvFavoriteSong.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false );
                    rvFavoriteSong.setLayoutManager(layoutManager);
                    rvFavoriteSong.setAdapter(songHomeAdapter);
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
    private void GetTopArtist(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<GenericResponse<List<Song>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Song>>> call, Response<GenericResponse<List<Song>>> response) {
                if (response.isSuccessful() && isAdded()) {
                    NewSongs = response.body().getData();
                    artistAdapter = new ArtistAdapter(requireContext(), NewSongs);
                    rvTopArtist.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false );
                    rvTopArtist.setLayoutManager(layoutManager);
                    rvTopArtist.setAdapter(artistAdapter);
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
    private void GetNewSong(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongs().enqueue(new Callback<GenericResponse<List<Song>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Song>>> call, Response<GenericResponse<List<Song>>> response) {
                if (response.isSuccessful() && isAdded()) {
                    NewSongs = response.body().getData();
                    newSongHomeAdapter = new NewSongHomeAdapter(requireContext(), NewSongs, songNewItemClick);
                    rvNewSong.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false );
                    rvNewSong.setLayoutManager(layoutManager);
                    rvNewSong.setAdapter(newSongHomeAdapter);
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