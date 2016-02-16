package com.im.dictionary.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.im.dictionary.App;

public class DB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final int VERSION = 1;

    public static final String[] ALL_COLUMNS = null;
    public static final String NOT_GROUPED = null;

    public DB(Context context) {
        super(context, DB.DATABASE_NAME, null, VERSION);
    }

    public static CardDao CARD_DAO_INSTANCE;

    public static DB DB_INSTANCE;

    static {
        DB_INSTANCE = new DB(App.getAppContext());
        CARD_DAO_INSTANCE = new CardDao();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + CardDao.TABLE_NAME + " " +
                        "( " + CardDao.COLUMN_ID + " integer primary key, " +
                        " " + CardDao.COLUMN_WORD + " text," +
                        " " + CardDao.COLUMN_TYPE + " integer," +
                        " " + CardDao.COLUMN_TRANSLATE + " place text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CardDao.TABLE_NAME + "");
        onCreate(db);
    }

    public static SQLiteDatabase getSQLiteDataBase() {
        return DB_INSTANCE.getWritableDatabase();
    }
}
