package com.xhb.onlystar.newcardata;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.xhb.onlystar.bean.GetUploadFileUrlReback;
import com.xhb.onlystar.bean.SignInfo;
import com.xhb.onlystar.network.NetworkState;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.DataOperation;
import com.xhb.onlystar.utils.MyUtils;
import com.xhb.onlystar.utils.PhoneInfo;

import org.w3c.dom.Text;

import static com.xhb.onlystar.newcardata.R.id.pzwz;
import static com.xhb.onlystar.newcardata.R.id.view;
import static com.xhb.onlystar.utils.Constant.task;
import static com.xhb.onlystar.utils.LocalDataDb.context;

public class AppSignActivity extends BaseActivity implements AMapLocationListener {

    private Button app_sign_in, app_sign_out;
    private TextView wz;
    private Intent intent = new Intent();
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private AppSignActivity instance;
    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_sign);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initView();
        initEvent();
        getLocation();
    }


    private void initView() {
        wz = (TextView) findViewById(R.id.wz);
        dialog = new ProgressDialog(AppSignActivity.this);
        instance = this;
        app_sign_in = (Button) findViewById(R.id.app_sign_in);
        app_sign_out = (Button) findViewById(R.id.app_sign_out);
    }

    private void initEvent() {

        app_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkState.isNetworkAvailable(getApplicationContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("state", false);
                    bundle.putInt("type", 0);
                    bundle.putString("msg", "网络异常,请检查你的网络");
                    intent.putExtras(bundle);
                    intent.setClass(AppSignActivity.this, AppSignResultActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        Log.e("myLog", Constant.user.getGrdm());
                        Log.e("myLog", Constant.user.getTrueName());
                        Log.e("myLog", new PhoneInfo(getApplicationContext()).getIMEI());
                        Log.e("myLog", "" + Constant.location.getLatitude());
                        Log.e("myLog", "" + Constant.location.getLongitude());
                        SignInfo info = new SignInfo(Constant.user.getGrdm(), Constant.user.getTrueName(), "in",
                                MyUtils.getCurrentLongTimeOfPzsj(), null, new PhoneInfo(getApplicationContext()).getIMEI(),"",
                                Constant.location.getLatitude(), Constant.location.getLongitude());
                        new AppSignAsyncTask(mHandle, 0).execute(info);
                    } catch (NullPointerException e) {
                        Toast.makeText(instance, "暂无定位信息...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        app_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkState.isNetworkAvailable(getApplicationContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("state", false);
                    bundle.putInt("type", 1);
                    bundle.putString("msg", "网络异常,请检查你的网络");
                    intent.putExtras(bundle);
                    intent.setClass(AppSignActivity.this, AppSignResultActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        SignInfo info = new SignInfo(Constant.user.getGrdm(), Constant.user.getTrueName(), "out",
                                MyUtils.getCurrentLongTimeOfPzsj(), null, new PhoneInfo(getApplicationContext()).getIMEI(),"", Constant.location.getLatitude(), Constant.location.getLongitude());
                        new AppSignAsyncTask(mHandle, 1).execute(info);
                    } catch (NullPointerException e) {
                        Toast.makeText(instance, "暂无定位信息...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        wz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

    }

    Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {//初始化数据
                Bundle bundle = new Bundle();
                boolean state = false;
                String data = msg.getData().getString("data");
                GetUploadFileUrlReback result = new Gson().fromJson(data, GetUploadFileUrlReback.class);
                String message = result.getMes();
                Log.e("myLog","-----message"+message);
                if (result.getCode() == 0) {
                    state = true;
                }
                bundle.putBoolean("state", state);
                bundle.putInt("type", 0);
                bundle.putString("msg", message);
                intent.putExtras(bundle);
                intent.setClass(AppSignActivity.this, AppSignResultActivity.class);
                startActivity(intent);
                finish();
            }
            if (msg.what == 1) {
                Bundle bundle = new Bundle();
                boolean state = false;
                String data = msg.getData().getString("data");
                GetUploadFileUrlReback result = new Gson().fromJson(data, GetUploadFileUrlReback.class);
                String message = result.getMes();
                Log.e("myLog","-----message"+message);
                if (result.getCode() == 0) {
                    state = true;
                }
                bundle.putBoolean("state", state);
                bundle.putInt("type", 1);
                bundle.putString("msg", message);
                intent.putExtras(bundle);
                intent.setClass(AppSignActivity.this, AppSignResultActivity.class);
                startActivity(intent);
                finish();
            }
            if (msg.what == 2) {
                if (dialog == null) {
                    return;
                }
                dialog.dismiss();
                AMapLocation loc = (AMapLocation) msg.obj;
                Constant.location = loc;
                if (Constant.location.getErrorCode() == 0) {//定位成功
                    wz.setText(Constant.location.getAddress());
                } else {
                    wz.setText("定位失败,请点击重试...");
                    Toast.makeText(AppSignActivity.this, "定位失败:" + Constant.location.getErrorInfo(), Toast.LENGTH_SHORT).show();
                }
                locationClient.stopLocation();
            }


        }
    };

    public class AppSignAsyncTask extends AsyncTask<SignInfo, Void, GetUploadFileUrlReback> {

        private Handler handle;
        private int type;

        public AppSignAsyncTask(Handler handle, int type) {
            this.handle = handle;
            this.type = type;
        }

        @Override
        protected GetUploadFileUrlReback doInBackground(SignInfo... params) {
            return DataOperation.AppSign(params[0]);
        }

        @Override
        protected void onPostExecute(GetUploadFileUrlReback getUploadFileUrlReback) {
            super.onPostExecute(getUploadFileUrlReback);
            Message message = new Message();
            message.what = type;
            Bundle bundle = new Bundle();
            bundle.putString("data", new Gson().toJson(getUploadFileUrlReback));
            message.setData(bundle);
            handle.sendMessage(message);
        }
    }


    public void getLocation() {
        if (MyUtils.selfPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION, getApplicationContext())) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("正在定位,请稍后...");
            dialog.show();
            locationClient = new AMapLocationClient(getApplicationContext());
            locationClient.setLocationListener(this);
            locationOption = new AMapLocationClientOption();
            // 设置定位模式为低功耗模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            locationOption.setNeedAddress(true);
            // 设置定位参数
            locationClient.setLocationOption(locationOption);
            // 启动定位
            locationClient.startLocation();
        } else {
            ActivityCompat.requestPermissions(instance,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_STATE);

        }
    }


    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandle.obtainMessage();
            msg.obj = loc;
            msg.what = 2;
            mHandle.sendMessage(msg);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length <= 0) {
            return;
        }
        switch (requestCode) {
            case Constant.LOCATION_STATE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AppSignActivity.this, "成功获取定位权限", Toast.LENGTH_SHORT).show();
                    getLocation();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(AppSignActivity.this, "网络定位权限被禁用", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }
}
