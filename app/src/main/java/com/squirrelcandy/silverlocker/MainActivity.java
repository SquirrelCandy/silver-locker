package com.squirrelcandy.silverlocker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView tvItem;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        tvItem = findViewById(R.id.tvItem);
        items = new ArrayList<>();

        items.add("pikachu");
        items.add("charmander");
        items.add("bulbasaur");
        items.add("squirtle");

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent viewAct = new Intent(MainActivity.this, ViewActivity.class);
                viewAct.putExtra("ITEM_ID", position);
                viewAct.putExtra("ITEM_NAME", adapter.getItem(position));
                startActivity(viewAct);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int pos, long id) {
                Log.v("long clicked","pos: " + pos);
                deleteItem(pos);
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(addIntent);
            }
        });
    }

    public void deleteItem(int pos) {
        items.remove(pos);
        adapter.notifyDataSetChanged();
    }
}
