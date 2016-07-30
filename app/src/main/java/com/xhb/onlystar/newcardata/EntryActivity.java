package com.xhb.onlystar.newcardata;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.push.PushTaskService;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.MyUtils;


public class EntryActivity extends BaseActivity {
    private ImageView welcomeImg = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        welcomeImg = (ImageView) this.findViewById(R.id.truck_imag);
        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
        anima.setDuration(1000);// 设置动画显示时间
        welcomeImg.startAnimation(anima);
        anima.setAnimationListener(new AnimationImpl());
        PgyCrashManager.register(this);
    }

    private class AnimationImpl implements AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            welcomeImg.setBackgroundResource(R.mipmap.truck);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            try {
                initPermission(); // 动画结束后跳转到别的页面
            }catch (ActivityNotFoundException e){
                Toast.makeText(getApplicationContext(), "出现异常...", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }
    private void initPermission() {
        //如果两个权限都有,进入主界面
        if (MyUtils.selfPermissionGranted(Manifest.permission.READ_PHONE_STATE, getApplicationContext())&&
                MyUtils.selfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, getApplicationContext())){
            skip();
        }// //获取手机信息权限
        else if (!MyUtils.selfPermissionGranted(Manifest.permission.READ_PHONE_STATE, getApplicationContext())) {
            ActivityCompat.requestPermissions(EntryActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    Constant.READ_PHONE_STATE);
        } //获取读取SD卡信息权限
       else if (!MyUtils.selfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, getApplicationContext())) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constant.READ_SD_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length<=0){ return;}
        if (requestCode == Constant.READ_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initPermission();
                //Toast.makeText(MainActivity_hq.this, "成功获取读取手机信息权限", Toast.LENGTH_SHORT).show();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(EntryActivity.this, "获取读取手机信息权限被禁用", Toast.LENGTH_SHORT).show();
                initPermission();
            }
        }
        if (requestCode == Constant.READ_SD_STATE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initPermission();
                //Toast.makeText(MainActivity_hq.this, "成功获取读取SD卡信息权限", Toast.LENGTH_SHORT).show();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(EntryActivity.this, "读取SD卡信息权限被禁用", Toast.LENGTH_SHORT).show();
                initPermission();
            }
        }
    }


    private void skip() {
        Intent intent = new Intent();
        Log.e("myLog", "登陆状态:" + Constant.login_state);
        if (Constant.login_state) {
            intent.setClass(this, MainActivity_hq.class);
        } else {
            intent.setClass(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}