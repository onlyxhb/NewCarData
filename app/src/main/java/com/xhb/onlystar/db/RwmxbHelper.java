package com.xhb.onlystar.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by onlyStar on 2016/3/31.
 */
public class RwmxbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "rwmxb.db";
    public static final String TABLE_NAME = "rwmxb";
    private static final int VERSON = 1;

    public RwmxbHelper(Context context) {
        super(context, DB_NAME, null, VERSON);
    }

    public RwmxbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + "(" +
                "rwdh varchar(20)," +
                "cx varchar(20)," +
                "clsbm varchar(50)," +
                "ys varchar(20)," +
                "sjID varchar(20)," +
                "sjhm varchar(20)," +
                "pzr varchar(20)," +
                "pzsj varchar(50)," +
                "lat float," +
                "lng float," +
                "PicPath varchar(100)," +
                "pzzt varchar(20)," +
                "bz varchar(20) )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
