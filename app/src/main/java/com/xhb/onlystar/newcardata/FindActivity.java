package com.xhb.onlystar.newcardata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.apater.BaseAdapter;
import com.xhb.onlystar.apater.MyListAdapter;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.db.RwmxbDal;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.DividerItemDecoration;
import com.xhb.onlystar.utils.LocalDataDb;
import com.xhb.onlystar.utils.Time;

import java.util.ArrayList;
import java.util.List;

public class FindActivity extends BaseActivity {
    @ViewInject(R.id.recycler_view_upload)
    private RecyclerView mRecyclerview;
    @ViewInject(R.id.refresh_layout_upload)
    private MaterialRefreshLayout mRefreshLayout;
    private MyListAdapter adapter;
    private List<Rwmxb> data = new ArrayList<Rwmxb>();
    private Intent intent = new Intent();
    private String findDate;
    private String rwrq, cx, clsbm, pzzt;
    private String queryRequest = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        ViewUtils.inject(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initView();
        PgyCrashManager.register(this);
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        rwrq = bundle.getString("rwrq");
        cx = bundle.getString("cx");
        clsbm = bundle.getString("clsbm");
        pzzt = bundle.getString("pzzt");
        Log.e("myLog", "查询条件 rwrq：" + rwrq + " cx：" + cx + " clsbm：" + clsbm + " pzzt：" + pzzt);
        if (TextUtils.isEmpty(pzzt) || pzzt.equals("")) {
            Log.e("myLog", "查询条件检测:" + "拍照状态为空");
        } else {
            queryRequest += " pzzt like " + "'%" + pzzt + "%'";
        }
        if (TextUtils.isEmpty(cx) || cx.equals("")) {
            Log.e("myLog", "查询条件检测:" + "车型为空");
        } else {
            queryRequest += " and cx like " + "'%" + cx + "%'";
        }
        if (TextUtils.isEmpty(clsbm) || clsbm.equals("")) {
            Log.e("myLog", "查询条件检测:" + "车辆识别码为空");
        } else {
            queryRequest += " and clsbm like " + "'%" + clsbm + "%'";
        }
        data = readData(queryRequest);
        if (data.size() == 0 || data == null) {
            Toast.makeText(FindActivity.this, "没有找到数据...", Toast.LENGTH_SHORT).show();
        } else {
            if (TextUtils.isEmpty(rwrq) || rwrq.equals("")) {
                Log.e("myLog", "查询条件检测:" + "任务日期为空");
                updateRecyclerView(data);
            } else {
                data = getDataByRwrq(data, rwrq);
                if (data.size() == 0 || data == null) {
                    Toast.makeText(FindActivity.this, "没有找到数据...", Toast.LENGTH_SHORT).show();
                } else {
                    updateRecyclerView(data);
                }
            }

        }
    }

    private List<Rwmxb> getDataByRwrq(List<Rwmxb> data, String mRwrq) {
        List<Rwmxb> u = new ArrayList<Rwmxb>();
        String rq = null;
        for (Rwmxb task : data) {
            rq = new LocalDataDb(getApplicationContext()).getRwzbByRwdh(task.getRwdh()).getRwsj();
            rq = Time.getShortTimeTwo(rq);
            if (rq.equals(mRwrq)) {
                Log.e("myLog", "查询条件检测："+"成功找到一个数据");
                u.add(task);
            } else {
                continue;
            }
        }
        return u;
    }

    private List<Rwmxb> readData(String request) {
        List<Rwmxb> u = new ArrayList<Rwmxb>();
        try {
            RwmxbDal dal = new RwmxbDal(getBaseContext());
            u = dal.getData(request);
        } catch (Exception e) {
            Toast.makeText(FindActivity.this, "加载数据失败！", Toast.LENGTH_SHORT).show();
        }
        return u;
    }

    public void updateRecyclerView(final List<Rwmxb> datas) {
        if (adapter == null) {
            //保存数据到数据库
            adapter = new MyListAdapter(this, datas, 0);
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
                intent.setClass(FindActivity.this, SendDataActivity.class);
                Log.e("myLog", "你点击的行数是:" + position);
                if (position < 0)
                    return;
                Constant.task = datas.get(position);
                startActivity(intent);
            }
        });
    }
}
