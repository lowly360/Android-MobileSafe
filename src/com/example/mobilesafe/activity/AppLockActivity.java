package com.example.mobilesafe.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.fragment.LockFragment;
import com.example.mobilesafe.fragment.UnLockFragment;

public class AppLockActivity extends FragmentActivity implements
		OnClickListener {

	private FrameLayout fl_content;
	private TextView tv_unlock;
	private TextView tv_lock;
	private FragmentManager fragmentManager;
	private LockFragment lockFragment;
	private UnLockFragment unLockFragment;
	private TextView tv_unlockApp;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initUI();
	}

	private void initUI() {
		setContentView(R.layout.activity_applock);
		fl_content = (FrameLayout) findViewById(R.id.fl_content);
		tv_unlock = (TextView) findViewById(R.id.tv_unlock);
		tv_lock = (TextView) findViewById(R.id.tv_lock);
		

		tv_unlock.setOnClickListener(this);

		tv_lock.setOnClickListener(this);
		
		fragmentManager = getSupportFragmentManager();
	
		FragmentTransaction transaction =fragmentManager.beginTransaction();
		
		unLockFragment = new UnLockFragment();
		lockFragment = new LockFragment();
		
		transaction.replace(R.id.fl_content, unLockFragment).commit();
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction fTransaction = fragmentManager.beginTransaction();
		switch (v.getId()) {
		
		case R.id.tv_unlock:
			
			tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
			tv_lock.setBackgroundResource(R.drawable.tab_right_default);
			fTransaction.replace(R.id.fl_content, unLockFragment);
			
			
			break;

		case R.id.tv_lock:
			
			tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
			tv_lock.setBackgroundResource(R.drawable.tab_right_pressed);
			
			fTransaction.replace(R.id.fl_content, lockFragment);
			
			break;

		default:
			break;
		}
		fTransaction.commit();
	}
}
