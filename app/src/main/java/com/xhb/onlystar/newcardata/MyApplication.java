package com.xhb.onlystar.newcardata;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.bean.Base;
import com.xhb.onlystar.bean.LoginReback;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.GetLocation;
import com.xhb.onlystar.utils.LocalDataDb;
import com.xhb.onlystar.utils.MyUtils;

/**
 * Created by onlyStar on 2016/3/30.
 */
public class MyApplication extends Application {
    public static int mNetWorkState;
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor edit;

    @Override
    public void onCreate() {
        super.onCreate();
        new InitDataAsyncTask().execute();
        //蒲公英注册
        PgyCrashManager.register(this);
    }

    private void initData() {
        String rwzb_json = preferences.getString("Rwzb_Base", null);
        if (!TextUtils.isEmpty(rwzb_json)) {
            Constant.Rwzb_Base = new Gson().fromJson(rwzb_json, Base.class);
        }
        String rwmxb_json = preferences.getString("Rwmxb_Base", null);
        if (!TextUtils.isEmpty(rwmxb_json)) {
            Constant.Rwzmxb_Base = new Gson().fromJson(rwmxb_json, Base.class);
        }

        //获取用户信息
        String login_str = preferences.getString("login", "");
        if (!TextUtils.isEmpty(login_str)) {
            Log.e("myLog", "保存的个人信息:" + new Gson().fromJson(login_str, LoginReback.class).getResult().get(0).getGrdm());
            Constant.user = new Gson().fromJson(login_str, LoginReback.class).getResult().get(0);
        }
        //获取登录状态
        Constant.login_state = preferences.getBoolean("login_state", false);
    }

    public class InitDataAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.e("myLog", "数据库中数据量:" + new LocalDataDb(getApplicationContext()).getDataLength());
            preferences = getSharedPreferences("data", MODE_PRIVATE);
            edit = preferences.edit();
            initData();
            return null;
        }
    }
}
