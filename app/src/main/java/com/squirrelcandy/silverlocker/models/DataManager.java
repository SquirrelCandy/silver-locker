package com.squirrelcandy.silverlocker.models;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.squirrelcandy.silverlocker.db.ItemDAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

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

//    public void readRaw(){
//        InputStream is = this.getResources().openRawResource(R.raw.textfile);
//        InputStreamReader isr = new InputStreamReader(is);
//        BufferedReader br = new BufferedReader(isr, 8192);    // 2nd arg is buffer size
//
//        // More efficient (less readable) implementation of above is the composite expression
//    /*BufferedReader br = new BufferedReader(new InputStreamReader(
//            this.getResources().openRawResource(R.raw.textfile)), 8192);*/
//
//        try {
//            String test;
//            while (true){
//                test = br.readLine();
//                // readLine() returns null if no more lines in the file
//                if(test == null) break;
//                tv.append("\n"+"    "+test);
//            }
//            isr.close();
//            is.close();
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
