/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.panda.xandroidframe.ui;

import java.lang.ref.SoftReference;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName: XFragment
 * @Description:封装fragment 支持3.0以上
 * @author: ZhouRui
 * @date: 2015-11-24 下午5:08:03
 * 
 */
@SuppressLint("NewApi")
public abstract class XFragment extends Fragment implements
		View.OnClickListener {

	public static final int WHICH_MSG = 0X37211;

	protected View fragmentRootView;
	private ThreadDataCallBack callback;
	private final KJFragmentHandle threadHandle = new KJFragmentHandle(this);

	/**
	 * 一个私有回调类，线程中初始化数据完成后的回调
	 */
	private interface ThreadDataCallBack {
		void onSuccess();
	}

	private static class KJFragmentHandle extends Handler {
		private final SoftReference<XFragment> mOuterInstance;

		KJFragmentHandle(XFragment outer) {
			mOuterInstance = new SoftReference<XFragment>(outer);
		}

		// 当线程中初始化的数据初始化完成后，调用回调方法
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == WHICH_MSG && mOuterInstance.get() != null) {
				mOuterInstance.get().callback.onSuccess();
			}
		}
	}

	protected abstract View inflaterView(LayoutInflater inflater,
			ViewGroup container, Bundle bundle);

	/**
	 * initialization widget, you should look like parentView.findviewbyid(id);
	 * call method
	 * 
	 * @param parentView
	 *            根View
	 */
	protected void initWidget(View parentView) {
	}

	/**
	 * initialization data
	 */
	protected void initData() {
	}

	/**
	 * 当通过changeFragment()显示时会被调用(类似于onResume)
	 */
	public void onChange() {
	}

	/**
	 * initialization data. And this method run in background thread, so you
	 * shouldn't change ui<br>
	 * on initializated, will call threadDataInited();
	 */
	protected void initDataFromThread() {
		callback = new ThreadDataCallBack() {
			@Override
			public void onSuccess() {
				threadDataInited();
			}
		};
	}

	/**
	 * 如果调用了initDataFromThread()，则当数据初始化完成后将回调该方法。
	 */
	protected void threadDataInited() {
	}

	/**
	 * widget click method
	 */
	protected void widgetClick(View v) {
	}

	@Override
	public void onClick(View v) {
		widgetClick(v);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragmentRootView = inflaterView(inflater, container, savedInstanceState);
		AnnotateUtil.initBindView(this, fragmentRootView);
		initData();
		initWidget(fragmentRootView);
		new Thread(new Runnable() {
			@Override
			public void run() {
				initDataFromThread();
				threadHandle.sendEmptyMessage(WHICH_MSG);
			}
		}).start();
		return fragmentRootView;
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T bindView(int id) {
		return (T) fragmentRootView.findViewById(id);
	}

	protected <T extends View> T bindView(int id, boolean click) {
		T view = bindView(id);
		if (click) {
			view.setOnClickListener(this);
		}
		return view;
	}
}
