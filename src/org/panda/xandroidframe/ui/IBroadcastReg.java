package org.panda.xandroidframe.ui;

/**
 * @ClassName: IBroadcastReg
 * @Description: 规范化注册广播接受者
 * @author: ZhouRui
 * @date: 2015-11-23 下午2:21:27
 * 
 */
public interface IBroadcastReg {
	/**
	 * 注册广播
	 */
	void registerBroadcast();

	/**
	 * 反注册广播
	 */
	void unRegisterBroadcast();
}
