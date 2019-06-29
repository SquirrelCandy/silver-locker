package com.squirrelcandy.silverlocker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etPassword;
    private Button btnLogin;
    private TextView tvAttempts;
    private String adminPassword = "1234";
    private int attempts = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPassword = (EditText)findViewById(R.id.etPassword);
        tvAttempts = (TextView)findViewById(R.id.tvAttempts);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString();

                if (attempts >= 1 && adminPassword.equals(password)) {
                    Intent tnt = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(tnt);
                }else if (attempts > 1) {
                    attempts--;
                    tvAttempts.setText("Attempts Remaining: " + Integer.toString(attempts));
                }else if (attempts == 1) {
                    btnLogin.setEnabled(false);
                    tvAttempts.setText("No more attempts! Try again later.");
                }
            }
        });
    }
}
