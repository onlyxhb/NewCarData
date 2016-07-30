package com.xhb.onlystar.newcardata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.utils.Constant;

public class DataDetailActivity extends BaseActivity {

    private String imageUrl;
    private String cx;
    private String carNum;
    private String pzr;
    private String pzsj;
    private int state;

    @ViewInject(R.id.cx)
    private TextView text_cx;
    @ViewInject(R.id.clsbm)
    private TextView text_clsbm;
    @ViewInject(R.id.pzr)
    private TextView text_pzr;
    @ViewInject(R.id.pzsj)
    private TextView text_pzsj;
    @ViewInject(R.id.text_detail)
    private TextView text_detail;
    @ViewInject(R.id.btn)
    private Button btn;
    @ViewInject(R.id.reason)
    private TextView reason;
    private Rwmxb task = Constant.task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);
        ViewUtils.inject(this);
        initData();
        initView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        PgyCrashManager.register(this);
    }

    private void initView() {
        //my_drawee_view.setImageURI(Uri.parse(imageUrl));
        text_cx.setText("车型:" + cx);
        text_clsbm.setText("VIN码:" + carNum);
        text_pzr.setText("拍照人:" + pzr);
        text_pzsj.setText("拍照时间:" + pzsj);
        switch (state) {
            case 0: {
                btn.setEnabled(true);
                btn.setText("未拍照");
                break;
            }
            case 1: {
                btn.setEnabled(true);
                btn.setText("未上传");
                break;
            }
            case 2: {
                reason.setVisibility(View.VISIBLE);
                reason.setText("图片不清晰,审核不通过...");
                btn.setEnabled(true);
                btn.setText("未通过");
                break;
            }
            case 3: {
                btn.setText("已通过");
                btn.setEnabled(false);
                break;
            }
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (state) {
                    case 0:
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), SendDataActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });

    }

    private void initData() {
        task = Constant.task;
        imageUrl = task.getPicPath();
        cx = task.getCx();
        carNum = task.getClsbm();
        pzr = task.getPzr();
        pzsj = task.getPzsj();
        state = 0;
        if (!TextUtils.isEmpty(task.getPzzt())) {
            state = Integer.valueOf(task.getPzzt());
        }
    }
}
