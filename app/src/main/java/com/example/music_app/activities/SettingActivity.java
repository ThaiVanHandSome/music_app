package com.example.music_app.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.music_app.R;
import com.example.music_app.activities.auth.LoginActivity;
import com.example.music_app.helpers.DialogHelper;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.internals.SharedPrefManagerLanguage;
import com.example.music_app.listeners.DialogClickListener;
import com.example.music_app.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingActivity extends AppCompatActivity {
    TextView changePassword, logout;
    RadioButton english, vietnamese;
    SharedPrefManagerLanguage sharedPrefManagerLanguage;

    User user;

    ImageView imageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = SharePrefManagerUser.getInstance(this).getUser();
        String language = SharedPrefManagerLanguage.getInstance(getApplicationContext()).getLanguage();
        setContentView(R.layout.activity_setting);
        mapping();

        if (language.equals("vi")) {
            vietnamese.setChecked(true);
            english.setChecked(false);
        } else {
            vietnamese.setChecked(false);
            english.setChecked(true);
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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                removeToken(user.getId());
                SharePrefManagerUser.getInstance(getApplicationContext()).logout();
                finish();
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper dialog = new DialogHelper(view.getContext(), new DialogClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        restartApplication();
                        SharedPrefManagerLanguage.getInstance(getApplicationContext()).saveLanguage("en");
                    }

                    @Override
                    public void onNegativeButtonClick() {
                        english.setChecked(false);
                        vietnamese.setChecked(true);
                    }
                });
                dialog.show();
                dialog.setTitle(getString(R.string.dialog_title_confirm_restart));
                dialog.setMessage(getString(R.string.dialog_message_confirm_restart));
                dialog.setPositiveButtonContent(getString(R.string.button_ok));
                dialog.setNegativeButtonContent(getString(R.string.button_cancel));
            }
        });

        vietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper dialog = new DialogHelper(view.getContext(), new DialogClickListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        restartApplication();
                        SharedPrefManagerLanguage.getInstance(getApplicationContext()).saveLanguage("vi");
                    }

                    @Override
                    public void onNegativeButtonClick() {
                        vietnamese.setChecked(false);
                        english.setChecked(true);
                    }
                });
                dialog.show();
                dialog.setTitle(getString(R.string.dialog_title_confirm_restart));
                dialog.setMessage(getString(R.string.dialog_message_confirm_restart));
                dialog.setPositiveButtonContent(getString(R.string.button_ok));
                dialog.setNegativeButtonContent(getString(R.string.button_cancel));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backActivity();
            }
        });
    }

    private void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void mapping() {
        changePassword = (TextView) findViewById(R.id.ChangePasswordTxt);
        logout = (TextView) findViewById(R.id.LogoutTxt);
        english = (RadioButton) findViewById(R.id.rad_english);
        vietnamese = (RadioButton) findViewById(R.id.rad_vietnamese);
        imageView = (ImageView) findViewById(R.id.back_icon);
    }

    void removeToken(int id) {
        String userId = String.valueOf(id);
        Log.e("removeToken", userId);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://music-app-967da-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference tokenRef = database.getReference("tokenPhone");
        tokenRef.child((userId)).removeValue();
    }
    private void backActivity(){
        finish();
    }
}
