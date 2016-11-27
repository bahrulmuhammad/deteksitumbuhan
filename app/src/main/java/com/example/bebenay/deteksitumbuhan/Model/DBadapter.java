package com.example.bebenay.deteksitumbuhan.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by bebe on 12/6/2015.
 */
public class DBadapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_NAMA = "tanaman_nama";
    static final String KEY_KETERANGAN = "tanaman_keterangan";
    static final String KEY_KONVEKSITAS  = "tanaman_konveksitas";
    static final String KEY_SOLIDITAS = "tanaman_soliditas";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "tanaman_hias";
    static final String DATABASE_TABEL = "tanaman";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE = "CREATE TABLE tanaman (_id integer PRIMARY KEY AUTOINCREMENT, " +
            "tanaman_nama TEXT NOT NULL, tanaman_konveksitas TEXT NOT NULL, tanaman_soliditas TEXT NOT NULL);";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBadapter(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrade database dari versi" + oldVersion + "ke versi" + newVersion);
            db.execSQL("DROP TABLE IF EXIST tanaman");
            onCreate(db);
        }
    }

    public DBadapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public Cursor getAllPlant() {
        return db.query(DATABASE_TABEL, new String[] {KEY_ROWID, KEY_NAMA, KEY_KONVEKSITAS, KEY_SOLIDITAS},
                null, null, null, null, null);
    }

    public long insertPlant(String nama, String konveksitas, String soliditas) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAMA, nama);
        initialValues.put(KEY_KONVEKSITAS, konveksitas);
        initialValues.put(KEY_SOLIDITAS, soliditas);
        return db.insert(DATABASE_TABEL, null, initialValues);
    }

}
