package com.star.eagleme.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

/**
 * Created by Emiya on 2016/6/2.
 */
public class ImageHelper
{

	/**
	 * 获取指定resId BitmapDrawable
	 *
	 * @param context 上下文
	 * @param resId   资源ID
	 * @return
	 */
	public static BitmapDrawable getBmpDrawable(Context context, int resId)
	{

		Bitmap bitmap = null;
		try
		{

			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inDither = false;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			opt.inJustDecodeBounds = false;//设置该属性可获得图片的长宽等信息，但是避免了不必要的提前加载动画

			//获取资源图片
			InputStream is = context.getResources().openRawResource(resId);
			bitmap = BitmapFactory.decodeStream(is, null, opt);
			is.close();

		}
		catch (IOException Exception)
		{
			return null;
		}

		return new BitmapDrawable(context.getResources(), bitmap);

	}

	/**
	 * 返回弱引用Bitmap
	 *
	 * @param context 上下文
	 * @param resId   资源id
	 * @return 软引用的bitmap
	 */
	public static Bitmap getSoftRefrenceBitmap(Context context, int resId)
	{
		Bitmap bitmap = getBmpDrawable(context, resId).getBitmap();
		SoftReference<Bitmap> sBitmapReference = new SoftReference<Bitmap>(bitmap);//使用bitmap软引用
		bitmap = null;//释放强引用

		return sBitmapReference.get();
	}

	/**
	 * 返回弱引用Bitmap
	 *
	 * @param bitmap Bitmap
	 * @return 软引用的bitmap
	 */
	public static Bitmap getSoftRefrenceBitmap(Bitmap bitmap)
	{
		SoftReference<Bitmap> sBitmapReference = new SoftReference<Bitmap>(bitmap);//使用bitmap软引用
		bitmap = null;//释放强引用

		return sBitmapReference.get();
	}

	/**
	 * 创建Bitmap 宽、高、配置
	 *
	 * @param width  宽
	 * @param height 高
	 * @param config 配置
	 * @return
	 */
	public static Bitmap createBitmap(int width, int height, Bitmap.Config config)
	{

		Bitmap bitmap = null;
		try
		{
			bitmap = Bitmap.createBitmap(width, height, config);
		}
		catch (OutOfMemoryError e)
		{
			while (bitmap == null)
			{
				System.gc();
				System.runFinalization();
				bitmap = createBitmap(width, height, config);
			}
		}
		return getSoftRefrenceBitmap(bitmap);

	}

	/**
	 * 创建Bitmap
	 *
	 * @param source The bitmap we are subsetting
	 * @param x      The x coordinate of the first pixel in source
	 * @param y      The y coordinate of the first pixel in source
	 * @param width  The number of pixels in each row
	 * @param height The number of rows
	 * @param m      Optional matrix to be applied to the pixels
	 * @param filter true if the source should be filtered.
	 * @return
	 */
	public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter)
	{

		Bitmap bitmap = null;
		try
		{
			bitmap = Bitmap.createBitmap(source, x, y, width, height, m, filter);
		}
		catch (OutOfMemoryError e)
		{
			while (bitmap == null)
			{
				System.gc();
				System.runFinalization();
				bitmap = Bitmap.createBitmap(source, x, y, width, height, m, filter);
			}
		}
		return getSoftRefrenceBitmap(bitmap);

	}

	/**
	 * 获取degree
	 *
	 * @param path 路径
	 * @return
	 */
	public static int readPictureDegree(String path)
	{

		File mFile = new File(path);
		if (!mFile.exists())
		{
			return 0;
		}

		int degree = 0;
		try
		{

			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation)
			{
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				default:
					break;
			}

		}
		catch (IOException e)
		{
		}
		return degree;

	}

	/**
	 * 获取指定尺寸Bitmap
	 *
	 * @param mPath      路径
	 * @param des_width  宽
	 * @param des_height 高
	 * @return Bitmap
	 */
	public static Bitmap getScaledBitmap(String mPath, int des_width, int des_height)
	{

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mPath, options);
		int bmp_width = options.outWidth;
		int bmp_height = options.outHeight;

		if (bmp_width > des_width || bmp_height > des_height)
		{

			float scale_width = bmp_width / (float) des_width;
			float scale_height = bmp_height / (float) des_height;

			if (scale_width > scale_height)
			{
				options.inSampleSize = ((int) scale_width) + 1;
			}
			else
			{
				options.inSampleSize = ((int) scale_height) + 1;
			}

		}

		if (options.inSampleSize < 1)
		{
			options.inSampleSize = 1;
		}
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap mBitmap = BitmapFactory.decodeFile(mPath, options);

		int degree = readPictureDegree(mPath);
		if (degree > 0)
		{
			mBitmap = reviseDegree(mBitmap, mPath, degree);
		}

		return getSoftRefrenceBitmap(mBitmap);

	}

	/**
	 * 修正degree
	 *
	 * @param bitmap 源文件
	 * @param mPath  路径
	 * @param degree 角度
	 * @return
	 */
	public static Bitmap reviseDegree(Bitmap bitmap, String mPath, int degree)
	{

		Matrix matrix = new Matrix();
		matrix.postRotate(degree);

		Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();
		return getSoftRefrenceBitmap(newBmp);

	}

	/**
	 * 压缩Bitmap生成文件
	 *
	 * @param bmp  源文件
	 * @param file 生成文件
	 */
	public static void compressBmpToFile(Bitmap bmp, File file)
	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		if (bmp != null)
		{

			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
			try
			{

				FileOutputStream fos = new FileOutputStream(file);
				fos.write(baos.toByteArray());
				fos.flush();
				fos.close();

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}

	}

	/**
	 * 给图片加水印
	 *
	 * @param watermark 水印
	 * @return 加水印的原图
	 */
	public static Bitmap WaterMask(final File file, Bitmap watermark, final String mUid)
	{

		Bitmap result = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		try
		{

			//获取本地原图
			Bitmap src = BitmapFactory.decodeFile(file.getPath());

			//获取原图宽高
			int w = src.getWidth();
			int h = src.getHeight();

			//根据bitmap缩放水印图片
			float w1 = w / 5;
			float h1 = (float) (w1 / 2.782);
			//获取原始水印图片的宽、高
			int w2 = watermark.getWidth();
			int h2 = watermark.getHeight();

			//计算缩放的比例
			float scalewidth = w1 / w2;
			float scaleheight = h1 / h2;

			Matrix matrix = new Matrix();
			matrix.postScale(scalewidth, scaleheight);

			watermark = Bitmap.createBitmap(watermark, 0, 0, w2, h2, matrix, true);
			//获取新的水印图片的宽、高
			h2 = watermark.getHeight();

			result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
			Canvas cv = new Canvas(result);
			//在canvas上绘制原图和新的水印图
			cv.drawBitmap(src, 0, 0, null);

			//水印图绘制在画布的左下角
			cv.drawBitmap(watermark, 0, cv.getHeight() - h2 - 1, null);
			cv.save(Canvas.ALL_SAVE_FLAG);
			cv.restore();
			watermark.recycle();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;

	}

}
