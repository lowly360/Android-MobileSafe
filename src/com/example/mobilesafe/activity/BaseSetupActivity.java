package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

/**
 * 不需要在清单文件中声明，因为不需要展示界面
 * 
 * @author Administrator
 * 
 */
public abstract class BaseSetupActivity extends Activity {
	private GestureDetector mDetector;
	public SharedPreferences mPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);

		mDetector = new GestureDetector(this, new SimpleOnGestureListener() {
			// 监听手势滑动事件
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// 判定纵向滑动幅度是否过大，过大不允许切换切面
				if (Math.abs(e2.getRawY() - e1.getRawY()) > 150) {
					Toast.makeText(BaseSetupActivity.this, "不能这样划哦",
							Toast.LENGTH_SHORT).show();
					return true;
				}

				if (Math.abs(velocityX) < 100) {
					Toast.makeText(BaseSetupActivity.this, "滑动的太慢了",
							Toast.LENGTH_SHORT).show();
					return true;
				}

				// 向右滑，上一页
				if (e2.getRawX() - e1.getRawX() > 200) {
					showPreviousPage();
					return true;
				}
				// 向左滑，下一页
				if (e1.getRawX() - e2.getRawX() > 200) {
					showNextPage();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}

	public abstract void showPreviousPage();

	public abstract void showNextPage();

	public void next(View v) {
		showNextPage();
	}

	public void previous(View v) {
		showPreviousPage();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);

	}
}
