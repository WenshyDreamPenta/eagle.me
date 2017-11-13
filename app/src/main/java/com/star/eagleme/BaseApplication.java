package com.star.eagleme; /**
 * Created by star on 2017/8/22.
 *
 * @description:
 */

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

public class BaseApplication extends MultiDexApplication
{
    private static Context mContext = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = getApplicationContext();
        //设置Stetho
        Stetho.initializeWithDefaults(mContext);

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
