package com.codeIncubnator.boltfax;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDataBase extends SQLiteOpenHelper {
    public MyDataBase(@Nullable Context context) {
        super(context, "BoltFax", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tab = "CREATE TABLE user(username text,contactnumber text,password text)";
        db.execSQL(tab);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void add_data(String name, String number, String password) {
        SQLiteDatabase obj = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("username", name);
        c.put("contactnumber", number);
        c.put("password", password);

        obj.insert("user", null, c);
        obj.close();


    }

    public void dell_all() {
        SQLiteDatabase obj = getWritableDatabase();

        String dell = "DELETE FROM user";
        obj.execSQL(dell);
    }

    public Cursor getAllInfo() {
        SQLiteDatabase obj = getWritableDatabase();


        Cursor c = obj.rawQuery("SELECT * FROM user", null);

        return c;

    }

    public Cursor getInfo(String number) {
        SQLiteDatabase obj = getWritableDatabase();


        Cursor c = obj.rawQuery("SELECT * FROM user where contactnumber='" + number + "'", null);

        return c;

    }

    public void update_record(String number, String pass) {
        SQLiteDatabase obj = getWritableDatabase();

        String up = "UPDATE user SET password='" + pass + "'WHERE contactnumber='" + number + "'";
        obj.execSQL(up);
    }
}

