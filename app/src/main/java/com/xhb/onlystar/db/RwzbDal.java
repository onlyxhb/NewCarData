package com.xhb.onlystar.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.bean.Rwzb;
import com.xhb.onlystar.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.order;
import static com.xhb.onlystar.newcardata.R.id.clsbm;
import static com.xhb.onlystar.newcardata.R.id.pzzt;
import static com.xhb.onlystar.newcardata.R.id.rwdh;

/**
 * Created by onlyStar on 2016/3/31.
 */
public class RwzbDal {
    private RwzbHelper dbhelper;

    public RwzbDal(Context context) {
        this.dbhelper = new RwzbHelper(context);
    }

    public RwzbDal() {
    }

    //更改状态
    public void UpdateData(String rwdh,int mState) {
        String pzzt = String.valueOf(mState);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "update " + RwzbHelper.TABLE_NAME + " set pzzt=" + "'" + pzzt + "'" + " where rwdh=" + "'" + rwdh + "'" ;
        db.execSQL(sql);
    }

    //更新记录
    public void UpdateData(Rwzb task) {
        deleteData(task);
        addData(task);
    }

    public void deleteData(Rwzb task) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "delete from " + RwzbHelper.TABLE_NAME + " where rwdh=" + "'" + task.getRwdh() + "'";
        db.execSQL(sql);
    }

    //删除所有记录
    public void deleteData() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "delete from " + RwzbHelper.TABLE_NAME;
        db.execSQL(sql);
    }

    public void addData(Rwzb task) {
        //Log.e("myLog", "添加任务" + task);
        //取得数据库操作实例
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "insert into " + RwzbHelper.TABLE_NAME + "(rwdh,drdh,sjlx,jxsdm," +
                "jxzmc,rwsj,rwlx,pzr,pzsj,pzzt,bz) values" +
                "(" + "'" + task.getRwdh() + "'" + "," + "'" + task.getDrdh() + "'" + "," + "'" + task.getSjlx() + "'" + "," + "'" +
                task.getJxsdm() + "'" + "," + "'" + task.getJxsmc() + "'" + "," + "'" + task.getRwsj() + "'" + "," + "'" + task.getRwlx() + "'" + "," + "'" + task.getPzr() + "'" + "," + "'" + task.getPzsj() + "'" + "," + "'" + task.getPzzt() + "'" + "," + "'" + task.getBz() + "'" + ")";
        //Log.e("myLog", "插入sql语句:" + sql);
        db.execSQL(sql);
    }

    public List<Rwzb> getData() {
        List<Rwzb> data = new ArrayList<Rwzb>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("select * from " + RwzbHelper.TABLE_NAME, null);
            while (c != null && c.moveToNext()) {
                String rwdh = c.getString(c.getColumnIndex("rwdh"));
                String drdh = c.getString(c.getColumnIndex("drdh"));
                String sjlx = c.getString(c.getColumnIndex("sjlx"));
                String jxsdm = c.getString(c.getColumnIndex("jxsdm"));
                String jxzmc = c.getString(c.getColumnIndex("jxzmc"));
                String rwsj = c.getString(c.getColumnIndex("rwsj"));
                String rwlx = c.getString(c.getColumnIndex("rwlx"));
                String pzr = c.getString(c.getColumnIndex("pzr"));
                String pzsj = c.getString(c.getColumnIndex("pzsj"));
                String pzzt = c.getString(c.getColumnIndex("pzzt"));
                String bz = c.getString(c.getColumnIndex("bz"));
                data.add(new Rwzb(rwdh, drdh, sjlx, jxsdm, jxzmc, rwsj, rwlx, pzr, pzsj, pzzt, bz));
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
        return data;
    }

    /*
        public List<Task> getData(String mPztt) {
            Log.e("myLog", "拍照状态" + mPztt);
            List<Task> taskData = new ArrayList<Task>();
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String sql = "select * from " + RwmxbHelper.TABLE_NAME + " where pzzt = " + "'" + mPztt + "'";
            Log.e("myLog", "sql:" + sql);
            Cursor c = db.rawQuery(sql, null);
            Log.e("myLog", "c" + c);
            while (c.moveToNext()) {
                int CurrentPage = c.getInt(c.getColumnIndex("CurrentPage"));
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
                Log.e("myLog", "clsbm" + clsbm);
                taskData.add(new Task(CurrentPage, rwdh, cx, clsbm, ys, sjID, sjhm, pzr, pzsj, lat, lng, PicPath, pzzt, bz));
            }
            c.close();
            return taskData;
        }*/
    public List<Rwzb> getData(int number, int pages,String date) {
        Log.e("myLog", "当前读取数据" + number + "  " + pages+" date"+date);
        List<Rwzb> taskData = new ArrayList<Rwzb>();
        String sql="select * from " + RwzbHelper.TABLE_NAME + " where pzr= " + "'" + Constant.user.getGrdm() + "'"+" and rwsj like "+"'%"+date+"%'" + " order by pzzt asc, rwdh desc" + " limit ?,?";
        Log.e("myLog",sql);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(sql,new String[]{String.valueOf(number), String.valueOf(pages)});
            while (c != null && c.moveToNext()) {
                String rwdh = c.getString(c.getColumnIndex("rwdh"));
                String drdh = c.getString(c.getColumnIndex("drdh"));
                String sjlx = c.getString(c.getColumnIndex("sjlx"));
                String jxsdm = c.getString(c.getColumnIndex("jxsdm"));
                String jxzmc = c.getString(c.getColumnIndex("jxzmc"));
                String rwsj = c.getString(c.getColumnIndex("rwsj"));
                String rwlx = c.getString(c.getColumnIndex("rwlx"));
                String pzr = c.getString(c.getColumnIndex("pzr"));
                String pzsj = c.getString(c.getColumnIndex("pzsj"));
                String pzzt = c.getString(c.getColumnIndex("pzzt"));
                String bz = c.getString(c.getColumnIndex("bz"));
                taskData.add(new Rwzb(rwdh, drdh, sjlx, jxsdm, jxzmc, rwsj, rwlx, pzr, pzsj, pzzt, bz));
                Log.e("myLog", "获取到数据:" + jxzmc);
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

/*
    public long getCount() {
        return 0;
    }

    //更改状态
    public void UpdateData(String clsbm, int mState) {
        String pzzt = String.valueOf(mState);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "update " + RwmxbHelper.TABLE_NAME + " set pzzt=" + "'" + pzzt + "'" + " where clsbm=" + "'" + clsbm + "'";
        db.execSQL(sql);
    }*/

    //根据任务批次号和任务号判断是否存在
    public boolean isExit(String mRwdh) {
        List<Rwzb> taskData = new ArrayList<Rwzb>();
        boolean isExit = false;
        taskData = getData();
        if (taskData.size() > 0) {
            for (int i = 0; i < taskData.size(); i++) {
                if (taskData.get(i).getRwdh().equals(mRwdh)) {
                    isExit = true;
                    break;
                } else if (i == (taskData.size() - 1)) isExit = false;
            }
        } else isExit = false;
        return isExit;
    }


    //根据任务批次号和任务号判断是否存在
    public String getpzzt(String mRwdh) {
        List<Rwzb> taskData = new ArrayList<Rwzb>();
        String pzzt=null;
        taskData = getData();
        if (taskData.size() > 0) {
            for (int i = 0; i < taskData.size(); i++) {
                if (taskData.get(i).getRwdh().equals(mRwdh)) {
                    pzzt=taskData.get(i).getPzzt();
                    break;
                } else if (i == (taskData.size() - 1)) pzzt=null;
            }
        } else pzzt=null;
        return pzzt;
    }

  /*  //-------------------待验证
    public List<Task> getDataByTime(String mDate) {
        List<Task> taskData = new ArrayList<Task>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql = "select * from " + RwmxbHelper.TABLE_NAME + " where task_date = " + "'" + mDate + "'";
        // Log.e("myLog", sql);
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            int CurrentPage = c.getInt(c.getColumnIndex("CurrentPage"));
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
            taskData.add(new Task(CurrentPage, rwdh, cx, clsbm, ys, sjID, sjhm, pzr, pzsj, lat, lng, PicPath, pzzt, bz));
        }
        c.close();
        return taskData;
    }*/
}
