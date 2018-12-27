package com.yuncai.call.tdvrcall.util;

import android.os.IBinder;

import com.android.internal.telephony.ITelephony;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;

/**
 * Function: 电话操作类
 * Created by TianMing.Xiong on 18-12-27.   boolean endCall();
 */

public class PhoneUtil {
    // 1是挂断,值2是接听
    private static final String CLOSE = "1";
    private static final String OPEN = "2";
    private static final String DEV_PATH = "/sys/devices/module_control.30/misc/medule-led/ledctrl";
    // 挂断电话
    public static void stopCall() {
        try {
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            // 获取远程TELEPHONY_SERVICE的IBinder对象的代理
            IBinder binder = (IBinder) method.invoke(null, new Object[] { "phone" });
            // 将IBinder对象的代理转换为ITelephony对象
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            // 挂断电话
            telephony.endCall();
            //telephony.cancelMissedCallsNotification();
        } catch (Exception e) {
        }


    }
    // 接听
    public static void answer() {
        writeLed(DEV_PATH,OPEN);
    }

    // 挂断
    public static void hangup() {
        writeLed(DEV_PATH,CLOSE);
    }


    // 设备控制指令
    private static void writeLed(String devPath,String cmd) {
        File file = new File(devPath);
        if(file.exists()){
            try {
                RandomAccessFile rdf = new RandomAccessFile(file, "rw");
                rdf.write(cmd.getBytes());
                rdf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
        }
    }
}
