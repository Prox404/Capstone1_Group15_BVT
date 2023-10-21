package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BabyVaccinationTracker.db";

    private static final String DATABASE_CREATE_USER = "CREATE TABLE Student (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "HoTen TEXT NOT NULL, " +
            "NgaySinh TEXT NOT NULL, " +
            "SoDT TEXT NOT NULL," +
            "DiaChi TEXT NOT NULL," +
            "Email TEXT NOT NULL," +
            "ID_Lop INTEGER NOT NULL," +
            "ID_Nganh INTEGER NOT NULL)";
    private static final int DATABASE_VERSION = 1;

    public MyDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
