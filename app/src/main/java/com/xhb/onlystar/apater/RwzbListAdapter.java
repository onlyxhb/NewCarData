package com.xhb.onlystar.apater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.bean.Rwzb;
import com.xhb.onlystar.bean.Save_pzrwTjReback;
import com.xhb.onlystar.newcardata.R;
import com.xhb.onlystar.newcardata.RwmxbActivity;
import com.xhb.onlystar.newcardata.SendDataActivity;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.DataOperation;
import com.xhb.onlystar.utils.LocalDataDb;
import com.xhb.onlystar.utils.MyUtils;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class RwzbListAdapter extends SimpleAdapter<Rwzb> {

    private ProgressDialog dialog = null;
    private AlertDialog  aDialog=null;
    /*
    index=0显示所有的数据
    index=1只显示未上传的信息
     */
    public RwzbListAdapter(Context context, List<Rwzb> datas, int index) {
        super(context, R.layout.rwzb_list_item, datas, index);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Rwzb task, int index) {
        TextView rwdh = (TextView) viewHolder.getView(R.id.rwdh);
        TextView num = (TextView) viewHolder.getView(R.id.num);
        TextView rwlx = (TextView) viewHolder.getView(R.id.rwlx);
        TextView pzr = (TextView) viewHolder.getView(R.id.pzr);
        Button button = viewHolder.getButton(R.id.btn_add);
        rwdh.setText(task.getRwdh());
        num.setText(new LocalDataDb(context).getNumByRwdh(task.getRwdh()) + "");
        if (Constant.user != null) {
            pzr.setText(Constant.user.getTrueName());
        } else {
            pzr.setText(task.getPzr());
        }
        switch (task.getRwlx()) {
            case "":
            case "1":
                rwlx.setText("正常拍照");
                break;
            case "2":
                rwlx.setText("重拍");
                break;
            case "3":
                rwlx.setText("督查任务");
                break;
        }

        switch (task.getPzzt()) {
            case "":
            case "0":
                button.setBackground(context.getResources().getDrawable(R.color.ToolBar_bg));
                button.setText("提交任务");
                break;
            case "1":
                button.setBackground(context.getResources().getDrawable(R.color.crimson));
                button.setText("已提交");
                break;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task.getPzzt().equals("1"))
                    Toast.makeText(context, "此任务已经提交过", Toast.LENGTH_SHORT).show();
                else if (task.getPzzt().equals("") || task.getPzzt().equals("0")) {
                    int pzzt = LocalDataDb.getPhotoState(task.getRwdh());
                    if (pzzt == 0) {
                        Toast.makeText(context, "此任务未拍照,请拍照上传后再提交...", Toast.LENGTH_SHORT).show();
                    } else if (pzzt == 1) {//有部分拍照
                        DialogTip("有部分任务未拍照,是否提交?", task);
                    } else if (pzzt == 2) {//全部拍照
                        Toast.makeText(context, "正在提交，请稍后...", Toast.LENGTH_SHORT).show();
                        Commit(task);
                    }
                }
            }
        });

    }


    private void DialogTip(String tip, final Rwzb task) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        aDialog = builder.show();
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView message = (TextView) view.findViewById(R.id.message);
        title.setText("小提示");
        message.setText(tip);
        Button ok = (Button) view.findViewById(R.id.ok);
        Button no = (Button) view.findViewById(R.id.no);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //上传---未上传的信息
                Toast.makeText(context, "正在提交，请稍后...", Toast.LENGTH_SHORT).show();
                try {
                    Commit(task);
                }catch (ConcurrentModificationException e){
                    Toast.makeText(context, "检测到异常...", Toast.LENGTH_SHORT).show();
                }catch (NullPointerException e){
                    Toast.makeText(context, "空指针异常...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                aDialog.dismiss();
            }
        });
    }

    public void Commit(final Rwzb mTask) {
        dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("正在提交...");
        dialog.show();

        new Thread() {
            @Override
            public void run() {
                List<Rwmxb> mxb = LocalDataDb.getRwmxbByRwdh(mTask.getRwdh());
                List<Rwmxb> getMxb = new ArrayList<Rwmxb>();
                for (Rwmxb u : mxb) {
                    if (u.getPzsj() == null || TextUtils.isEmpty(u.getPzsj()) || u.getPzsj() == "" || u.getPzsj().equals("null")) {
                        continue;
                    } else {
                        getMxb.add(u);
                    }
                }
                String time = MyUtils.getCurrentLongTimeOfPzsj();
                mTask.setPzsj(time);
                Log.e("myLog","提交的mxb数:"+getMxb.size());
                Save_pzrwTjReback save_pzrwTjReback = DataOperation.Save_pzrwTj(mTask, getMxb);
                Bundle bundle = new Bundle();
                String result = new Gson().toJson(save_pzrwTjReback);
                bundle.putString("data", result);
                Message message = new Message();
                message.setData(bundle);
                message.what = 0;
                mHandle.sendMessage(message);

            }
        }.start();
    }

    Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (dialog == null||aDialog==null) {
                dialog = new ProgressDialog(context);
                aDialog= new AlertDialog.Builder(context).show();
            }
            if (msg.what == 0) {
                if (dialog == null||aDialog==null) {
                    return;
                }
                dialog.dismiss();
                aDialog.dismiss();
                String result = bundle.getString("data");
                Save_pzrwTjReback reback = new Gson().fromJson(result, Save_pzrwTjReback.class);
                if (reback == null || reback.equals("")) {
                    Toast.makeText(context, "连接服务器失败,请稍后再试...", Toast.LENGTH_SHORT).show();
                } else if (reback.getCode().equals("0")) {//提交成功
                    Toast.makeText(context, "数据提交成功", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "错误" + reback.getCode() + ":" + reback.getMes(), Toast.LENGTH_LONG).show();
            }
        }
    };
}
