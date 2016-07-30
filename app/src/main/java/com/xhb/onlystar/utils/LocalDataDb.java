package com.xhb.onlystar.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.bean.Rwzb;
import com.xhb.onlystar.db.RwmxbDal;
import com.xhb.onlystar.db.RwzbDal;
import com.xhb.onlystar.newcardata.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onlystar on 2016/4/5.
 */
public class LocalDataDb {
    public static Context context;

    public LocalDataDb(Context context) {
        this.context = context;
    }


    //获取本地数据库中的全部数据---------无条件
    public static List<Rwmxb> getAllData() {
        List<Rwmxb> u = new ArrayList<Rwmxb>();
        try {
            RwmxbDal dal = new RwmxbDal(context);
            u = dal.getData();
        } catch (Exception e) {
            //Toast.makeText(context, "加载数据失败！", Toast.LENGTH_SHORT).show();
            Log.e("myLog", "获取所有的数据失败!");
        }
        return u;
    }


    //获取本地数据库中的全部数据---------拍照状态
    public static List<Rwmxb> getAllData(String request) {
        List<Rwmxb> u = new ArrayList<Rwmxb>();
        try {
            RwmxbDal dal = new RwmxbDal(context);
            u = dal.getData(request);
        } catch (Exception e) {
            //Toast.makeText(context, "加载数据失败！", Toast.LENGTH_SHORT).show();
            Log.e("myLog", "获取所有的数据失败!");
        }
        return u;
    }

    //分页----获取本地数据库中数据
    public static List<Rwmxb> getAllData(int number, int pages) {
        List<Rwmxb> u = new ArrayList<>();
        try {
            RwmxbDal dal = new RwmxbDal(context);
            u = dal.getData(number, pages);
        } catch (Exception e) {
            //Toast.makeText(context, "加载数据失败！", Toast.LENGTH_SHORT).show();
            Log.e("myLog", "获取所有的数据失败!");
        }
        return u;
    }


//    //获取所有的时间
//    public static List<Date> getDataTime(int number, int pages) {
//        List<Task> u = new ArrayList<>();
//        try {
//            RwmxbDal dal = new RwmxbDal(context);
//            u = dal.getData(number, pages);
//        } catch (Exception e) {
//            Toast.makeText(context, "加载数据失败！", Toast.LENGTH_SHORT).show();
//        }
//        List<Date> data = new ArrayList<Date>();
//        for (int i = 0; i < u.size(); i++) {
//            String m = u.get(i).getPzsj();
//            int year = Integer.valueOf(m.substring(0, 4));
//            int month = Integer.valueOf(m.substring(4, 6));
//            int day = Integer.valueOf(m.substring(6, 8));
//            Log.e("myLog", "解析的时间: " + year + "年" + month + "月" + day + "日");
//            Date mDate = new Date(year, month, day);
//            if (!data.contains(mDate)) {
//                data.add(mDate);
//            } else {
//                Log.e("myLog", "该日期已存在，无需重复添加: ");
//            }
//        }
//        return data;
//    }

    //获取长度
    public static int getDataLength() {
        List<Rwmxb> u = new ArrayList<Rwmxb>();
        try {
            RwmxbDal dal = new RwmxbDal(context);
            u = dal.getData();
            Log.e("myLog", "本地数据的个数:" + u.size());
        } catch (Exception e) {
            Log.e("myLog", "获取本地数据失败!");
            Toast.makeText(context, "加载数据失败！", Toast.LENGTH_SHORT).show();
        }
        return u.size();
    }

    //更改任务的状态
    public static void UpdateMxbState(String mRwdh, String mClsbm, int state) {
        try {
            RwmxbDal dal = new RwmxbDal(context);
            dal.UpdateData(mRwdh, mClsbm, state);
        } catch (Exception e) {
            Toast.makeText(context, "修改数据失败！", Toast.LENGTH_SHORT).show();
        }
    }

    public static void UpdateGps(String mRwdh, String mClsbm,float lat,float lng) {
        try {
            RwmxbDal dal = new RwmxbDal(context);
            dal.UpdateGps(mRwdh, mClsbm, lat,lng);
        } catch (Exception e) {
            Toast.makeText(context, "修改数据失败！", Toast.LENGTH_SHORT).show();
        }
    }

