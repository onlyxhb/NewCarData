package com.xhb.onlystar.apater;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.newcardata.R;
import com.xhb.onlystar.newcardata.SendDataActivity;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.LocalDataDb;

import java.util.List;

public class MyListAdapter extends SimpleAdapter<Rwmxb> {
    /*
    index=0显示所有的数据
    index=1只显示未上传的信息
     */
    public MyListAdapter(Context context, List<Rwmxb> datas, int index) {
        super(context, R.layout.alltask_list_item, datas, index);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Rwmxb task, int index) {
        //TextView rwdh = (TextView) viewHolder.getView(R.id.rwdh);
        //TextView rwsj = (TextView) viewHolder.getView(R.id.rwsj);
        TextView cx = (TextView) viewHolder.getView(R.id.cx);
        TextView clsbm = (TextView) viewHolder.getView(R.id.clsbm);
        TextView pzzt = (TextView) viewHolder.getView(R.id.pzzt);
        Button button = viewHolder.getButton(R.id.btn_add);
        //初始化信息
        if (!TextUtils.isEmpty(task.getCx())) {
            cx.setText(task.getCx());
        }
        clsbm.setText(task.getClsbm());
        //处理图像
//        String imageUrl = task.getPicPath();
//        if (!TextUtils.isEmpty(imageUrl) && index == 0) {
//            draweeView.setVisibility(View.VISIBLE);
//            draweeView.setImageURI(Uri.parse(task.getPicPath()));
//        } else {
//            draweeView.setVisibility(View.GONE);
//        }


        switch (task.getPzzt()) {
            case "2": {
                button.setEnabled(true);
                pzzt.setText("未拍照");
                button.setText("去拍照");
                break;
            }
            case "3": {
                button.setEnabled(true);
                pzzt.setText("未上传");
                button.setText("去上传");
                break;
            }
            case "4": {
                button.setEnabled(true);
                pzzt.setText("已上传");
                button.setText("审核中");
                button.setEnabled(false);
                break;
            }
            case "5": {
                button.setEnabled(true);
                pzzt.setText("打回重拍");
                button.setText("去拍照");
                break;
            }
            case "6": {
                button.setEnabled(true);
                pzzt.setText("已通过");
                button.setText("已通过");
                button.setEnabled(false);
                break;
            }
        }

        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (task.getPzzt()) {
                        case "2":
                            Constant.task = task;
                            Intent intent = new Intent();
                            intent.setClass(context, SendDataActivity.class);
                            context.startActivity(intent);
                            break;
                        case "3":
                            DialogTip("你已经拍照过该车信息，但尚未上传，是否查看？", task);
                            break;
                        case "5":
                            Toast.makeText(context, "你上传的照片审核不通过!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }

    }

    public void resetLayout(int layoutId) {
        this.layoutResId = layoutId;
        notifyItemRangeChanged(0, getDatas().size());
    }

    private void DialogTip(String tip, final Rwmxb task) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                //上传---未上传的信息
                Constant.task = task;
                Intent intent = new Intent();
                intent.setClass(context, SendDataActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
