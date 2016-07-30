package com.xhb.onlystar.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.xhb.onlystar.bean.AppResultOfMxb;
import com.xhb.onlystar.bean.AppResultOfZb;
import com.xhb.onlystar.bean.AppSign;
import com.xhb.onlystar.bean.BeginUpload;
import com.xhb.onlystar.bean.CreateUpload;
import com.xhb.onlystar.bean.GetRwList;
import com.xhb.onlystar.bean.GetUploadFileUrl;
import com.xhb.onlystar.bean.GetUploadFileUrlReback;
import com.xhb.onlystar.bean.Login;
import com.xhb.onlystar.bean.LoginReback;
import com.xhb.onlystar.bean.Rwmxb;
import com.xhb.onlystar.bean.Rwzb;
import com.xhb.onlystar.bean.Save_pzrwTj;
import com.xhb.onlystar.bean.Save_pzrwTjReback;
import com.xhb.onlystar.bean.SignInfo;
import com.xhb.onlystar.bean.UploadInfo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by onlystar on 2016/5/9.
 */
public class DataOperation {

    //登陆信息的处理与获取
    public static LoginReback login(String UserId, String Pwd) {
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        Login user = new Login(UserId, Pwd, RequestTime, TimeMd5);
        String data = new Gson().toJson(user);
        LoginReback loginReback = MyUtils.getData(Constant.LOGIN, "LoginInResult", LoginReback.class, data);
        return loginReback;
    }

    //获取任务总表信息------无条件
    public static AppResultOfZb getRwzbData(String date) {
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        List<String> queryCondition = new ArrayList<String>();
        Log.e("myLog", "任务总表的Grdm:" + Constant.user.getGrdm());
        queryCondition.add("pzr#" + Constant.user.getGrdm());
        queryCondition.add("rwsj#"+date);
        GetRwList param = new GetRwList(queryCondition, RequestTime, TimeMd5);
        String data = new Gson().toJson(param);
        Log.e("myLog","获取任务总表request："+data);
        AppResultOfZb zbResult = MyUtils.getData(Constant.RMZB, "getRwzbListResult", AppResultOfZb.class, data);
        return zbResult;
    }


    //获取任务总表信息------未提交0--------->pzzt==0
    public static AppResultOfZb getRwzbData_noCommte() {
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        List<String> queryCondition = new ArrayList<String>();
        Log.e("myLog", "任务总表的Grdm:" + Constant.user.getGrdm());
        queryCondition.add("pzr#" + Constant.user.getGrdm());
        queryCondition.add("pzzt#0");
        GetRwList param = new GetRwList(queryCondition, RequestTime, TimeMd5);
        String data = new Gson().toJson(param);
        AppResultOfZb zbResult = MyUtils.getData(Constant.RMZB, "getRwzbListResult", AppResultOfZb.class, data);
        return zbResult;
    }

    //获取任务总表信息------已提交1--------->pzzt==1
    public static AppResultOfZb getRwzbData_haveCommte() {
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        List<String> queryCondition = new ArrayList<String>();
        Log.e("myLog", "任务总表的Grdm:" + Constant.user.getGrdm());
        queryCondition.add("pzr#" + Constant.user.getGrdm());
        queryCondition.add("pzzt#1");
        GetRwList param = new GetRwList(queryCondition, RequestTime, TimeMd5);
        String data = new Gson().toJson(param);
        AppResultOfZb zbResult = MyUtils.getData(Constant.RMZB, "getRwzbListResult", AppResultOfZb.class, data);
        return zbResult;
    }

    //获取任务明细表内容
    public static List<AppResultOfMxb> getRwmxbData(AppResultOfZb zbResult) {
        List<AppResultOfMxb> mxbResult = new ArrayList<AppResultOfMxb>();
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        List<String> queryCondition = new ArrayList<String>();
        //获取所有的任务明细表
        for (Rwzb zwzb : zbResult.getResult()) {
            Log.e("myLog", "任务明细表的rwdh:" + zwzb.getRwdh());
            queryCondition.clear();
            queryCondition.add("rwdh#" + zwzb.getRwdh());
            GetRwList param = new GetRwList(queryCondition, RequestTime, TimeMd5);
            String data = new Gson().toJson(param);
            //Log.e("myLOg","任务明细表:"+data);
            mxbResult.add(MyUtils.getData(Constant.RWMXB, "getRwmxbListResult", AppResultOfMxb.class, data));
        }
        return mxbResult;
    }

