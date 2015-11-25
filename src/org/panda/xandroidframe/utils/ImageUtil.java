package org.panda.xandroidframe.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class ImageUtil {
	/** 压缩比 默认值 */
	@SuppressWarnings("unused")
	private static final int DEFAULT_VALUE = -1;

	/**
	 * 生成缩略图片
	 * 
	 * @param context
	 * @param filePath
	 *            大图地址
	 * @param width
	 *            生成图片宽度 不需要设置传入-1{@link ImageUtil.DEFAULT_VALUE}
	 * @param height
	 *            生成图片高度 不需要设置传入-1{@link ImageUtil.DEFAULT_VALUE}
	 * @param maxNumOfPixels
	 *            最大像素值 不需要设置传入-1{@link ImageUtil.DEFAULT_VALUE}
	 * @return
	 */
	public static File getSmallImageFile(Context context, String filePath,
			int width, int height, int maxNumOfPixels) {
		// 生成随机jpeg文件
		File smallFile = new File(getRandomFileName(context.getCacheDir()
				.getAbsolutePath()));
		String smallFilePath = smallFile.getAbsolutePath();
		Bitmap bitmap = reduce(filePath, width, height, maxNumOfPixels);
		FileUtil.bitmapToFile(bitmap, smallFilePath);
		return smallFile;
	}

	/**
	 * 获取一个随机的图片文件名称，格式为jpeg
	 */
	public static String getRandomFileName(String folderPath) {
		String path = folderPath;
		if (folderPath == null) {
			return null;
		}
		if (File.separatorChar != path.charAt(path.length() - 1)) {
			path = path + File.separator;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String head = format.format(date);
		Random random = new Random();
		head = head + random.nextInt();
		path = path + head.substring(0, 15) + ".jpeg";
		return path;
	}

	/**
	 * @param bitmapFile
	 *            bitmap文件路径
	 * @param width
	 *            宽度 不需要设置传入-1
	 * @param height
	 *            高度 不需要设置传入-1
	 * @param maxNumOfPixels
	 *            最低像素值
	 * @return
	 */
	public static Bitmap reduce(String bitmapFile, int width, int height,
			int maxNumOfPixels) {
		Bitmap bitmap = null;
		if (FileUtil.isFile(bitmapFile)) {
			return bitmap;
		}
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(bitmapFile, opts);

		int minLength = Math.min(height, width);
		opts.inSampleSize = computeInitialSampleSize(opts, minLength,
				maxNumOfPixels);
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(bitmapFile, opts);
		return bitmap;
	}

	/**
	 * @param options
	 * @param minSideLength
	 *            长宽最短值 <li>如果不需要设置 传入-1
	 * @param maxNumOfPixels
	 *            最低像素值<li>如果不需要设置 传入-1
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				// 小于8，指数增长
				roundedSize <<= 1;
			}
		} else {
			// 如果初始尺寸大于8，那么按照线性增长
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		// 2的128次方缩放
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// 测量不为0 返回压缩最大的一个
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			// 原比例
			return 1;
		} else if (minSideLength == -1) {
			// 最大长度为0，返回像素比
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
