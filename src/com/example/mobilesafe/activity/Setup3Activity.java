package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class Setup3Activity extends BaseSetupActivity {
	private EditText etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		String phone = mPref.getString("phone", " ");
		etPhone = (EditText) findViewById(R.id.et_phone);
		etPhone.setText(phone);
	}

	@Override
	public void showPreviousPage() {
		
		
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}

	public void selectContact(View v) {
		startActivityForResult(new Intent(this, ContactActivity.class), 0);

	}

	@Override
	public void showNextPage() {
		String phone = etPhone.getText().toString().trim(); //过滤空格
		if(TextUtils.isEmpty(phone)){
			ToastUtils.showToast(this,"安全密码不能为空");		
			return;
		}
		
		mPref.edit().putString("phone", phone).commit();
		
		startActivity(new Intent(this, Setup4Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			String phone = data.getStringExtra("phone");
			phone = phone.replaceAll("-", " ").replaceAll(" ", "");// 替换-和空格
			//phone = phone.toString().trim();
			etPhone.setText(phone);
			ToastUtils.showToast(this, phone);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
