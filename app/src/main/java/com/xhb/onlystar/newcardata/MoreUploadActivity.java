package com.xhb.onlystar.newcardata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.apater.BaseAdapter;
import com.xhb.onlystar.apater.UpLoadListAdapter;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.bean.UploadInfo;
import com.xhb.onlystar.db.PictureDal;
import com.xhb.onlystar.db.RwmxbDal;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.DataOperation;
import com.xhb.onlystar.utils.DividerItemDecoration;
import com.xhb.onlystar.utils.LocalDataDb;
import com.xhb.onlystar.utils.Photo;
import com.xhb.onlystar.utils.UploadUtil;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MoreUploadActivity extends BaseActivity {
    @ViewInject(R.id.recycler_view_upload)
    private RecyclerView mRecyclerview;
    @ViewInject(R.id.refresh_layout_upload)
    private MaterialRefreshLayout mRefreshLayout;
    @ViewInject(R.id.bottom_upload)
    public static Button bottom_upload;
    @ViewInject(R.id.upload_select)
    private CheckBox upload_select;
    @ViewInject(R.id.text_select)
    private TextView text_select;
    private UpLoadListAdapter adapter;
    public static List<Rwmxb> data = new ArrayList<Rwmxb>();
    private Intent intent = new Intent();
    //上传图片的地址
    private List<String> picData = new ArrayList<>();
    public ArrayList<Bitmap> ImageData = new ArrayList<Bitmap>();
    public static Map<Integer, Boolean> isCheckedMap;
    private ProgressDialog dialog = null;
    File tmp = null;
    private String filePath;//记录图片路径
    int index = 0;
    int num = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_upload);
        ViewUtils.inject(this);
        Constant.data.clear();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initView();
        initEvent();
        PgyCrashManager.register(this);
    }

    private boolean initFile(Rwmxb task,int i) {
        if (Environment.MEDIA_UNMOUNTED.equals(Environment.getExternalStorageState()) &&
                Environment.isExternalStorageRemovable()) {
            // TODO: 2016/5/10 没有挂在sd卡
            Log.e("myLog", "拍照没有挂在sd卡");
            tmp = this.getCacheDir();
        } else {
            filePath = Environment.getExternalStorageDirectory().getPath();//获取sd卡路径
            filePath = filePath + "/CarData/picture/";
            SendDataActivity.isFolderExists(filePath);
            filePath = filePath +task.getRwdh()+task.getClsbm() + i+".png";//自定义图片的完整路径
            tmp = new File(filePath);
                if (!tmp.exists()) {
                    return false;
                }
        }
        return true;
    }

    private void initEvent() {
        bottom_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.data == null || Constant.data.size() == 0) {
                    Toast.makeText(MoreUploadActivity.this, "暂无要上传的数据...", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setMessage("正在上传1/" + Constant.data.size());
                    dialog.show();
                    //循环上传任务
                    num = 1;
                    upLoad(num - 1);
                }
            }
        });
    }

    //上传第i个任务
    public void upLoad(int i) {
        if (i >= Constant.data.size() || i < 0){
            return;
        }
        Rwmxb task = Constant.data.get(i);
        int num = readPicture(task.getRwdh(), task.getClsbm()).size();
        for (int j = 0; j< num; j++) {
            boolean isExit = initFile(task,j);
            if (isExit) {
                Bitmap map = Photo.getBitmapFromPath(filePath, 800, 800);
                createUpLoad(map, task);
            } else {
                continue;
            }
        }
    }


    Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Bundle bundle = msg.getData();
                index += bundle.getInt("index");
                boolean state = bundle.getBoolean("state");
                Rwmxb task = new Gson().fromJson(bundle.getString("data"), Rwmxb.class);
                if (state) {
                    Log.e("myLog","批量上传比较:"+num+" "+index+" "+readPicture(task.getRwdh(), task.getClsbm()).size());
                    if (index == readPicture(task.getRwdh(), task.getClsbm()).size()) {//上传成功一个任务
                        index=0;
                        LocalDataDb.UpdateMxbState(task.getRwdh(), task.getClsbm(), 4);
                        Constant.isRefreshMxb = true;
                        if (num == Constant.data.size()) {
                            dialog.dismiss();
                            Log.e("myLog", "成功上传" + num + "个任务");
                           initView();
                            Constant.data.clear();
                            Toast.makeText(MoreUploadActivity.this, "成功上传" + num + "个任务", Toast.LENGTH_SHORT).show();
                            return;
                        } else{
                            num++;
                            dialog.setMessage("正在上传" + num + "/" + Constant.data.size());
                            upLoad(num-1);
                        }
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(MoreUploadActivity.this, "批量上传失败", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };

    private void createUpLoad(final Bitmap map, final Rwmxb task) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Photo.saveFile(map, tmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int index = 0;//成功上传的图片数
                boolean state = false;
                //Log.e("myLog","创建上传任务的路径"+tmp.getAbsolutePath()+" 长度"+tmp.length());
                InputStream fs = Photo.Bitmap2IS(map);
                long filelen = tmp.length();
                UploadInfo uploadInfo = DataOperation.createUpload2(task, tmp.getAbsolutePath(), filelen);
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
                            state = true;
                            uploadInfo.setBlockIndex(uploadInfo.getBlockIndex() + 1);
                            if (uploadInfo.getBlockIndex() == uploadInfo.getBlockCount()) {
                                Log.e("myLog", "上传成功:" + "blockIndex：" + uploadInfo.getBlockIndex());
                                index++;
                            }
                        }
                    } catch (IOException e) {
                        state = false;
                        e.printStackTrace();
                    }
                }
                Message message = new Message();
                Bundle bundle = new Bundle();
                String data = new Gson().toJson(task);
                bundle.putString("data", data);
                bundle.putInt("index", index);
                bundle.putBoolean("state", state);
                message.setData(bundle);
                message.what = 1;
                mHandle.sendMessage(message);
            }

        }.start();
    }

    private void initView() {
        dialog = new ProgressDialog(MoreUploadActivity.this);
        data.clear();
        data = LocalDataDb.getAllData(" pzzt = '3' ");
        if (data == null || data.size() == 0) {
            Toast.makeText(MoreUploadActivity.this, "没有可上传的任务...", Toast.LENGTH_SHORT).show();
        } else {
            isCheckedMap = new HashMap<Integer, Boolean>();
            for (int i = 0; i < data.size(); i++) {
                isCheckedMap.put(i, false);
            }
            Constant.data.clear();
            updateRecyclerView(data);
        }

        upload_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isCheckedMap == null){
                    return;
                }
                Set<Integer> set = isCheckedMap.keySet();
                Iterator<Integer> iterator = set.iterator();
                if (isChecked) {
                    while (iterator.hasNext()) {
                        Integer keyId = iterator.next();
                        isCheckedMap.put(keyId, true);
                    }
                    text_select.setText("取消");
                } else {
                    while (iterator.hasNext()) {
                        Integer keyId = iterator.next();
                        isCheckedMap.put(keyId, false);
                    }
                    text_select.setText("全选");
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    //从数据库中读取图片
    private List<String> readPicture(String rwdh, String clsbm) {
        PictureDal dal = new PictureDal(getBaseContext());
        return dal.getPicPath(rwdh, clsbm);
    }

    private List<Rwmxb> readData(String state) {
        List<Rwmxb> u = new ArrayList<>();
        try {
            RwmxbDal dal = new RwmxbDal(getBaseContext());
            //u = dal.getDataByZt(state);
        } catch (Exception e) {
            Toast.makeText(MoreUploadActivity.this, "加载数据失败！", Toast.LENGTH_SHORT).show();
        }
        return u;
    }

    public void updateRecyclerView(final List<Rwmxb> datas) {
        if (adapter == null) {
            //保存数据到数据库
            adapter = new UpLoadListAdapter(this, datas, 0);
            mRecyclerview.setAdapter(adapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        } else {
            adapter.refreshData(datas);
        }
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent.setClass(MoreUploadActivity.this, SendDataActivity.class);
                Log.e("myLog", "你点击的行数是:" + position);
                if (position < 0 )
                    return;
                Constant.task = datas.get(position);
                startActivity(intent);
            }
        });
    }

}
