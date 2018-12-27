package com.yuncai.call.tdvrcall.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yuncai.call.tdvrcall.R;
import com.yuncai.call.tdvrcall.db.QuickDialLab;
import com.yuncai.call.tdvrcall.util.LogUtils;
import com.yuncai.call.tdvrcall.util.PhoneUtil;
import com.yuncai.call.tdvrcall.util.SystemUtils;

/**
   * 获取来电号码服务
   */
  public class IncomingCallService extends Service {

      /**
       * 记录上一个电话状态
       */
      private int lastCallState  = TelephonyManager.CALL_STATE_IDLE;

      /**
       * 电话服务管理器
       */
      private TelephonyManager telephonyManager;

      /**
       * 电话状态监听器
       */
      private MyPhoneStateListener myPhoneStateListener;

      @Override
      public IBinder onBind(Intent intent) {
          return null;
      }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getIncomingCall();
        LogUtils.e("来电服务已经开启");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
      public void onDestroy() {
          super.onDestroy();
          // 不获取来电号码
          LogUtils.e("来电服务已经关闭");
          getIncomingCallCancel();
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

          private String incomingNumberFinal;
          private WindowManager.LayoutParams layoutParams;
          private WindowManager windowManager;
          private View incomingView;
          private TextView tvIncoming;
          private Button btnAnswer;
          private Button btnHangup;

          @Override
          public void onCallStateChanged(int state, String incomingNumber) {
              super.onCallStateChanged(state, incomingNumber);

              incomingNumberFinal=incomingNumber;
              switch (state) {
                  case TelephonyManager.CALL_STATE_RINGING://响铃
                      lastCallState = TelephonyManager.CALL_STATE_RINGING;
                      //自定义来电界面
                      showFloatingWindow(incomingNumber);
                      break;
                  case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                      hideFloatingWindow();
                      //呼入电话
                      if (lastCallState==TelephonyManager.CALL_STATE_RINGING){
                          //自定义呼入界面
                      }
                      //呼出电话
                      else{
                          //自定义呼出界面

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
              if(windowManager!=null && incomingView!=null){
                  try {
                      windowManager.removeView(incomingView);
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
                       incomingView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.incomming_phone_view, null);
                       initIncomingView(incomingView);
                      tvIncoming.setText("来电号码："+new QuickDialLab(IncomingCallService.this).qureyNameByPhone(incomingNumber));
                      btnAnswer.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              PhoneUtil.answer();
                          }
                      });
                      btnHangup.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              PhoneUtil.stopCall();
                          }
                      });
                      // 设置LayoutParam
                      layoutParams = new WindowManager.LayoutParams();
                      layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                       //设置行为选项,具体有哪些值可取在后面附上
                       layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                       //设置悬浮窗的显示位置
                       layoutParams.gravity = Gravity.LEFT | Gravity.TOP;

                      layoutParams.format = PixelFormat.RGBA_8888;
                      layoutParams.width = SystemUtils.getScreenWidth(getApplication());
                      layoutParams.height = SystemUtils.getScreenHeight(getApplication());
                      layoutParams.x = 0;
                      layoutParams.y = 0;
                      // 将悬浮窗控件添加到WindowManager
                      windowManager.addView(incomingView, layoutParams);


          }

          private void initIncomingView(View incomingView) {
              tvIncoming = (TextView) incomingView.findViewById(R.id.tv_coming_phone);
              btnAnswer = (Button) incomingView.findViewById(R.id.btn_answer);
              btnHangup = (Button) incomingView.findViewById(R.id.btn_reject);
          }
      }


  }

