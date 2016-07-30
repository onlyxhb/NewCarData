package com.xhb.onlystar.bean;

import java.io.Serializable;

/**
 * Created by onlystar on 2016/4/29.
 */

//任务总表
public class Rwzb implements Serializable {

    private String rwdh;//任务单号
    private String drdh; //导入单号
    private String sjlx;//数据源类型   1商用车  2乘用车
    private String jxsdm;// 经销商代码
    private String jxsmc;//经销商名称
    private String rwsj;//任务时间
    private String rwlx;//任务类型   1正常拍照  2重拍  3督察任务
    private String pzr;//拍照人代码
    private String pzsj;//拍照时间
    private String pzzt;//拍照状态  “”=0=未上传,2未拍照,3未上传 ;1=上传,4已上传,5打回重拍,6已通过
    private String bz;//备注
    public Rwzb() {
    }

    public Rwzb(String rwdh, String drdh, String sjlx, String jxsdm, String jxsmc, String rwsj, String rwlx, String pzr, String pzsj, String pzzt, String bz) {
        this.rwdh = rwdh;
        this.drdh = drdh;
        this.sjlx = sjlx;
        this.jxsdm = jxsdm;
        this.jxsmc = jxsmc;
        this.rwsj = rwsj;
        this.rwlx = rwlx;
        this.pzr = pzr;
        this.pzsj = pzsj;
        this.pzzt = pzzt;
        this.bz = bz;
    }

    public String getBz() {
        return bz;
    }

    public String getDrdh() {
        return drdh;
    }

    public String getJxsdm() {
        return jxsdm;
    }

    public String getJxsmc() {
        return jxsmc;
    }

    public String getPzr() {
        return pzr;
    }

    public String getPzsj() {
        return pzsj;
    }

    public String getPzzt() {
        return pzzt;
    }

    public String getRwdh() {
        return rwdh;
    }

    public String getRwlx() {
        return rwlx;
    }

    public String getRwsj() {
        return rwsj;
    }

    public String getSjlx() {
        return sjlx;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public void setDrdh(String drdh) {
        this.drdh = drdh;
    }

    public void setJxsdm(String jxsdm) {
        this.jxsdm = jxsdm;
    }

    public void setJxsmc(String jxsmc) {
        this.jxsmc = jxsmc;
    }

    public void setPzr(String pzr) {
        this.pzr = pzr;
    }

    public void setPzsj(String pzsj) {
        this.pzsj = pzsj;
    }

    public void setPzzt(String pzzt) {
        this.pzzt = pzzt;
    }

    public void setRwdh(String rwdh) {
        this.rwdh = rwdh;
    }

    public void setRwlx(String rwlx) {
        this.rwlx = rwlx;
    }

    public void setRwsj(String rwsj) {
        this.rwsj = rwsj;
    }

    public void setSjlx(String sjlx) {
        this.sjlx = sjlx;
    }
}
