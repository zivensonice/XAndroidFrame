package org.panda.xandroidframe.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

/**
 * @ClassName: SystemUtils
 * @Description:系统提供的常用工具类
 * @author: ZhouRui
 * @date: 2015-11-25 下午4:45:59
 * 
 */
public final class SystemUtils {
	/**
	 * 按照格式返回匹配时间
	 * 
	 * @param pattern
	 *            匹配规则
	 * @return
	 */
	public static String getDateTime(String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern,
				Locale.CHINA);
		return dateFormat.format(new Date());
	}

	/**
	 * 按照HH:mm的格式返回时间
	 */
	public static String getDateTime() {
		return getDateTime("HH:mm");
	}

	/**
	 * 获取手机的IMEI号
	 */
	public static String getPhoneIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 发短信
	 * 
	 * @param aty
	 * @param tel
	 *            收信方
	 * @param content
	 *            短信内容
	 */
	public static void sendSMS(Activity aty, String tel, String content) {
		Uri uri = Uri.parse("smsto:" + tel);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", content);
		aty.startActivity(intent);
	}

	/**
	 * 打电话
	 * 
	 * @param context
	 * @param tel
	 *            电话号码
	 */
	public static void callTel(Context context, String tel) {
		Uri uri = Uri.parse("tel:" + tel);
		Intent intent = new Intent(Intent.ACTION_CALL, uri);
		context.startActivity(intent);
	}

	/**
	 * 从市场上下载应用
	 * 
	 * @param context
	 * @param packageName
	 *            包名 eg.如百度地图-com.baidu.BaiduMap
	 */
	public static void installFromMarket(Context context, String packageName) {
		Uri uri = Uri.parse("market://details?id=" + packageName);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
	}

	/**
	 * 判断当前应用是否在后台运行
	 * 
	 * @param context
	 * @param packageName
	 *            包名
	 * @return
	 */
	public static boolean isAvilible(Context context, String packageName) {
		// 获取packagemanager
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		// 用于存储所有已安装程序的包名
		List<String> packageNames = new ArrayList<String>();
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (packageInfos != null) {
			for (int i = 0; i < packageInfos.size(); i++) {
				String packName = packageInfos.get(i).packageName;
				packageNames.add(packName);
			}
		}
		// 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
		return packageNames.contains(packageName);
	}

	/**
	 * 从文件中安装APK
	 * 
	 * @param context
	 * @param file
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("application/vnd.android.package-archive");
		intent.setData(Uri.fromFile(file));
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 检测网络是否可用
	 */
	public static boolean checkNet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return networkInfo.isAvailable();
	}

	/**
	 * 判断是否是Wifi连接
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State state = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		return State.CONNECTED == state;
	}

	/**
	 * 关闭软键盘
	 */
	public static void hideKeyBoard(Activity context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(context.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 检测手机休眠
	 */
	public static boolean isSleeping(Context context) {
		KeyguardManager km = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		return km.inKeyguardRestrictedInputMode();
	}

	/**
	 * 获取versionName 版本名称如1.1.2
	 */
	public static String getAppVersionName(Context context) {
		String name = "";
		try {
			name = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取VersionCode 内部版本号 如17
	 */
	public static int getAppVersionCode(Context context) {
		int code = -1;
		try {
			code = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * 回到起始页Activity
	 */
	public static void goStart(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		context.startActivity(intent);
	}

	/**
	 * 获取可用内存空间 单位M
	 */
	public static int getDeviceUsableMemory(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Activity.ACTIVITY_SERVICE);
		MemoryInfo info = new MemoryInfo();
		am.getMemoryInfo(info);
		return (int) (info.availMem / (1024 * 1024));
	}

	/**
	 * 返回当前系统版本号如 API-17
	 */
	public static int getSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 返回当前系统版本2.3.3
	 */
	public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
}
