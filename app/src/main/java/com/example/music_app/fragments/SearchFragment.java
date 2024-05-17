package com.example.music_app.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_app.R;

public class SearchFragment extends Fragment {

    SearchView searchView;
    FragmentManager fragmentManager;
    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        // Ánh xạ các view
        initView(view);
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
        return view;
    }
    private void initView(View view) {
        searchView = (SearchView) view.findViewById(R.id.searchView);
        fragmentManager = getChildFragmentManager();

    }
}