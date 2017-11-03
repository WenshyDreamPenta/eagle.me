package com.star.eagleme.ui.widgets.refreshview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.star.eagleme.R;

/**
 * Created by star on 2017/8/30.
 *
 * @description:
 */
public class HeaderReFresh implements CommonRefreshLayout.OnRefreshListener
{

    private CommonRefreshLayout mRefreshLayout;
    //loading
    private ImageView iv_loading;
    private RelativeLayout rl_loading_cover;
    private AnimationDrawable animDrawable;

    private Context mContext;

    public HeaderReFresh(View contentView, CommonRefreshLayout crl, Context context)
    {
        this.mRefreshLayout = crl;
        this.mContext = context;

    }

    private Handler mHandle = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            //设置刷新控件完成刷新
            mRefreshLayout.setOnRefreshComplete();
            initLoadingView();
        }
    };

    @Override
    public void onPullPercentage(float percent)
    {
        initLoadingView();

    }

    @Override
    public void onHeaderRefresh()
    {

        mHandle.sendEmptyMessageDelayed(0, 500);
    }

    @Override
    public void onHeaderPrepareRefresh()
    {

    }

    @Override
    public void onHeaderCamcelPrepareRefresh()
    {
    }

    private void initLoadingView()
    {

        try
        {
            animDrawable = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.loading_person);
            iv_loading.setImageDrawable(animDrawable);
        } catch (OutOfMemoryError e)
        {
            //内存溢出處理
            System.gc();
            System.runFinalization();
            initLoadingView();
        }
        showLoading();

    }

    private void showLoading()
    {

        rl_loading_cover.setVisibility(View.VISIBLE);
        iv_loading.setVisibility(View.VISIBLE);
        animDrawable.start();

    }
    private void hideLoading()
    {

        rl_loading_cover.setVisibility(View.GONE);
        if (animDrawable.isRunning())
        {
            animDrawable.stop();
        }
        iv_loading.setVisibility(View.GONE);

    }
}
