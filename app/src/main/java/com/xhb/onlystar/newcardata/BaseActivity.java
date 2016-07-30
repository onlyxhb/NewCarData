package com.xhb.onlystar.newcardata;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
/*
    //写一个广播的内部类，当收到动作时，结束activity
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this);//这一句必须加上，不然虽然能退出，但报很多错误
            finish();//到这里，多个activity可以关闭掉程序了 但是进程仍然存在，因此加上了下边一句话，可以杀死进程
            android.os.Process.killProcess(android.os.Process.myPid());

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        this.registerReceiver(this.broadcastReceiver, filter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.broadcastReceiver);
    }*/
}