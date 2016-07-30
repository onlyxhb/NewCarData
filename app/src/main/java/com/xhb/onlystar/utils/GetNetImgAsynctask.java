package com.xhb.onlystar.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.xhb.onlystar.bean.*;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.handle;
import static android.R.attr.path;

/**
 * Created by onlystar on 2016/6/18.
 */

public class GetNetImgAsynctask extends AsyncTask<Void, Void, List<String>> {
    private Context context;
    private Rwmxb task;
    private ProgressDialog dialog;
    private Handler handler;


    public GetNetImgAsynctask(Rwmxb task,ProgressDialog dialog,Handler handler, Context context) {
        this.task = task;
        this.dialog=dialog;
        this.handler=handler;
        this.context = context;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        List<String> filePic = new ArrayList<String>();
        GetUploadFileUrlReback reback = DataOperation.getUploadFileUrlList(task.getRwdh(), task.getClsbm());
        if (reback.getCode() != 0) {
            return null;
        } else {
            for (FileUrl url : reback.getResult()) {
                String pic = Constant.IP + url.getPicPath();
                String path = DataCleanManager.getCachePath(context);
                //JAVA中正则表达式,用"\\\\"表示"\"
                pic = pic.replaceAll("\\\\", "/");
                Log.i("myLog", "SendDataActiviy------------>pic:" + pic);
                if (checkURL(pic)) {
                    byte[] data = new byte[0];
                    try {
                        data = DataOperation.getImage(pic);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        if (bitmap != null) {
                            bitmap = Photo.compressImage(bitmap);
                            Photo.saveFile(bitmap, new File(path));
                            filePic.add(path);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return filePic;
    }

    @Override
    protected void onPostExecute(List<String> reback) {
        if(dialog!=null){
            dialog.dismiss();
        }
        Message message = new Message();
        message.what = 4;
        Bundle bundle=new Bundle();
        bundle.putString("reback",new Gson().toJson(reback));
        message.setData(bundle);
        handler.sendMessage(message);
        super.onPostExecute(reback);
    }

    public static boolean checkURL(String url) {
        boolean value = false;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            int code = conn.getResponseCode();
            Log.i("myLog", "picPath的url响应码:" + code);
            if (code != 200) {
                value = false;
            } else {
                value = true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

}
