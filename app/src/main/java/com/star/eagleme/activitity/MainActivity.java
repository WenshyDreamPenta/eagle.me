package com.star.eagleme.activitity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.star.eagleme.R;
import com.star.eagleme.socket.client.ConnectionClient;
import com.star.eagleme.socket.protocol.DataAckProtocol;
import com.star.eagleme.socket.protocol.DataProtocol;
import com.star.eagleme.socket.request.RequestCallBack;
import com.star.eagleme.utils.logutil.ManageLog;

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
    private ThreadPoolExecutor threadPoolExecutor;
    private ConnectionClient connClinet;
    private RequestCallBack requestCallBack;

    private TextView etText;
    private TextView etSend;
    private TextView tvget;
    private TextView tvclose;
    private TextView tvconnect;
    private TextView tvrefresh;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initThreadExcute();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        //pointAnimView.setRadius(20f);

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
                    if(connClinet == null) return;
                    connClinet.addNewRequest(data);
                }
                break;
            case R.id.tv_close:
                if (connClinet != null)
                {
                    connClinet.closeConnect();
                } else
                {
                    Toast.makeText(this, "please connect server!", Toast.LENGTH_LONG);
                }
                break;
            case R.id.tv_connect:
                connClinet = new ConnectionClient(requestCallBack);
                break;
            case R.id.tv_get:
                break;
            case R.id.tv_refresh:
                Intent intent = new Intent(this,RefreshActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void initViews()
    {
        etText = (TextView) findViewById(R.id.et_text);
        etSend = (TextView) findViewById(R.id.tv_send);
        tvget = (TextView) findViewById(R.id.tv_get);
        tvclose = (TextView) findViewById(R.id.tv_close);
        tvconnect = (TextView) findViewById(R.id.tv_connect);
        tvrefresh = (TextView) findViewById(R.id.tv_refresh);

        // pointAnimView = (PointAnimView) findViewById(R.id.pv_animview);

        etSend.setOnClickListener(this);
        tvclose.setOnClickListener(this);
        tvconnect.setOnClickListener(this);
        tvget.setOnClickListener(this);
        tvrefresh.setOnClickListener(this);
    }

    private void initThreadExcute()
    {
        threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        requestCallBack = new RequestCallBack()
        {

            @Override
            public void onSuccess(Object msg)
            {
                if (msg instanceof DataAckProtocol)
                {
                    ManageLog.D("DataAckProtocol-msg", ((DataAckProtocol) msg).getUnused());
                } else
                {
                    ManageLog.D("msg", msg.toString());
                }

            }

            @Override
            public void onFailed(int errorCode, String msg)
            {

            }
        };

    }











}
