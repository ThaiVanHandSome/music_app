package com.example.music_app.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.MainActivity;
import com.example.music_app.R;
import com.example.music_app.internals.SharePrefManagerAccount;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.LoginRequest;
import com.example.music_app.models.LoginResponse;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.utils.Validate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView signUpText, forgotPasswordText;
    private TextInputEditText emailTxt, passwordTxt;
    private TextInputLayout emailLayout, passwordLayout;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;
    private CheckBox checkBoxRemember;
    private FrameLayout overlay;
    private Validate validate = new Validate();
    private APIService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mapping();

        fillText();

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginRequest req = new LoginRequest();
                req.setEmail(emailTxt.getText().toString());
                req.setPassword(passwordTxt.getText().toString());
                if(checkSuccess()) {
                    overlay.setBackgroundColor(Color.argb(89, 0, 0, 0));
                    overlay.setVisibility(View.VISIBLE);
                    overlay.setFocusable(true);
                    overlay.setClickable(true);
                    progressBar.setVisibility(View.VISIBLE);
                    if(checkBoxRemember.isChecked()) {
                        SharePrefManagerAccount.getInstance(getApplicationContext()).remember(req);
                    } else {
                        SharePrefManagerAccount.getInstance(getApplicationContext()).clear();
                    }
                    login(req);
                }
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        handleEvent();
    }

    private void fillText() {
        boolean isRemember = SharePrefManagerAccount.getInstance(getApplicationContext()).isRemember();
        if(isRemember) {
            LoginRequest req = SharePrefManagerAccount.getInstance(getApplicationContext()).getRemenember();
            emailTxt.setText(req.getEmail());
            passwordTxt.setText(req.getPassword());
            checkBoxRemember.setChecked(true);
        }
    }

    private void login(LoginRequest req) {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.authenticate(req).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                overlay.setVisibility(View.INVISIBLE);
                overlay.setFocusable(false);
                overlay.setClickable(false);
                LoginResponse res = response.body();
                if(res == null) {
                    Toast.makeText(LoginActivity.this, "Username Or Password Wrong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(res.isSuccess()) {
                    User user = new User();
                    user.setFirstName(res.getFirstName());
                    user.setLastName(res.getLastName());
                    user.setAvatar(res.getAvatar());
                    user.setEmail(res.getEmail());
                    user.setGender(res.getGender());
                    user.setId(res.getId());
                    user.setAccessToken(res.getAccessToken());
                    user.setRefreshToken(res.getRefreshToken());
                    SharePrefManagerUser.getInstance(getApplicationContext()).loginSuccess(user);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                overlay.setVisibility(View.INVISIBLE);
                overlay.setFocusable(false);
                overlay.setClickable(false);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkSuccess() {
        return emailLayout.getError() == null && passwordLayout.getError() == null;
    }

    private void handleEvent() {
        setEvent(emailLayout, emailTxt, "email");
        setEvent(passwordLayout, passwordTxt, "");
    }

    private void setEvent(TextInputLayout textInputLayout, TextInputEditText textInput, String type) {
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inp = textInput.getText().toString();
                if(inp.length() == 0) {
                    textInputLayout.setError("Vui lòng nhập trường này");
                } else {
                    if(type.equals("email") && !validate.validateEmail(inp)) {
                        textInputLayout.setError("Vui lòng nhập email chính xác!");
                    } else {
                        textInputLayout.setError(null);
                    }
                }
            }
        });

        textInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String inp = textInput.getText().toString();
                if(b) {
                    if(inp.length() != 0) {
                        if(type.equals("email") && !validate.validateEmail(inp)) {
                            textInputLayout.setError("Vui lòng nhập email chính xác!");
                        }
                        return;
                    }
                    textInputLayout.setError(null);
                } else {
                    if(inp.length() == 0) {
                        textInputLayout.setError("Vui lòng nhập trường này");
                    }
                }
            }
        });
    }

    private void mapping() {
        signUpText = (TextView) findViewById(R.id.signUpText);
        forgotPasswordText = (TextView) findViewById(R.id.forgotPasswordText);
        emailTxt = (TextInputEditText) findViewById(R.id.emailTxt);
        passwordTxt = (TextInputEditText) findViewById(R.id.passwordTxt);
        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        btnLogin = (MaterialButton) findViewById(R.id.btnLogin);
        checkBoxRemember = (CheckBox) findViewById(R.id.checkBoxRemember);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        overlay = (FrameLayout) findViewById(R.id.overlay);
    }
}