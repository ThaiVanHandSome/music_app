package com.example.music_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.fragments.SearchInitFragment;
import com.example.music_app.fragments.SearchedFragment;
import com.example.music_app.fragments.SearchingFragment;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (SearchView) findViewById(R.id.searchView);
        fragmentManager = getSupportFragmentManager();
        searchView.clearFocus();
        fragmentManager.beginTransaction()
                .replace(R.id.search_frame_layout, SearchInitFragment.newInstance())
                .commit();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("onCreateView", "Code chay vao day 3");
                fragmentManager.beginTransaction()
                        .replace(R.id.search_frame_layout, SearchedFragment.newInstance())
                        .commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("onCreateView", "Code chay vao day 4");
                fragmentManager.beginTransaction()
                        .replace(R.id.search_frame_layout, SearchingFragment.newInstance())
                        .commit();
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                fragmentManager.beginTransaction()
                        .replace(R.id.search_frame_layout, SearchInitFragment.newInstance())
                        .commit();
                return false; // Trả về true nếu bạn đã xử lý sự kiện và không muốn SearchView được đóng
            }
        });
    }
}