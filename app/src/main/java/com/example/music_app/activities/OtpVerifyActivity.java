package com.example.music_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.models.RegisterResponse;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerifyActivity extends AppCompatActivity {

    private MaterialButton btnVerify;

    private EditText otp1Txt, otp2Txt, otp3Txt, otp4Txt, otp5Txt, otp6Txt;

    private ProgressBar progressBar;

    private TextView countdownTxt;

    private APIService apiService;

    private int countdownDurtion = 15 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        mapping();

        CountDownTimer countDownTimer = new CountDownTimer(countdownDurtion, 1000) {
            @Override
            public void onTick(long l) {
                long totalSecond = l / 1000;
                long minutes = totalSecond / 60;
                long seconds = totalSecond % 60;
                String formattedTime = String.format("%02d:%02d", minutes, seconds);
                countdownTxt.setText(formattedTime);
            }

            @Override
            public void onFinish() {
                Toast.makeText(OtpVerifyActivity.this, "Time out!", Toast.LENGTH_SHORT).show();
            }
        };

        countDownTimer.start();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                verify();
            }
        });
    }

    private void mapping() {
        btnVerify = (MaterialButton) findViewById(R.id.btnVerify);
        otp1Txt = (EditText) findViewById(R.id.opt1Txt);
        otp2Txt = (EditText) findViewById(R.id.opt2Txt);
        otp3Txt = (EditText) findViewById(R.id.opt3Txt);
        otp4Txt = (EditText) findViewById(R.id.opt4Txt);
        otp5Txt = (EditText) findViewById(R.id.opt5Txt);
        otp6Txt = (EditText) findViewById(R.id.opt6Txt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        countdownTxt = (TextView) findViewById(R.id.countdownTxt);
    }

    private void verify() {
        String token = otp1Txt.getText().toString() + otp2Txt.getText().toString() + otp3Txt.getText().toString() + otp4Txt.getText().toString() + otp5Txt.getText().toString() + otp6Txt.getText().toString();
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.verifyOtp(token).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(OtpVerifyActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                assert response.body() != null;
                if(response.body().isSuccess()) {
                    Intent intent = new Intent(OtpVerifyActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(OtpVerifyActivity.this, "Call API Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}