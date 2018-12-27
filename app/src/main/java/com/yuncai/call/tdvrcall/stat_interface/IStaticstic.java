package com.yuncai.call.tdvrcall.stat_interface;

import java.util.ArrayList;

public interface IStaticstic {
    /**
     * 电话查询统计结果回调
     * @param phoneNumbers 查询到的所有电话号码集合
     * @param callDates 查询到的所有电话时间集合
     * @param callTypes 查询到所有电话电话类型集合
     * @param callDurations 查询到所有电话时长集合
     */
	void showData(ArrayList<String> phoneNumbers, ArrayList<Long> callDates,
                  ArrayList<Integer> callTypes, ArrayList<Long> callDurations, int allSoundRecord);

	
}
