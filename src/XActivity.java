import java.lang.ref.SoftReference;

import org.panda.xandroidframe.ui.AnnotateUtil;
import org.panda.xandroidframe.ui.IActivityDescribable;
import org.panda.xandroidframe.ui.IActivitySkippable;
import org.panda.xandroidframe.ui.IBroadcastReg;
import org.panda.xandroidframe.ui.XActivityManager;
import org.panda.xandroidframe.ui.XFragment;
import org.panda.xandroidframe.ui.XSupportFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * @ClassName: XActivity
 * @Description:封装Activity基类 实现数据加载 广播注册 点击监听 页面跳转
 * @author: ZhouRui
 * @date: 2015-11-24 下午5:15:54
 * 
 */
public abstract class XActivity extends FragmentActivity implements
		View.OnClickListener, IBroadcastReg, IActivityDescribable,
		IActivitySkippable {

	public static final int WHICH_MSG = 0x3527;

	public Activity aty;

	protected XFragment currentKJFragment;
	protected XSupportFragment currentSupportFragment;
	private ThreadDataCallBack callback;
	private final KJActivityHandle threadHandle = new KJActivityHandle(this);

	/**
	 * Activity状态
	 */
	public int activityState = DESTROY;

	/**
	 * 一个私有回调类，线程中初始化数据完成后的回调
	 */
	private interface ThreadDataCallBack {
		void onSuccess();
	}

	private static class KJActivityHandle extends Handler {
		private final SoftReference<XActivity> mOuterInstance;

		KJActivityHandle(XActivity outer) {
			mOuterInstance = new SoftReference<XActivity>(outer);
		}

		// 当线程中初始化的数据初始化完成后，调用回调方法
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == WHICH_MSG && mOuterInstance.get() != null) {
				mOuterInstance.get().callback.onSuccess();
			}
		}
	}

	/**
	 * 如果调用了initDataFromThread()，则当数据初始化完成后将回调该方法。
	 */
	protected void threadDataInited() {
	}

	/**
	 * 在线程中初始化数据，注意不能在这里执行UI操作
	 */
	@Override
	public void initDataFromThread() {
		callback = new ThreadDataCallBack() {
			@Override
			public void onSuccess() {
				threadDataInited();
			}
		};
	}

	@Override
	public void initData() {
	}

	@Override
	public void initWidget() {
	}

	// 仅仅是为了代码整洁点
	private void initializer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initDataFromThread();
				threadHandle.sendEmptyMessage(WHICH_MSG);
			}
		}).start();
		initData();
		initWidget();
	}

	/**
	 * listened widget's click method
	 */
	@Override
	public void widgetClick(View v) {
	}

	/**
	 * 点击事件转发
	 */
	@Override
	public void onClick(View v) {
		widgetClick(v);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T bindView(int id) {
		return (T) findViewById(id);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T bindView(int id, boolean click) {
		T view = (T) findViewById(id);
		if (click) {
			view.setOnClickListener(this);
		}
		return view;
	}

	@Override
	public void registerBroadcast() {
	}

	@Override
	public void unRegisterBroadcast() {
	}

	@Override
	public void skipActivity(Class<?> clazz) {
		showActivity(clazz);
		aty.finish();
	}

	@Override
	public void skipActivity(Intent intent) {
		showActivity(intent);
		aty.finish();
	}

	@Override
	public void showActivity(Class<?> clazz) {
		Intent intent = new Intent(aty, clazz);
		showActivity(intent);
	}

	@Override
	public void showActivity(Intent intent) {
		aty.startActivity(intent);
	}

	@Override
	public void setRootView() {

	}

	/***************************************************************************
	 * print Activity callback methods
	 ***************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		aty = this;
		XActivityManager.getManager().addActivity(this);

		setRootView(); // 必须放在annotate之前调用
		AnnotateUtil.initBindView(this);
		initializer();
		registerBroadcast();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		activityState = RESUME;
	}

	@Override
	protected void onPause() {
		super.onPause();
		activityState = PAUSE;
	}

	@Override
	protected void onStop() {
		super.onStop();
		activityState = STOP;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		unRegisterBroadcast();
		activityState = DESTROY;
		super.onDestroy();
		XActivityManager.getManager().finishActivity(this);
	}

	/**
	 * 用Fragment替换视图
	 * 
	 * @param resView
	 *            将要被替换掉的视图
	 * @param targetFragment
	 *            用来替换的Fragment
	 */
	@SuppressLint("NewApi")
	public void changeFragment(int resView, XFragment targetFragment) {
		if (targetFragment.equals(currentKJFragment)) {
			return;
		}
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		if (!targetFragment.isAdded()) {
			transaction.add(resView, targetFragment, targetFragment.getClass()
					.getName());
		}
		if (targetFragment.isHidden()) {
			transaction.show(targetFragment);
			targetFragment.onChange();
		}
		if (currentKJFragment != null && currentKJFragment.isVisible()) {
			transaction.hide(currentKJFragment);
		}
		currentKJFragment = targetFragment;
		transaction.commit();
	}

	/**
	 * 用Fragment替换视图
	 * 
	 * @param resView
	 *            将要被替换掉的视图
	 * @param targetFragment
	 *            用来替换的Fragment
	 */
	public void changeFragment(int resView, XSupportFragment targetFragment) {
		if (targetFragment.equals(currentSupportFragment)) {
			return;
		}
		android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		if (!targetFragment.isAdded()) {
			transaction.add(resView, targetFragment, targetFragment.getClass()
					.getName());
		}
		if (targetFragment.isHidden()) {
			transaction.show(targetFragment);
			targetFragment.onChange();
		}
		if (currentSupportFragment != null
				&& currentSupportFragment.isVisible()) {
			transaction.hide(currentSupportFragment);
		}
		currentSupportFragment = targetFragment;
		transaction.commit();
	}
}
