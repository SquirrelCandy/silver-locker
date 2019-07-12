package com.squirrelcandy.silverlocker.acts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.squirrelcandy.silverlocker.R;
import com.squirrelcandy.silverlocker.db.ItemDAO;
import com.squirrelcandy.silverlocker.models.Item;

public class ViewActivity extends AppCompatActivity {

    private TextView tvID;
    private TextView tvName;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        tvID = findViewById(R.id.tvId);
        tvName = findViewById(R.id.tvName);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);

        int pos = -1;
        String name = "NAME";

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pos = bundle.getInt("ITEM_POS");
            name = bundle.getString("ITEM_NAME");
        }

        ItemDAO dao = new ItemDAO(getApplicationContext());
        Item item = dao.findItemByName(name);

        if (item != null && item.getUid() != 0) {
            tvID.setText(String.valueOf(item.getUid()));
            tvName.setText(item.getName());
            tvUsername.setText(item.getUsername());
            tvEmail.setText(item.getEmail());
            tvPassword.setText(item.getPassword());
        }
    }
}
