package com.star.eagleme.Base;

import android.os.Bundle;
import android.view.View;

/**
 * <pre>
 *     author: wangmx
 *     time  : 2017/12/22
 *     desc  :
 * </pre>
 */
interface IBaseView extends View.OnClickListener {

    /**
     * 初始化数据
     *
     * @param bundle 传递过来的 bundle
     */
    void initData(final Bundle bundle);

    /**
     * 绑定布局
     *
     * @return 布局 Id
     */
    int bindLayout();

    /**
     * 初始化 view
     */
    void initView(final Bundle savedInstanceState, final View view);

    /**
     * 业务操作
     */
    void doBusiness();

    /**
     * 视图点击事件
     *
     * @param view 视图
     */
    void onWidgetClick(final View view);
}
