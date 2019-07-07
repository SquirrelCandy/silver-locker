package com.squirrelcandy.silverlocker.acts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squirrelcandy.silverlocker.R;
import com.squirrelcandy.silverlocker.db.ItemDAO;
import com.squirrelcandy.silverlocker.models.Item;

public class AddItemActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private ItemDAO dao;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        dao = new ItemDAO(getApplicationContext());

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = new Item();
                item.setName(etName.getText().toString());
                item.setUsername(etUsername.getText().toString());
                item.setEmail(etEmail.getText().toString());
                item.setPassword(etPassword.getText().toString());

                int newID = (int) dao.saveItem(item);
                Log.d("Added item", "New UID="+ newID);
            }
        });

        Button btnDone = findViewById(R.id.btnDone);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(AddItemActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}
