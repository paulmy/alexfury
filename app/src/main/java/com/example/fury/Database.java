package com.example.fury;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class Database extends SQLiteOpenHelper implements BaseColumns {
    public static final String HUNGRINESS_COLUMN = "hungriness";
    public static final String HAPPINESS_COLUMN = "happiness";
    public static final String CLEANLINESS_COLUMN = "cleanliness";
    public static final String STRENGTH_COLUMN = "strength";
    private static final String DATABASE_NAME = "tamagochi.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "Tamagochi";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DATABASE_TABLE + " (" +
                HUNGRINESS_COLUMN + " VARCHAR(10), " +
                HAPPINESS_COLUMN + " VARCHAR(10), " +
                CLEANLINESS_COLUMN + " VARCHAR(10), " +
                STRENGTH_COLUMN + " VARCHAR(10)); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
