package com.star.eagleme.ui.itemview;

/**
 * Created by star on 2017/8/10.
 */


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.star.eagleme.R;

public class EasyItemView extends FrameLayout {

    private ImageView ivReport;
    private TextView tvReport;



    public EasyItemView(Context context)
    {
        this(context, null);
    }

    public EasyItemView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public EasyItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {

        LayoutInflater.from(getContext()).inflate(R.layout.layout_report_item, this);
        ivReport = (ImageView) findViewById(R.id.iv_report);
        tvReport = (TextView) findViewById(R.id.tv_report);
    }

    public void setReportReason(String reason)
    {
        tvReport.setText(reason);
    }

    public void setSelected(boolean isSelected)
    {

    }
}

