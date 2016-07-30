package com.xhb.onlystar.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.apater.BaseAdapter;
import com.xhb.onlystar.apater.RwzbListAdapter;
import com.xhb.onlystar.bean.AppResultOfZb;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.bean.Rwzb;
import com.xhb.onlystar.db.PictureDal;
import com.xhb.onlystar.db.RwmxbDal;
import com.xhb.onlystar.db.RwzbDal;
import com.xhb.onlystar.network.NetBroadcastReceiver;
import com.xhb.onlystar.network.NetUtil;
import com.xhb.onlystar.network.NetworkState;
import com.xhb.onlystar.newcardata.AppSignActivity;
import com.xhb.onlystar.newcardata.DatePickerActivity;
import com.xhb.onlystar.newcardata.FindRequireActivity;
import com.xhb.onlystar.newcardata.LoginActivity;
import com.xhb.onlystar.newcardata.MainActivity_hq;
import com.xhb.onlystar.newcardata.MipcaCaptureActivity;
import com.xhb.onlystar.newcardata.MoreUploadActivity;
import com.xhb.onlystar.newcardata.MyApplication;
import com.xhb.onlystar.newcardata.R;
import com.xhb.onlystar.newcardata.RwmxbActivity;
import com.xhb.onlystar.newcardata.SendDataActivity;
import com.xhb.onlystar.push.PushTaskService;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.DataCleanManager;
import com.xhb.onlystar.utils.DataOperation;
import com.xhb.onlystar.utils.DividerItemDecoration;
import com.xhb.onlystar.utils.GetNetDataAsyncTask;
import com.xhb.onlystar.utils.LocalDataDb;
import com.xhb.onlystar.utils.MyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import static android.app.Activity.RESULT_OK;
import static com.xhb.onlystar.newcardata.R.id.rwrq;
import static com.xhb.onlystar.utils.LocalDataDb.context;

public class RwzbFragment extends Fragment implements NetBroadcastReceiver.NetEventHandler {

