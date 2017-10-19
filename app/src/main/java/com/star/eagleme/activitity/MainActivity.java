package com.star.eagleme.activitity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.star.eagleme.R;
import com.star.eagleme.socket.protocol.DataAckProtocol;
import com.star.eagleme.socket.protocol.DataProtocol;
import com.star.eagleme.socket.request.ClientRequestTask;
import com.star.eagleme.socket.request.RequestCallBack;
import com.star.eagleme.utils.ManageLog;
import com.star.eagleme.widgets.animview.PointAnimView;
import com.star.eagleme.widgets.refreshview.CommonRefreshLayout;
import com.star.eagleme.widgets.refreshview.HeaderReFresh;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * MainActivity
 *
 * @author star
 * @date 2017/10/18
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private ClientRequestTask clientRequestTask;
    private ThreadPoolExecutor threadPoolExecutor;

    private TextView etText;
    private TextView etSend;

    /**
     * 下拉刷新控件
     */
    private CommonRefreshLayout crl;

    /**
     * 列表控件
     */
    private RecyclerView rv;

    /**
     * 显示数据
     */
    private ArrayList<String> data = new ArrayList<String>();

    private HeaderReFresh headerReFresh;
    private PointAnimView pointAnimView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        clientRequestTask = new ClientRequestTask(new RequestCallBack() {

            @Override
            public void onSuccess(Object msg)
            {
                if(msg instanceof DataAckProtocol)
                {
                    ManageLog.D("DataAckProtocol-msg", ((DataAckProtocol) msg).getUnused());
                }
                else
                {
                    ManageLog.D("msg", msg.toString());
                }

            }

            @Override
            public void onFailed(int errorCode, String msg)
            {

            }
        });
        setRequest();

        pointAnimView.setRadius(20f);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_send:
                String sendText = etText.getText().toString();
                if (sendText != null && !sendText.equals(""))
                {
                    DataProtocol data = new DataProtocol();
                    data.setData(sendText);
                    data.setDtype(1);
                    data.setMsgId(22);
                    clientRequestTask.addRequest(data);
                }
                break;
            default:
                break;
        }
    }

    private void initViews()
    {
        etText = (TextView) findViewById(R.id.et_text);
        etSend = (TextView) findViewById(R.id.tv_send);

        pointAnimView = (PointAnimView) findViewById(R.id.pv_animview);

        etSend.setOnClickListener(this);
    }

    private void  setRequest()
    {

        threadPoolExecutor.execute(clientRequestTask);

    }
}
