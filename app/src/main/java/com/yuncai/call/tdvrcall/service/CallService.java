package com.yuncai.call.tdvrcall.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.yuncai.call.tdvrcall.db.QuickDialLab;
import com.yuncai.call.tdvrcall.util.LogUtils;

/**
 * 获取来电号码服务
 */
public class CallService extends Service {
    /**
     * 电话服务管理器
     */
    private TelephonyManager telephonyManager;
    /**
     * 电话状态监听器
     */
    private MyPhoneStateListener myPhoneStateListener;
    private String incomingNumberFinal;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getIncomingCall();
        LogUtils.e("拨号服务已经开启");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getIncomingCallCancel();
        LogUtils.e("拨号服务已经关闭");
    }

    /**
     * 获取来电号码
     */
    private void getIncomingCall() {
        // 获取电话系统服务
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 不获取来电号码
     */
    private void getIncomingCallCancel() {
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    /**
     * 电话状态监听器
     */
    class MyPhoneStateListener extends PhoneStateListener {

        private int lastCallState;
        private WindowManager windowManager;
        private Button button;
        private WindowManager.LayoutParams layoutParams;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            incomingNumberFinal=incomingNumber;
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://响铃
                    lastCallState = TelephonyManager.CALL_STATE_RINGING;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                    //呼入电话
                    if (lastCallState==TelephonyManager.CALL_STATE_RINGING){
                        //自定义呼入界面
                    }
                    //呼出电话
                    else{
                        //自定义呼出界面
                        showFloatingWindow(incomingNumber);
                    }
                    lastCallState = TelephonyManager.CALL_STATE_OFFHOOK;
                    break;

                case TelephonyManager.CALL_STATE_IDLE://无状态
                    hideFloatingWindow();
                    //无通话
                    if (lastCallState==TelephonyManager.CALL_STATE_IDLE){

                    }
                    //通话挂断
                    else{
                        lastCallState = TelephonyManager.CALL_STATE_IDLE;
                        //自定义通话挂断界面
                    }
                    break;

                default:
                    break;
            }

        }

        private void hideFloatingWindow() {
            if(windowManager!=null && button!=null){
                try {
                    windowManager.removeView(button);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        // 显示来电界面
        private void showFloatingWindow(String incomingNumber) {
            // 获取WindowManager服务
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            // 新建悬浮窗控件
            button = new Button(getApplicationContext());
            button.setText("拨打号码："+new QuickDialLab(CallService.this).qureyNameByPhone(incomingNumber));
            button.setBackgroundColor(Color.BLUE);
            button.setOnTouchListener(new FloatingOnTouchListener());
            // 设置LayoutParam
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            //设置行为选项,具体有哪些值可取在后面附上
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            //设置悬浮窗的显示位置
            layoutParams.gravity = Gravity.LEFT | Gravity.TOP;

            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.width = 500;
            layoutParams.height = 100;
            layoutParams.x = 300;
            layoutParams.y = 300;
            // 将悬浮窗控件添加到WindowManager
            windowManager.addView(button, layoutParams);


        }
        private class FloatingOnTouchListener implements View.OnTouchListener {
            private int x;
            private int y;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int nowX = (int) event.getRawX();
                        int nowY = (int) event.getRawY();
                        int movedX = nowX - x;
                        int movedY = nowY - y;
                        x = nowX;
                        y = nowY;
                        layoutParams.x = layoutParams.x + movedX;
                        layoutParams.y = layoutParams.y + movedY;

                        // 更新悬浮窗控件布局
                        windowManager.updateViewLayout(view, layoutParams);
                        break;
                    default:
                        break;
                }
                return false;
            }
        }
    }
}

