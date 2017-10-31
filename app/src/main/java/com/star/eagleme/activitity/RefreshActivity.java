package com.star.eagleme.activitity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.star.eagleme.R;
import com.star.eagleme.widgets.refresh.PullRefreshLayout;

public class RefreshActivity extends AppCompatActivity
{
    private PullRefreshLayout pullRefreshLayout;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.crl);

        //下拉刷新
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {

            }
        });

    }

}
