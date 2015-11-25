package org.panda.xandroidframe.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.text.TextUtils;

/**
 * @ClassName: FileHelper
 * @Description: 文件操作工具类
 * @author: ZhouRui
 * @date: 2015-11-21 下午12:46:37
 * 
 */
public class FileUtil {
	/**
	 * @return 检测SD卡是否可用
	 */
	public static boolean hasAvaildableSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * 保存bytep[]到文件中
	 * 
	 * @param datas
	 *            数据
	 * @param folderPath
	 *            文件路径
	 * @param fileName
	 *            文件名
	 */
	public static void saveFileCache(byte[] datas, String folderPath,
			String fileName) {
		File folder = new File(folderPath);
		folder.mkdirs();
		File file = new File(folder, fileName);
		ByteArrayInputStream bIps = new ByteArrayInputStream(datas);
		OutputStream ops = null;
		if (!file.exists()) {
			try {
				file.createNewFile();
				ops = new FileOutputStream(file);
				byte[] buff = new byte[1024 * 4];
				int len;
				while (-1 != (len = bIps.read(buff))) {
					ops.write(buff, 0, len);
				}
				ops.flush();
			} catch (Exception e) {
				throw new RuntimeException(FileUtil.class.getClass()
						.getName(), e);
			} finally {
				closeIO(bIps, ops);
			}
		}

	}

	/**
	 * @param folderPath
	 *            文件路径
	 * @param fileName
	 *            文件名
	 * @return 获取的文件
	 */
	public static File getSaveFile(String folderPath, String fileName) {
		File file = new File(folderPath + File.separator + fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * @param folderName
	 *            文件夹名
	 * @return 文件路径
	 */
	public static String getSavePath(String folderName) {
		return getSaveFolder(folderName).getAbsolutePath();
	}

	/**
	 * @param folderName
	 *            文件夹名
	 * @return 文件夹绝对路径
	 */
	public static File getSaveFolder(String folderName) {
		File folder = new File(getSDCardPath() + File.separator + folderName
				+ File.separator);
		folder.mkdirs();
		return folder;
	}

	/**
	 * @return SD卡的绝对路径
	 */
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 数据流写入byte[]
	 * 
	 * @param ips
	 *            输入流
	 * @return 字符组
	 */
	public static byte[] input2Byte(InputStream ips) {
		if (ips == null) {
			return null;
		}
		BufferedInputStream bIps = new BufferedInputStream(ips);
		ByteArrayOutputStream bOps = new ByteArrayOutputStream();
		byte[] results = null;
		byte[] buff = new byte[1024 * 4];
		int len;
		try {
			while (-1 != (len = bIps.read(buff))) {
				bOps.write(buff, 0, len);
			}
			results = bOps.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeIO(bIps, bOps, ips);
		}
		return results;
	}

	/**
	 * 复制流
	 * 
	 * @param fIps
	 * @param fOps
	 */
	public static void copyFileStream(FileInputStream fIps,
			FileOutputStream fOps) {
		FileChannel fcI = fIps.getChannel();
		FileChannel fcO = fOps.getChannel();
		try {
			fcI.transferTo(0, fcI.size(), fcO);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeIO(fIps, fOps);
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param from
	 *            复制源文
	 * @param to
	 *            目标
	 */
	public static void copyFile(File from, File to) {
		if (!from.exists() || !from.isFile()) {
			return;
		}
		if (to == null) {
			return;
		}
		FileInputStream fIps = null;
		FileOutputStream fOps = null;
		try {
			fIps = new FileInputStream(from);
			fOps = new FileOutputStream(to);
			copyFileStream(fIps, fOps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeIO(fIps, fOps);
		}
	}

	/**
	 * 把Bitmap存储到文件-存储格式：PNG
	 * 
	 * @param bitmap
	 *            待处理图片
	 * @param bitmapPath
	 *            输出路径
	 * @return 处理结果
	 */
	public static boolean bitmapToFile(Bitmap bitmap, String bitmapPath) {
		boolean result = false;
		if (bitmap == null || TextUtils.isEmpty(bitmapPath)) {
			result = false;
		}
		File folder = new File(bitmapPath.substring(0,
				bitmapPath.lastIndexOf(File.separator)));
		if (!folder.exists()) {
			folder.mkdirs();
		}
		OutputStream ops = null;
		try {
			ops = new BufferedOutputStream(new FileOutputStream(bitmapPath),
					8 * 1024);
			bitmap.compress(CompressFormat.PNG, 100, ops);
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeIO(ops);
		}
		return result;
	}

	/**
	 * 把文件读入字符串
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 读入结果
	 */
	public static String readFile(String filePath) {
		String result = null;
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			try {
				InputStream ips = new BufferedInputStream(new FileInputStream(
						filePath), 8 * 1024);
				result = inputStream2String(ips);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 输入流->字符串
	 * 
	 * @param ips
	 *            输入流
	 * @return
	 */
	public static String inputStream2String(InputStream ips) {
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(ips));
			String line;
			if (null != (line = br.readLine())) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeIO(ips);
		}
		return sb == null ? null : sb.toString();
	}

	/**
	 * 从Assert文件中读取一个文件，例如在Assert文件中存放一个"demo.txt"的文件 <li>
	 * readFileFromAssert(context,"demo.txt");
	 * 
	 * @param context
	 * @param fileName
	 *            文件名称
	 * @return 字符集
	 */
	public static String readFileFromAssert(Context context, String fileName) {
		InputStream ips = null;
		try {
			ips = context.getResources().getAssets().open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream2String(ips);
	}

	/**
	 * 关闭文件流
	 */
	public static void closeIO(Closeable... closeables) {
		if (closeables == null || closeables.length == 0) {
			return;
		}
		for (Closeable closeable : closeables) {
			if (closeable == null) {
				continue;
			}
			try {
				closeable.close();
			} catch (IOException e) {
				L.e("%s,closeIO方法关闭流异常。", FileUtil.class.getClass().getName());
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 是否是一个文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isFile(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			return true;
		}
		return false;
	}
}