    // 任务提交
    public static Save_pzrwTjReback Save_pzrwTj(Rwzb rwzbModel, List<Rwmxb> rwmxbList) {
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        Save_pzrwTj save_pzrwTJ = new Save_pzrwTj(rwzbModel, rwmxbList, RequestTime, TimeMd5);
        String data = new Gson().toJson(save_pzrwTJ);
        Save_pzrwTjReback save_pzrwTjReback = MyUtils.getData(Constant.SAVE, "Save_pzrwTjResult",
                Save_pzrwTjReback.class, data);
        return save_pzrwTjReback;
    }

    // 创建上传任务
    public static UploadInfo createUpload(Rwmxb RwmxModel, String FileName, int FileSize) {
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        CreateUpload upLoad = new CreateUpload(RwmxModel, FileName, FileSize, RequestTime, TimeMd5);
        String data = new Gson().toJson(upLoad);
        UploadInfo createUploadReback = MyUtils.getData(Constant.CreateUpload, "CreateUploadResult", UploadInfo.class,
                data);
        return createUploadReback;
    }

    // 分块上传文件
    public static UploadInfo beginUpload(UploadInfo Fileinfo, String FileContext, String Compression) {
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        String StrMd5 = new MD5().encrypByMd5(FileContext);
        BeginUpload beginUpload = new BeginUpload(Fileinfo, FileContext, Compression, StrMd5, RequestTime, TimeMd5);
        String data = new Gson().toJson(beginUpload);
        UploadInfo createUploadReback = MyUtils.getData(Constant.BeginUpload, "BeginUploadResult", UploadInfo.class,
                data);
        return createUploadReback;
    }

    // 创建上传任务2
    public static UploadInfo createUpload2(Rwmxb RwmxModel, String FileName, long FileSize) {
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        CreateUpload upLoad = new CreateUpload(RwmxModel, FileName, FileSize, RequestTime, TimeMd5);
        String data = new Gson().toJson(upLoad);
        UploadInfo createUploadReback = MyUtils.getData(Constant.CreateUpload2, "CreateUpload2Result", UploadInfo.class,
                data);
        return createUploadReback;
    }

    // 分块上传文件2
    public static UploadInfo beginUpload2(UploadInfo Fileinfo, String FileContext, String Compression) {
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        String StrMd5 = new MD5().encrypByMd5(FileContext);
        BeginUpload beginUpload = new BeginUpload(Fileinfo, FileContext, Compression, StrMd5, RequestTime, TimeMd5);
        String data = new Gson().toJson(beginUpload);
        UploadInfo createUploadReback = MyUtils.getData(Constant.BeginUpload2, "BeginUpload2Result", UploadInfo.class,
                data);
        return createUploadReback;

    }

    //获取照片列表
    public static GetUploadFileUrlReback getUploadFileUrlList(String rwdh, String clsbm){
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        GetUploadFileUrl getUploadFileUrl=new GetUploadFileUrl(rwdh,clsbm,RequestTime,TimeMd5);
        String data = new Gson().toJson(getUploadFileUrl);
        GetUploadFileUrlReback result=MyUtils.getData(Constant.GETFILEURL, "getUploadFileUrlListResult", GetUploadFileUrlReback.class,
                data);
        return result;

    }

    //签到
    public static GetUploadFileUrlReback AppSign(SignInfo SignModel){
        String RequestTime = MyUtils.getCurrentLongTime();
        String TimeMd5 = MyUtils.getTimeMd5(RequestTime);
        AppSign appSign=new AppSign(SignModel,RequestTime,TimeMd5);
        String data = new Gson().toJson(appSign);
        Log.e("myLog","签到request:"+data);
        GetUploadFileUrlReback result=MyUtils.getData(Constant.APPSIGN, "AppSignResult", GetUploadFileUrlReback.class,
                data);
        return result;
    }



    //加载网络图片
    public static Bitmap getBitmapByUrl(String url) {
        Bitmap bm=null;
        try {
            URL aURL = new URL(url);
            URLConnection con = aURL.openConnection();
            con.connect();
            InputStream is = con.getInputStream();
          /* 建立缓冲区是一个良好的编程习惯. */
            BufferedInputStream bis = new BufferedInputStream(is);
            /* 解析网络上的图片 */
             bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            /* 这时图片已经被加载到ImageView中. */
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    public static byte[] getImage(String path) throws IOException {
        URL url = new URL(path.toString().trim());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");   //设置请求方法为GET
        conn.setReadTimeout(5*1000);    //设置请求过时时间为5秒
        InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据
        byte[] data =readInputStream(inputStream);     //获得图片的二进制数据
        return data;

    }

    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();

    }

}