    //更改任务的状态
    public static void UpdateZbState(String mRwdh,int state) {
        try {
            RwzbDal dal = new RwzbDal(context);
            dal.UpdateData(mRwdh, state);
        } catch (Exception e) {
            Toast.makeText(context, "修改数据失败！", Toast.LENGTH_SHORT).show();
        }
    }

    //拼接年月日 0-11          2016-06-12
    public static String getDateByInt(int year, int month, int day) {
        //month = month + 1;
        String mYear = String.valueOf(year);
        String mMonth = String.valueOf(month);
        String mDay = String.valueOf(day);
        String date = mYear;
        date+="-";
        if (mMonth.length() == 1) {
            date += "0" + mMonth;
        } else if (mMonth.length() == 2) {
            date += mMonth;
        }
        date+="-";
        if (mDay.length() == 1) {
            date += "0" + mDay;
        } else if (mDay.length() == 2) {
            date += mDay;
        }
        return date.trim();
    }

    //拼接年月日 0-11      20160612
    public static String getDateByIntTwo(int year, int month, int day) {
        //month = month + 1;
        String mYear = String.valueOf(year);
        String mMonth = String.valueOf(month);
        String mDay = String.valueOf(day);
        String date = mYear;
        if (mMonth.length() == 1) {
            date += "0" + mMonth;
        } else if (mMonth.length() == 2) {
            date += mMonth;
        }
        if (mDay.length() == 1) {
            date += "0" + mDay;
        } else if (mDay.length() == 2) {
            date += mDay;
        }
        return date.trim();
    }

    //根据任务单号找时间
    public static String getTimeByRwdh(String rwdh) {
        String str = null;
        List<Rwzb> datas = new ArrayList<Rwzb>();
        RwzbDal dal = new RwzbDal(context);
        datas = dal.getData();
        for (Rwzb data : datas) {

            if (data.getRwdh().equals(rwdh)) {
                str = data.getRwsj();
            }
        }
        return str;
    }

    //根据任务单号查找该单号任务个数
    public static int getNumByRwdh(String rwdh) {
        int num = 0;
        List<Rwmxb> datas = new ArrayList<Rwmxb>();
        RwmxbDal dal = new RwmxbDal(context);
        datas = dal.getData();
        for (Rwmxb data : datas) {
            if (data.getRwdh().equals(rwdh)) {
                num++;
            }
        }
        return num;
    }

    //根据任务单号查找任务总表
    public static Rwzb getRwzbByRwdh(String rwdh) {
        Rwzb rwzb = new Rwzb();
        List<Rwzb> datas = new ArrayList<Rwzb>();
        RwzbDal dal = new RwzbDal(context);
        datas = dal.getData();
        for (Rwzb data : datas) {
            if (data.getRwdh().equals(rwdh)) {
                rwzb = data;
                break;
            }
        }
        return rwzb;
    }

    //通过任务单号找所有的任务明细表
    public static List<Rwmxb> getRwmxbByRwdh(String rwdh) {
        List<Rwmxb> result = new ArrayList<Rwmxb>();
        List<Rwmxb> datas = new ArrayList<Rwmxb>();
        RwmxbDal dal = new RwmxbDal(context);
        datas = dal.getData();
        for (Rwmxb data : datas) {
            if (data.getRwdh().equals(rwdh)) {
                result.add(data);
            }
        }
        return result;
    }

    //检查任务单号下任务明细是否存在拍照----------   0全部未拍照   1有部分拍照 2全部拍照 3其他
    public static int getPhotoState(String rwdh) {
        List<Rwmxb> result = getRwmxbByRwdh(rwdh);
        //统计已经拍照的个数
        int num = 0;
        for (Rwmxb data : result) {
            if (!TextUtils.isEmpty(data.getPzsj()) && data.getPzsj() != null&&!data.getPzsj().equals("null")) {
                num++;
            }
        }
        Log.e("myLog", "任务单号" + rwdh + "的已拍照明细数:" + num);
        if (num == 0)
            return 0;
        else if (num > 0 && num < result.size())
            return 1;
        else if (num == result.size())
            return 2;
        else return 3;
    }

}
