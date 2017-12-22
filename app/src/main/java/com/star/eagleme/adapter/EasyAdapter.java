package com.star.eagleme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.star.eagleme.Base.BaseApplication;
import com.star.eagleme.Base.BaseRecycleAdapter;
import com.star.eagleme.bean.EasyBean;
import com.star.eagleme.ui.itemview.EasyItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 2017/8/10.
 */

public class EasyAdapter extends BaseRecycleAdapter<EasyAdapter.ReportViewHolder> implements View.OnClickListener {
	private Context mContext;
	private EasyAdapter.OnItemClickListener mListener;
	private List<EasyBean> datas = new ArrayList<>();


	public EasyAdapter(Context context, List<EasyBean> datas, BaseApplication baseApplication) {
		super(context, datas, baseApplication);
		this.mContext = context;
		this.datas = datas;

	}


	@Override
	public void onClick(View v) {
		if (mListener != null) {
			mListener.onItemClick(v, (int) v.getTag());
		}
	}

	@Override
	public EasyAdapter.ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = new EasyItemView(mContext);
		view.setOnClickListener(this);
		return new EasyAdapter.ReportViewHolder(view);
	}

	@Override
	public int getItemCount() {
		return mDatas.size();

	}

	@Override
	public void onBindViewHolder(ReportViewHolder holder, final int position) {
		EasyBean textReport = (EasyBean) getItem(position);
		((EasyItemView) holder.itemView).setReportReason(textReport.getText());
		holder.itemView.setSelected(textReport.isSelected());
		holder.itemView.setTag(position);
		holder.itemView.setClickable(true);
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				for (EasyBean data : datas) {
					data.setSelected(false);
				}
				datas.get(position).setSelected(true);
				notifyDataSetChanged();

				if (mListener != null) {
					mListener.onItemClick(v, position);

				}

			}
		});
	}

	public void setOnItemClickListener(EasyAdapter.OnItemClickListener listener) {
		this.mListener = listener;
	}

	class ReportViewHolder extends RecyclerView.ViewHolder {

		public ReportViewHolder(View itemView) {
			super(itemView);
		}

	}

	public interface OnItemClickListener {

		public void onItemClick(View view, int position);

	}
}