    private ImageView loading;
    private TextView date_selecotr;
    private RecyclerView mRecyclerview;
    private MaterialRefreshLayout mRefreshLayout;
    private FloatingActionButton bottom_btn;
    private RwzbListAdapter adapter = null;
    private final static int SCANNIN_GREQUEST_CODE = 1;
    public final static int REQUEST_DATE = 2;
    private List<Rwzb> data = new ArrayList<Rwzb>();
    private int pageIndex = 0, pageSize = 4;
    private PopupWindow pop = null;
    private LinearLayout ll_popup, rwzb_line;
    private Intent intent = new Intent();
    private Handler mHandle = new Handler();
    private ProgressDialog dialog;
    private NetBroadcastReceiver netReceiver;//网络状态监测
    private boolean netState = false;
    private String date = MyUtils.getToday();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rwzb, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PgyCrashManager.register(getActivity());
        //注册网络监听广播
        regist();
        //先加载本地数据，有网则刷新
        initView(view);
        getData(date);
        initData(view);
        initEvent(view);
    }

    private void initData(final View view) {
        mHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {//初始化数据
                    Boolean state = msg.getData().getBoolean("state");
                    Log.e("myLog", "state " + state);
                    if (!state) {
                        Toast.makeText(getActivity(), "获取数据失败,请稍后再试...", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                       // Toast.makeText(getActivity(), "获取服务器数据成功...", Toast.LENGTH_SHORT).show();
                    }
                    getData(date);
                    initEvent(view);
                }
            }
        };

        if (netState) {
            /*dialog = new ProgressDialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("正在加载数据,请稍后...");
            dialog.show();*/
            loading = (ImageView) view.findViewById(R.id.loading);
            loading.setVisibility(View.VISIBLE);
            intent.setClass(getActivity(), PushTaskService.class);
            getActivity().startService(intent);
            new GetNetDataAsyncTask(getActivity(), loading, MyUtils.getToday(), mHandle).execute();
        }
    }

    private void initView(View view) {
        netState = NetUtil.getNetworkState(getActivity()) != 0 ? true : false;
        loading = (ImageView) view.findViewById(R.id.loading);
        if (!netState) {
            loading.setVisibility(View.GONE);
        }
        date_selecotr = (TextView) view.findViewById(R.id.date_selecotr);
        date_selecotr.setText(date);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_layout);
        bottom_btn = (FloatingActionButton) view.findViewById(R.id.bottom_btn);
        rwzb_line = (LinearLayout) view.findViewById(R.id.rwzb_line);
        data.clear();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        pop = new PopupWindow(getActivity());
        View mView = getActivity().getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) mView.findViewById(R.id.ll_popup);
        pop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(mView);
        RelativeLayout parent = (RelativeLayout) mView.findViewById(R.id.parent);
        Button bt1 = (Button) mView
                .findViewById(R.id.item_popupwindows_find);
        Button bt2 = (Button) mView
                .findViewById(R.id.item_popupwindows_scan);
        Button bt3 = (Button) mView
                .findViewById(R.id.item_popupwindows_upload);
        Button bt4 = (Button) mView
                .findViewById(R.id.item_popupwindows_logout);
        Button bt5 = (Button) mView
                .findViewById(R.id.item_popupwindows_cancel);
        Button bt6 = (Button) mView
                .findViewById(R.id.item_popupwindows_sign);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.setClass(getActivity(), FindRequireActivity.class);
                startActivity(intent);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scanFunction();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.setClass(getActivity(), MoreUploadActivity.class);
                startActivity(intent);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.setClass(getActivity(), PushTaskService.class);
                getActivity().stopService(intent);//结束服务
                Constant.login_state = false;
                Constant.user=null;
                DataCleanManager.clearAllCache(getActivity());
                DataCleanManager.deleteAllFiles(new File(DataCleanManager.getRootPath(getActivity()) + "/CarData/cache/"));//所有的图片成功删除
           /*   DataCleanManager.deleteAllFiles(new File(DataCleanManager.getRootPath(getActivity()) + "/CarData/picture/"));//所有的图片成功删除
                new RwzbDal(getActivity()).deleteData();
                new RwmxbDal(getActivity()).deleteData();
                new PictureDal(getActivity()).deleteData();
                context.deleteDatabase("hmdb");
                context.deleteDatabase("logdb.db");*/
                MyApplication.edit.clear();
                MyApplication.edit.commit();

                intent.setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); // 说明动作
                getActivity().sendBroadcast(intent);// 该函数用于发送广播
                getActivity().finish();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.setClass(getActivity(), AppSignActivity.class);
                startActivity(intent);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }

    private void initEvent(final View view) {
        try {
            bottom_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.showAtLocation(view, Gravity.TOP, 0, 0);
                }
            });
            adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Log.e("myLog", "你点击的行数是:" + position + " size: " + data.size());
                    if (position >= 0 && position < data.size()) {
                        intent.setClass(getActivity(), RwmxbActivity.class);
                        Constant.rwzb = data.get(position);
                        startActivity(intent);
                    }
                }
            });

            rwzb_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.setClass(getActivity(), DatePickerActivity.class);
                    startActivityForResult(intent, REQUEST_DATE);
                }
            });

        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "空指针异常...", Toast.LENGTH_SHORT).show();
        }

    }


    private void scanFunction() {
        intent.setClass(getActivity(), MipcaCaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    public void getData(final String date) {
        try {
            pageIndex = 0;
            pageSize = 4;
            data = readData(pageIndex * pageSize, pageSize, date);
            if (data == null) {
                Toast.makeText(getActivity(), "加载数据失败！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (data.size() > 0) {
                pageIndex++;
            }
            updateRecyclerView(data);
            mRefreshLayout.setLoadMore(true);
            mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
                @Override
                public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                    mRefreshLayout.setLoadMore(true);
                    List<Rwzb> tmpList = readData(pageIndex * pageSize, pageSize, date);
                    Log.e("myLog", "tmpList----size" + tmpList.size() + "  data--size" + data.size());
                    if (tmpList == null) {
                        Toast.makeText(getActivity(), "加载数据失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (tmpList.size() < pageSize) {
                        Toast.makeText(getActivity(), "无更多数据", Toast.LENGTH_LONG).show();
                        materialRefreshLayout.setLoadMore(false);
                    } else {
                        Toast.makeText(getActivity(), "加载了" + tmpList.size() + "条记录", Toast.LENGTH_SHORT).show();
                    }
                    // data.addAll(tmpList);
                    pageIndex++;
                    adapter.loadMoreData(tmpList);
                    data = adapter.getDatas();
                    tmpList.clear();
                    tmpList = null;
                    materialRefreshLayout.finishRefresh();
                    mRecyclerview.smoothScrollToPosition(pageIndex * pageSize);
                }

                @Override
                public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                    List<Rwzb> tmpList = readData(pageIndex * pageSize, pageSize, date);
                    Log.e("myLog", "tmpList----size" + tmpList.size() + "  data--size" + data.size());
                    if (tmpList == null) {
                        Toast.makeText(getActivity(), "加载数据失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (tmpList.size() < pageSize) {
                        Toast.makeText(getActivity(), "无更多数据", Toast.LENGTH_LONG).show();
                        materialRefreshLayout.setLoadMore(false);
                    } else {
                        Toast.makeText(getActivity(), "加载了" + tmpList.size() + "条记录", Toast.LENGTH_SHORT).show();
                    }
                    // data.addAll(tmpList);
                    pageIndex++;
                    adapter.loadMoreData(tmpList);
                    data = adapter.getDatas();
                    tmpList.clear();
                    tmpList = null;
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            });
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "空指针异常...", Toast.LENGTH_SHORT).show();
        }
    }


    private List<Rwzb> readData(int number, int pages, String mDate) {
        List<Rwzb> u = new ArrayList<Rwzb>();
        try {
            RwzbDal dal = new RwzbDal(getActivity());
            u = dal.getData(number, pages,mDate);
            if(dal.getData().size()<=0){
                Toast.makeText(getContext(), "本地暂无数据", Toast.LENGTH_SHORT).show();
            }else if(u.size()==0&&pageIndex==0){
                Toast.makeText(getContext(), mDate+"暂未发布任务", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            return null;
        }
        return u;
    }

    public void updateRecyclerView(final List<Rwzb> datas) {
        if (adapter == null) {
            //保存数据到数据库
            adapter = new RwzbListAdapter(getActivity(), datas, 0);
            mRecyclerview.setAdapter(adapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
            mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        } else {
            adapter.refreshData(datas);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == SCANNIN_GREQUEST_CODE) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            Log.e("myLog", "扫码返回值" + result);
            checkFor(result);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_DATE) {
            Bundle bundle = data.getExtras();
            int year = Integer.valueOf(bundle.getString("year"));
            int month = Integer.valueOf(bundle.getString("month"));
            int day = Integer.valueOf(bundle.getString("day"));
            date = LocalDataDb.getDateByInt(year, month, day);
            date_selecotr.setText(date);
            loading.setVisibility(View.VISIBLE);
            new GetNetDataAsyncTask(getActivity(), loading, date, mHandle).execute();
        }
    }

    private void checkFor(String result) {
        List<Rwmxb> rwmxb = new LocalDataDb(getActivity()).getAllData();
        for (int i = 0; i < data.size(); i++) {
            if (rwmxb.get(i).getClsbm() == result || rwmxb.get(i).getClsbm().equals(result)) {
                DialogTip("车辆识别码为:" + result + ",任务列表中有此车辆，是否拍照?", rwmxb.get(i));
                break;
            } else if (i == data.size() - 1) {
                DialogTip("车辆识别码为:" + result + ",\n未找到相关任务...");
            }
        }
    }

    private void DialogTip(String tip, final Rwmxb task) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                Constant.task = task;
                intent.setClass(getActivity(), SendDataActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void DialogTip(String tip) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView message = (TextView) view.findViewById(R.id.message);
        title.setText("小提示");
        message.setText(tip);
        Button ok = (Button) view.findViewById(R.id.ok);
        ok.setVisibility(View.GONE);
        Button no = (Button) view.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        if (Constant.isRefreshZb) {
            getData(date);
            Constant.isRefreshZb = false;
        }
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("myLog", "TaskListFragment执行销毁");
        intent.setClass(getActivity(), PushTaskService.class);
        getActivity().stopService(intent);
        getActivity().unregisterReceiver(netReceiver);
    }

    /**
     * 注册广播
     */
    private void regist() {
        netReceiver = new NetBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(netReceiver, filter);
        NetBroadcastReceiver.mListeners.add(this);
    }

    @Override
    public void onNetChange() {
        if (getActivity() == null) {
            return;
        }
        if (NetUtil.getNetworkState(getActivity()) == NetUtil.NETWORN_NONE) {//没有网络
            netState = false;
            // Toast.makeText(getActivity(), "你的网络已断开连接,请检查网络！", Toast.LENGTH_SHORT).show();
        } else if (NetUtil.getNetworkState(getActivity()) == NetUtil.NETWORN_WIFI) {//wifi网络
            netState = true;
            //Toast.makeText(getActivity(), "你目前是wifi网络！", Toast.LENGTH_SHORT).show();
        } else {//手机网络
            netState = true;
            //Toast.makeText(getActivity(), "你目前是手机网络！", Toast.LENGTH_SHORT).show();
        }
    }
}
