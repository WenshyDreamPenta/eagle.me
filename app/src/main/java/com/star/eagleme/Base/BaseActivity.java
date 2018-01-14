package com.star.eagleme.Base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.star.eagleme.utils.CommonUtil;

/**
 * <pre>
 *     author: wangmx
 *     time  : 2017/12/22
 *     desc  : Activity 基类
 * </pre>
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

	//当前Activity 视图View
	protected View contentView;
	protected BaseActivity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		Bundle bundle = getIntent().getExtras();
		initData(bundle);
		setBaseView(bindLayout());
		initView(savedInstanceState, contentView);
		doBusiness();
	}

	protected void setBaseView(@LayoutRes int layoutId) {
		setContentView(contentView = LayoutInflater.from(this).inflate(layoutId, null));
	}

	@Override
	public void onClick(final View view) {
		if (!CommonUtil.isFastClick()) onWidgetClick(view);
	}
}
