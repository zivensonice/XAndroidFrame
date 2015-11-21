package org.panda.xandroidframe.utils;

import java.util.List;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

/**
 * @类名:L
 * @功能描述:日志输出控制类 <li>默认打印TAG为XMN,通过{@link setTag}设置打印TAG <li>通过
 *               {@link setDebugLevel}
 *               方法设置打印级别,设置为如果大于50,表示不打印任何日志,设置小于等于为表示打印所有级别日志
 * @作者:ZhouRui
 * @时间:2014-11-20 下午3:20:53
 * @Copyright 2014
 */
public class L {

	/** 日志输出级别NONE,不打印任何日志 */
	protected static final int LEVEL_NONE = 0;
	/** 日志输出级别V */
	private static final int LEVEL_VERBOSE = 1;
	/** 日志输出级别D */
	private static final int LEVEL_DEBUG = 2;
	/** 日志输出级别I */
	private static final int LEVEL_INFO = 3;
	/** 日志输出级别W */
	private static final int LEVEL_WARN = 4;
	/** 日志输出级别E */
	private static final int LEVEL_ERROR = 5;

	/** 日志输出时的TAG */
	private static String mTag = "XAndroidFrame";
	/** 是否允许输出log, 设置为>5,表示不打印任何日志,设置为<=0表示打印所有级别日志 */
	private static int mDebuggable = LEVEL_DEBUG;
	/** 记录Log打印时间 */
	private static long mTimestamp = 0;
	/** Log写入文件锁 */
	private static final Object mLogLock = new Object();

