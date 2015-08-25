package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EnterPwdActivity extends Activity implements OnClickListener {
	private Button bt_0;
	private Button bt_1;
	private Button bt_2;
	private Button bt_3;
	private Button bt_4;
	private Button bt_5;
	private Button bt_6;
	private Button bt_7;
	private Button bt_8;
	private Button bt_9;
	private EditText et_pwd;
	private String str;
	private Button bt_clear;
	private Button bt_back;
	private Button btnButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();

	}

	private void initUI() {
		setContentView(R.layout.activity_setlock_pwd);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		// et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);

		bt_0 = (Button) findViewById(R.id.bt_0);
		bt_1 = (Button) findViewById(R.id.bt_1);
		bt_2 = (Button) findViewById(R.id.bt_2);
		bt_3 = (Button) findViewById(R.id.bt_3);
		bt_4 = (Button) findViewById(R.id.bt_4);
		bt_5 = (Button) findViewById(R.id.bt_5);
		bt_6 = (Button) findViewById(R.id.bt_6);
		bt_7 = (Button) findViewById(R.id.bt_7);
		bt_8 = (Button) findViewById(R.id.bt_8);
		bt_9 = (Button) findViewById(R.id.bt_9);

		bt_clear = (Button) findViewById(R.id.bt_clear);
		bt_back = (Button) findViewById(R.id.bt_back);

		btnButton = (Button) findViewById(R.id.btn_ok);

		bt_0.setOnClickListener(this);
		bt_1.setOnClickListener(this);
		bt_2.setOnClickListener(this);
		bt_3.setOnClickListener(this);
		bt_4.setOnClickListener(this);
		bt_5.setOnClickListener(this);
		bt_6.setOnClickListener(this);
		bt_7.setOnClickListener(this);
		bt_8.setOnClickListener(this);
		bt_9.setOnClickListener(this);
		bt_clear.setOnClickListener(this);
		bt_back.setOnClickListener(this);
		btnButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_0:
			str = et_pwd.getText().toString();
			et_pwd.setText(str + "0");
			break;
		case R.id.bt_1:
			str = et_pwd.getText().toString();
			et_pwd.setText(str + "1");
			break;
		case R.id.bt_2:
			str = et_pwd.getText().toString();
			et_pwd.setText(str + "2");
			break;
		case R.id.bt_3:
			str = et_pwd.getText().toString();
			et_pwd.setText(str + "3");
			break;
		case R.id.bt_4:
			str = et_pwd.getText().toString();
			et_pwd.setText(str + "4");
			break;
		case R.id.bt_5:
			str = et_pwd.getText().toString();
			et_pwd.setText(str + "5");
			break;
		case R.id.bt_6:
			str = et_pwd.getText().toString();
			et_pwd.setText(str + "6");
			break;
		case R.id.bt_7:
			str = et_pwd.getText().toString();
			et_pwd.setText(str + "7");
			break;
		case R.id.bt_8:
			str = et_pwd.getText().toString();
			et_pwd.setText(str + "8");
			break;
		case R.id.bt_9:
			str = et_pwd.getText().toString();
			et_pwd.setText(str + "9");
			break;
		case R.id.bt_clear:
			et_pwd.setText("");
			break;
		case R.id.bt_back:
			str = et_pwd.getText().toString();
			if (TextUtils.isEmpty(str)) {

				break;
			}

			et_pwd.setText(str.substring(0, str.length() - 1));
			break;
		case R.id.btn_ok:
			str = et_pwd.getText().toString().trim();
			if (str.equals("123")) {
				finish();
			} else {
				ToastUtils.showToast(this, "密码错误,请重新输入!");
				break;
			}
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// 当用户输入后退健 的时候。我们进入到桌面
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
	}
}
