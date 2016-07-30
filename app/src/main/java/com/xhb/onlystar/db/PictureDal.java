package com.xhb.onlystar.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.xhb.onlystar.utils.MyUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.path;
import static android.R.id.list;

/**
 * Created by onlyStar on 2016/3/31.
 */
public class PictureDal {
    private PictureHelper dbhelper;

    public PictureDal(Context context) {
        this.dbhelper = new PictureHelper(context);
    }

    public PictureDal() {
    }

    //将图片一字节形式存储数据库读取操作
    public long insert(String rwdh, String clsbm, String picPath) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("rwdh", rwdh);
        cv.put("clsbm", clsbm);
        cv.put("picPath", picPath);
        long result = db.insert(PictureHelper.TABLE_NAME, null, cv);
        db.close();
        return result;//返回新添记录的行号，与主键id无关
    }

    public List<String> getPicPath(String rwdh, String clsbm) {
        List<String> path=new ArrayList<String>();
        String picPath = null;
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql = "select * from " + PictureHelper.TABLE_NAME + " where rwdh = " + "'" + rwdh + "'" + " and clsbm= " + "'" + clsbm + "'";
        Cursor c = null;
        try {
            c = db.rawQuery(sql, null);
            while (c != null && c.moveToNext()) {
                 picPath = c.getString(c.getColumnIndex("picPath"));
            }
        } finally {
            if (c != null) {
                try {
                    c.close();
                    db.close();
                } catch (Exception e) {
                    //ignore this
                }
            }
        }
       path=MyUtils.getListByJson(picPath);
        return path;
    }

    //删除图片
    public void delete(String rwdh, String clsbm) {
        String sql = "delete from " + PictureHelper.TABLE_NAME + " where rwdh = " + "'" + rwdh + "'" + " and clsbm= " + "'" + clsbm + "'";
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        db.execSQL(sql);
        db.close();
    }

    //删除所有记录
    public void deleteData() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "delete from " + PictureHelper.TABLE_NAME;
        db.execSQL(sql);
    }
}
