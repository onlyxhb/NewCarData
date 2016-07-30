package com.xhb.onlystar.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xhb.onlystar.bean.Rwmxb;

import java.util.ArrayList;
import java.util.List;

import static com.xhb.onlystar.newcardata.R.id.clsbm;
import static com.xhb.onlystar.newcardata.R.id.pzzt;
import static com.xhb.onlystar.newcardata.R.id.rwdh;
import static com.xhb.onlystar.utils.Constant.task;

/**
 * Created by onlyStar on 2016/3/31.
 */
public class RwmxbDal {
    private RwmxbHelper dbhelper;

    public RwmxbDal(Context context) {
        this.dbhelper = new RwmxbHelper(context);
    }

    public RwmxbDal() {
    }

    public void addData(Rwmxb task) {
        //Log.e("myLog", "添加任务" + task);
        //取得数据库操作实例
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "insert into " + RwmxbHelper.TABLE_NAME + "(rwdh,cx,clsbm,ys," +
                "sjID,sjhm,pzr,pzsj,lat,lng,PicPath,pzzt,bz) values" +
                "(" + "'" + task.getRwdh() + "'" + "," + "'" + task.getCx() + "'" + "," + "'" + task.getClsbm() + "'" + "," + "'" + task.getYs() + "'" + "," + "'" +
                task.getSjID() + "'" + "," + "'" + task.getSjhm() + "'" + "," + "'" + task.getPzr() + "'" + "," + "'" + task.getPzsj() + "'" + "," + task.getLat() + "," +
                task.getLng() + "," + "'" + task.getPicPath() + "'" + "," + "'" + task.getPzzt() + "'" + "," + "'" + task.getBz() + "'" + ")";
        // Log.e("myLog", "插入sql语句:" + sql);
        db.execSQL(sql);
    }

