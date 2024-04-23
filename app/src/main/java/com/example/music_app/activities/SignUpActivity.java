package com.example.music_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.models.RegisterModel;
import com.example.music_app.models.RegisterResponse;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText firstNameTxt, lastNameTxt, emailTxt, phoneNumberTxt, passwordTxt;
    private RadioGroup radioGroup;

    private MaterialButton btnSignUp;

    private APIService apiService;

    private ProgressBar progressBar;

    private int gender = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mapping();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                signUp();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = findViewById(i);
                if(checkedRadioButton != null) {
                    String tag = checkedRadioButton.getTag().toString();
                    gender = Integer.parseInt(tag);
                }
            }
        });
    }

    private void mapping() {
        firstNameTxt = (TextInputEditText) findViewById(R.id.firstNameTxt);
        lastNameTxt = (TextInputEditText) findViewById(R.id.lastNameTxt);
        emailTxt = (TextInputEditText) findViewById(R.id.emailTxt);
        phoneNumberTxt = (TextInputEditText) findViewById(R.id.phoneNumberTxt);
        passwordTxt = (TextInputEditText) findViewById(R.id.passwordTxt);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnSignUp = (MaterialButton) findViewById(R.id.btnSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void signUp() {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setFirstName(firstNameTxt.getText().toString());
        registerModel.setLastName(lastNameTxt.getText().toString());
        registerModel.setEmail(emailTxt.getText().toString());
        registerModel.setPhoneNumber(phoneNumberTxt.getText().toString());
        registerModel.setPassword(passwordTxt.getText().toString());
        registerModel.setGender(gender);

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.register(registerModel).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, OtpVerifyActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Call API Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}