package com.xhb.onlystar.widget.caldendar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhb.onlystar.newcardata.R;

/**
 * Created by 何清 on 2016/5/13 0013.
 *
 * @description
 */
public class ImageAndTextView extends LinearLayout {

    private Context mContext;
    private ImageView mImageView;
    private TextView mTextView;

    public ImageAndTextView(Context context) {
        super(context);
        mContext = context;
        initView();
    }
    public ImageAndTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initAttr(attrs);
    }
    public ImageAndTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
        initAttr(attrs);
    }

    private void initView(){
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT
                ,LayoutParams.MATCH_PARENT));
        setOrientation(VERTICAL);
        LayoutInflater.from(mContext).inflate(R.layout.image_and_text_layout, this);

        mImageView = (ImageView) findViewById(R.id.image_and_text_image);
        mTextView = (TextView) findViewById(R.id.image_and_text_text);
    }

    private void initAttr(AttributeSet attrs){
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs
                , R.styleable.image_and_text);
        int imageBgId = typedArray.getResourceId(
                R.styleable.image_and_text_image_bg, 0);
        ColorStateList textColor = typedArray.getColorStateList(
                R.styleable.image_and_text_text_color);
        String text = typedArray.getString(R.styleable.image_and_text_text);

        typedArray.recycle();

        mImageView.setBackgroundResource(imageBgId);
        if (textColor != null) {
            mTextView.setTextColor(textColor);
        }
        mTextView.setText(text);

    }

    public void setSelected(boolean selected){
        mImageView.setSelected(selected);
        mTextView.setSelected(selected);
    }



}
