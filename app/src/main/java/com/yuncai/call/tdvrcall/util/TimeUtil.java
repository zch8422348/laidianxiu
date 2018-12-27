package com.yuncai.call.tdvrcall.util;

/**
 * Function:
 * Created by TianMing.Xiong on 18-12-22.
 */

public class TimeUtil {
    /**
     * 将毫秒转时分秒
     * @param time
     * @return
     */
    public static String generateTime(long time) { int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02d时%02d分%02d秒", hours, minutes, seconds) : String.format("%02d分%02d秒", minutes, seconds);
    }

}
