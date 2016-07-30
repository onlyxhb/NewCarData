package com.xhb.onlystar.newcardata;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.xhb.onlystar.fragment.CarDataFragment;
import com.xhb.onlystar.fragment.OtherFragment;
import com.xhb.onlystar.fragment.RwzbFragment;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.MyUtils;
import com.xhb.onlystar.widget.caldendar.ImageAndTextView;

/**
 * Created by 何清 on 2016/5/13 0013.
 *
 * @description
 */
public class MainActivity_hq extends FragmentActivity
        implements View.OnClickListener {

    private ImageAndTextView[] mImageAndTextViews = new ImageAndTextView[3];
    private ImageAndTextView mPreSelected = null;

    private Fragment[] mFragments = new Fragment[3];
    private FragmentManager mFragmentManager;
    private Fragment mPreFragment;

    public static MainActivity_hq instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_hq);
        instance = this;
        mFragmentManager = getSupportFragmentManager();
        mFragments[0] = new RwzbFragment();
        mFragments[1] = new CarDataFragment();
        mFragments[2] = new OtherFragment();
        mFragmentManager.beginTransaction().add(R.id.fragment_container, mFragments[0]).commit();
        mPreFragment = mFragments[0];
        initView();
        PgyCrashManager.register(this);
        checkForUpdate();
    }

    private void initView() {
        mImageAndTextViews[0] = (ImageAndTextView) findViewById(R.id.bottom_bar_first);
        mImageAndTextViews[1] = (ImageAndTextView) findViewById(R.id.bottom_bar_second);
        mImageAndTextViews[2] = (ImageAndTextView) findViewById(R.id.bottom_bar_third);
        mImageAndTextViews[0].setSelected(true);
        mPreSelected = mImageAndTextViews[0];
        for (int i = 0; i < mImageAndTextViews.length; i++) {
            mImageAndTextViews[i].setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bottom_bar_first:
                changeBottomBarState(v);
                switchFragment(mFragments[0]);
                break;
            case R.id.bottom_bar_second:
                changeBottomBarState(v);
                switchFragment(mFragments[1]);
                break;
            case R.id.bottom_bar_third:
                changeBottomBarState(v);
                switchFragment(mFragments[2]);
                break;
        }
    }

    private void changeBottomBarState(View v) {
        if (v.getId() != mPreSelected.getId()) {
            mPreSelected.setSelected(false);
            mPreSelected = ((ImageAndTextView) v);
            mPreSelected.setSelected(true);
        }
    }

    private void switchFragment(Fragment to) {
        if (mPreFragment != to) {
            if (!to.isAdded()) {    // 先判断是否被add过
                mFragmentManager.beginTransaction().hide(mPreFragment)
                        .add(R.id.fragment_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                mFragmentManager.beginTransaction()
                        .hide(mPreFragment).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mPreFragment = to;
        }
    }


    private void checkForUpdate() {
        PgyUpdateManager.register(MainActivity_hq.this,
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        new AlertDialog.Builder(MainActivity_hq.this)
                                .setTitle("车辆监管升级提示")
                                .setMessage("发现新版本,是否立即更新")
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                startDownloadTask(
                                                        MainActivity_hq.this,
                                                        appBean.getDownloadURL());
                                            }
                                        })
                                .setPositiveButton("取消",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                .show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), ExitDigActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}