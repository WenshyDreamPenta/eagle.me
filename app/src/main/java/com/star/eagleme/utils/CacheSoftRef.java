package com.star.eagleme.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: star
 * Descriptions: Bitmap软引用类
 * Date: 2017/10/25.
 */

public class CacheSoftRef
{
    //首先定义一个HashMap,保存引用对象
    private Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

    /**
     * 保存Bitmap的软引用到HashMap
     * @param name name
     * @param resid 资源Id
     * @param context 上下文
     */
    public void addBitmapToCache(String name, int resid , Context context)
    {
        //强引用的Bitmap对象
        Bitmap bitmap = getBmpDrawable(context, resid).getBitmap();
        //软引用的Bitmap对象
        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
        //添加该对象到Map中使其缓存
        imageCache.put(name, softBitmap);
    }

    /**
     *获取的时候，可以通过SoftReference的get()方法得到Bitmap对象
     * @param name
     * @return Bitmap
     */
    public Bitmap getBitmapByPath(String name)
    {
        //从缓存中取软引用的Bitmap对象
        SoftReference<Bitmap> softBitmap = imageCache.get(name);
        //判断是否存在软引用
        if (softBitmap == null)
        {
            return null;
        }
        //通过软引用取出Bitmap对象，如果由于内存不足Bitmap被回收，将取得空，如果未被回收，
        //则可重复使用，提高速度。
        Bitmap bitmap = softBitmap.get();

        return bitmap;
    }
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

        } catch (OutOfMemoryError e)
        {
            //内存溢出處理，釋放内存，重新獲取
            System.gc();
            System.runFinalization();
            getBmpDrawable(context, resId);

        } catch (IOException Exception)
        {
            return null;
        }

        return new BitmapDrawable(context.getResources(), bitmap);

    }
}
