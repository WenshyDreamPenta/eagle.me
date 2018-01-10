package com.star.eagleme.Base;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;


import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author: wangmx
 *     time  : 2017/12/22
 *     desc  : BaseRecycleAdapter 基类Adapter
 * </pre>
 */
public abstract class BaseRecycleAdapter<VH extends ViewHolder> extends Adapter<VH>
{

    protected BaseApplication mApplication;
    protected Context mContext;
    protected List<?> mDatas = new ArrayList<>();

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
