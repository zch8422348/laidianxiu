package com.yuncai.call.tdvrcall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuncai.call.tdvrcall.R;


/**
 * Function:
 * Created by TianMing.Xiong on 18-6-25.
 * 有个返回监听：onBackOnclick
 */

public class CommomTitleBar extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private TextView tvTitleBar;
    private String mTitleText;

    public CommomTitleBar(Context context) {
        super(context);
        initView(context);
    }

    public CommomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        mTitleText = attrs.getAttributeValue(NAMESPACE, "title_text");
        setTitleText(mTitleText);
    }

    public CommomTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        mTitleText = attrs.getAttributeValue(NAMESPACE, "title_text");
        setTitleText(mTitleText);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.commom_title_bar, this);
        tvTitleBar = view.findViewById(R.id.tv_title_bar);
    }

    public void setTitleText(String text){
        if(null!=tvTitleBar){
            tvTitleBar.setText(""+text);
        }
    }



}
