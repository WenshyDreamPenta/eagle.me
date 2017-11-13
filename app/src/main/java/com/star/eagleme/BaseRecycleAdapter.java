package com.star.eagleme;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;


import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecycleAdapter<VH extends ViewHolder> extends Adapter<VH>
{

    protected BaseApplication mApplication;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<?> mDatas = new ArrayList<>();
    protected long mOldTime;

    public BaseRecycleAdapter(Context context, List<?> datas, BaseApplication baseApplication)
    {
        this.mApplication = baseApplication;
        this.mContext = context;
        this.mDatas = datas;
    }

    public void setDatas(List<?> datas)
    {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount()
    {
        // TODO Auto-generated method stub
        return mDatas.size();
    }

    public Object getItem(int pos)
    {
        if (pos >= 0 && pos < mDatas.size())
        {
            return mDatas.get(pos);
        }
        return mDatas.get(pos);
    }


}
