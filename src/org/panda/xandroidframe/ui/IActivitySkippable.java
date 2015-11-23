package org.panda.xandroidframe.ui;

import android.app.Activity;
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
	public void skipActivity(Activity aty);

	/** 跳转到指定Activity 同时当前activity关闭，传输数据 */
	public void skipActivity(Activity aty, Intent intent);

	/** 跳转到指定Activity 同时当前activity不finish */
	public void showActivity(Activity aty);

	/** 跳转到指定Activity 同时当前activity不finish 通过Intent传输数据 */
	public void showActivity(Activity aty, Intent intent);
}
