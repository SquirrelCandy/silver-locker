package com.squirrelcandy.silverlocker.acts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squirrelcandy.silverlocker.R;
import com.squirrelcandy.silverlocker.db.ItemDAO;
import com.squirrelcandy.silverlocker.models.DataManager;
import com.squirrelcandy.silverlocker.models.Item;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 101;
    private ListView listView;
    private FloatingActionButton fabAdd, fabImport, fabExport;
    private ArrayAdapter<String> adapter;
    private ArrayList<Item> items;
    private ArrayList<String> itemNames;
    private boolean isFABOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

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

        FloatingActionButton fabMenu = findViewById(R.id.fabMenu);
        fabAdd = findViewById(R.id.fabAdd);
        fabImport = findViewById(R.id.fabImport);
        fabExport = findViewById(R.id.fabExport);

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabMenu();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(addIntent);
            }
        });

        /**
         * Import FAB kicks off Android File Chooser and passes in request code.
         * A listener receives result and checks request code to know it is about this import.
         * If the result is okay, get the file URI from the result.
         */
        fabImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabMenu();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataManager data = new DataManager();
                data.exportCsvToExternalStorage(getApplicationContext());
            }
        });
    }

    private void toggleFabMenu() {
        if (isFABOpen) {
            isFABOpen = false;
            fabAdd.animate().translationY(-150);
            fabImport.animate().translationX(-130);
            fabImport.animate().translationY(-130);
            fabExport.animate().translationX(-150);

        } else {
            isFABOpen = true;
            fabAdd.animate().translationY(0);
            fabImport.animate().translationX(0);
            fabImport.animate().translationY(0);
            fabExport.animate().translationX(0);
        }
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

    /**
     * Activity Listener that receives some callback.
     * File Chooser uses requestCode = 101 to receive file URI
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("FileChooser", "File Uri: " + uri.toString());
                    // Get the path

                    String path = null;
                    try {
                        path = DataManager.convertUriToPath(getApplicationContext(), uri);
                        Log.d("FileChooser", "File Path: " + path);
                        DataManager.importFile(new File(path));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    private void requestPermission(Activity context) {
//        boolean hasPermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
//        if (!hasPermission) {
//            ActivityCompat.requestPermissions(context,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    REQUEST_WRITE_STORAGE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == Preferences.)
//        switch (requestCode) {
//            case Preferences.REQUEST_WRITE_STORAGE: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "The app was allowed to write to your storage!", Toast.LENGTH_LONG).show();
//                    // Reload the activity with permission granted or use the features what required the permission
//                } else {
//                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
}
