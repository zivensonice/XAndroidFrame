package org.panda.xandroidframe.ui;

import org.panda.xandroidframe.utils.ScreenUtil;
import org.panda.xandroidframe.utils.SystemUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: ViewInject
 * @Description: 视图注入类 注入Toast、Dialog
 * @author: ZhouRui
 * @date: 2015-11-25 下午7:07:24
 * 
 */
public class ViewInject {
	private ViewInject viewInject;

	private ViewInject() {
	}

	public ViewInject getInstance() {
		if (viewInject == null) {
			synchronized (ViewInject.class) {
				if (viewInject == null) {
					viewInject = new ViewInject();
				}
			}
		}
		return viewInject;
	}

	/**
	 * 退出确认对话框
	 * 
	 * @param context
	 * @param str
	 *            内容
	 * @param listener
	 *            确定点击
	 */
	public void getExitDialog(Context context, String str,
			android.content.DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(str);
		builder.setCancelable(false);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("确定", listener);
		builder.create();
		builder.show();
		builder = null;
	}

	/**
	 * 填充视图对话框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param view
	 *            视图
	 */
	public void getDialogView(Context context, String title, View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(view);
		builder.create();
		builder.show();
		builder = null;
	}

	/**
	 * 显示进度进度条
	 * 
	 * @param context
	 * @param content
	 *            进度条显示内容
	 * @param cancle
	 *            是否可以取消
	 * @return 进度条
	 */
	public ProgressDialog getProgressDialog(Context context, String content,
			boolean cancle) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.getWindow().setLayout(ScreenUtil.getScreenW(context),
				ScreenUtil.getScreenH(context));
		dialog.setMessage(content);
		dialog.setCancelable(cancle);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
		return dialog;
	}

	public void getDateDialog(String title, final TextView textView) {
		// DatePickerDialog dialog = new
		String[] dateStr = SystemUtil.getDateTime("yyyy-MM-hh").split("-");
		int year = Integer.valueOf(dateStr[0]);
		int month = Integer.valueOf(dateStr[1]);
		int day = Integer.valueOf(dateStr[2]);
		DatePickerDialog dialog = new DatePickerDialog(textView.getContext(),
				new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						textView.setText(year + "-" + monthOfYear + "-"
								+ dayOfMonth);
					}
				}, year, month - 1, day);
		dialog.setTitle(title);
		dialog.show();

	}

	public void toast(String content) {
		Activity aty = XActivityManager.getManager().topActivity();
		toast(aty, content);
	}

	public void toastLong(String content) {
		Activity aty = XActivityManager.getManager().topActivity();
		toastLong(aty, content);
	}

	public void toastLong(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}

	public void toast(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

}
