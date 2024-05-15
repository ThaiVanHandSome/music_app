package com.example.music_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_app.R;

public class SearchInitFragment extends Fragment {

    public SearchInitFragment() {
        // Required empty public constructor
    }

    public static SearchInitFragment newInstance() {
        SearchInitFragment fragment = new SearchInitFragment();
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
        return inflater.inflate(R.layout.fragment_search_init, container, false);
    }
}