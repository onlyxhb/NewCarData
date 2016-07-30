package com.xhb.onlystar.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by onlyStar on 2016/3/31.
 */
public class RwzbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "rwzb.db";
    public static final String TABLE_NAME = "rwzb";
    private static final int VERSON = 1;

    public RwzbHelper(Context context) {
        super(context, DB_NAME, null, VERSON);
    }

    public RwzbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + "(" +
                "rwdh varchar(20) primary key ," +
                "drdh varchar(20)," +
                "sjlx varchar(50)," +
                "jxsdm varchar(20)," +
                "jxzmc varchar(50)," +
                "rwsj varchar(20)," +
                "rwlx varchar(20)," +
                "pzr varchar(50)," +
                "pzsj varchar(100)," +
                "pzzt varchar(20)," +
                "bz varchar(20) )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
