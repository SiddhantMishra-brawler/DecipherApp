package com.example.docxter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbhelper extends SQLiteOpenHelper {
    public static final String db_name="Decipher.db";
    public static final int VERSION =1;
    public dbhelper(Context context) {
        super(context, db_name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         String sqlstmt= "CREATE TABLE "+detailtable.detailtableinner.Table_Name +" ("+
                detailtable.detailtableinner._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                detailtable.detailtableinner.Column_Name+ " TEXT NOT NULL, "+
                detailtable.detailtableinner.timestamp+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+
                ");";
         sqLiteDatabase.execSQL(sqlstmt);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+detailtable.detailtableinner.Table_Name);
        onCreate(sqLiteDatabase);
    }
}
