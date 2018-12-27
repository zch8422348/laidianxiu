package com.yuncai.call.tdvrcall.leavingmsg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.yuncai.call.tdvrcall.R;


public class RecordingActivity extends Activity {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.record_activity);
	}

	//返回键
	public void onBackOnclick(View view){
		RecordingActivity.this.finish();
	}
}
