package org.panda.xandroidframe.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @ClassName: SPHelper
 * @Description:SharedPreference工具类 不支持跨进程应用
 * @author: ZhouRui
 * @date: 2015-11-20 下午3:39:44
 * 
 */
public class SPUtil {

	/**
	 * @param context
	 *            上下文
	 * @param fileName
	 *            SharedPreferences文件名称
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean put(Context context, String fileName, String key,
			Object value) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		if (value instanceof String) {
			editor.putString(key, (String) value);
		} else if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		} else if (value instanceof Float) {
			editor.putFloat(key, (Float) value);
		} else if (value instanceof Long) {
			editor.putLong(key, (Long) value);
		} else {
			editor.putString(key, value.toString());
		}
		return SPCompat.apply(editor);
	}

	/**
	 * @param context
	 *            上下文
	 * @param fileName
	 *            SharedPreferences文件名称
	 * @param key
	 * @param defaultValue
	 *            默认值
	 * @return 获取的结果，需要强转
	 */
	public static Object get(Context context, String fileName, String key,
			Object defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		if (defaultValue instanceof String) {
			return sp.getString(key, (String) defaultValue);
		} else if (defaultValue instanceof Integer) {
			return sp.getInt(key, (Integer) defaultValue);
		} else if (defaultValue instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultValue);
		} else if (defaultValue instanceof Float) {
			return sp.getFloat(key, (Float) defaultValue);
		} else if (defaultValue instanceof Long) {
			return sp.getLong(key, (Long) defaultValue);
		} else {
			return null;
		}
	}

	/**
	 * 清空
	 */
	public static boolean clear(Context context, String fileName) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		return SPCompat.apply(editor);
	}

	/**
	 * 移除
	 */
	public static boolean remove(Context context, String fileName, String key) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.remove(key);
		return SPCompat.apply(editor);
	}

	/**
	 * @param context
	 * @param fileName
	 * @return 所有的键值对
	 */
	public static Map<String, ?> getAll(Context context, String fileName) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return sp.getAll();
	}

	/**
	 * 是否包含
	 */
	public static boolean contain(Context context, String fileName, String key) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.remove(key);
		return SPCompat.apply(editor);
	}

	/**
	 * @ClassName: SPCompat
	 * @Description: SharedPreferences兼容类，IO操作建议使用异步方式完成 apply方法异步 commit方法同步 但是
	 *               API 9开始才支持apply方法。所以从Editor类里面找到apply方法就使用，没找到就使用commit方法
	 * @author: ZhouRui
	 * @date: 2015-11-20 下午3:51:13
	 * 
	 */
	private static class SPCompat {
		private final static Method sApplyMethod = sFindApplyMethod();

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static Method sFindApplyMethod() {
			try {
				Class clazz = SharedPreferences.Editor.class;
				clazz.getMethod("apply");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			return null;
		}

		public static boolean apply(SharedPreferences.Editor editor) {
			try {
				if (null != sApplyMethod) {
					Object obj = sApplyMethod.invoke(editor);
					if (obj instanceof Boolean) {
						return (Boolean) obj;
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
			}
			return editor.commit();
		}
	}
}
