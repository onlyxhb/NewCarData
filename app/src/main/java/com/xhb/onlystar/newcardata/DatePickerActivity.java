package com.xhb.onlystar.newcardata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.utils.LocalDataDb;
import com.xhb.onlystar.widget.caldendar.MonthDateView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatePickerActivity extends BaseActivity {

    private ImageView iv_left;
    private ImageView iv_right;
    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;
    private MonthDateView monthDateView;
    private List<Date> allDate = new ArrayList<Date>();
    public static DatePickerActivity instance;
    private Intent intent = new Intent();
    List<com.xhb.onlystar.bean.Date> list = new ArrayList<com.xhb.onlystar.bean.Date>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        initView();
        initEvent();
        PgyCrashManager.register(this);
    }

    private void initView() {
        list.add(new com.xhb.onlystar.bean.Date(2016,05,02));
        setContentView(R.layout.activity_date_picker);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        tv_date = (TextView) findViewById(R.id.date_text);
        tv_week = (TextView) findViewById(R.id.week_text);
        tv_today = (TextView) findViewById(R.id.tv_today);
        monthDateView.setTextView(tv_date, tv_week);
        monthDateView.setDaysHasThingList(list);
    }

    private void initEvent() {
    /*    monthDateView.setDateClick(new MonthDateView.DateClick() {
            @Override
            public void onClickOnDate() {
                String date=LocalDataDb.getDateByInt(monthDateView.getmSelYear(),monthDateView.getmSelMonth()+1,monthDateView.getmSelDay());
                Log.e("myLog", "你选择的日期为:" +date);
                for (com.xhb.onlystar.bean.Date MDate : list) {
                    if (MDate.getYear() != monthDateView.getmSelYear()) return;
                    if (MDate.getMonth() != monthDateView.getmSelMonth()+1) return;
                    if (MDate.getDay() != monthDateView.getmSelDay()) {
                        Toast.makeText(DatePickerActivity.this, "未找到该天的数据", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        intent.setClass(DatePickerActivity.this, FindActivity.class);
                        intent.putExtra("findDate",date);
                        startActivity(intent);
                        instance.finish();
                    }
                }
            }
        });*/

        iv_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.onLeftClick();
            }
        });
        iv_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.onRightClick();
            }
        });
        tv_today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.setTodayToView();
            }
        });
    }

//    private List<com.xhb.onlystar.bean.Date> initSetNum() {
//        List<Integer> myList = new ArrayList<Integer>();
//        List<com.xhb.onlystar.bean.Date> data = new ArrayList<>();
//        data = new LocalDataDb(getApplication()).getDataTime(0, Constant.totalIndex);
//        for (com.xhb.onlystar.bean.Date time : data) {
//            myList.add(time.getDay());
//            Log.e("myLog","数据的日期:"+time.getDay());
//            //需要修改
////            SimpleDateFormat formatter = new SimpleDateFormat("dd");
////            Date date = new Date(time);
////            myList.add(Integer.valueOf(formatter.format(date)));
//            // Log.e("myLog","数据的日期:"+formatter.format(date));
//        }
//        return myList;
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    public void getAllDateByDb() {
        List<Rwmxb> data = new LocalDataDb(getApplicationContext()).getAllData(String.valueOf(0));
    }

    public void cancel(View view) {
        this.finish();
    }

    public void sure(View view) {
        String date=LocalDataDb.getDateByInt(monthDateView.getmSelYear(),monthDateView.getmSelMonth()+1,monthDateView.getmSelDay());
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("year", monthDateView.getmSelYear()+"");
        bundle.putString("month",monthDateView.getmSelMonth()+1+"");
        bundle.putString("day", monthDateView.getmSelDay()+"");
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        this.finish();
    }
}
