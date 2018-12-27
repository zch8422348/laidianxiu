package com.yuncai.call.tdvrcall.stat_interface;


import com.yuncai.call.tdvrcall.bean.RecordBean;

import java.util.ArrayList;

public interface ICallBack {
	void returnData(ArrayList<RecordBean> callLogs);

}
