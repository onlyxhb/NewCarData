package com.xhb.onlystar.network;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xhb.onlystar.newcardata.MyApplication;

import java.util.ArrayList;

public class NetBroadcastReceiver extends BroadcastReceiver {
    public static ArrayList<NetEventHandler> mListeners = new ArrayList<NetEventHandler>();
    private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("dsc", "dsc");
		if (intent.getAction().equals(NET_CHANGE_ACTION)) {
            MyApplication.mNetWorkState = NetUtil.getNetworkState(context);
            if (mListeners.size() > 0)//通知接口完成加载
                for (NetEventHandler handler : mListeners) {
                    handler.onNetChange();
           }
        }
	}
	
	public interface NetEventHandler {
        public void onNetChange();
    }
}