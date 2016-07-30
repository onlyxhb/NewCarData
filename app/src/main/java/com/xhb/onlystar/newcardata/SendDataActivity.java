package com.xhb.onlystar.newcardata;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.bean.LoginReback;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.bean.Rwzb;
import com.xhb.onlystar.bean.Save_pzrwTjReback;
import com.xhb.onlystar.bean.UploadInfo;
import com.xhb.onlystar.db.PictureDal;
import com.xhb.onlystar.db.RwmxbDal;
import com.xhb.onlystar.network.NetUtil;
import com.xhb.onlystar.network.NetworkState;
import com.xhb.onlystar.utils.Bimp;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.DataCleanManager;
import com.xhb.onlystar.utils.DataOperation;
import com.xhb.onlystar.utils.GetLocation;
import com.xhb.onlystar.utils.GetNetImgAsynctask;
import com.xhb.onlystar.utils.LocalDataDb;
import com.xhb.onlystar.utils.MyUtils;
import com.xhb.onlystar.utils.PhoneInfo;
import com.xhb.onlystar.utils.Photo;
import org.apache.commons.codec.binary.Base64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.R.id.list;
import static com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.L;
import static com.xhb.onlystar.newcardata.R.id.id_link;
import static com.xhb.onlystar.newcardata.R.id.imageviewer;
import static com.xhb.onlystar.newcardata.R.id.num;
import static com.xhb.onlystar.utils.Constant.location;
import static com.xhb.onlystar.utils.GetNetImgAsynctask.checkURL;
import static com.xhb.onlystar.utils.LocalDataDb.context;

public class SendDataActivity extends BaseActivity implements View.OnClickListener, AMapLocationListener {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView rwdh, rwrq, cx, clsbm, zt, pzzdm, pzsj, pzwz, commite_text;
    private EditText bz;
    private ImageView view_expand;
    private LinearLayout expand_info;
    private boolean expand = false;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private View parentView;
    private String filePath;//记录图片路径
    private RelativeLayout to_save, to_upLoad, to_commit;
    public int max = 0;//最大图片数
    private Rwzb localRwzb = new Rwzb();
    private ProgressDialog dialog = null;
    private Rwmxb task = Constant.task;
    int index = 0, total = 0;//目前成功上传的图片数    已上传的图片数
    private Intent intent = new Intent();
    private SendDataActivity instance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断 activity被销毁后 有没有数据被保存下来
        if (savedInstanceState != null) {
            TAKE_PICTURE = savedInstanceState.getInt("TAKE_PICTURE", TAKE_PICTURE);
            filePath = savedInstanceState.getString("filePath", filePath);
            task = new Gson().fromJson(savedInstanceState.getString("task", new Gson().toJson(task)), Rwmxb.class);
            Constant.task = task;
        } else {
            Log.i("myLog", "savedInstanceState为空");
        }
        parentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
        setContentView(parentView);
        initView();
        initData();
        initEvent();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        PgyCrashManager.register(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("myLog", "竖屏");
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("myLog", "横屏");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("TAKE_PICTURE", TAKE_PICTURE);
        outState.putString("filePath", filePath);
        outState.putString("task", new Gson().toJson(task));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Constant.task = new Gson().fromJson(MyApplication.preferences.getString("task", null), Rwmxb.class);
        task = Constant.task;
        TAKE_PICTURE = MyApplication.preferences.getInt("TAKE_PICTURE", 0);
        filePath = MyApplication.preferences.getString("filePath", null);
    }