    public List<Rwmxb> getData() {
        List<Rwmxb> taskData = new ArrayList<Rwmxb>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor c=null;
        try {
             c = db.rawQuery("select * from " + RwmxbHelper.TABLE_NAME, null);
            while (c!=null&&c.moveToNext()) {
                String rwdh = c.getString(c.getColumnIndex("rwdh"));
                String cx = c.getString(c.getColumnIndex("cx"));
                String clsbm = c.getString(c.getColumnIndex("clsbm"));
                String ys = c.getString(c.getColumnIndex("ys"));
                String sjID = c.getString(c.getColumnIndex("sjID"));
                String sjhm = c.getString(c.getColumnIndex("sjhm"));
                String pzr = c.getString(c.getColumnIndex("pzr"));
                String pzsj = c.getString(c.getColumnIndex("pzsj"));
                float lat = c.getFloat(c.getColumnIndex("lat"));
                float lng = c.getFloat(c.getColumnIndex("lng"));
                String PicPath = c.getString(c.getColumnIndex("PicPath"));
                String pzzt = c.getString(c.getColumnIndex("pzzt"));
                String bz = c.getString(c.getColumnIndex("bz"));
                taskData.add(new Rwmxb(rwdh, cx, clsbm, ys, sjID, sjhm, pzr, pzsj, lat, lng, PicPath, pzzt, bz));
            }
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    //ignore this
                }
            }
        }
        return taskData;
    }

    public List<Rwmxb> getData(String request) {
        List<Rwmxb> taskData = new ArrayList<Rwmxb>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql = "select * from " + RwmxbHelper.TABLE_NAME + " where " + request;
        Log.e("myLog", "查询的sql:" + sql);
        Cursor c=null;
        try {
             c = db.rawQuery(sql, null);
            Log.e("myLog", "c" + c);
            while (c!=null&&c.moveToNext()) {
                String rwdh = c.getString(c.getColumnIndex("rwdh"));
                String cx = c.getString(c.getColumnIndex("cx"));
                String clsbm = c.getString(c.getColumnIndex("clsbm"));
                String ys = c.getString(c.getColumnIndex("ys"));
                String sjID = c.getString(c.getColumnIndex("sjID"));
                String sjhm = c.getString(c.getColumnIndex("sjhm"));
                String pzr = c.getString(c.getColumnIndex("pzr"));
                String pzsj = c.getString(c.getColumnIndex("pzsj"));
                float lat = c.getFloat(c.getColumnIndex("lat"));
                float lng = c.getFloat(c.getColumnIndex("lng"));
                String PicPath = c.getString(c.getColumnIndex("PicPath"));
                String pzzt = c.getString(c.getColumnIndex("pzzt"));
                String bz = c.getString(c.getColumnIndex("bz"));
                taskData.add(new Rwmxb(rwdh, cx, clsbm, ys, sjID, sjhm, pzr, pzsj, lat, lng, PicPath, pzzt, bz));
            }
        }finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    //ignore this
                }
            }
        }
        return taskData;
    }

    public List<Rwmxb> getData(int number, int pages) {
        Log.e("myLog", "当前读取数据" + number + "  " + pages);
        List<Rwmxb> taskData = new ArrayList<Rwmxb>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
       Cursor c=null;
        try{
         c = db.rawQuery("select * from " + RwmxbHelper.TABLE_NAME + " order by rwdh desc" + " limit ?,?",
                new String[]{String.valueOf(number), String.valueOf(pages)});
        while (c!=null&&c.moveToNext()) {
            String rwdh = c.getString(c.getColumnIndex("rwdh"));
            String cx = c.getString(c.getColumnIndex("cx"));
            String clsbm = c.getString(c.getColumnIndex("clsbm"));
            String ys = c.getString(c.getColumnIndex("ys"));
            String sjID = c.getString(c.getColumnIndex("sjID"));
            String sjhm = c.getString(c.getColumnIndex("sjhm"));
            String pzr = c.getString(c.getColumnIndex("pzr"));
            String pzsj = c.getString(c.getColumnIndex("pzsj"));
            float lat = c.getFloat(c.getColumnIndex("lat"));
            float lng = c.getFloat(c.getColumnIndex("lng"));
            String PicPath = c.getString(c.getColumnIndex("PicPath"));
            String pzzt = c.getString(c.getColumnIndex("pzzt"));
            String bz = c.getString(c.getColumnIndex("bz"));
            taskData.add(new Rwmxb(rwdh, cx, clsbm, ys, sjID, sjhm, pzr, pzsj, lat, lng, PicPath, pzzt, bz));
        }}finally {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
                //ignore this
            }
        }
    }
        return taskData;
    }


    public List<Rwmxb> getData(String mRwdh, String keyWord) {
        List<Rwmxb> taskData = new ArrayList<Rwmxb>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql = "select * from " + RwmxbHelper.TABLE_NAME + " where rwdh = " + "'" + mRwdh + "' and clsbm like " + "'%" + keyWord + "%'";
        Log.e("myLog", "sql------->" + sql);
        Cursor c=null;
        try{
         c = db.rawQuery(sql,
                null);
        while (c!=null&&c.moveToNext()) {
            String rwdh = c.getString(c.getColumnIndex("rwdh"));
            String cx = c.getString(c.getColumnIndex("cx"));
            String clsbm = c.getString(c.getColumnIndex("clsbm"));
            String ys = c.getString(c.getColumnIndex("ys"));
            String sjID = c.getString(c.getColumnIndex("sjID"));
            String sjhm = c.getString(c.getColumnIndex("sjhm"));
            String pzr = c.getString(c.getColumnIndex("pzr"));
            String pzsj = c.getString(c.getColumnIndex("pzsj"));
            float lat = c.getFloat(c.getColumnIndex("lat"));
            float lng = c.getFloat(c.getColumnIndex("lng"));
            String PicPath = c.getString(c.getColumnIndex("PicPath"));
            String pzzt = c.getString(c.getColumnIndex("pzzt"));
            String bz = c.getString(c.getColumnIndex("bz"));
            taskData.add(new Rwmxb(rwdh, cx, clsbm, ys, sjID, sjhm, pzr, pzsj, lat, lng, PicPath, pzzt, bz));
            Log.e("myLog", "模糊查询------------>" + rwdh);
        }}finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    //ignore this
                }
            }
        }
        return taskData;
    }

    public long getCount() {
        return 0;
    }

    //更新记录
    public void UpdateData(Rwmxb task) {
        deleteData(task);
        addData(task);
    }

    //删除记录
    public void deleteData(Rwmxb task) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "delete from " + RwmxbHelper.TABLE_NAME + " where rwdh=" + "'" + task.getRwdh() + "'" + " and clsbm= " + "'" + task.getClsbm() + "'";
        db.execSQL(sql);
    }

    //删除所有记录
    public void deleteData() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "delete from " + RwmxbHelper.TABLE_NAME;
        db.execSQL(sql);
    }

    //更改状态
    public void UpdateData(String rwdh, String clsbm, int mState) {
        String pzzt = String.valueOf(mState);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "update " + RwmxbHelper.TABLE_NAME + " set pzzt=" + "'" + pzzt + "'" + " where rwdh=" + "'" + rwdh + "'" + " and clsbm= " + "'" + clsbm + "'";
        db.execSQL(sql);
    }

    //更改Gps
    public void UpdateGps(String mRwdh, String mClsbm,float lat,float lng) {
        Log.e("myLog"," "+lat+" "+lng);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "update " + RwmxbHelper.TABLE_NAME + " set lat=" + lat  +" , lng ="+ lng + " where rwdh=" + "'" + mRwdh + "'" + " and clsbm= " + "'" + mClsbm + "'";
        db.execSQL(sql);
    }


    //更改拍照时间
    public void UpdatePzsj(String rwdh, String clsbm, String time) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "update " + RwmxbHelper.TABLE_NAME + " set pzsj=" + "'" + time + "'" + " where rwdh=" + "'" + rwdh + "'" + " and clsbm= " + "'" + clsbm + "'";
        db.execSQL(sql);
    }

    //根据任务批次号和任务号判断是否存在
    public boolean isExit(String mRwdh, String mClsbm) {
        List<Rwmxb> taskData = new ArrayList<Rwmxb>();
        boolean isExit = false;
        taskData = getData();
        if (taskData.size() > 0) {
            for (int i = 0; i < taskData.size(); i++) {
                if (taskData.get(i).getRwdh().equals(mRwdh) && taskData.get(i).getClsbm().equals(mClsbm)) {
                    isExit = true;
                    break;
                } else if (i == (taskData.size() - 1)) isExit = false;
            }
        } else isExit = false;
        return isExit;
    }


    //-------------------待验证
    public List<Rwmxb> getDataByTime(String mDate) {
        List<Rwmxb> taskData = new ArrayList<Rwmxb>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql = "select * from " + RwmxbHelper.TABLE_NAME + " where task_date = " + "'" + mDate + "'";
        // Log.e("myLog", sql);
        Cursor c=null;
        try {
             c = db.rawQuery(sql, null);
            while (c!=null&&c.moveToNext()) {
                String rwdh = c.getString(c.getColumnIndex("rwdh"));
                String cx = c.getString(c.getColumnIndex("cx"));
                String clsbm = c.getString(c.getColumnIndex("clsbm"));
                String ys = c.getString(c.getColumnIndex("ys"));
                String sjID = c.getString(c.getColumnIndex("sjID"));
                String sjhm = c.getString(c.getColumnIndex("sjhm"));
                String pzr = c.getString(c.getColumnIndex("pzr"));
                String pzsj = c.getString(c.getColumnIndex("pzsj"));
                float lat = c.getFloat(c.getColumnIndex("lat"));
                float lng = c.getFloat(c.getColumnIndex("lng"));
                String PicPath = c.getString(c.getColumnIndex("PicPath"));
                String pzzt = c.getString(c.getColumnIndex("pzzt"));
                String bz = c.getString(c.getColumnIndex("bz"));
                taskData.add(new Rwmxb(rwdh, cx, clsbm, ys, sjID, sjhm, pzr, pzsj, lat, lng, PicPath, pzzt, bz));
            }
        }finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    //ignore this
                }
            }
        }
        return taskData;
    }
}
