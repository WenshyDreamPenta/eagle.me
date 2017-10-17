package com.star.eagleme.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.star.eagleme.socket.utils.ManageLog;

public class SwitchLogReceivcer extends BroadcastReceiver
{
    private static final String TAG = "SwitchLogReceivcer";
    public static final String SWITCH_LOG_ACTION = "eagle.intent.action.SWITCH_LOG_ACTION";
    // 日志开关的反馈广播
    public static final String RELY_MESSAGE_ACTION = "eagle.intent.action.RELY_MESSAGE_ACTION";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ManageLog.D(TAG, "action = " + intent.getAction());
        if (intent.getAction().equals(SWITCH_LOG_ACTION))
        {
            String appName = intent.getStringExtra("appName");
            if (null != appName && appName.equals("com.star.eagleme"))
            {

                boolean openLog = intent.getBooleanExtra("isOpen", false);
                int level = intent.getIntExtra("logLevel", 0);
                Log.d(TAG, "SWITCH_LOG_ACTION isOpen = " + openLog
                        + ";level = " + level);
                boolean bIsSuccess = ManageLog.switchLog(openLog, level);

                ManageLog.D(TAG, "switchLog result:" + bIsSuccess);
            }
        }


    }
}
