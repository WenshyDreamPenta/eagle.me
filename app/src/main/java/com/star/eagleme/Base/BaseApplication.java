package com.star.eagleme.Base;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.stetho.Stetho;
import com.star.eagleme.utils.Utils;

/**
 * Created by star on 2017/8/22.
 */
public class BaseApplication extends MultiDexApplication
{
    private static Context mContext = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = getApplicationContext();

	    ARouter.init(this); //设置ARouter
        Stetho.initializeWithDefaults(mContext);//设置Stetho
	    Utils.init(this);
    }

    public static Context getAppContext() {
        return mContext;
    }
}
