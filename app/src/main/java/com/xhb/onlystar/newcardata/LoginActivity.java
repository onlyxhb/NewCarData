package com.xhb.onlystar.newcardata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.bean.LoginReback;
import com.xhb.onlystar.network.NetworkState;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.DataOperation;

public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.login_user)
    private EditText login_user;
    @ViewInject(R.id.login_pwd)
    private EditText login_pwd;
    @ViewInject(R.id.login)
    private Button login;
    @ViewInject(R.id.forget_pwd)
    private TextView forget_pwd;
    Intent intent = new Intent();
    private  ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dialog = new ProgressDialog(LoginActivity.this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ViewUtils.inject(this);
        initEvent();
        PgyCrashManager.register(this);
    }

    private void initEvent() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "找回密码功能尚未开通", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Login() {
        if (login_user.getText().toString().equals("")) {
            login_user.setFocusable(true);
            login_user.setFocusableInTouchMode(true);
            login_user.requestFocus();
            login_user.setError("请输入用户名");
        } else if (login_pwd.getText().toString().equals("")) {
            login_pwd.setFocusable(true);
            login_pwd.setFocusableInTouchMode(true);
            login_pwd.requestFocus();
            login_pwd.setError("密码输入错误");
        } else {
            //向主线程发消息，跳转页面
            if(NetworkState.isNetworkAvailable(getApplicationContext())){
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("正在登陆...");
                if (dialog != null){
                    dialog.show();
                }
                String UserId = login_user.getText().toString().trim();
                String Pwd = login_pwd.getText().toString().trim();
                new LoginAsyncTask(dialog,this).execute(new String[]{UserId, Pwd});
            }else
                Toast.makeText(LoginActivity.this, "连接网络失败,请检查你的网络...", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog = null;
    }


    public class LoginAsyncTask extends AsyncTask<String,Void,String>{
        ProgressDialog dialog;
        Context context;
        public LoginAsyncTask(ProgressDialog dialog, Context context) {
            this.dialog = dialog;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            LoginReback reback = DataOperation.login(params[0], params[1]);
            Message message = new Message();
            Bundle bundle = new Bundle();
            String result = new Gson().toJson(reback);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            LoginReback reback = new Gson().fromJson(result, LoginReback.class);
            if (reback == null || reback.equals("")) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "连接服务器失败,请稍后再试...", Toast.LENGTH_SHORT).show();
            } else if (reback.getCode() == 0) {//登陆成功
                //保存数据至本地
                Constant.login_state=true;
                Constant.user=reback.getResult().get(0);
                MyApplication.edit.putString("login",result);
                MyApplication.edit.putBoolean("login_state", Constant.login_state);
                MyApplication.edit.commit();
                intent.setClass(LoginActivity.this, MainActivity_hq.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            } else {//登陆失败
                Toast.makeText(LoginActivity.this, "错误" + reback.getCode() + ":" + reback.getMes(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }
    }
}