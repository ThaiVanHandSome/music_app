package com.example.music_app.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.music_app.R;
import com.example.music_app.activities.auth.LoginActivity;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.internals.SharedPrefManagerLanguage;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    TextView changePassword, logout;
    RadioButton english, vietnamese;
    SharedPrefManagerLanguage sharedPrefManagerLanguage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mapping();
        sharedPrefManagerLanguage = new SharedPrefManagerLanguage(this);
        String savedLanguage = sharedPrefManagerLanguage.getLanguage();
        setLocale(savedLanguage);

        if (savedLanguage.equals("en")) {
            english.setChecked(true);
        } else {
            vietnamese.setChecked(true);
        }
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                SharePrefManagerUser.getInstance(getApplicationContext()).logout();
                finish();
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("en");
                sharedPrefManagerLanguage.saveLanguage("en");
                restartApplication();
            }
        });

        vietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("vi");
                sharedPrefManagerLanguage.saveLanguage("vi");
                restartApplication();
            }
        });
    }

    private void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    void mapping() {
        changePassword = (TextView) findViewById(R.id.ChangePasswordTxt);
        logout = (TextView) findViewById(R.id.LogoutTxt);
        english = (RadioButton) findViewById(R.id.rad_english);
        vietnamese = (RadioButton) findViewById(R.id.rad_vietnamese);
    }
}
