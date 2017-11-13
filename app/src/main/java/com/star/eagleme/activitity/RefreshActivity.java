package com.star.eagleme.activitity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.star.eagleme.R;
import com.star.eagleme.adapter.EasyAdapter;
import com.star.eagleme.bean.EasyBean;
import com.star.eagleme.ui.divider.RecyclerViewDivider;
import com.star.eagleme.BaseApplication;
import com.star.eagleme.ui.widgets.refresh.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class RefreshActivity extends AppCompatActivity
{
    private PullRefreshLayout pullRefreshLayout;
    private RecyclerView mRecyclerView;
    private EasyAdapter mAdapter;
    private List<EasyBean> reportResons ;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.crl);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        reportResons = new ArrayList<EasyBean>();
        reportResons.add(new EasyBean("one", false));
        reportResons.add(new EasyBean("two", false));
        reportResons.add(new EasyBean("three", false));
        reportResons.add(new EasyBean("four", false));
        reportResons.add(new EasyBean("five", false));

        mAdapter = new EasyAdapter(RefreshActivity.this, reportResons, (BaseApplication)getApplication());
        LinearLayoutManager verticalManager = new LinearLayoutManager(RefreshActivity.this);
        verticalManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(verticalManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(RefreshActivity.this, LinearLayoutManager.VERTICAL, 2 , ContextCompat.getColor(RefreshActivity.this, R.color.color_312e36)));

        //下拉刷新
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                mHandler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        pullRefreshLayout.setRefreshing(false);
                    }
                }, 2 * 1000);
            }
        });

    }

}
