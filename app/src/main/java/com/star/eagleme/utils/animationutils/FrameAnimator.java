package com.star.eagleme.utils.animationutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.star.eagleme.BaseApplication;
import com.star.eagleme.utils.ImageHelper;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

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
        FramesSequenceAnimation framesSequenceAnimation = new FramesSequenceAnimation(imageView, mProgressAnimFrames, FPS);

        return framesSequenceAnimation;
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
        private ArrayList<Bitmap> bmpList = new ArrayList<Bitmap>();

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
	        Bitmap bitmap = ImageHelper.getSoftRefrenceBitmap(mContext,mFrames[0]);
	        if(bitmap != null)
	        {
		        imageView.setImageBitmap(bitmap);
                bmpList.add(bitmap);
	        }

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
                        if (mShouldRun)
                        {
                            try
                            {
	                            Bitmap getBitmap = ImageHelper.getSoftRefrenceBitmap(mContext, imageRes);
	                            if (getBitmap != null)
	                            {
		                            imageView.setImageBitmap(getBitmap);
		                            bmpList.add(getBitmap);
	                            }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
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

        public void releaseBitmap() {

            if (bmpList.size() != 0) {
                for (Bitmap bmp : bmpList) {
                    bmp.recycle();
                }
                bmpList.clear();
                bmpList = null;
            }

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
        void AnimationStopped();
    }
}