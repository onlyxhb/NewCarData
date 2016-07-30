package com.xhb.onlystar.bean;

import java.io.Serializable;

/**
 * Created by onlystar on 2016/4/29.
 */

//任务明细表
public class Rwmxb {
    private String rwdh;//任务单号
    private String cx; //车型
    private String clsbm;//车辆识别码
    private String ys;//颜色  珍珠白(金属漆)
    private String sjID;//手机ID
    private String sjhm;//手机号码
    private String pzr;//拍照人代码
    private String pzsj;//拍照时间  /Date(1461656040000+0800)/
    private float lat;//维度
    private float lng;//经度
    private String PicPath;//照片路径
    private String pzzt;//状态  “”=0=未上传,2未拍照,3未上传 ;1=上传,4已上传,5打回重拍,6已通过
    private String bz;//备注
    public Rwmxb() {
    }

    public Rwmxb( String rwdh, String cx, String clsbm, String ys, String sjID, String sjhm, String pzr, String pzsj, float lat, float lng, String picPath, String pzzt, String bz) {
        this.rwdh = rwdh;
        this.cx = cx;
        this.clsbm = clsbm;
        this.ys = ys;
        this.sjID = sjID;
        this.sjhm = sjhm;
        this.pzr = pzr;
        this.pzsj = pzsj;
        this.lat = lat;
        this.lng = lng;
        PicPath = picPath;
        this.pzzt = pzzt;
        this.bz = bz;
    }

    public String getPicPath() {
        return PicPath;
    }

    public String getBz() {
        return bz;
    }

    public String getClsbm() {
        return clsbm;
    }

    public String getCx() {
        return cx;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
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

    public String getSjID() {
        return sjID;
    }

    public String getSjhm() {
        return sjhm;
    }

    public String getYs() {
        return ys;
    }

    public void setPicPath(String picPath) {
        PicPath = picPath;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public void setClsbm(String clsbm) {
        this.clsbm = clsbm;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLng(float lng) {
        this.lng = lng;
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

    public void setSjID(String sjID) {
        this.sjID = sjID;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public void setYs(String ys) {
        this.ys = ys;
    }
}
