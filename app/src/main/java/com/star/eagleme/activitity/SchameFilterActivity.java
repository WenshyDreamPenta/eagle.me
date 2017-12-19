package com.star.eagleme.activitity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;

public class SchameFilterActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Uri uri = getIntent().getData();
		if(uri == null){
			return;
		}
		ARouter.getInstance().build(uri).navigation();
		finish();
	}
}
