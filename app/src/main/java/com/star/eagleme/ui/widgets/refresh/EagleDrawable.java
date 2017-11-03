package com.star.eagleme.ui.widgets.refresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;

import com.star.eagleme.utils.DimenUtils;
import com.star.eagleme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 2017/08/31.
 */

public class EagleDrawable extends RefreshDrawable implements Runnable
{

    private boolean isRunning;
    private Handler mHandler = new Handler();

    protected int mOffset;
    protected float mPercent;
    protected int drawableMiddleWidth;
    protected List<Bitmap> bitmaps = new ArrayList<>();
    protected RectF rectF = new RectF();

    public EagleDrawable(Context context, PullRefreshLayout layout)
    {
        super(context, layout);
        getBitmaps(context);
    }

    private void getBitmaps(Context context)
    {
        System.gc();

        BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_eaglelive_loading_01);
        drawableMiddleWidth = drawable.getMinimumWidth() /2 + DimenUtils.dp2px(getContext() , 15);
        bitmaps.add(drawable.getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_eaglelive_loading_02)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_eaglelive_loading_03)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_eaglelive_loading_04)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_eaglelive_loading_05)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_eaglelive_loading_06)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_eaglelive_loading_07)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_eaglelive_loading_08)).getBitmap());
    }

    @Override
    public void setPercent(float percent)
    {
        mPercent = percent;
        int centerX = getBounds().centerX();
        rectF.left = centerX - drawableMiddleWidth  + DimenUtils.dp2px(getContext() , 25);
        rectF.right = centerX + drawableMiddleWidth  - DimenUtils.dp2px(getContext() , 25);
        rectF.top = -drawableMiddleWidth * 2  + DimenUtils.dp2px(getContext() , 10);
        rectF.bottom = - DimenUtils.dp2px(getContext() , 30);
    }

    @Override
    public void setColorSchemeColors(int[] colorSchemeColors)
    {
    }

    @Override
    public void offsetTopAndBottom(int offset)
    {
        mOffset += offset;
        invalidateSelf();
    }

    @Override
    public void start()
    {
        isRunning = true;
        mHandler.postDelayed(this, 50);
    }

    @Override
    public void run()
    {
        if (isRunning)
        {
            mHandler.postDelayed(this, 50);
            invalidateSelf();
        }
    }

    @Override
    public void stop()
    {
        isRunning = false;
        mHandler.removeCallbacks(this);
        System.gc();
    }

    @Override
    public boolean isRunning()
    {
        return isRunning;
    }

    @Override
    public void draw(Canvas canvas)
    {
        int num = (int) (System.currentTimeMillis() / 40 % 8);
        final int saveCount = canvas.save();
        canvas.translate(0, mOffset + DimenUtils.dp2px(getContext() , 40));
        Bitmap bitmap = bitmaps.get(num);
        canvas.drawBitmap(bitmap, null, rectF, null);
        canvas.restoreToCount(saveCount);
    }
}
