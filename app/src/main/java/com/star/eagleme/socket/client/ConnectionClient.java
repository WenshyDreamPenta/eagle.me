package com.star.eagleme.socket.client;

import com.star.eagleme.socket.protocol.DataProtocol;
import com.star.eagleme.socket.request.ClientRequestTask;
import com.star.eagleme.socket.request.RequestCallBack;

/**
 * Created by star on 2017/8/15.
 */

public class ConnectionClient
{
    private boolean isClosed;

    private ClientRequestTask mClientRequestTask;

    public ConnectionClient(RequestCallBack requestCallBack)
    {
        mClientRequestTask = new ClientRequestTask(requestCallBack);
        new Thread(mClientRequestTask).start();
    }

    public void addNewRequest(DataProtocol data)
    {
        if (mClientRequestTask != null && !isClosed)
        {
            mClientRequestTask.addRequest(data);
        }

    }

    public void closeConnect()
    {
        isClosed = true;
        mClientRequestTask.stop();
    }
}
