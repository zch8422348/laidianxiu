package com.yuncai.call.tdvrcall.util;

import android.widget.Toast;

import com.yuncai.call.tdvrcall.CallApplication;

public class ToastUtils {
	

	private static Toast mToast;

	/**
	 * 显示Toast
	 */
	public static void showToast(String msg){
		if (mToast == null) {
			mToast = Toast.makeText(CallApplication.getContext(), "", Toast.LENGTH_SHORT);
		}
		mToast.setText(msg);
		mToast.show();
}
}