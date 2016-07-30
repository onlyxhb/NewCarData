package com.xhb.onlystar.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.network.NetBroadcastReceiver;
import com.xhb.onlystar.network.NetUtil;
import com.xhb.onlystar.network.NetworkState;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.GetNetDataAsyncTask;

/**
 * 短信推送服务类，在后台长期运行，每个一段时间就向服务器发送一次请求
 *
 * @author jerry
 */
public class PushTaskService extends Service implements NetBroadcastReceiver.NetEventHandler{
    private MyThread myThread;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager manager;
    private PendingIntent pi;
    private RequestQueue mQueue;
    private boolean flag = true;
    private NetBroadcastReceiver netReceiver;//网络状态监测
    private int netType=0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mQueue = Volley.newRequestQueue(getApplicationContext());
        this.myThread = new MyThread();
        netType = NetUtil.getNetworkState(this);
        this.myThread.start();
        PgyCrashManager.register(this);
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        this.flag = false;
        super.onDestroy();
    }
    private class MyThread extends Thread {
        @Override
        public void run() {
            while (flag) {
                if (Constant.login_state && netType!=NetUtil.NETWORN_NONE) {
                    if (netType==NetUtil.NETWORN_WIFI) {
                        try {
                            // 每个10分钟向服务器发送一次请求
                            Thread.sleep(60000 * 10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (netType==NetUtil.NETWORN_MOBILE) {
                        try {
                            // 每个60分钟向服务器发送一次请求
                            Thread.sleep(60000 * 60);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else
                        flag = false;
                    new GetNetDataAsyncTask(getApplicationContext()).execute();
                }
            }
        }
    }


    /**
     * 注册广播
     */
    private void regist() {
        netReceiver = new NetBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(netReceiver, filter);
        NetBroadcastReceiver.mListeners.add(this);
    }
    @Override
    public void onNetChange() {
        if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {//没有网络
            netType=NetUtil.NETWORN_NONE;
        } else if(NetUtil.getNetworkState(this) == NetUtil.NETWORN_WIFI){//wifi网络
            netType=NetUtil.NETWORN_WIFI;
        }else{//手机网络
            netType=NetUtil.NETWORN_MOBILE;
        }
    }
}
