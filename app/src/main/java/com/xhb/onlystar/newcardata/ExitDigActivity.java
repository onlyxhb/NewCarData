package com.xhb.onlystar.newcardata;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.push.PushTaskService;
import com.xhb.onlystar.utils.DataCleanManager;

import java.io.File;

public class ExitDigActivity extends BaseActivity {

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);
        layout = (LinearLayout) findViewById(R.id.exit_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        PgyCrashManager.register(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    public void exitbutton1(View v) {
        this.finish();
    }

    public void exitbutton0(View v) {
//第一种方法
//        DataCleanManager.clearAllCache(getApplicationContext());
//        Intent intent=new Intent();
//        intent.setClass(ExitDigActivity.this, PushTaskService.class);
//        stopService(intent);
//        this.finish();
//        MainActivity_hq.instance.finish();
//第二种方法
//        int currentVersion = android.os.Build.VERSION.SDK_INT;
//        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
//            Intent startMain = new Intent(Intent.ACTION_MAIN);
//            startMain.addCategory(Intent.CATEGORY_HOME);
//            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(startMain);
//            System.exit(0);
//        } else {// android2.1
//            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//            am.restartPackage(getPackageName());
//        }
//第三种方法
        Intent intent = new Intent();
        intent.setClass(ExitDigActivity.this, PushTaskService.class);
        stopService(intent);
//        intent.setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); // 说明动作
//        this.sendBroadcast(intent);// 该函数用于发送广播
        finish();
        DataCleanManager.deleteAllFiles(new File(DataCleanManager.getRootPath(MainActivity_hq.instance)+"/CarData/cache/"));//所有的图片成功删除
        super.finish();
        MainActivity_hq.instance.finish();
    }
}
