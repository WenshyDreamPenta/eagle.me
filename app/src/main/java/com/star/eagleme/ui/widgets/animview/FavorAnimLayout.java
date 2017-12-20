package com.star.eagleme.ui.widgets.animview;

import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.star.eagleme.R;
import com.star.eagleme.utils.BitmapsUtil;

import java.util.Random;

/**
 * Created by star on 2017/10/10.
 *
 * @description: cos曲线配合imageview
 */
public class FavorAnimLayout extends RelativeLayout {
	private Point currentPoint;
	private LayoutParams lp;
	private int mHeight;
	private int mWidth;
	private AnimatorSet animSet;
	private TimeInterpolator interpolatorType = new LinearInterpolator();
	private Random random = new Random();
	//图片大小
	private int dHeight;
	private int dWidth;
	//图片数组
	private int[] mDrawbleSrc = new int[]{R.mipmap.ic_live_praise_01, R.mipmap.ic_live_praise_02, R.mipmap.ic_live_praise_03,
			R.mipmap.ic_live_praise_04, R.mipmap.ic_live_praise_05, R.mipmap.ic_live_praise_06, R.mipmap.ic_live_praise_07,
			R.mipmap.ic_live_praise_08};

	public FavorAnimLayout(Context context) {
		super(context);
		init();
	}

	public FavorAnimLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		//初始化显示的图片
		Drawable drawable = getResources().getDrawable(mDrawbleSrc[0]);
		dHeight = drawable.getIntrinsicHeight();
		dWidth = drawable.getIntrinsicWidth();

		//底部 并且 水平居中
		lp = new LayoutParams(dWidth, dHeight);
		lp.addRule(CENTER_HORIZONTAL, TRUE);//这里的TRUE 要注意 不是true
		lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
	}

	//添加View
	public void addSinAnim() {
		ImageView imageView = new ImageView(getContext());
		imageView.setLayoutParams(lp);
		imageView.setImageDrawable(
				BitmapsUtil.getBmpDrawable(getContext(), mDrawbleSrc[random.nextInt(mDrawbleSrc.length)]));
		addView(imageView);
		startSinAnimation(imageView);
	}

	//开启动画
	public void startSinAnimation(final ImageView view) {
		Point startP = new Point(0, 0);
		Point endP = new Point(mWidth - dWidth, mHeight);
		final ValueAnimator valueAnimator = ValueAnimator.ofObject(new PointSinEvaluator(), startP, endP);
		valueAnimator.setRepeatCount(-1);
		valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
		valueAnimator.setTarget(view);
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				currentPoint = (Point) animation.getAnimatedValue();
				view.setX(currentPoint.x);
				view.setY(currentPoint.y);
				// 这里顺便做一个alpha动画
				//	view.setAlpha(1 - animation.getAnimatedFraction());

			}
		});
		animSet = new AnimatorSet();
		animSet.play(valueAnimator);
		animSet.setDuration(5000);
		setInterpolatorType(5);
		animSet.setInterpolator(interpolatorType);
		animSet.start();

	}


	// Sets interpolator type.
	public void setInterpolatorType(int type) {
		switch (type) {
			case 1:
				interpolatorType = new BounceInterpolator();
				break;
			case 2:
				interpolatorType = new AccelerateDecelerateInterpolator();
				break;
			case 3:
				interpolatorType = new DecelerateInterpolator();
				break;
			case 4:
				interpolatorType = new AnticipateInterpolator();
				break;
			case 5:
				interpolatorType = new LinearInterpolator();
				break;
			case 6:
				interpolatorType = new LinearOutSlowInInterpolator();
				break;
			case 7:
				interpolatorType = new OvershootInterpolator();
			default:
				interpolatorType = new LinearInterpolator();
				break;
		}
	}

	public void pauseAnimation() {
		if (animSet != null) {
			animSet.pause();
		}
	}

	public void stopAnimation() {
		if (animSet != null) {
			animSet.cancel();
			this.clearAnimation();
		}
	}
}
