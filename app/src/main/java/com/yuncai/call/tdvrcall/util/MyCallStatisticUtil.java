package com.yuncai.call.tdvrcall.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import com.yuncai.call.tdvrcall.stat_interface.IStaticstic;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;

/**
 * 功能：通话记录统计
 * @author TianMing.Xiong
 *
 */
public class MyCallStatisticUtil {
	// 创建一个放录音文件的文件夹 // 语音文件保存路径
	private static final String FilePath = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/VoiceRecord";
	private static MyCallStatisticUtil instance;
	private ArrayList<String> recordFiles = new ArrayList<String>();

	private MyCallStatisticUtil() {
	}

	public static MyCallStatisticUtil getInstance() {
		if (instance == null) {
			return new MyCallStatisticUtil();
		}
		return instance;
	}

	/**
	 * 功能：查询从paramLong至今的所有通话记录
	 * @param context
	 * @param paramLong 时间戳
	 */
	public void collectData(Context context, long paramLong, IStaticstic is) {
		rlCollectData(context, paramLong, (new Date()).getTime(), is);
	}

	/**
	 * 功能：查询从startParamLong到endParamLong时间段的所有通话记录
	 *
	 * @param context
	 * @param startParamLong  时间戳
	 *            开始时间
	 * @param endParamLong
	 *            结束时间
	 * @param is
	 *            回调接口
	 */
	@SuppressWarnings("deprecation")
	public void rlCollectData(final Context context, final long startParamLong,
							  final long endParamLong, final IStaticstic is) {
		final ArrayList<Long> callDates = new ArrayList<Long>();
		final ArrayList<Long> callDurations = new ArrayList<Long>();
		final ArrayList<Integer> callTypes = new ArrayList<Integer>();
		final ArrayList<String> phoneNumbers = new ArrayList<String>();
		phoneNumbers.clear();
		callDates.clear();
		callTypes.clear();
		callDurations.clear();

		Executors.newCachedThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
					return;
				}
				Cursor localCursor = context.getContentResolver().query(
						CallLog.Calls.CONTENT_URI,
						null,
						"date >= " + startParamLong + " " + " AND date <="
								+ endParamLong, null, "date DESC");
				((Activity) context).startManagingCursor(localCursor);

				int i = localCursor.getColumnIndex("number");
				int j = localCursor.getColumnIndex("date");
				int k = localCursor.getColumnIndex("type");
				int m = localCursor.getColumnIndex("duration");
				while (localCursor.moveToNext()) {
					String str = localCursor.getString(i);
					long l1 = localCursor.getLong(j);
					int n = localCursor.getInt(k);
					long l2 = localCursor.getLong(m);
					phoneNumbers.add(str);
					callDates.add(Long.valueOf(l1));
					callTypes.add(Integer.valueOf(n));
					callDurations.add(Long.valueOf(l2));
				}
				// 查询录音数量
				int allSoundRecord = 0;
				allSoundRecord = getRecordFiles(startParamLong,endParamLong);
				if (null != is) {
					is.showData(phoneNumbers, callDates, callTypes,callDurations,allSoundRecord);
				}
				if(VERSION.SDK_INT < 14) {
					localCursor.close();
				 }

			}
		});
	}

	// 存储一个音频文件数组到list当中
	private synchronized int getRecordFiles(long startParamLong, long endParamLong) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File myRecAudioDir = new File(FilePath);
			if(myRecAudioDir!=null && !myRecAudioDir.exists()){
				myRecAudioDir.mkdirs();
			}
			File files[] = myRecAudioDir.listFiles();
			if (files != null) {
				recordFiles.clear();
				int myleng = files.length;
				for (int i = 0; i < myleng; i++) {
					if (files[i].getName().indexOf(".") >= 0) {
							/* 只取.mp3文件 */
						String fileS = files[i].getName().substring(
								files[i].getName().indexOf("."));
						if (".mp3".equals(fileS) || ".wav".equals(fileS)){
							File file = new File(files[i].getAbsolutePath());
                            long lastTime = file.lastModified();
							if((startParamLong < lastTime) && (lastTime < endParamLong)){
                                recordFiles.add(file.getAbsolutePath());
                            }
						}
					}
				}

			}
		}
		return recordFiles.size();
	}


}