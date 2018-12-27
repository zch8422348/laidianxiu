package com.yuncai.call.tdvrcall.leavingmsg;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yuncai.call.tdvrcall.R;
import com.yuncai.call.tdvrcall.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import de.greenrobot.event.EventBus;

public class YinListFragment extends Fragment {
	
	private ListView lv_luyin_list;
	private boolean sdCardExit;
	private ArrayList<String> recordFiles,liuyanNameLists;
	private ArrayAdapter<String> adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_luyin_right, container);
		lv_luyin_list = (ListView)v.findViewById(R.id.lv_luyin_list);
		lv_luyin_list. setEmptyView (v.findViewById(R.id.ll_no_liuyan));  

		// 
		initData();
		return v;
	}

	private void initData() {
		//存放留言录音文件名
		recordFiles = new ArrayList<String>();
		liuyanNameLists = new ArrayList<>() ;
		/* 判断SD Card是否插入 */
		sdCardExit = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		// 
		getData();
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, liuyanNameLists);
		/* 将ArrayAdapter添加ListView对象中 */
		lv_luyin_list.setAdapter(adapter);
		lv_luyin_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					File myPFile = new File(recordFiles.get(position));
					playAudio(myPFile.getAbsolutePath());
				

			}
		});
		
		//长按删除
		lv_luyin_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
//				ToastUtils.showToast("长按~~");
				// TODO Auto-generated method stub
				AlertDialog.Builder bulder = new AlertDialog.Builder(getActivity());
				bulder.setTitle("提示");
				bulder.setMessage("您将要删除此条留言吗？一旦删除将不能恢复！");
				bulder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String pathName = recordFiles.get(position);
						File file = new File(pathName);
						if(file.isFile()){
							file.delete();
							EventBus.getDefault().post("add_liuyan");
							ToastUtils.showToast("文件已经删除！");
						}
						dialog.dismiss();
					}
				});
				bulder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				bulder.create().show();
				return true;
			}
		});
		
		
	}
	
	
	private void getData() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		path += "/liuyan/temp";
		//判断文件夹是否存在
		File file=new File(path);
		//先清空数据
		recordFiles.clear();
		liuyanNameLists.clear();
		/* 取得SD Card目录里的所有.mp3文件 */
		getRecordFiles(file);
		
	}

		// 存储一个音频文件数组到list当中
		private void getRecordFiles(File myRecAudioDir) {
			if (sdCardExit) {
				File files[] = myRecAudioDir.listFiles();
				if (files != null) {
					int myleng = files.length;
					for (int i = 0; i < myleng; i++) {
						if(files[i].isDirectory()){
							getRecordFiles(files[i]);
						}else{
							if (files[i].getName().indexOf(".") >= 0) {
								/* 只取.amr文件 */
								String fileS = files[i].getName().substring(
										files[i].getName().indexOf("."));
								if (fileS.toLowerCase().equals(".amr"))
									recordFiles.add(files[i].getAbsolutePath());
								    //只保存名字
//								    String name = files[i].getAbsolutePath() ;
//								    String[] strings = name.split("/");
//								     String realName = strings[strings.length-1]; 
								    liuyanNameLists.add(files[i].getName());
							}
						}
						
					}
					
					//排序
				     Collections.sort(recordFiles);
				     Collections.reverse(recordFiles);
				     Collections.sort(liuyanNameLists);
				     Collections.reverse(liuyanNameLists);
				}
			}
		}

		
		/**
		 * 播放指定名称的歌曲
		 * @param audioPath
		 *  指定默认播放的音乐
		 */
		public void playAudio(String audioPath) {
			Intent mIntent = new Intent();
			mIntent.setAction(Intent.ACTION_VIEW);
			Uri uri = Uri.parse("file://" + audioPath);
			mIntent.setDataAndType(uri, "audio/mp3");
			getActivity().startActivity(mIntent);
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			EventBus.getDefault().unregister(this);
		}
		
		//添加后刷新列表
		public void onEventMainThread(String event) {
			//来电唤醒，隐藏首页
			if("add_liuyan".equals(event)){
				if(null!=adapter){
					getData();
					adapter.notifyDataSetChanged();
				}
			}
		
		}


}
