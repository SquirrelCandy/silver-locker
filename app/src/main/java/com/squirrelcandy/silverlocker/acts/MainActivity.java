package com.squirrelcandy.silverlocker.acts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squirrelcandy.silverlocker.R;
import com.squirrelcandy.silverlocker.db.ItemDAO;
import com.squirrelcandy.silverlocker.models.Item;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import static com.squirrelcandy.silverlocker.models.DataManager.REQUEST_WRITE_STORAGE;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_WRITE_STORAGE = 112;
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
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent viewAct = new Intent(MainActivity.this, ViewActivity.class);
                viewAct.putExtra("ITEM_POS", position);
                viewAct.putExtra("ITEM_NAME", adapter.getItem(position));
                startActivity(viewAct);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_context_item, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.view:
                Intent viewAct = new Intent(MainActivity.this, ViewActivity.class);
                viewAct.putExtra("ITEM_POS", info.position);
                viewAct.putExtra("ITEM_NAME", adapter.getItem(info.position));
                startActivity(viewAct);
                return true;
            case R.id.edit:
                // edit stuff here
                return true;
            case R.id.delete:
                ItemDAO dao = new ItemDAO(getApplicationContext());
                for (int i=0; i < items.size(); i++) {
                    if (items.get(i).getName().equals(itemNames.get(info.position))) {
                        dao.deleteItemByID(items.get(i).getUid());
                        items.remove(i);
                        itemNames.remove(info.position);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void requestPermission(Activity context) {
        boolean hasPermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Preferences.REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "The app was allowed to write to your storage!", Toast.LENGTH_LONG).show();
                    // Reload the activity with permission granted or use the features what required the permission
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
