package org.panda.xandroidframe.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.graphics.BitmapFactory;

public class ImageHelper {
//	public static File getSmallImageFile(Context context, String filePath,
//			int width, int height, boolean allowAdjust) {
//		File file = new File(context.getCacheDir().getAbsolutePath());
//		Options opt = new Options();
//		Bitmap bitmap = BitmapFactory.decode;
//	}

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

//	public static Bitmap reduce(String bitmap, int width, int height,
//			boolean allowAdjust) {
//		Options opts = new Options();
//		opts.inJustDecodeBounds = true;
//	}

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
