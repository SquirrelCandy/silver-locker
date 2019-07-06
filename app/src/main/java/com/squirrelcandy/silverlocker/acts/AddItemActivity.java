package com.squirrelcandy.silverlocker.acts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squirrelcandy.silverlocker.R;

public class AddItemActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(AddItemActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}
