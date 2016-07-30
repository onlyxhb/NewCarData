package com.xhb.onlystar.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by onlyStar on 2016/3/31.
 */
public class PictureHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "picture.db";
    public static final String TABLE_NAME = "picture";
    private static final int VERSON = 1;

    public PictureHelper(Context context) {
        super(context, DB_NAME, null, VERSON);
    }

    public PictureHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "create table " + TABLE_NAME + "(" +
                "rwdh varchar(20)," +
                "clsbm varchar(50)," +
                "picPath  varchar(1000))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
