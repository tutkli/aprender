package com.example.clara.aprender;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context) {
        super(context, "aprender.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS USUARIO (id VARCHAR, autor VARCHAR, isbn VARCHAR PRIMARY KEY, situacion VARCHAR, ubicacion VARCHAR, sintesis VARCHAR, portada BLOB)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS NIVEL(ubicacion VARCHAR PRIMARY KEY)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SOLUCION(ubicacion VARCHAR PRIMARY KEY)");

        ContentValues contentValues = new ContentValues();
        contentValues.put("ubicacion", "Esta en una ubicacion especial");
        long ins = sqLiteDatabase.insert("ubicaciones", null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS LIBROS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS UBICACIONES");

    }

}