package com.squirrelcandy.silverlocker.models;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.squirrelcandy.silverlocker.db.ItemDAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DataManager {

    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWriteable = false;

    public void checkExternalMedia(){
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    public static void importFile(Context context, File file) {
        String ext = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
        Log.d("import", "File Extension = " + ext);
        if ("txt".equalsIgnoreCase(ext)) {
            importPipeDelimitedTextFile(context, file);
        }else if ("csv".equalsIgnoreCase(ext)) {
            importCSV(file);
        }
    }

    private static void importPipeDelimitedTextFile(Context context, File file) {
        Log.d("import", "importing pipe delimied text file");
        Log.d("import", "Path="+file.getAbsolutePath());

        List<String> lines = readRawFile(file);
        ItemDAO dao = new ItemDAO(context);

        for (int i=1; i < lines.size(); i++) {
            String pieces[] = lines.get(i).split("\\|");
            Item item = new Item();
            item.setName(pieces[0]);
            item.setUsername(pieces[1]);
            item.setEmail(pieces[2]);
            item.setPassword(pieces[3]);
        }
    }

    private static void importCSV(File file) {
        Log.d("import", "importing CSV file");
        Log.d("import", "Path="+file.getAbsolutePath());
    }

    public void exportCsvToExternalStorage(Context appContext){
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/SilverLocker");
        dir.mkdirs();
        File file = new File(dir, "locket.txt");

        ItemDAO dao = new ItemDAO(appContext);
        ArrayList<Item> items = dao.readAllItems();
        StringBuffer sb = new StringBuffer();

        if (items.size() > 0) {
            sb.append("name|username|email|password\n");
            for (int i=0; i <= items.size(); i++) {
                Item item = items.get(i);
                sb.append(item.getName()).append("|").append(item.getUsername()).append("|")
                        .append(item.getEmail()).append("|").append(item.getPassword()).append("\n");
            }
        } else {
            sb.append("NO_ITEMS_FOUND");
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(sb.toString().getBytes());
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertUriToPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static List<String> readRawFile(File file){
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return lines;
    }
}
