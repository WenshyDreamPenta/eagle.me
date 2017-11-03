package com.star.eagleme.utils.animationutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.widget.ImageView;

import com.star.eagleme.base.BaseApplication;

import java.lang.ref.SoftReference;


/**
 * author: star
 * descriptions: 帧动画播放
 *               使用：1.帧数组资源id在arrays.xml
 *                    2.适合做帧数较大的帧动画使用，避免OOM
 * date: 2017/10/24 11:46
 */

public class FrameAnimator
{
    public int FPS;  // 每秒播放帧数，fps = 1/t，t-动画两帧时间间隔
    private Context mContext;
    // 单例
    private static FrameAnimator mInstance;

    // 从xml中读取资源ID数组
    private int[] mProgressAnimFrames;

    /**
     * 单例构造
     * @param resId 资源数组id
     * @param fps   帧数
     * @return
     */
    public static FrameAnimator getInstance(int resId, int fps)
    {
        if (mInstance == null)
        {
            mInstance = new FrameAnimator();
        }
        mInstance.FPS = fps;
        mInstance.mContext = BaseApplication.getAppContext();
        mInstance.mProgressAnimFrames = mInstance.getData(resId);

        return mInstance;
    }

    /**
     * @param imageView 初始化帧Anim
     * @return progress dialog animation
     */
    public FramesSequenceAnimation createFramesAnim(ImageView imageView)
    {
        return new FramesSequenceAnimation(imageView, mProgressAnimFrames, FPS);
    }


    /**
     * 内部类-帧动画播放类
     */
    public class FramesSequenceAnimation
    {
        private int[] mFrames; // 帧数组
        private int mIndex; // 当前帧
        private boolean mShouldRun; // 开始/停止播放用
        private boolean mIsRunning; // 动画是否正在播放，防止重复播放
        private SoftReference<ImageView> mSoftReferenceImageView; // 软引用ImageView，以便及时释放掉
        private Handler mHandler;
        private int mDelayMillis;
        private OnAnimationStoppedListener mOnAnimationStoppedListener; //播放停止监听

        private Bitmap mBitmap = null;
        private BitmapFactory.Options mBitmapOptions;//Bitmap管理类，可有效减少Bitmap的OOM问题

        /**
         * 初始化帧动画
         *
         * @param imageView 目标View
         * @param frames    帧保存数组
         * @param fps       fps
         */
        public FramesSequenceAnimation(ImageView imageView, int[] frames, int fps)
        {
            //参数初始化
            mHandler = new Handler();
            mFrames = frames;
            mIndex = -1;
            mSoftReferenceImageView = new SoftReference<ImageView>(imageView);
            mShouldRun = false;
            mIsRunning = false;
            mDelayMillis = 1000 / fps;//帧动画时间间隔，毫秒

            //设置第一帧
            imageView.setImageResource(mFrames[0]);

            // 当图片大小类型相同时进行复用，避免频繁GC
            Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            Bitmap.Config config = bmp.getConfig();
            mBitmap = Bitmap.createBitmap(width, height, config);
            mBitmapOptions = new BitmapFactory.Options();

            //设置Bitmap内存复用
            mBitmapOptions.inBitmap = mBitmap;//Bitmap复用内存块，类似对象池，避免不必要的内存分配和回收
            mBitmapOptions.inMutable = true;//解码时返回可变Bitmap
            mBitmapOptions.inSampleSize = 1;//缩放比例

        }

        /**
         * 播放动画，同步锁防止多线程读帧时，数据安全问题
         */
        public synchronized void start()
        {
            mShouldRun = true;
            if (mIsRunning) return;

            //读帧线程
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    ImageView imageView = mSoftReferenceImageView.get();
                    if (!mShouldRun || imageView == null)
                    {
                        mIsRunning = false;
                        if (mOnAnimationStoppedListener != null)
                        {
                            mOnAnimationStoppedListener.AnimationStopped();
                        }
                        mHandler.removeCallbacks(this);
                        return;
                    }

                    mIsRunning = true;
                    //新开线程去读下一帧
                    mHandler.postDelayed(this, mDelayMillis);

                    if (imageView.isShown())
                    {
                        int imageRes = getNextFrame();
                        if (mBitmap != null && !mShouldRun)
                        {
                            if (mOnAnimationStoppedListener != null)
                            {
                                mOnAnimationStoppedListener.AnimationStarted();
                            }
                            Bitmap bitmap = null;
                            try
                            {
                                bitmap = BitmapFactory.decodeResource(imageView.getResources(), imageRes, mBitmapOptions);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            if (bitmap != null)
                            {
                                imageView.setImageBitmap(bitmap);
                            }
                            else
                            {
                                imageView.setImageResource(imageRes);
                                mBitmap.recycle();
                                mBitmap = null;
                            }
                        }
                        else
                        {
                            imageView.setImageResource(imageRes);
                        }
                    }

                }
            };

            mHandler.post(runnable);
        }

        /**
         * 停止播放
         */
        public synchronized void stop()
        {
            mShouldRun = false;
        }

        /**
         * 获取下一帧
         * @return int 资源ResId
         */
        private int getNextFrame()
        {
            mIndex++;
            if (mIndex >= mFrames.length)
            {
                mIndex = mFrames.length - 1;
                mShouldRun = false;//数组遍历完成，则停止动画，当前停留在最后一帧
            }
            return mFrames[mIndex];
        }

        /**
         * 设置停止播放监听
         *
         * @param listener
         */
        public void setOnAnimStopListener(OnAnimationStoppedListener listener)
        {
            this.mOnAnimationStoppedListener = listener;
        }
    }

    /**
     * 从xml中读取帧数组
     *
     * @param resId 数组地址
     * @return 资源ID数组
     */
    private int[] getData(int resId)
    {
        TypedArray array = mContext.getResources().obtainTypedArray(resId);

        int len = array.length();
        int[] intArray = new int[array.length()];

        for (int i = 0; i < len; i++)
        {
            intArray[i] = array.getResourceId(i, 0);
        }
        array.recycle();

        return intArray;
    }

    /**
     *监听接口
     */
    public interface OnAnimationStoppedListener
    {
        void AnimationStarted();
        void AnimationStopped();
    }
}