package com.xhb.onlystar.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.Gson;
import com.xhb.onlystar.apater.RwzbListAdapter;
import com.xhb.onlystar.bean.AppResultOfMxb;
import com.xhb.onlystar.bean.AppResultOfZb;
import com.xhb.onlystar.bean.Base;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.bean.Rwzb;
import com.xhb.onlystar.db.RwmxbDal;
import com.xhb.onlystar.db.RwmxbHelper;
import com.xhb.onlystar.db.RwzbDal;
import com.xhb.onlystar.newcardata.MainActivity_hq;
import com.xhb.onlystar.newcardata.MyApplication;

import java.io.PipedOutputStream;
import java.util.List;

import static com.loc.a.t;

/**
 * Created by onlystar on 2016/5/18.
 */
//获取任务总表数据
public class GetNetDataAsyncTask extends AsyncTask<String, Void, AppResultOfZb> {
    private Context context;
    private ProgressDialog dialog;
    private Handler handle;
    private boolean state = true;
    private ImageView loading;
    private String date=MyUtils.getToday();

    public GetNetDataAsyncTask(Context context) {
        this.context = context;
    }

    public GetNetDataAsyncTask(Context context, ProgressDialog dialog, Handler handle) {//type==0获取
        this.context = context;
        this.dialog = dialog;
        this.handle = handle;
    }

    public GetNetDataAsyncTask(Context context, ImageView loading,String date, Handler handle) {//type==0获取
        this.context = context;
        this.loading = loading;
        this.date=date;
        this.handle = handle;
    }

    @Override
    protected AppResultOfZb doInBackground(String... params) {
        AppResultOfZb zbResult = DataOperation.getRwzbData(date);
        if (zbResult == null || zbResult.getResult() == null || zbResult.getCode() != 0) {
            state = false;
            return null;
        } else {
            saveRwzb(zbResult.getResult());
            state = true;
            return zbResult;
        }
    }

    @Override
    protected void onPostExecute(AppResultOfZb appResultOfZb) {
        super.onPostExecute(appResultOfZb);
        //获取任务明细表数据
        if (appResultOfZb == null) {
            state = false;
            Log.e("myLog", "获取任务总表信息失败");
            rebackHandle();
            // Toast.makeText(context, "获取任务总表信息失败", Toast.LENGTH_SHORT).show();
            return;
        } else {
            state = true;
            Log.e("myLog", "获取任务总表信息成功");
            Constant.RWZB_RESULT = appResultOfZb;
            Constant.Rwzb_Base = new Base(Constant.RWZB_RESULT.getCode(), Constant.RWZB_RESULT.getCountPage(), Constant.RWZB_RESULT.getCurrentPage(), Constant.RWZB_RESULT.getMes(), Constant.RWZB_RESULT.getSizePerPage());
            MyApplication.edit.putString("Rwzb_Base", new Gson().toJson(Constant.Rwzb_Base));
            MyApplication.edit.commit();
            new GetMxbDataAsyncTask().execute(appResultOfZb);
        }
    }

    class GetMxbDataAsyncTask extends AsyncTask<AppResultOfZb, Void, List<AppResultOfMxb>> {
        @Override
        protected List<AppResultOfMxb> doInBackground(AppResultOfZb... params) {
            List<AppResultOfMxb> result = DataOperation.getRwmxbData(params[0]);
            for (AppResultOfMxb mxbResult : result) {
                if (mxbResult == null || mxbResult.getResult() == null || mxbResult.getCode() != 0) {
                    state = false;
                    return null;
                }else{
                    saveRwmxb(mxbResult.getResult());
                }
            }
            state = true;
            return result;
        }

