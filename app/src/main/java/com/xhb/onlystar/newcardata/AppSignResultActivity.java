package com.xhb.onlystar.newcardata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import static android.R.attr.y;

public class AppSignResultActivity extends BaseActivity {

    private TextView app_sign_result, app_sign_result_err;
    private Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_sign_result);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initView();
    }

    private void initView() {
        app_sign_result = (TextView) findViewById(R.id.app_sign_result);
        app_sign_result_err = (TextView) findViewById(R.id.app_sign_result_err);
        back = (Button) findViewById(R.id.back);
        boolean state = getIntent().getExtras().getBoolean("state");
        int type = getIntent().getExtras().getInt("type");
        String msg = getIntent().getExtras().getString("msg");
        if (type == 0) {
            if (state) {
                app_sign_result.setText("签到成功");
            } else {
                app_sign_result.setText("签到失败");
                app_sign_result_err.setVisibility(View.VISIBLE);
                app_sign_result_err.setText("失败原因:" + msg);
            }
        } else if (type == 1) {
            if (state) {
                app_sign_result.setText("签退成功");
            } else {
                app_sign_result.setText("签退失败");
                app_sign_result_err.setVisibility(View.VISIBLE);
                app_sign_result_err.setText("失败原因:" + msg);
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
