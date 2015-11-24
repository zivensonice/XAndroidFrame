package org.panda.xandroidframe.ui;

import android.content.Intent;

/**
 * @ClassName: IActivitySkippable
 * @Description:Activity 跳转功能
 * @author: ZhouRui
 * @date: 2015-11-23 下午2:53:47
 * 
 */
public interface IActivitySkippable {
	/** 跳转到指定Activity 同时当前activity关闭 */
	public void skipActivity(Class<?> clazz);

	/** 跳转到指定Activity 同时当前activity关闭，传输数据 */
	public void skipActivity(Intent intent);

	/** 跳转到指定Activity 同时当前activity不finish */
	public void showActivity(Class<?> clazz);

	/** 跳转到指定Activity 同时当前activity不finish 通过Intent传输数据 */
	public void showActivity(Intent intent);
}
