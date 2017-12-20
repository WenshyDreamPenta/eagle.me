package com.star.eagleme.socket.request;

/**
 * Created by star on 2017/8/15.
 */

public interface RequestCallBack {
	void onSuccess(Object msg);

	void onFailed(int errorCode, String msg);
}
