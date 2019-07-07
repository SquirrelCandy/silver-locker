package com.squirrelcandy.silverlocker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.squirrelcandy.silverlocker.models.Item;

import java.util.ArrayList;

public class ItemDAO {

    SqliteAdapter itemDB;

    public ItemDAO(Context context) {
        itemDB = new SqliteAdapter(context);
    }

    public long saveItem(Item item) {
        SQLiteDatabase db = itemDB.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(itemDB.NAME, item.getName());
        content.put(itemDB.USERNAME, item.getUsername());
        content.put(itemDB.EMAIL, item.getEmail());
        content.put(itemDB.PASSWORD, item.getPassword());
        return db.insert(itemDB.TABLE_NAME, null , content);
    }

    public ArrayList<Item> readAllItems() {
        SQLiteDatabase db = itemDB.getWritableDatabase();
        String[] columns = {itemDB.UID, itemDB.NAME, itemDB.USERNAME, itemDB.EMAIL, itemDB.PASSWORD};
        Cursor cursor = db.query(itemDB.TABLE_NAME, columns,null,null,null,null,null);
        ArrayList<Item> items = new ArrayList<>();

        while (cursor.moveToNext()) {
            Item item = new Item();
            item.setUid(cursor.getInt(cursor.getColumnIndex(itemDB.UID)));
            item.setName(cursor.getString(cursor.getColumnIndex(itemDB.NAME)));
            item.setUsername(cursor.getString(cursor.getColumnIndex(itemDB.USERNAME)));
            item.setEmail(cursor.getString(cursor.getColumnIndex(itemDB.EMAIL)));
            item.setPassword(cursor.getString(cursor.getColumnIndex(itemDB.PASSWORD)));
            items.add(item);
        }
        return items;
    }

    public int updateItem(Item item) {
        SQLiteDatabase db = itemDB.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(itemDB.NAME, item.getName());
        content.put(itemDB.USERNAME, item.getUsername());
        content.put(itemDB.EMAIL, item.getEmail());
        content.put(itemDB.PASSWORD, item.getPassword());
        String[] whereArgs= { String.valueOf(item.getUid()) };
        return db.update(itemDB.TABLE_NAME, content, itemDB.UID +" = ?",whereArgs );
    }

    /**
     * Deletes an item by UID
     * @param uid
     * @return number of rows affected
     */
    public int deleteItemByID(int uid) {
        SQLiteDatabase db = itemDB.getWritableDatabase();
        String[] whereArgs = { String.valueOf(uid) };
        return db.delete(itemDB.TABLE_NAME , itemDB.UID +" = ?", whereArgs);
    }

    /**
     * Truncates all items from the item table and returns number of items deleted
     * @return number of all items deleted from table
     */
    public int deleteAllItems() {
        SQLiteDatabase db = itemDB.getWritableDatabase();
        return db.delete(itemDB.TABLE_NAME, "1", null);
    }

    static class SqliteAdapter extends SQLiteOpenHelper {
        private Context context;
        private static final String DATABASE_NAME = "SilverLocker";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_NAME = "ITEMS";
        private static final String UID = "UID";          //Primary Key
        private static final String NAME = "NAME";
        private static final String USERNAME = "USERNAME";
        private static final String EMAIL = "EMAIL";
        private static final String PASSWORD = "PASSWORD";

        private static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME
                + " ("+ UID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME +" VARCHAR(255) ,"
                + USERNAME +" VARCHAR(255) ,"
                + EMAIL +" VARCHAR(255) ,"
                + PASSWORD +" VARCHAR(225));";

        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+ TABLE_NAME;

        public SqliteAdapter(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
