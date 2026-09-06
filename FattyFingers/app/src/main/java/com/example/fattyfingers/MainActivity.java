package com.example.fattyfingers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout clWelcome;
    private TextView txtLogin, txtSign, txtForgot;
    private Button btnSubmit;
    private TextInputLayout inputPassword, inputConfirmPassword;
    private TextInputEditText etPassword, etEmail, etConfirmPassword;
    private boolean isSignUp = false;
    private DatabaseHelper myDb;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Eatmore_Launcher);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);


        clWelcome = findViewById(R.id.cl_welcome);
        txtLogin = findViewById(R.id.txtLog);
        txtSign = findViewById(R.id.txtSign);
        btnSubmit = findViewById(R.id.btn_submit);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        etPassword = findViewById(R.id.et_Password);
        etEmail = findViewById(R.id.et_email);
        etConfirmPassword = findViewById(R.id.et_confirm_password);


        switchToLoginView();

        clWelcome.setOnClickListener(v -> clWelcome.setVisibility(View.GONE));

        txtLogin.setOnClickListener(v -> {
            switchToLoginView();
        });

        txtSign.setOnClickListener(v -> {
            switchToSignUpView();
        });

        btnSubmit.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isSignUp) {
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                if (confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
                    Toast.makeText(MainActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isInserted = myDb.insertUser(email, password);
                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                    switchToLoginView();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to create account.", Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean isValidUser = myDb.checkUser(email, password);
                SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("USER_EMAIL", email);
                editor.apply();
                if (isValidUser) {
                    Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Home.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void switchToLoginView() {
        isSignUp = false;

        txtLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.text_seletcted));
        txtLogin.setTextColor(ContextCompat.getColor(this, R.color.white_card));
        txtSign.setBackground(ContextCompat.getDrawable(this, R.drawable.text_unselected));
        txtSign.setTextColor(ContextCompat.getColor(this, R.color.blue));

        btnSubmit.setText("LOG IN");
        inputConfirmPassword.setVisibility(View.GONE);
    }

    private void switchToSignUpView() {
        isSignUp = true;

        txtSign.setBackground(ContextCompat.getDrawable(this, R.drawable.text_seletcted));
        txtSign.setTextColor(ContextCompat.getColor(this, R.color.white_card));
        txtLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.text_unselected));
        txtLogin.setTextColor(ContextCompat.getColor(this, R.color.blue));

        btnSubmit.setText("SIGN UP");
        inputConfirmPassword.setVisibility(View.VISIBLE);
    }
}