    private void initView() {
        if (task == null || task.getPzzt() == null || task.getPzzt().equals("null")) {
            Constant.task = new Gson().fromJson(MyApplication.preferences.getString("task", null), Rwmxb.class);
            task = Constant.task;
        } else {
            MyApplication.edit.putString("task", new Gson().toJson(task));
            MyApplication.edit.commit();
        }
        dialog = new ProgressDialog(SendDataActivity.this);
        instance = this;
        String imei = new PhoneInfo(getApplicationContext()).getIMEI();
        Log.e("myLog", "手机IMEI:" + imei);
        task.setSjID(imei);
        localRwzb = new LocalDataDb(getApplicationContext()).getRwzbByRwdh(task.getRwdh());
        initGridView();
        to_save = (RelativeLayout) findViewById(R.id.to_save);
        to_upLoad = (RelativeLayout) findViewById(R.id.to_upload);
        to_commit = (RelativeLayout) findViewById(R.id.to_commit);
        view_expand = (ImageView) findViewById(R.id.view_expand);
        expand_info = (LinearLayout) findViewById(R.id.expand_info);
        commite_text = (TextView) findViewById(R.id.commite_text);
        rwdh = (TextView) findViewById(R.id.rwdh);
        rwrq = (TextView) findViewById(R.id.rwrq);
        cx = (TextView) findViewById(R.id.cx);
        clsbm = (TextView) findViewById(R.id.clsbm);
        zt = (TextView) findViewById(R.id.zt);
        pzzdm = (TextView) findViewById(R.id.pzzdm);
        pzsj = (TextView) findViewById(R.id.pzsj);
        pzwz = (TextView) findViewById(R.id.pzwz);
        bz = (EditText) findViewById(R.id.bz);
        rwdh.setText(task.getRwdh());
        rwrq.setText(new LocalDataDb(getApplicationContext()).getTimeByRwdh(task.getRwdh()));
        if (!TextUtils.isEmpty(task.getCx())) {
            cx.setText(task.getCx());
        }
        clsbm.setText(task.getClsbm());
        pzzdm.setText(task.getPzr());
        if (!TextUtils.isEmpty(task.getPzsj()) && task.getPzsj() != null && !task.getPzsj().equals("null")) {
            pzsj.setText(task.getPzsj());
        }
        Log.e("myLog", " task:" + task + "  pzzt:" + task.getPzzt());
        if (!task.getPzzt().equals("2")) {
            pzwz.setText("北纬" + task.getLat() + "  东经" + task.getLng());
        }
        if (LocalDataDb.getRwzbByRwdh(task.getRwdh()).getPzzt().equals("1")) {
            commite_text.setText("已提交");
        }
        updateState(task.getPzzt());
    }

    Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (dialog == null) {
                dialog = new ProgressDialog(SendDataActivity.this);
            }
            if (msg.what == 0) {
                dialog.dismiss();
                boolean state = bundle.getBoolean("state");
                if (state) {
                    LocalDataDb.UpdateMxbState(task.getRwdh(), task.getClsbm(), 3);
                    LocalDataDb.UpdateGps(task.getRwdh(), task.getClsbm(), task.getLat(), task.getLng());
                    Constant.isRefreshMxb = true;
                    task.setPzzt("3");
                    updateState("3");
                    Toast.makeText(SendDataActivity.this, "图片保存成功", Toast.LENGTH_SHORT).show();
                }
            }
            if (msg.what == 1) {
                int num = bundle.getInt("num");
                total++;
                boolean state = bundle.getBoolean("state");
                if (state) {
                    index++;
                    if (total == Bimp.drr.size()) {
                        dialog.dismiss();
                        LocalDataDb.UpdateMxbState(task.getRwdh(), task.getClsbm(), 4);
                        updateState("4");
                        task.setPzzt("4");
                        Constant.isRefreshMxb = true;
                        Log.e("myLog", "成功上传" + index + "张图片");
                        LocalDataDb.UpdateGps(task.getRwdh(), task.getClsbm(), task.getLat(), task.getLng());
                        Toast.makeText(SendDataActivity.this, "成功上传" + index + "张图片", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        dialog.setMessage("正在上传" + (index + 1) + "/" + Bimp.drr.size());
                        Log.e("myLog", " index:" + index + " total:" + total + "   size:" + Bimp.drr.size());
                        createUpLoad(num + 1);
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(SendDataActivity.this, "上传第" + (num + 1) + "张图片时图片失败...", Toast.LENGTH_SHORT).show();
                }
            }
            if (msg.what == 2) {
                if (dialog == null) {
                    return;
                }
                dialog.dismiss();
                String result = bundle.getString("data");
                Save_pzrwTjReback reback = new Gson().fromJson(result, Save_pzrwTjReback.class);
                if (reback == null || reback.equals("")) {
                    Toast.makeText(SendDataActivity.this, "连接服务器失败,请稍后再试...", Toast.LENGTH_SHORT).show();
                } else if (reback.getCode().equals("0")) {//提交成功
                    Constant.isRefreshZb = true;
                    LocalDataDb.UpdateZbState(task.getRwdh(), 1);
                    LocalDataDb.UpdateGps(task.getRwdh(), task.getClsbm(), task.getLat(), task.getLng());
                    Toast.makeText(SendDataActivity.this, "数据提交成功", Toast.LENGTH_SHORT).show();
                    commite_text.setText("已提交");
                } else
                    Toast.makeText(SendDataActivity.this, "错误" + reback.getCode() + ":" + reback.getMes(), Toast.LENGTH_LONG).show();
            }
            if (msg.what == 3) {
                if (dialog == null) {
                    return;
                }
                dialog.dismiss();
                AMapLocation loc = (AMapLocation) msg.obj;
                location = loc;
                if (location.getErrorCode() == 0) {//定位成功
                    float lat = (float) location.getLatitude();
                    float lon = (float) location.getLongitude();
                    Constant.task.setLat(lat);
                    Constant.task.setLng(lon);
                    task.setLat(lat);
                    task.setLng(lon);
                    pzwz.setText("北纬" + lat + "  东经" + lon);
                } else {
                    pzwz.setText("定位失败,请点击重试...");
                    Toast.makeText(SendDataActivity.this, "定位失败:" + location.getErrorInfo(), Toast.LENGTH_SHORT).show();
                }
                locationClient.stopLocation();
            }
            if (msg.what == 4) {
                if (dialog == null) {
                    return;
                }
                dialog.dismiss();
                String result = bundle.getString("reback");
                List<String> mPath = MyUtils.getListByJson(result);
                Bimp.drr.clear();
                for (String str : mPath) {
                    Bimp.drr.add(str);
                }
                adapter.update();
            }
        }
    };

