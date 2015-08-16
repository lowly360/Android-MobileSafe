package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import android.content.Intent;
import android.os.Bundle;

public class Setup1Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
		
	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this,Setup2Activity.class));
		finish();
		//进入动画，退出动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		
	}
}
