package com.weiyi.itreader.ui;

import com.weiyi.itreader.common.Constant;
import com.weiyi.itreader.ui.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 功能：欢迎界面，处理一些程序载入时动画信息
 * 
 * @author 魏艺荣
 * @version 1.0
 * */
public class WelcomeActivity extends Activity {
	/** Called when the activity is first created. */
	int screenHeight,screenWidth;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.in);
		ImageView view = (ImageView) findViewById(R.id.welcome_bg);
		view.setAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(WelcomeActivity.this,
						ITActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}