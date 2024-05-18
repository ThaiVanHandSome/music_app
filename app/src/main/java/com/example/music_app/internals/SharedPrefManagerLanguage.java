package com.example.music_app.internals;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManagerLanguage {
    private static final String PREF_NAME = "language";
    private static final String KEY_LANGUAGE = "key_language";

    private SharedPreferences instance;
    public SharedPrefManagerLanguage(Context context) {
        instance = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLanguage(String language) {
        SharedPreferences.Editor editor = instance.edit();
        editor.putString(KEY_LANGUAGE, language);
        editor.apply();
    }

    public String getLanguage() {
        return instance.getString(KEY_LANGUAGE, "en");
    }
}
