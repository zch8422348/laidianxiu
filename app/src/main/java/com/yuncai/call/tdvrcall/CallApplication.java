package com.yuncai.call.tdvrcall;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.yuncai.call.tdvrcall.util.PhoneUtil;
import com.yuncai.call.tdvrcall.util.ToastUtils;

/**
 * Function:
 * Created by TianMing.Xiong on 18-12-18.
 */

public class CallApplication extends Application {
    private static Context mContext;
    private static int currVolume;
    private TelephonyManager telephonyManager;
    // 电话状态
    private final String PHONE_STATE_UNKNOWN = "Unknown" ;
    private final String PHONE_STATE_IDLE = "idle" ;
    private final String PHONE_STATE_OFF_HOOK = "off_hook" ;
    private final String PHONE_STATE_RINGING = "ringing" ;
    String callStateStr = PHONE_STATE_UNKNOWN ;


    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;
        registerHeadsetPlugReceiver();
        registerPhoneListen();
    }


    private void registerPhoneListen() {
        String srvcName = Context.TELEPHONY_SERVICE;
        telephonyManager = (TelephonyManager) getSystemService(srvcName);
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE );
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        telephonyManager.listen(listener,PhoneStateListener.LISTEN_NONE);

    }

    PhoneStateListener listener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:// 不响铃也不在通话
                    callStateStr = PHONE_STATE_IDLE;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:// 正在通话
                    callStateStr = PHONE_STATE_OFF_HOOK ;
                    // TODO 显示自己的通话界面
                    break;
                case TelephonyManager.CALL_STATE_RINGING:// 电话响铃
                    callStateStr = PHONE_STATE_RINGING;
                    // TODO 显示自己的来电界面
                    break;
                    default:
                        break;
            }
            ToastUtils.showToast(callStateStr);
        }
    };

    public static Context getContext() {
        return mContext;
    }

    private void registerHeadsetPlugReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(headsetPlugReceiver, intentFilter);
    }

    private BroadcastReceiver headsetPlugReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
        if ("android.intent.action.HEADSET_PLUG".equals(action)) {
                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) {// 耳机拔出
                        handleHeadsetDisconnected();
                    }else {// 耳机插上
                        handleHeadsetConnected();
                    }
                }
            }
        }

    };

    private void handleHeadsetConnected() {
        ToastUtils.showToast("耳机连接！");
        // 如果响铃状态，接听
        if(PHONE_STATE_RINGING.equals(callStateStr)){
            ToastUtils.showToast("接听。。。。");
            // 接听前，静音一下消除杂音
            PhoneUtil.answer();
        }
    }

    private void handleHeadsetDisconnected() {
        ToastUtils.showToast("耳机拔出！");
        // 如果通话状态，挂断
        if(PHONE_STATE_OFF_HOOK.equals(callStateStr)){
            ToastUtils.showToast("挂断。。。。");
            PhoneUtil.hangup();
        }
    }


}
