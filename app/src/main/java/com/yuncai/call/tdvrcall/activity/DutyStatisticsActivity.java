package com.yuncai.call.tdvrcall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yuncai.call.tdvrcall.R;
import com.yuncai.call.tdvrcall.stat_interface.IStaticstic;
import com.yuncai.call.tdvrcall.util.MyCallStatisticUtil;
import com.yuncai.call.tdvrcall.util.TimeUtil;

import java.util.ArrayList;

/**
 * 值班统计
 */
public class DutyStatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_statistics);
        MyCallStatisticUtil.getInstance().collectData(this,0 ,new IStaticstic() {

            @Override
            public void showData(final ArrayList<String> phoneNumbers,
                                 final ArrayList<Long> callDates, final ArrayList<Integer> callTypes,
                                 final ArrayList<Long> callDurations,final int allSoundRecord) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        reFreshUI(phoneNumbers, callDates, callTypes, callDurations,allSoundRecord);
                    }
                });
            }
        });
    }

    private void reFreshUI(ArrayList<String> phoneNumbers, ArrayList<Long> callDates, ArrayList<Integer> callTypes, ArrayList<Long> callDurations, int allSoundRecord) {
        //总电话数
        //总录音
        //打电话总时长
        //打进
        //打出
        //未接
        String info = "";

        //总电话数
        int count1 = callTypes.size() ;
        info+="总电话数："+callTypes.size()+"\n";
        //总录音
        int count5=allSoundRecord;
        info+="总录音："+allSoundRecord+"\n";
        //打电话总时长
        long alltime = 0 ;
        for (Long long1 : callDurations) {
            alltime+=long1;
        }
        info+="总电话时长："+ TimeUtil.generateTime(alltime*1000)+"\n";
        int  a=0,b=0,c=0 ;
        for (Integer integer : callTypes) {
            //1打进2打出3未接
            if(1==integer){
                a++ ;
            }
            if(2==integer){
                b++ ;
            }
            if(3==integer){
                c++ ;
            }
        }
        //打进
        int count2 = a ;
        info+="打进："+a+"\n";
        //打出
        int count3 = b ;
        info+="打出："+b+"\n";
        //未接
        int count4 = c ;
        info+="未接："+c;

        TextView tvStatInfo = (TextView) findViewById(R.id.tv_stat_info);
        tvStatInfo.setText(info);


    }
}
