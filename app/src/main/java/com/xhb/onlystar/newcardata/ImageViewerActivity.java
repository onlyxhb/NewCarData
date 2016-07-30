package com.xhb.onlystar.newcardata;
import com.xhb.onlystar.utils.Photo;
import com.xhb.onlystar.widget.caldendar.RoundImageViewHead;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import android.net.*;

public class ImageViewerActivity extends BaseActivity {
	private RoundImageViewHead imageviewer;
	private Bitmap bitmap;
	private LinearLayout wholeWindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_viewer);
		imageviewer = (RoundImageViewHead) findViewById(R.id.imageviewer);
		Intent intent = getIntent();
		if (intent != null) {
			String picPath=intent.getStringExtra("picPath");
			bitmap = Photo.getBitmapFromPath(picPath);
			if(bitmap!=null){
				Matrix matrix = new Matrix(); //接收图片之后放大 1.2倍
				matrix.postScale(1.2f, 1.2f);
				Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
				imageviewer.setImageBitmap(bit);
			}
			//下面是以原图显示
//			bitmap = intent.getParcelableExtra("bitmap");
//			imageviewer.setImageBitmap(bitmap);
		}
		/*wholeWindow=(LinearLayout) findViewById(R.id.whole_window);
		wholeWindow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/



	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
