package com.yuncai.call.tdvrcall.leavingmsg;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.yuncai.call.tdvrcall.R;
import com.yuncai.call.tdvrcall.util.ToastUtils;
import com.yuncai.call.tdvrcall.view.RecordButton;

import java.io.File;

import de.greenrobot.event.EventBus;


public class LuYinFragment extends Fragment {
	
	private RecordButton mRecordButton = null;
	private Button angry_btn1;
	private Button angry_btn2;
	private Button angry_btn3;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_luyin_left, container);
		initView(v);
		initData();
		return v;
	}

	private void initData() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		path += "/liuyan/temp";
		//判断文件夹是否存在
		File file=new File(path);
		if(!file.exists()){
			file.mkdirs();//如果不存在则创建
			}

		mRecordButton.setSavePath(file.getAbsolutePath()+"/");
		mRecordButton
				.setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {

					@Override
					public void onFinishedRecord(String audioPath) {
						ToastUtils.showToast("录音完成！");
						//刷新列表
						EventBus.getDefault().post("add_liuyan");
						

					}
				});
		//点击播放预览（/luyin/temp）
		angry_btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		//将录音文件保存到/luyin/liuyan文件夹下
		angry_btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		//删除当前录音文件
		angry_btn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		
	}

	private void initView(View v) {
		mRecordButton = (RecordButton) v.findViewById(R.id.record_button);
		angry_btn1 = (Button)v.findViewById(R.id.angry_btn1);
		angry_btn2 = (Button)v.findViewById(R.id.angry_btn2);
		angry_btn3 = (Button)v.findViewById(R.id.angry_btn3);

	}

}
