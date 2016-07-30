package com.xhb.onlystar.widget.caldendar;

import com.android.volley.toolbox.NetworkImageView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageViewHead extends ImageView {
	private String namespace="http://shadow.com";
    private int color;

    public RoundImageViewHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
       // color=Color.parseColor(attrs.getAttributeValue(namespace, "BorderColor"));
       color=0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub    
        
        super.onDraw(canvas);    
        Paint paint = new Paint();
		/* 去锯齿 */
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(color);
		
		/* 设置paint的　style　为STROKE：空心 */
		paint.setStyle(Paint.Style.STROKE);
		/* 设置paint的外框宽度 */
		
		int defaultWidth = getWidth();
		int defaultHeight = getHeight();
		
		paint.setStrokeWidth(3);
		canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, defaultWidth, paint);
    }
}
