package com.xhb.onlystar.apater;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.xhb.onlystar.bean.Rwzb;
import com.xhb.onlystar.newcardata.R;
import com.xhb.onlystar.utils.LocalDataDb;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by onlystar on 2016/5/3.
 */
public class LeadAdapter extends android.widget.BaseAdapter {

    private Context context;
    private List<Rwzb> rwzb;
    private LayoutInflater mInflater = null;

    public LeadAdapter(Context context, List<Rwzb> rwzb) {
        this.context = context;
        this.rwzb = rwzb;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return rwzb.size();
    }

    @Override
    public Object getItem(int position) {
        return rwzb.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = mInflater.inflate(R.layout.lead_item, null);
            holder.rwdh = (TextView) convertView.findViewById(R.id.rwdh);
            // holder.bz = (TextView) convertView.findViewById(R.id.bz);
            // holder.sjlx = (TextView) convertView.findViewById(R.id.sjlx);
            // holder.rwlx = (TextView) convertView.findViewById(R.id.rwlx);
            holder.rwsj = (TextView) convertView.findViewById(R.id.rwsj);
            holder.jxsdm = (TextView) convertView.findViewById(R.id.jxsdm);
            holder.jxsmc = (TextView) convertView.findViewById(R.id.jxsmc);
            holder.pzr = (TextView) convertView.findViewById(R.id.pzr);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.pzzt = (TextView) convertView.findViewById(R.id.pzzt);
            // holder.lead=(Button) convertView.findViewById(R.id.lead);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        holder.rwdh.setText("任务单号:" + rwzb.get(position).getRwdh());
        // holder.bz.setText(rwzb.get(position).getBz());
        // holder.sjlx.setText(rwzb.get(position).getSjlx());
        // holder.rwlx.setText(rwzb.get(position).getRwlx());
        holder.rwsj.setText("任务时间:" + rwzb.get(position).getRwsj());
        holder.jxsdm.setText("经销商代码:" + rwzb.get(position).getJxsdm());
        holder.jxsmc.setText(rwzb.get(position).getJxsmc());
        holder.pzr.setText("拍照人:" + rwzb.get(position).getPzr());
        holder.number.setText("发布任务量:" + new LocalDataDb(context).getNumByRwdh(rwzb.get(position).getRwdh()));
        switch (rwzb.get(position).getPzzt()) {
            case "2": {
                holder.pzzt.setText("未拍照");
                break;
            }
            case "3": {
                holder.pzzt.setText("未上传");
                break;
            }
            case "4": {
                holder.pzzt.setText("已上传");
                break;
            }
            case "5": {
                holder.pzzt.setText("打回重拍");
                break;
            }
            case "6": {
                holder.pzzt.setText("已通过");
                break;
            }
        }

        return convertView;
    }


    static class ViewHolder {
        private TextView bz;//备注
        private TextView drdh; //导入单号
        private TextView jxsdm;// 经销商代码
        private TextView jxsmc;//经销商名称
        private TextView pzr;//拍照人代码
        private TextView number;//拍照时间
        private TextView pzzt;//拍照状态
        private TextView rwdh;//任务单号
        private TextView rwlx;//任务类型
        private TextView rwsj;//任务时间
        private TextView sjlx;//数据源类型
        private Button lead;//导入任务

    }

}
