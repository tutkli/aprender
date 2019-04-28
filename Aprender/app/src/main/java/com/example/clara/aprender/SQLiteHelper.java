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
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS USUARIO (id NUMBER, nombre VARCHAR)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS NIVEL(id NUMBER PRIMARY KEY), titulo VARCHAR, espacios NUMBER, resultado VARCHAR, solucion NUMBER");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SOLUCION(idUsuario NUMBER, idNivel NUMBER, idSolucion NUMBER, solucion VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USUARIO");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NIVEL");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SOLUCION");
    }
}