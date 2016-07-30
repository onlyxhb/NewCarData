package com.xhb.onlystar.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xhb.onlystar.newcardata.MainActivity_hq;
import com.xhb.onlystar.newcardata.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.R.attr.path;

/**
 * Created by onlystar on 2016/5/2.
 */
public class MyUtils {
    public static <T> T getData(String url, String tip, Class<T> valueTyp, String param) {
        String reback = HttpRequest.sendPost(url, param);//返回值
        //Log.e("myLog", "请求地址:"+url+"\n请求类型:" + tip + "  请求值:"+param+"\n返回值:" + reback);
        Log.e("myLog", "请求类型:" + tip + " 返回值:" + reback);
        if (!TextUtils.isEmpty(reback)) {
            JsonObject object = new JsonParser().parse(reback).getAsJsonObject();
            String result = object.get(tip).toString();
            return new Gson().fromJson(result, valueTyp);
        } else return null;

    }


    public static List<String> getListByJson(String json){
        List<String> path=new ArrayList<String>();
        JsonElement jsonElement=new JsonParser().parse(json);  //将json字符串转换成JsonElement
        JsonArray jsonArray=jsonElement.getAsJsonArray();  //将JsonElement转换成JsonArray
        Iterator it=jsonArray.iterator();  //Iterator处理
        while(it.hasNext()){  //循环
            jsonElement=(JsonElement) it.next(); //提取JsonElement
            json=jsonElement.toString();  //JsonElement转换成String
            String mPath=new Gson().fromJson(json, String.class); //String转化成JavaBean
            path.add(mPath);  //加入List
        }
        return path;
    }

//    public static List<String> getPicDataByCarNum(String car_num) {
//        List<String> data = new ArrayList<>();
//        ArrayList<ImageItem> ImageData = new ArrayList<ImageItem>();
//        for (moreUpload item : MyApplication.uploads) {
//            if (item.getTask().getClsbm() == car_num || item.getTask().getClsbm().equals(car_num)) {
//                ImageData = item.getPic();
//                Constant.task = item.getTask();
//            }
//        }
//        for (ImageItem image : ImageData) {
//            data.add(image.getImagePath());
//        }
//        return data;
//    }


//    public static ArrayList<ImageItem> getPicDataByCarNum(Task task) {
//        ArrayList<ImageItem> ImageData = new ArrayList<ImageItem>();
//        for (moreUpload item : MyApplication.uploads) {
//            if (item.getTask().getClsbm() == task.getClsbm() || item.getTask().getClsbm().equals(task.getClsbm())) {
//                ImageData = item.getPic();
//                Constant.task = item.getTask();
//            }
//        }
//        return ImageData;
//    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String getCurrentLongTime() {
        Long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return sdf.format(time);
    }

    public static String getToday() {
        Long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(time);
    }

    public static String getCurrentLongTimeOfPzsj() {
        Long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(time);
    }

    public static String getTimeMd5(String RequestTime) {
        MD5 md5 = new MD5();
        String pass = "hysoft";
        return md5.encrypByMd5(pass + RequestTime + pass).toUpperCase();
    }


    public static boolean selfPermissionGranted(String permission, Context context) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Integer.valueOf(android.os.Build.VERSION.SDK) >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return result;
    }


    public static void notification(String content, Context context) {
        Bitmap btm = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        Notification.Builder builder = new Notification.Builder(context);
        Intent intent = new Intent(context,
                MainActivity_hq.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pi);
        builder.setSmallIcon(R.mipmap.ic_launcher);//顶部小图标

        //对 5.0 及以上版本做兼容性处理
        // boolean isAboveLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        //builder.setSmallIcon(isAboveLollipop ? R.drawable.small_icon : R.drawable.white_small_icon);

        builder.setTicker(content);//顶部通知信息
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setLargeIcon(btm);//通知栏图标
        builder.setAutoCancel(true);
        builder.setContentText(content);//通知栏内容
        builder.setContentTitle("新任务发布");//通知栏标题
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }


    public static void checkPermission(Context context, Activity activity, String permission, int requestCode) {
        boolean state = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        if (!state) {
            return;
        }
        int result = ContextCompat.checkSelfPermission(context, permission);
        if (result == PackageManager.PERMISSION_GRANTED) { //有权限---可以操作
        } else if (result == PackageManager.PERMISSION_DENIED) {//没有权限----申请权限
            state = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if (state) {//可以得到一个解释该权限的提示给用户
                //展示提示信息给用户，用户看完提示信息后再次请求权限
            } else {//不展示提示信息，直接请求权限
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        requestCode);
            }
        } else {
            //执行获取权限后的操作
        }
    }
}
