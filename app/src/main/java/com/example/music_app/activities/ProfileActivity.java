package com.example.music_app.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.utils.ObjectUtils;
import com.example.music_app.R;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.ResponseMessage;
import com.example.music_app.models.UpdateProfileRequest;
import com.example.music_app.models.User;
import com.example.music_app.models.UserResponse;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.utils.MultipartUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private User user;

    private int gender, userId;
    TextView title;
    TextInputEditText ho, ten, email;

    CircleImageView avatar;

    RadioGroup gioiTinh;

    RadioButton nam, nu;

    MaterialButton btnSubmit;
    FrameLayout overlay;
    ProgressBar progressBar;

    private Uri mUri;
    private APIService apiService;

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if(data == null) {
                    return;
                }
                Uri uri = data.getData();
                mUri = uri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    avatar.setImageBitmap(bitmap);
                    System.out.println(mUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mapping();
        user = SharePrefManagerUser.getInstance(getApplicationContext()).getUser();
        Glide.with(getApplicationContext())
                .load(user.getAvatar())
                .into(avatar);
        showInfo();
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        gioiTinh.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.RadioButtonNam) {
                    gender =0;
                } else if (checkedId == R.id.RadioButtonNu) {
                    gender =1;
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openOverlay();
                    updateInfo();
                } catch (URISyntaxException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    void mapping(){
        title = (TextView) findViewById(R.id.title_appbar_with_back_icon);
        ho = (TextInputEditText) findViewById(R.id.tv_Ho_profile);
        ten = (TextInputEditText) findViewById(R.id.tv_Ten_profile);
        gioiTinh = (RadioGroup) findViewById(R.id.radioGroupGioiTinh);
        nam = (RadioButton) findViewById(R.id.RadioButtonNam);
        nu = (RadioButton) findViewById(R.id.RadioButtonNu);
        email = (TextInputEditText) findViewById(R.id.tv_gmail_profile);
        avatar = (CircleImageView) findViewById(R.id.avatar_profile);
        btnSubmit = (MaterialButton) findViewById(R.id.btnSubmit_profile);
        overlay = (FrameLayout) findViewById(R.id.overlay);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
    void showInfo(){
        title.setText("Trang cá nhân");
        ho.setText(user.getLastName());
        ten.setText(user.getFirstName());
        email.setText(user.getEmail());
        userId = user.getId();
        if(user.getGender() == 0){
            nam.setChecked(true);
        }
        if(user.getGender() == 1){
            nu.setChecked(true);
        }
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture!"));
    }

    private void updateInfo() throws IOException, URISyntaxException {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        UpdateProfileRequest newInfoUser = new UpdateProfileRequest();

        newInfoUser.setLastName(Objects.requireNonNull(ho.getText()).toString());
        newInfoUser.setFirstName(Objects.requireNonNull(ten.getText()).toString());
        newInfoUser.setGender(gender);
        String id = String.valueOf(userId);
        try {
            uploadAvatar();
        } catch (Exception ignored) {
            Log.e("FailToUpload", "Code chay vao day");
        }
        apiService.updateProfile(id, newInfoUser).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    hideOverlay();
                    Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                hideOverlay();
                Log.e("ThatBai", "Code chay vao day1");
            }
        });

    }

    private void uploadAvatar() throws IOException, URISyntaxException {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        MultipartBody.Part imagePart = MultipartUtil.createMultipartFromUri(this, mUri, "imageFile", "image_file.png");
        String id = String.valueOf(userId);
        Log.d("success_parse", imagePart.toString());
        apiService.uploadAvatar(imagePart, id).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                Log.d("success_parse", imagePart.toString());
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {

                Log.d("error", t.toString());
            }
        });
    }
    private void hideOverlay() {
        overlay.setVisibility(View.INVISIBLE);
        overlay.setFocusable(false);
        overlay.setClickable(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void openOverlay() {
        overlay.setBackgroundColor(Color.argb(89, 0, 0, 0));
        overlay.setVisibility(View.VISIBLE);
        overlay.setFocusable(true);
        overlay.setClickable(true);
        progressBar.setVisibility(View.VISIBLE);
    }
}