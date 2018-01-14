package com.star.eagleme.utils;

/**
 * <pre>
 *     author : wangmingxing
 *     time   : 2018/1/14
 *     desc   : common Util
 * </pre>
 */
public class CommonUtil {
    //上次点击时间
    private static long lastClick = 0;

    //判断是否快速点击
    public static boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - lastClick >= 200) {
            lastClick = now;
            return false;
        }
        return true;
    }
}