    private void updateState(String s) {
        switch (s) {
            case "2": {
                zt.setText("未拍照");
                break;
            }
            case "3": {
                zt.setText("未上传");
                break;
            }
            case "4": {
                zt.setText("已上传");
                break;
            }
            case "5": {
                zt.setText("打回重拍");
                break;
            }
            case "6": {
                zt.setText("已通过");
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog = null;
    }

    private void initData() {
        Bimp.drr.clear();
        if (task.getPzzt().equals("2")) {
            getLocation();
        }
        Log.e("myLog", "当前拍照状态:" + task.getPzzt());
        if (task == null || task.getPzzt() == null || task.getPzzt().equals("null")) {
            return;
        }
        if (task.getPzzt().equals("3")) {
            List<String> path = readPicturePath(task.getRwdh(), task.getClsbm());
            Log.e("myLog", "数据库的path数:" + path.size());
            int delete = getImg(path);
            if (delete != 0) {
                Toast.makeText(SendDataActivity.this, "有" + delete + "张图片可能已被删除...", Toast.LENGTH_SHORT).show();
            }
        } else if (task.getPzzt().equals("4")) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("正在加载网络图片，请稍后");
            dialog.show();
            if (NetworkState.isNetworkAvailable(getApplicationContext())) {
                new GetNetImgAsynctask(task, dialog, mHandle, getApplicationContext()).execute();
            } else {
                Toast.makeText(instance, "获取网络图片失败，请检查你的网络", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private int getImg(List<String> path) {
        int delete = 0;
        for (String mPath : path) {
            File file = new File(mPath);
            Log.e("myLog", "path----------->" + mPath);
            if (file.exists()) {
                Bimp.drr.add(mPath);
            } else {
                delete++;
                continue;
            }
        }
        return delete;
    }

    private void initEvent() {
        to_save.setOnClickListener(this);
        to_upLoad.setOnClickListener(this);
        to_commit.setOnClickListener(this);

        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Log.e("myLog", "------>arg2" + arg2 + "-------->Bimp.drr.size" + Bimp.drr.size());
                if (arg2 == Bimp.drr.size()) {
                    if (MyUtils.selfPermissionGranted(Manifest.permission.CAMERA, getApplicationContext())) {
                        photo();
                    } else {
                        ActivityCompat.requestPermissions(instance,
                                new String[]{Manifest.permission.CAMERA},
                                Constant.CAMERA_STATE);
                    }
                } else {
                    intent.putExtra("picPath", Bimp.drr.get(arg2));
                    intent.setClass(SendDataActivity.this, ImageViewerActivity.class);
                    startActivity(intent);
                }
            }
        });

        pzwz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        view_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expand) {//true 不展开  false展开
                    view_expand.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.view_expand));
                    expand_info.setVisibility(View.GONE);
                    expand = false;
                } else {
                    view_expand.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.view_normal));
                    expand_info.setVisibility(View.VISIBLE);
                    expand = true;
                }
            }
        });
    }

    private void initGridView() {
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_save: {
                if (task.getPzzt().equals("3")) {
                    DialogTip("已经保存过照片，确定要覆盖吗？", 0);
                } else {
                    tosaveFun();
                }
            }
            break;
            case R.id.to_upload:
                if (task.getPzzt().equals("4")) {
                    DialogTip("已经上传过该任务照片，确定继续？", 1);
                } else {
                    touploadFun();
                }
                break;
            case R.id.to_commit:
                //提交上报
                if (LocalDataDb.getRwzbByRwdh(task.getRwdh()).getPzzt().equals("1")) {
                    Toast.makeText(instance, "此任务已提交过", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (task.getPzzt().equals("4")) {
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setMessage("正在提交...");
                    dialog.show();
                    String time = MyUtils.getCurrentLongTimeOfPzsj();
                    localRwzb.setPzsj(time);
                    task.setBz(bz.getText().toString().trim());
                    commit();
                } else {
                    Toast.makeText(SendDataActivity.this, "请先上传照片...", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void save() {
        new Thread() {
            @Override
            public void run() {
                deletePicture(task.getRwdh(), task.getClsbm());
                List<String> picPath = new ArrayList<String>();
                for (int i = 0; i < Bimp.drr.size(); i++) {
                    picPath.add(Bimp.drr.get(i));
                }
                savePicturePath(task.getRwdh(), task.getClsbm(), new Gson().toJson(picPath));
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putBoolean("state", true);
                message.setData(bundle);
                message.what = 0;
                mHandle.sendMessage(message);
            }
        }.start();
    }

    private void createUpLoad(final int num) {
        new Thread() {
            @Override
            public void run() {
                try {
                    boolean state = false;
                    try {
                        File tmp = new File(Bimp.drr.get(num));
                        if (!tmp.exists()) {
                            state = false;
                        }
                        Log.e("myLog", "长传的图片名:" + tmp.getAbsolutePath() + " " + Bimp.drr.get(num));
                        FileInputStream fs = new FileInputStream(tmp);
                        //Log.e("myLog","创建上传任务的路径"+tmp.getAbsolutePath()+" 长度"+tmp.length());
                        long filelen = tmp.length();
                        Log.e("myLog", "上传图片的长度:" + filelen);
                        UploadInfo uploadInfo = DataOperation.createUpload2(task, tmp.getAbsolutePath(), filelen);
                        if (uploadInfo.getCode() == 0) {
                            Log.e("myLog", "分块图片的块数:" + uploadInfo.getBlockCount());
                            for (int i = 0; i < uploadInfo.getBlockCount(); i++) {
                                byte[] data = new byte[uploadInfo.getBlockSize()];
                                // 最后一块
                                if (uploadInfo.getBlockIndex() + 1 == uploadInfo.getBlockCount()) {
                                    long lastlen = filelen - uploadInfo.getBlockSize() * uploadInfo.getBlockIndex();
                                    data = new byte[(int) lastlen];
                                }
                                try {
                                    fs.read(data, 0, data.length);
                                    Log.e("myLog", "读取长度:" + data.length);
                                    String mData = new String(Base64.encodeBase64(data));
                                    uploadInfo = DataOperation.beginUpload2(uploadInfo, mData, "");
                                    if (uploadInfo.getCode() != 0) {
                                        state = false;
                                        Log.e("myLog", "上传失败");
                                        break;
                                    } else {
                                        uploadInfo.setBlockIndex(uploadInfo.getBlockIndex() + 1);
                                        if (uploadInfo.getBlockIndex() == uploadInfo.getBlockCount()) {
                                            Log.e("myLog", "上传成功:" + "blockIndex：" + uploadInfo.getBlockIndex());
                                            state = true;
                                        }
                                    }
                                } catch (IOException e) {
                                    state = false;
                                    e.printStackTrace();
                                }
                            }
                        } else state = false;
                    } catch (IOException e) {
                        state = false;
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("num", num);
                    bundle.putBoolean("state", state);
                    message.setData(bundle);
                    message.what = 1;
                    mHandle.sendMessage(message);
                } catch (NullPointerException e) {
                    Toast.makeText(SendDataActivity.this, "空指针异常...", Toast.LENGTH_SHORT).show();
                }
            }

        }.start();
    }

    private void commit() {
        //任务提交
        new Thread() {
            @Override
            public void run() {
                List<Rwmxb> rwmxb = new ArrayList<Rwmxb>();
                rwmxb.add(task);
                Save_pzrwTjReback save_pzrwTjReback = DataOperation.Save_pzrwTj(localRwzb, rwmxb);
                Message message = new Message();
                Bundle bundle = new Bundle();
                String result = new Gson().toJson(save_pzrwTjReback);
                bundle.putString("data", result);
                message.setData(bundle);
                message.what = 2;
                mHandle.sendMessage(message);
            }
        }.start();
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandle.obtainMessage();
            msg.obj = loc;
            msg.what = 3;
            mHandle.sendMessage(msg);
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

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.drr.size() == 8) {
                return 8;
            }
            return (Bimp.drr.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.drr.size()) {
                holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
                if (position == 8) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                try {
                    Log.e("myLog", position + " " + Bimp.drr.size() + "   path:" + Bimp.drr.get(position));
                    Bitmap bitmap = Photo.getBitmapFromPath(Bimp.drr.get(position));
                    if (bitmap != null) {
                        Matrix matrix = new Matrix(); //接收图片之后放大 1.2倍
                        matrix.postScale(1.2f, 1.2f);
                        Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                bitmap.getHeight(), matrix, true);
                        holder.image.setImageBitmap(bit);
                    }
                } catch (OutOfMemoryError e) {
                    Log.e("myLog", "内存溢出");
                }
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (max == Bimp.drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    public void back(View view) {
        onBackPressed();
    }

    private static int TAKE_PICTURE = 0x000001;

    public void photo() {
        if(Constant.location==null||Constant.location.getErrorCode()!=0){
            Toast.makeText(instance, "你还没有定位或者定位失败...", Toast.LENGTH_SHORT).show();
            return ;
        }

        if (MyUtils.selfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, getApplicationContext())) {
            filePath = DataCleanManager.getAppPath(getApplicationContext());
            File tmp = new File(filePath);
            try {
                if (!tmp.exists()) {
                    tmp.createNewFile();
                }
                //启动相机获取原图
                Log.e("myLog", "拍照前的filePath:----0" + filePath);
                MyApplication.edit.putInt("TAKE_PICTURE", TAKE_PICTURE);
                MyApplication.edit.putString("filePath", filePath);
                MyApplication.edit.commit();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Log.e("myLog", "拍照前的filePath:----1" + filePath);
      /*  让拍照的图片保存在指定目录下
          使用android.net URI-----uri指向指定的文件对象*/
                Uri uri = Uri.fromFile(tmp);
        /*给intent增加一个参数,通过MediaStore.EXTRA_OUTPUT对系统拍照后图片保存路径进行修改
         更改系统图片的默认路径*/
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                try {
                    startActivityForResult(intent, TAKE_PICTURE);
                } catch (ActivityNotFoundException e) {
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    try {
                        startActivityForResult(intent, TAKE_PICTURE);
                    } catch (ActivityNotFoundException e1) {
                        Toast.makeText(this, "打开相机应用程序失败!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (NullPointerException e) {
                Toast.makeText(SendDataActivity.this, "空指针异常...", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("myLog", "创建文件失败...");
                e.printStackTrace();
            }
        } else {
            Toast.makeText(SendDataActivity.this, "没有读写SD卡信息权限", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TAKE_PICTURE = MyApplication.preferences.getInt("TAKE_PICTURE", 0);
        filePath = MyApplication.preferences.getString("filePath", null);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PICTURE) {
                Log.e("myLog", "拍照后的filePath:----1" + filePath);
                if (filePath == null || TAKE_PICTURE == 0) {
                    filePath = MyApplication.preferences.getString("filePath", null);
                    dialog = new ProgressDialog(SendDataActivity.this);
                    dialog.setMessage("捕获异常，正在重新加载页面，请稍后...");
                    dialog.show();
                } else {
                    try {
                        Log.e("myLog", "拍照后的filePath:----2" + filePath);
                        if (Bimp.drr.size() >= 8) {
                            Toast.makeText(SendDataActivity.this, "你最多可拍照8张相片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String time = MyUtils.getCurrentLongTimeOfPzsj();
                        savePzsj(time);
                        pzsj.setText(time);
                        task.setPzsj(time);
                        Bimp.drr.add(filePath);
                        adapter.update();
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(SendDataActivity.this, "内存溢出...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            } else {
                Toast.makeText(SendDataActivity.this, "获取资源失败!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //判断目录是否存在
    public static boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            } else {
                return false;

            }
        }
        return true;
    }

    //删除所有的图片
    private void deletePicture(String rwdh, String clsbm) {
        PictureDal dal = new PictureDal(getBaseContext());
        dal.delete(rwdh, clsbm);
    }

    //保存拍照时间
    private void savePzsj(String time) {
        RwmxbDal dal = new RwmxbDal(getBaseContext());
        dal.UpdatePzsj(task.getRwdh(), task.getClsbm(), time);
    }

    //保存图片
    private void savePicturePath(String rwdh, String clsbm, String picPath) {
        PictureDal dal = new PictureDal(getBaseContext());
        Long result = dal.insert(rwdh, clsbm, picPath);
        Log.e("myLog", "新添记录的行号:" + result);
    }

    //从数据库中读取图片
    private List<String> readPicturePath(String rwdh, String clsbm) {
        try {
            PictureDal dal = new PictureDal(getBaseContext());
            return dal.getPicPath(rwdh, clsbm);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length <= 0) {
            return;
        }
        switch (requestCode) {
            case Constant.CAMERA_STATE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SendDataActivity.this, "已获取拍照权限", Toast.LENGTH_SHORT).show();
                    photo();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(SendDataActivity.this, "相机权限被禁用", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case Constant.LOCATION_STATE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SendDataActivity.this, "成功获取定位权限", Toast.LENGTH_SHORT).show();
                    getLocation();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(SendDataActivity.this, "网络定位权限被禁用", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        Bimp.drr.clear();
        super.onBackPressed();
    }


    public void tosaveFun() {
        //保存图片至数据库
        Log.e("myLog", "保存图片的大小:" + Bimp.drr.size());
        if (Bimp.drr.size() <= 0) {
            Toast.makeText(SendDataActivity.this, "图片为空，无法保存...", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("正在保存图片...");
        dialog.show();
        save();
    }

    public void touploadFun() {
        //重新加载图片
        if (Bimp.drr.size() > 0) {
            int size = Bimp.drr.size();
            Log.e("myLog", "200*200图片的大小:" + Bimp.drr.size());
            index = 0;
            total = 0;
            task.setBz(bz.getText().toString().trim());
            if (dialog == null) {
                dialog = new ProgressDialog(SendDataActivity.this);
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("正在上传1/" + Bimp.drr.size());
            dialog.show();
            //循环上传图片
            createUpLoad(0);
        } else {
            Toast.makeText(SendDataActivity.this, "图片为空，无法上传...", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private void DialogTip(String tip, final int index) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView message = (TextView) view.findViewById(R.id.message);
        title.setText("小提示");
        message.setText(tip);
        Button ok = (Button) view.findViewById(R.id.ok);
        Button no = (Button) view.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (index == 0) {
                    tosaveFun();
                } else if (index == 1) {
                    touploadFun();
                }
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
