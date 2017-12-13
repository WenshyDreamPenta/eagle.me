package com.star.eagleme.activitity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jaeger.library.StatusBarUtil;
import com.star.eagleme.R;
import com.star.eagleme.bean.EasyBean;
import com.star.eagleme.socket.client.ConnectionClient;
import com.star.eagleme.socket.protocol.DataAckProtocol;
import com.star.eagleme.socket.protocol.DataProtocol;
import com.star.eagleme.socket.request.RequestCallBack;
import com.star.eagleme.ui.widgets.animview.PointAnimView;
import com.star.eagleme.utils.FrameAnimatorUtil;
import com.star.eagleme.utils.logutil.ManageLog;

import java.lang.reflect.Method;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

/**
 * MainActivity
 *
 * @author star
 * @date 2017/10/18
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
	private ThreadPoolExecutor threadPoolExecutor;
	private ConnectionClient connClinet;
	private RequestCallBack requestCallBack;

	private TextView etText;
	private TextView etSend;
	private TextView tvget;
	private TextView tvclose;
	private TextView tvconnect;
	private TextView tvrefresh;
	private SimpleDraweeView imageView;
	private PointAnimView pointAnimView;

	public static Class<EasyBean> cls;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Fresco.initialize(this);
		setContentView(R.layout.activity_main);
		StatusBarUtil.setTransparent(this);
		initViews();
		initThreadExcute();

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
				.detectDiskWrites()
				.detectNetwork()
				.penaltyLog()
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
				.detectLeakedClosableObjects()
				.penaltyLog()
				.penaltyDeath()
				.build());
		FrameAnimatorUtil.FramesSequenceAnimation animation = FrameAnimatorUtil.getInstance(R.array.logo_anim, 24)
				.createFramesAnim(imageView);
		animation.start();
		//imageView.setVisibility(View.GONE);
		//pointAnimView.setVisibility(View.VISIBLE);
		//pointAnimView.setRadius(20f);
		final int resid = R.mipmap.ic_eaglelive_loading_01;
		Subscription subscribe = Observable.create(new Observable.OnSubscribe<Drawable>()
		{

			@Override
			public void call(Subscriber<? super Drawable> subscriber)
			{
				Drawable drawable = getTheme().getDrawable(resid);
				subscriber.onNext(drawable);
				subscriber.onCompleted();
			}
		}).subscribe(new Observer<Drawable>()
		{
			@Override
			public void onCompleted()
			{

			}

			@Override
			public void onError(Throwable e)
			{

			}

			@Override
			public void onNext(Drawable drawable)
			{

			}
		});
		subscribe.unsubscribe();

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.tv_send:
				String sendText = etText.getText().toString();
				if (sendText != null && !sendText.equals(""))
				{
					DataProtocol data = new DataProtocol();
					data.setData(sendText);
					data.setDtype(1);
					data.setMsgId(22);
					if (connClinet == null) return;
					connClinet.addNewRequest(data);
				}
				break;
			case R.id.tv_close:
				if (connClinet != null)
				{
					connClinet.closeConnect();
				}
				else
				{
					Toast.makeText(this, "please connect server!", Toast.LENGTH_LONG);
				}
				break;
			case R.id.tv_connect:
				connClinet = new ConnectionClient(requestCallBack);
				break;
			case R.id.tv_get:
				break;
			case R.id.tv_refresh:
				Intent intent = new Intent(this, RefreshActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}

	private void initViews()
	{
		etText = (TextView) findViewById(R.id.et_text);
		etSend = (TextView) findViewById(R.id.tv_send);
		tvget = (TextView) findViewById(R.id.tv_get);
		tvclose = (TextView) findViewById(R.id.tv_close);
		tvconnect = (TextView) findViewById(R.id.tv_connect);
		tvrefresh = (TextView) findViewById(R.id.tv_refresh);
		imageView = (SimpleDraweeView) findViewById(R.id.iv_view);

		pointAnimView = (PointAnimView) findViewById(R.id.pv_animview);
		initReflect();

		etSend.setOnClickListener(this);
		tvclose.setOnClickListener(this);
		tvconnect.setOnClickListener(this);
		tvget.setOnClickListener(this);
		tvrefresh.setOnClickListener(this);
		gestureDetector = new GestureDetector(MainActivity.this, onGestureListener);
	}

	private void initThreadExcute()
	{
		threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
		requestCallBack = new RequestCallBack()
		{

			@Override
			public void onSuccess(Object msg)
			{
				if (msg instanceof DataAckProtocol)
				{
					ManageLog.D("DataAckProtocol-msg", ((DataAckProtocol) msg).getUnused());
				}
				else
				{
					ManageLog.D("msg", msg.toString());
				}

			}

			@Override
			public void onFailed(int errorCode, String msg)
			{

			}
		};

	}

	//java反射相关
	public void initReflect()
	{
		try
		{

			// 使用invoke调用方法，并且获取方法的返回值，需要传入一个方法所在类的对象，new Object[]
			// {"Kai"}是需要传入的参数，与上面的String.class相对应
			Class<?> easyBeanClass = Class.forName("com.star.eagleme.bean.EasyBean");
			Object book = easyBeanClass.newInstance();

			Method method = easyBeanClass.getDeclaredMethod("getText");
			Method setMethod = easyBeanClass.getDeclaredMethod("setText", String.class);
			setMethod.invoke(book, new Object[]{"Kai"});
			method.invoke(book);
			method.setAccessible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private GestureDetector gestureDetector;
	private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener()
	{
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			float minMove = 120;         //最小滑动距离
			float minVelocity = 0;      //最小滑动速度
			float beginX = e1.getX();
			float endX = e2.getX();
			float beginY = e1.getY();
			float endY = e2.getY();

			if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity)
			{   //左滑
			}
			else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity)
			{   //右滑
			}
			else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity)
			{
				Toast.makeText(getApplicationContext(), velocityX + "上滑", Toast.LENGTH_SHORT).show();//上滑
			}
			else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity)
			{   //下滑
				Toast.makeText(getApplicationContext(), velocityX + "下滑", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
	};

	public boolean onTouchEvent(MotionEvent event)
	{
		return gestureDetector.onTouchEvent(event);
	}
}
