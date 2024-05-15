package com.example.music_app.internals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.music_app.activities.auth.LoginActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SharePrefSearchHistory {
    private static final String SHARED_PREF_NAME = "history_search";

    private static final String KEY_SEARCH_HISTORY = "search_history";
    private static SharePrefSearchHistory mInstance;
    private static Context ctx;

    private SharePrefSearchHistory(Context context) {
        ctx = context;
    }
    public static synchronized SharePrefSearchHistory getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new SharePrefSearchHistory(context);
        }
        return mInstance;
    }

    public static void saveSearchHistory(Context context, String searchTerm) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Set<String> history = preferences.getStringSet(KEY_SEARCH_HISTORY, new HashSet<String>());
        history.add(searchTerm);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(KEY_SEARCH_HISTORY, history);
        editor.apply();
    }

    public static ArrayList<String> getSearchHistory(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Set<String> history = preferences.getStringSet(KEY_SEARCH_HISTORY, new HashSet<String>());
        return new ArrayList<>(history);
    }


    public static void clearSearchHistory(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_SEARCH_HISTORY);
        editor.apply();
    }

}