	/**
	 * 方法名: setTag
	 * 
	 * 功能描述: 设置打印日志TAG
	 * 
	 * @param tag
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void setTag(String tag) {
		mTag = tag;
	}

	/**
	 * 方法名: setDebuggable
	 * 
	 * 功能描述: 设置打印控制级别
	 * 
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void setDebugLevel(int debugLevel) {

		mDebuggable = debugLevel;
	}

	/**
	 * 方法名: v
	 * 
	 * 功能描述: 以级别为 v 的形式输出LOG
	 * 
	 * @param msg
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void v(String msg) {
		if (mDebuggable <= LEVEL_VERBOSE) {
			Log.v(mTag, msg);
		}
	}

	/**
	 * 方法名: d
	 * 
	 * 功能描述: 以级别为 d 的形式输出LOG
	 * 
	 * @param msg
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void d(Object msg) {
		if (mDebuggable <= LEVEL_DEBUG) {
			if (msg instanceof String) {
				Log.d(mTag, (String) msg);
			} else if (msg instanceof Integer) {
				Log.d(mTag, "int:" + msg);
			} else if (msg instanceof Float) {
				Log.d(mTag, "int:" + msg);
			} else if (msg instanceof Double) {
				Log.d(mTag, "int:" + msg);
			} else if (msg instanceof Long) {
				Log.d(mTag, "long:" + msg);
			} else if (msg instanceof Map) {
				try {
					@SuppressWarnings("unchecked")
					Map<String, Object> maps = (Map<String, Object>) msg;
					for (Map.Entry<String, Object> entry : maps.entrySet()) {
						L.d("key=%s,value=%s", entry.getKey().toString(), entry
								.getValue().toString());
					}
				} catch (Exception e) {
					L.d("不能转换成Map<String,Object>类型");
				}
			} else {
				Log.d(mTag, "other:" + msg.toString());
			}
		}
	}

	public final static void d(String msg, Object... objects) {
		d(String.format(msg, objects));
	}

	/**
	 * 方法名: d
	 * 
	 * 功能描述: 以级别为 d 的形式输出LOG
	 * 
	 * @param msg
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void i(Object msg) {
		if (mDebuggable <= LEVEL_INFO) {
			if (msg instanceof String) {
				Log.i(mTag, (String) msg);
			} else if (msg instanceof Integer) {
				Log.i(mTag, "int:" + msg);
			} else if (msg instanceof Float) {
				Log.i(mTag, "int:" + msg);
			} else if (msg instanceof Double) {
				Log.i(mTag, "int:" + msg);
			} else if (msg instanceof Long) {
				Log.i(mTag, "long:" + msg);
			} else if (msg instanceof Map) {
				try {
					@SuppressWarnings("unchecked")
					Map<String, Object> maps = (Map<String, Object>) msg;
					for (Map.Entry<String, Object> entry : maps.entrySet()) {
						L.i("key=%s,value=%s", entry.getKey().toString(), entry
								.getValue().toString());
					}
				} catch (Exception e) {
					L.i("不能转换成Map<String,Object>类型");
				}
			} else {
				Log.i(mTag, "other:" + msg.toString());
			}
		}
	}

	public final static void i(String msg, Object... objects) {
		i(String.format(msg, objects));
	}

	/**
	 * 方法名: d
	 * 
	 * 功能描述: 以级别为 d 的形式输出LOG
	 * 
	 * @param msg
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void e(Object msg) {
		if (mDebuggable <= LEVEL_ERROR) {
			if (msg instanceof String) {
				Log.e(mTag, (String) msg);
			} else if (msg instanceof Integer) {
				Log.e(mTag, "int:" + msg);
			} else if (msg instanceof Float) {
				Log.e(mTag, "int:" + msg);
			} else if (msg instanceof Double) {
				Log.e(mTag, "int:" + msg);
			} else if (msg instanceof Long) {
				Log.e(mTag, "long:" + msg);
			} else if (msg instanceof Map) {
				try {
					@SuppressWarnings("unchecked")
					Map<String, Object> maps = (Map<String, Object>) msg;
					for (Map.Entry<String, Object> entry : maps.entrySet()) {
						L.e("key=%s,value=%s", entry.getKey().toString(), entry
								.getValue().toString());
					}
				} catch (Exception e) {
					L.e("不能转换成Map<String,Object>类型");
				}
			} else {
				Log.e(mTag, "other:" + msg.toString());
			}
		}
	}

	public final static void e(String msg, Object... objects) {
		e(String.format(msg, objects));
	}

	/**
	 * 方法名: w
	 * 
	 * 功能描述: 以级别为 w 的形式输出LOG
	 * 
	 * @param msg
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void w(String msg) {
		if (mDebuggable <= LEVEL_WARN) {
			Log.w(mTag, msg);
		}
	}

	/**
	 * 方法名: w
	 * 
	 * 功能描述:以级别为 w 的形式输出Throwable
	 * 
	 * @param tr
	 *            异常
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void w(Throwable tr) {
		if (mDebuggable <= LEVEL_WARN) {
			Log.w(mTag, "", tr);
		}
	}

	/**
	 * 方法名: w
	 * 
	 * 功能描述:以级别为 w 的形式输出LOG信息和Throwable
	 * 
	 * @param msg
	 *            打印信息
	 * @param tr
	 *            warning异常信息
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void w(String msg, Throwable tr) {
		if (mDebuggable <= LEVEL_WARN && null != msg) {
			Log.w(mTag, msg, tr);
		}
	}

	/**
	 * 方法名: e
	 * 
	 * 功能描述:以级别为 e 的形式输出LOG信息和Throwable
	 * 
	 * @param msg
	 *            打印error日志
	 * @param tr
	 *            打印error异常
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void e(String msg, Throwable tr) {
		if (mDebuggable <= LEVEL_ERROR && null != msg) {
			Log.e(mTag, msg, tr);
		}
	}

	/**
	 * 方法名: log2File
	 * 
	 * 功能描述:把Log存储到文件中
	 * 
	 * @param log
	 *            需要存储的日志
	 * @param path
	 *            存储路径
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void log2File(String log, String path) {
		log2File(log, path, true);
	}

	/**
	 * 方法名: log2File
	 * 
	 * 功能描述: 把Log储存到文件
	 * 
	 * @param log
	 *            log信息
	 * @param path
	 *            文件路径
	 * @param append
	 *            是否使用添加到文件结尾的方式存储
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void log2File(String log, String path, boolean append) {
		// 资源写入锁
		synchronized (mLogLock) {
			// TODO 写入文件 FileUtil.writeFile(log + "\r\n", path, append);
		}
	}

	/**
	 * 方法名: msgStartTime
	 * 
	 * 功能描述:以级别为 e 的形式输出msg信息,附带时间戳，用于输出一个时间段起始点
	 * 
	 * @param msg
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void msgStartTime(String msg) {
		mTimestamp = System.currentTimeMillis();
		if (!TextUtils.isEmpty(msg)) {
			e("[Started：" + mTimestamp + "]" + msg);
		}
	}

	/**
	 * 方法名: elapsed
	 * 
	 * 功能描述:<li>以级别为 e 的形式输出msg信息,附带时间戳，用于输入中间花费了多长时间 <li>配合{@link msgStartTime}
	 * 一起使用
	 * 
	 * @param msg
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static void elapsed(String msg) {
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - mTimestamp;
		mTimestamp = currentTime;
		e("[Elapsed：" + elapsedTime + "]" + msg);
	}

	/**
	 * 方法名: printList
	 * 
	 * 功能描述: 用于输入List<T>,泛型类必须实现toString方法
	 * 
	 * @param list
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static <T> void printList(List<T> list) {
		if (list == null || list.size() < 1) {
			return;
		}
		int size = list.size();
		i("---begin---");
		for (int i = 0; i < size; i++) {
			i(i + ":" + list.get(i).toString());
		}
		i("---end---");
	}

	/**
	 * 方法名: printArray
	 * 
	 * 功能描述: 用于输出泛型数组,泛型必须实现toString方法
	 * 
	 * @param array
	 * @return void
	 * 
	 *         </br>throws
	 */
	public static <T> void printArray(T[] array) {
		if (array == null || array.length < 1) {
			return;
		}
		int length = array.length;
		i("---begin---");
		for (int i = 0; i < length; i++) {
			i(i + ":" + array[i].toString());
		}
		i("---end---");
	}
}
