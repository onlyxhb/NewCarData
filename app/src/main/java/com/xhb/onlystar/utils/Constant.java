package com.xhb.onlystar.utils;

import android.util.ArrayMap;

import com.amap.api.location.AMapLocation;
import com.xhb.onlystar.bean.AppResultOfMxb;
import com.xhb.onlystar.bean.AppResultOfZb;
import com.xhb.onlystar.bean.Base;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.bean.Rwzb;
import com.xhb.onlystar.bean.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by onlyStar on 2016/3/15.
 */
public class Constant {
    public static final int READ_PHONE_STATE=1;//获取手机号权限
    public static final int CAMERA_STATE=2;//获取手机号权限
    public static final int LOCATION_STATE=3;//获取定位权限
    public static final int READ_SD_STATE=5;//获取读取sd权限
    public static final String IP="http://61.183.41.211:8089";
    public static final String BASE=IP+"/WCFService/APPJsonService.svc";
    public static final String UPLOAD_BASE=IP+"/WCFService/UpLoad/UpLoadApp.svc";
    public static final String LOGIN=BASE+"/LoginIn";//登陆
    public static final String RMZB=BASE+"/getRwzbList";//任务总表
    public static final String RWMXB=BASE+"/getRwmxbList";//任务明细表
    public static final String SAVE=BASE+"/Save_pzrwTj";//任务提交
    public static final String CreateUpload=UPLOAD_BASE+"/CreateUpload";//创建上传任务
    public static final String CreateUpload2=UPLOAD_BASE+"/CreateUpload2";//创建上传任务
    public static final String BeginUpload=UPLOAD_BASE+"/BeginUpload";//分块上传文件
    public static final String BeginUpload2=UPLOAD_BASE+"/BeginUpload2";//分块上传文件
    public static final String GETFILEURL=BASE+"/getUploadFileUrlList";//获取照片列表
    public static final String APPSIGN=BASE+"/AppSign";//签到
    public static  boolean login_state=false;
    public static User user=new User();
    public static Rwzb rwzb=new Rwzb();//
    public static Rwmxb task=new Rwmxb();//
    public static List<Rwmxb> data = new ArrayList<Rwmxb>();
    public  static boolean isRefreshMxb=false;
    public  static boolean isRefreshZb=false;
    public static AppResultOfZb RWZB_RESULT=new AppResultOfZb();
    public static AppResultOfMxb RWMXB_RESULT=new AppResultOfMxb();
    public static Base Rwzb_Base=new Base();
    public static Base Rwzmxb_Base=new Base();
    public static AMapLocation location;
    public static List<AppResultOfMxb> appResultOfMxb=new ArrayList<AppResultOfMxb>();
}
