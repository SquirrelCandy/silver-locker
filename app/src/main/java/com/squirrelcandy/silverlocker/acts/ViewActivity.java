package com.squirrelcandy.silverlocker.acts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.squirrelcandy.silverlocker.R;

public class ViewActivity extends AppCompatActivity {

    TextView tvID;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        tvID = findViewById(R.id.tvId);
        tvName = findViewById(R.id.tvName);

        int id = -1;
        String name = "NAME";

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("ITEM_ID");
            name = bundle.getString("ITEM_NAME");
        }

        tvID.setText(String.valueOf(id));
        tvName.setText(name);
    }
}
