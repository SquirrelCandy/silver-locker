package com.squirrelcandy.silverlocker.acts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squirrelcandy.silverlocker.R;
import com.squirrelcandy.silverlocker.db.ItemDAO;
import com.squirrelcandy.silverlocker.models.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private TextView tvItem;
    private ArrayAdapter<String> adapter;
    private ArrayList<Item> items;
    private ArrayList<String> itemNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        tvItem = findViewById(R.id.tvItem);

        ItemDAO dao = new ItemDAO(getApplicationContext());
        items = dao.readAllItems();

        itemNames = new ArrayList<>();
        for (int i=0; i < items.size(); i++) {
            itemNames.add(items.get(i).getName());
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, itemNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent viewAct = new Intent(MainActivity.this, ViewActivity.class);
                viewAct.putExtra("ITEM_POS", position);
                viewAct.putExtra("ITEM_NAME", adapter.getItem(position));
                startActivity(viewAct);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int pos, long id) {
                Log.d("long clicked","pos: " + pos);
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
        ItemDAO dao = new ItemDAO(getApplicationContext());
        for (int i=0; i < items.size(); i++) {
            if (items.get(i).getName().equals(itemNames.get(pos))) {
                dao.deleteItemByID(items.get(i).getUid());
                items.remove(i);
                itemNames.remove(pos);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
