package com.star.eagleme.socket.request;

import com.star.eagleme.socket.protocol.BasicProtocol;

/**
 * Created by star on 2017/8/15.
 */

public interface RequestCallBack
{
    void onSuccess(BasicProtocol msg);

    void onFailed(int errorCode, String msg);
}
