package com.xhb.onlystar.fragment;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.xhb.onlystar.newcardata.R;
import com.xhb.onlystar.platescan.ACameraActivity;
import com.xhb.onlystar.platescan.utils.HttpUtil;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CarDataFragment extends Fragment {

    private static final String tag = "myLog";
    private final int IMPORT_CODE=1;
    private final int TAKEPHOTO_CODE=2;
    private TextView tvResult;
    private static byte[] bytes;
    private static String extension;
    private LinearLayout ll_progress;
    private Button importImg,takePhoto;
    public static final String action="plate.scan";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_data, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        tvResult = (TextView) view.findViewById(R.id.tv_result);
        ll_progress = (LinearLayout) view.findViewById(R.id.ll_progress);
        importImg= (Button) view.findViewById(R.id.importImg);
        takePhoto= (Button) view.findViewById(R.id.takePhoto);


        importImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importImg();
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

    }



    public void importImg() {
        tvResult.setText("");
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMPORT_CODE);
    }

    /**
     * 拍照
     */
    public void takePhoto(){
        Intent intent = new Intent(getActivity(),ACameraActivity.class);
        startActivityForResult(intent, TAKEPHOTO_CODE);
    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent data) {
        super.onActivityResult(arg0, arg1, data);
        if(data==null){
            return;
        }
        Uri uri = data.getData();
        if(arg1== Activity.RESULT_OK){
            switch (arg0) {
                case IMPORT_CODE:
                    if(uri==null){
                        return;
                    }
                    try {
                        String uriPath = getUriAbstractPath(uri);
                        extension = getExtensionByPath(uriPath);
                        InputStream is=getActivity().getContentResolver().openInputStream(uri);
                        bytes = HttpUtil.Inputstream2byte(is);
                        if(!(bytes.length>(1000*1024*5))){
                            new MyAsynTask().execute();
                        }else{
                            Toast.makeText(getActivity(), "图片太大！！！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case TAKEPHOTO_CODE:
                    if(tvResult.getVisibility()==View.GONE){
                        tvResult.setVisibility(View.VISIBLE);
                    }
                    tvResult.setText("");
                    String result = data.getStringExtra("result");
                    Log.d(tag, "result:  "+result);
                    setMsg(result);
                    break;
            }
        }
    }

    /**
     * 根据路径获取文件扩展名
     * @param path
     */
    private String getExtensionByPath(String path) {
        if(path!=null){
            return path.substring(path.lastIndexOf(".")+1);
        }
        return null;
    }


    /**
     * 根据uri获取绝对路径
     * @param uri
     */
    private String getUriAbstractPath(Uri uri) {
        {
            // can post image
            String [] proj={MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().managedQuery( uri,proj,null,null,null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);
        }
    }

    class MyAsynTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            ll_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            return startScan();
        }

        @Override
        protected void onPostExecute(String result) {
            if(result!=null){
                ll_progress.setVisibility(View.GONE);
                handleResult(result);
            }
        }

    }

    /**
     * 处理服务器返回的结果
     * @param result
     */
    private void handleResult(String result) {
        tvResult.setVisibility(View.VISIBLE);
        setMsg(result);
    }

    public String startScan(){
        String xml = HttpUtil.getSendXML(action,extension);
        return HttpUtil.send(xml,bytes);
    }



    public void setMsg(String msg){
        int statusIndex=msg.indexOf("</status>");
        String status=msg.substring(14, statusIndex);
        if(status.equals("OK")){
            int numIndex=msg.indexOf("</platenumber>");
            String result=msg.substring(50, numIndex);
            tvResult.setText("车牌号为"+result);
        }else{
            tvResult.setText("车牌号识别失败");
        }


    }
}
