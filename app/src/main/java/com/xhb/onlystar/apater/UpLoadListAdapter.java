package com.xhb.onlystar.apater;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.newcardata.MoreUploadActivity;
import com.xhb.onlystar.newcardata.R;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.LocalDataDb;
import com.xhb.onlystar.utils.Time;

import java.util.List;

public class UpLoadListAdapter extends SimpleAdapter<Rwmxb> {
    /*
    index=0显示所有的数据
    index=1只显示未上传的信息
     */

    private int mPosition;


    public UpLoadListAdapter(Context context, List<Rwmxb> datas, int index) {
        super(context, R.layout.upload_list_item, datas, index);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Rwmxb task, int index) {
        mPosition = getPosition();
        TextView rwdh = (TextView) viewHolder.getView(R.id.rwdh);
        TextView rwsj = (TextView) viewHolder.getView(R.id.rwsj);
        TextView cx = (TextView) viewHolder.getView(R.id.cx);
        TextView clsbm = (TextView) viewHolder.getView(R.id.clsbm);
        CheckBox upload_check = (CheckBox) viewHolder.getView(R.id.upload_check);
        //初始化信息
        rwdh.setText(task.getRwdh());
        String time = new LocalDataDb(context).getTimeByRwdh(task.getRwdh());
        rwsj.setText(Time.getShortTime(time));
        if (!TextUtils.isEmpty(task.getCx())) {
            cx.setText(task.getCx());
        }
        clsbm.setText(task.getClsbm());
        upload_check.setChecked(MoreUploadActivity.isCheckedMap.get(mPosition));
        upload_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e("myLog", "被选中");
                    Constant.data.add(task);
                    Log.e("myLog", Constant.data.size() + "");
                    MoreUploadActivity.bottom_upload.setText("批量上传(" +  Constant.data.size() + ")");
                    MoreUploadActivity.isCheckedMap.put(mPosition, true);
                } else {
                    Log.e("myLog", "未被选中");
                    Constant.data.remove(task);
                    Log.e("myLog", Constant.data.size() + "");
                    MoreUploadActivity.bottom_upload.setText("批量上传(" + Constant.data.size() + ")");
                    MoreUploadActivity.isCheckedMap.put(mPosition, false);
                }
            }
        });

    }

    public void resetLayout(int layoutId) {
        this.layoutResId = layoutId;
        notifyItemRangeChanged(0, getDatas().size());
    }


}






