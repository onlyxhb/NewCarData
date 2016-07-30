package com.xhb.onlystar.newcardata;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.LocalDataDb;

public class FindRequireActivity extends BaseActivity {

    @ViewInject(R.id.rwrq)
    private TextView rwrq;
    @ViewInject(R.id.cx)
    private EditText cx;
    @ViewInject(R.id.clsbm)
    private EditText clsbm;
    @ViewInject(R.id.pzzt)
    private Spinner pzzt;
    private Intent intent = new Intent();
    public static final int REQUEST_DATE = 1;
    private static final String[] m = {"未拍照", "未上传", "已上传", "打回重拍", "已通过"};
    private ArrayAdapter<String> adapter;
    private int position = 0;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_require);
        ViewUtils.inject(this);
        initView();
        initEvent();
        PgyCrashManager.register(this);
    }

    private void initView() {
//将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this, R.layout.spinner, m);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(R.layout.drop_down_item);
        //将adapter 添加到spinner中
        pzzt.setAdapter(adapter);
        //添加事件Spinner事件监听
        pzzt.setOnItemSelectedListener(new SpinnerSelectedListener());
        //设置默认值
        pzzt.setVisibility(View.VISIBLE);
    }

    private void initEvent() {
        rwrq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(FindRequireActivity.this, DatePickerActivity.class);
                startActivityForResult(intent, REQUEST_DATE);
            }
        });

    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            position = arg2;
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    public void cancel(View view) {
        this.finish();
    }

    public void sure(View view) {
        queryData();
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_DATE) {
            Bundle bundle = data.getExtras();
            year = Integer.valueOf(bundle.getString("year"));
            month = Integer.valueOf(bundle.getString("month"));
            day = Integer.valueOf(bundle.getString("day"));
            rwrq.setText(LocalDataDb.getDateByInt(year, month, day));
        }
    }
    private void queryData() {
        intent.setClass(FindRequireActivity.this, FindActivity.class);
        Bundle data = new Bundle();
        if (rwrq.getText().toString().trim().equals("") || TextUtils.isEmpty(rwrq.getText().toString().trim())) {
            data.putString("rwrq", "");
        } else {
            data.putString("rwrq", LocalDataDb.getDateByIntTwo(year, month, day));
        }
        data.putString("cx", cx.getText().toString().trim());
        data.putString("clsbm", clsbm.getText().toString().trim());
        data.putString("pzzt", getPzztByPosition(position));
        intent.putExtras(data);
        startActivity(intent);
    }


    public String getPzztByPosition(int mPosition) {
        String s = "";
        switch (mPosition) {
            case 0:
                s = "2";
                break;
            case 1:
                s = "3";
                break;
            case 2:
                s = "4";
                break;
            case 3:
                s = "5";
                break;
            case 4:
                s = "6";
                break;
            default:
                s = "";
                break;
        }
        return s;
    }
}
