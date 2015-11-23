package org.panda.xandroidframe.ui;

import android.view.View;

public interface IActivityDescribable {
	// 记录Activity当前所处状态
	int DESTROY = 0;
	int STOP = 1;
	int PAUSE = 2;
	int RESUME = 3;

	/**
	 * 获取布局
	 */
	void setRootView();

	/**
	 * 初始化数据
	 */
	void initData();

	/**
	 * 从子线程获取数据
	 */
	void initDataFromThread();

	/**
	 * 初始化组件
	 */
	void initWidget();

	/**
	 * 组件点击事件实现 通过View.OnClickListener转发
	 * 
	 * @param view
	 *            视图
	 */
	void widgetClick(View view);
}
