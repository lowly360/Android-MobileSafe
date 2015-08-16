package com.example.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AddressDao;
import com.example.mobilesafe.utils.ToastUtils;

public class AdressQueryActivity extends Activity {
	private TextView tvReuslt;
	private EditText etNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_query);
		tvReuslt = (TextView) findViewById(R.id.tv_result);
		etNumber = (EditText) findViewById(R.id.et_number);

		etNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String number = etNumber.getText().toString().trim();
				String reuslt = "";
				if (!TextUtils.isEmpty(number)) {
					reuslt = AddressDao.getAddress(number);
					tvReuslt.setText("查询结果为:" + reuslt);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	public void QueryAdress(View v) {
		String number = etNumber.getText().toString().trim();
		String reuslt = "";
		if (!TextUtils.isEmpty(number)) {
			reuslt = AddressDao.getAddress(number);
			tvReuslt.setText("查询结果为:" + reuslt);
		} else {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			etNumber.startAnimation(shake);
			vibrate();
		}
	}
	/**
	 *手机震动 需要权限android.permission.VIBRATE 
	 */
	public void vibrate() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(1000);
	}
}
