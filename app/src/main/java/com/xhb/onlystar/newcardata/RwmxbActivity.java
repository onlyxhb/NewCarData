package com.xhb.onlystar.newcardata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.apater.BaseAdapter;
import com.xhb.onlystar.apater.MyListAdapter;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.bean.Rwzb;
import com.xhb.onlystar.db.RwmxbDal;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.DividerItemDecoration;
import com.xhb.onlystar.utils.GetNetDataAsyncTask;
import com.xhb.onlystar.utils.LocalDataDb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onlystar on 2016/5/19.
 */
public class RwmxbActivity extends BaseActivity {

    private SearchView search_view;
    private ListView listView;
    private List<String> allClsbm = new ArrayList<String>();
    private TextView rwdh, rwsj;
    private RecyclerView mRecyclerview;
    private MaterialRefreshLayout mRefreshLayout;
    private ImageButton send;
    private MyListAdapter adapter;
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private List<Rwmxb> data = new ArrayList<Rwmxb>();
    private Intent intent = new Intent();
    private Rwzb rwzb = Constant.rwzb;
    private ProgressDialog dialog = null;
    private Handler mHandle = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        PgyCrashManager.register(this);
        initView();
        getData();
        initEvent();
    }

    private void initView() {
        if (rwzb == null || rwzb.getPzzt() == null || rwzb.getPzzt().equals("null")) {
            Constant.rwzb = new Gson().fromJson(MyApplication.preferences.getString("rwzb", null), Rwzb.class);
            rwzb = Constant.rwzb;
        } else {
            MyApplication.edit.putString("rwzb", new Gson().toJson(rwzb));
            MyApplication.edit.commit();
        }
        listView = (ListView) findViewById(R.id.lv);
        search_view = (SearchView) findViewById(R.id.search_view);
        int id = search_view.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) search_view.findViewById(id);
        textView.setTextColor(Color.BLACK);  textView.setHintTextColor(Color.parseColor("#d3d3d3"));
        rwdh = (TextView) findViewById(R.id.rwdh);
        rwsj = (TextView) findViewById(R.id.rwsj);
        mRecyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        mRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh_layout);
        send = (ImageButton) findViewById(R.id.send);
        data.clear();
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        rwdh.setText(rwzb.getRwdh());
        rwsj.setText(rwzb.getRwsj());

    }


    public void getData() {
        if (rwzb == null) {
            Toast.makeText(RwmxbActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
            return;
        }
        data = new LocalDataDb(getApplicationContext()).getRwmxbByRwdh(rwzb.getRwdh());
        if (data == null || data.size() < 0) {
            Toast.makeText(RwmxbActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
            return;
        }
        updateRecyclerView(data);
        mRefreshLayout.setLoadMore(false);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                if (rwzb == null) {
                    Toast.makeText(RwmxbActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                data = new LocalDataDb(getApplicationContext()).getRwmxbByRwdh(rwzb.getRwdh());
                if (data == null || data.size() < 0) {
                    Toast.makeText(RwmxbActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.refreshData(data);
                materialRefreshLayout.finishRefresh();
            }
        });
    }

    private void initEvent() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFunction();
            }
        });
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e("myLog", "你点击的行数是:" + position);
                try {
                    if (position >= 0 && position < data.size()) {
                        intent.setClass(RwmxbActivity.this, SendDataActivity.class);
                        Constant.task = data.get(position);
                        startActivity(intent);
                    }
                }catch (SecurityException e){
                    Toast.makeText(RwmxbActivity.this, "出现异常...", Toast.LENGTH_SHORT).show();
                }catch (IndexOutOfBoundsException e){
                    Toast.makeText(RwmxbActivity.this, "下标越界", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //search_view.setIconified(false);
        search_view.setSubmitButtonEnabled(true);
        search_view.onActionViewExpanded();//表示在内容为空时不显示取消的x按钮，内容不为空时显示.
        //search_view.setSubmitButtonEnabled(true);//编辑框后显示search按钮，个人建议用android:imeOptions=”actionSearch”代替。
        //search_view的几个监听事件
        search_view.setOnCloseListener(new SearchView.OnCloseListener() {//表示点击取消按钮listener，默认点击搜索输入框
            @Override
            public boolean onClose() {
                return false;
            }
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//表示输入框文字listener
            @Override
            public boolean onQueryTextSubmit(String query) {//开始搜索listener
                checkFor(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {//输入框内容变化listener
                if (newText.length() != 0) {
                    allClsbm=getAllClsbm(rwzb.getRwdh(),newText);
                    if(allClsbm==null||allClsbm.size()<=0)
                    {
                        allClsbm.add("未找到数据");
                    }
                    listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.
                            simple_expandable_list_item_1,allClsbm ));
                    listView.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.GONE);
                }

                return true;
            }
        });
        hideSoftInput();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              try {
                  hideSoftInput();
                  checkFor(allClsbm.get(position).toString());
                  listView.setVisibility(View.GONE);
                  search_view.setQueryHint("请输入要查找的车辆识别码");
              }catch (IndexOutOfBoundsException e){
                  Toast.makeText(RwmxbActivity.this, "下标越界", Toast.LENGTH_SHORT).show();
              }
            }
        });
    }

    private void scanFunction() {
        intent.setClass(this, MipcaCaptureActivity.class);
        // intent.setClass(TaskListActivity.this, TestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    private List<Rwmxb> readData(int number, int pages) {
        List<Rwmxb> u = new ArrayList<Rwmxb>();
        try {
            RwmxbDal dal = new RwmxbDal(this);
            u = dal.getData(number, pages);
        } catch (Exception e) {
            return null;
        }
        return u;
    }

    public void updateRecyclerView(final List<Rwmxb> datas) {
        if (adapter == null) {
            //保存数据到数据库
            adapter = new MyListAdapter(this, data, 0);
            mRecyclerview.setAdapter(adapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        } else {
            adapter.refreshData(data);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK && requestCode == SCANNIN_GREQUEST_CODE) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            Log.e("myLog", "扫码返回值" + result);
            checkFor(result);
        }
    }

    private void checkFor(String result) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getClsbm() == result || data.get(i).getClsbm().equals(result)) {
                DialogTip("车辆识别码为:" + result + ",任务列表中有此车辆，是否拍照?", data.get(i));
                break;
            } else if (i == data.size() - 1) {
                DialogTip("车辆识别码为:" + result + ",\n未找到相关任务...");
            }
        }
    }

    private void DialogTip(String tip, final Rwmxb task) {
        LayoutInflater inflater = LayoutInflater.from(this);
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
                Constant.task = task;
                intent.setClass(RwmxbActivity.this, SendDataActivity.class);
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
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        if (Constant.isRefreshMxb) {
           // refreshData();
            getData();
            Constant.isRefreshMxb=false;
        }
        super.onResume();
    }

    private void refreshData() {
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("正在加载数据,请稍后...");
        dialog.show();
        mHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Boolean state = msg.getData().getBoolean("state");
                Log.e("myLog", "state " + state);
                Constant.isRefreshMxb = false;
                if (!state) {
                    Toast.makeText(RwmxbActivity.this, "获取数据失败,请稍后再试...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (msg.what == 1) {//初始化数据
                    getData();
                }
            }
        };
        new GetNetDataAsyncTask(getApplicationContext(), dialog, mHandle).execute();
    }


    //模糊查找数据库中的数据
    public List<String> getAllClsbm(String rwdh,String keyWord) {
        List<String> mData = new ArrayList<String>();
        RwmxbDal dal = new RwmxbDal(getApplicationContext());
        List<Rwmxb> u = dal.getData(rwdh,keyWord);
        for (Rwmxb t : u) {
            if (!mData.contains(t)) {
                mData.add(t.getClsbm());
            } else {
                continue;
            }

        }
        return mData;
    }

    //隐藏键盘
    private void hideSoftInput() {
        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View v = getCurrentFocus();
            if (v == null) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            search_view.clearFocus();
        }
    }
}
