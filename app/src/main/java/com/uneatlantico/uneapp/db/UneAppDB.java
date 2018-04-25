package com.uneatlantico.uneapp.db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class UneAppDB extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "UneAppDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public UneAppDB(Context context) {
        super(context, DATABASE_NAME, "data/data/com.uneatlantico.uneapp/databases/", null, DATABASE_VERSION);
    }

}