        @Override
        protected void onPostExecute(List<AppResultOfMxb> appResultOfMxbs) {
            if (appResultOfMxbs == null) {
                state = false;
                rebackHandle();
                Log.e("myLog", "获取任务明细表信息失败");
                return;
                // Toast.makeText(context, "获取任务明细表信息失败", Toast.LENGTH_SHORT).show();
            } else {
                state = true;
                int index = 0;
                for (AppResultOfMxb mxbResult : appResultOfMxbs) {
                    if (mxbResult.getCode() == 0) {
                        Constant.RWMXB_RESULT = mxbResult;
                        try {
                        }catch (SQLiteCantOpenDatabaseException e){
                            Log.e("myLog", "数据存储失败");
                           // Toast.makeText(MainActivity_hq.instance, "数据存储失败", Toast.LENGTH_SHORT).show();
                        }
                        index += mxbResult.getCountPage();
                        Constant.Rwzmxb_Base = new Base(mxbResult.getCode(), mxbResult.getCountPage(), mxbResult.getCurrentPage(), mxbResult.getMes(), mxbResult.getSizePerPage());
                    } else {
                        Log.e("myLog", "获取任务明细表信息失败");
                    }
                }
                //int localPage = Integer.valueOf(Constant.Rwzb_Base.CurrentPage);//本地
                // int currentPage = Integer.valueOf(Constant.RWZB_RESULT.getCurrentPage());//当前
                int local = MyApplication.preferences.getInt("local", 0);
                Log.e("myLog", "通知比较" + Constant.RWZB_RESULT.getResult().size() + "  " + local);
                if (Constant.RWZB_RESULT.getResult().size() > local) {
                    MyUtils.notification(Constant.RWZB_RESULT.getCountPage() + "个经销商共发布了" + index + "个任务", context);
                }
                MyApplication.edit.putInt("local", Constant.RWZB_RESULT.getResult().size());
                MyApplication.edit.putString("Rwmxb_Base", new Gson().toJson(Constant.Rwzmxb_Base));
                MyApplication.edit.commit();
            }
            rebackHandle();
        }
    }
    private void saveRwmxb(List<Rwmxb> datas) {
        RwmxbHelper db = new RwmxbHelper(context);
        int number = 0;
        db.getWritableDatabase();
        String imei ="";
        if (MyUtils.selfPermissionGranted("android.permission.READ_PHONE_STATE", context)) {
            imei = new PhoneInfo(context).getIMEI();
        }else{
            Message msg=new Message();
            Bundle bundle=new Bundle();
            bundle.putString("data","没有获取手机IMEI的权限");
            msg.what=1;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
        Log.e("myLog","当前手机的IMEI:"+imei);
        for (Rwmxb task : datas) {
            task.setSjID(imei);
            try {
                RwmxbDal dal = new RwmxbDal(context);
                if (!dal.isExit(task.getRwdh(), task.getClsbm())) {//本地数据不存在
                    if(!TextUtils.isEmpty(task.getPzsj())&&!task.getPzsj().equals("")){//未拍照
                       // Log.e("myLog","rwmxb------本地无数据-----4  "+"pzsj:"+task.getPzsj()+" pzzt:"+task.getPzzt()+" pic:"+task.getPicPath());
                        task.setPzzt("4");
                    }else{
                        //Log.e("myLog","rwmxb------本地无数据-----2  "+"pzsj:"+task.getPzsj()+" pzzt:"+task.getPzzt()+" pic:"+task.getPicPath());
                        task.setPzzt("2");
                    }
                    dal.addData(task);
                    number++;
                   // Log.e("myLog", "保存---任务明细表数据成功！");
                } else{//
                    if(!TextUtils.isEmpty(task.getPzsj())&&!task.getPzsj().equals("")){
                       // Log.e("myLog","rwmxb------本地有数据-----4  "+"pzsj:"+task.getPzsj()+" pzzt:"+task.getPzzt()+" pic:"+task.getPicPath());
                        task.setPzzt("4");
                        dal.UpdateData(task);
                        // Log.e("myLog", "更新---任务明细表数据成功！");
                    }else{
                       // Log.e("myLog","rwmxb------本地有数据-----无"+"pzsj:"+task.getPzsj()+" pzzt:"+task.getPzzt()+" pic:"+task.getPicPath());
                        // Log.e("myLog", "任务明细表数据已存在...");
                    }
                }
            } catch (Exception e) {
                Log.e("myLog", "任务明细表数据保存失败！");
            }
        }
    }


    private void saveRwzb(List<Rwzb> datas) {
        RwmxbHelper db = new RwmxbHelper(context);
        int number = 0;
        db.getWritableDatabase();
        for (Rwzb task : datas) {
            try {
                RwzbDal dal = new RwzbDal(context);
                if (!dal.isExit(task.getRwdh())) {
                    dal.addData(task);
                    number++;
                    Log.e("myLog", "保存任务总表数据成功！");
                } else{
                    if(!task.getPzzt().equals(dal.getpzzt(task.getRwdh()))){
                        dal.UpdateData(task);
                        Log.e("myLog", "任务总表数据已存在: " + task);
                    }else{
                        Log.e("myLog", "任务状态相同，不需要更新 " + task);
                    }
                }
            } catch (Exception e) {
                Log.e("myLog", "保存任务总表失败！");
            }
        }
    }

    private void rebackHandle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("state", state);
        if(loading!=null&&handle!=null){
            loading.setVisibility(View.GONE);
            Message message = new Message();
            message.what = 1;
            message.setData(bundle);
            handle.sendMessage(message);
        }else if (dialog != null && handle != null) {
            dialog.dismiss();
            Message message = new Message();
            message.what = 1;
            message.setData(bundle);
            handle.sendMessage(message);
        } else {
            Log.e("myLog", "不满足条件");
        }
    }



    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                String data=msg.getData().getString("data");
                if(data!=null&&data!=""){
                    Toast.makeText(context, data, Toast.LENGTH_LONG).show();
                }
            }
        }

    };
}
