package com.star.eagleme; /**
 * Created by star on 2017/8/22.
 *
 * @description:
 */

import android.content.Context;
import android.support.multidex.MultiDexApplication;

public class BaseApplication extends MultiDexApplication
{
    private static Context mContext = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = getApplicationContext();

    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }

    public static Context getAppContext() {
        return mContext;
    }
}
