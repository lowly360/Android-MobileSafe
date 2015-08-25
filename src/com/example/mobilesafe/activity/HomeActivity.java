package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.Md5Uils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private GridView gvHome;
	private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					// 手机防盗
					showPasswordDialog();
					break;
				case 1:
					// 通讯卫士
					startActivity(new Intent(HomeActivity.this,
							CallSafeActivity.class));
					break;

				case 2:
					// 应用管理
					startActivity(new Intent(HomeActivity.this,
							AppMangerActivity.class));
					break;

				case 3:
					// 进程管理
					startActivity(new Intent(HomeActivity.this,
							ProcessMangerActivity.class));
					break;
					
				case 4:
					// 流量管理
					startActivity(new Intent(HomeActivity.this,
							TrafficManager.class));
					break;

				case 5:
					// 手机杀毒
					startActivity(new Intent(HomeActivity.this,
							AntivirusActivity.class));
					break;

				case 6:
					// 缓存清理
					startActivity(new Intent(HomeActivity.this,
							CleanCacheActivity.class));
					break;

				case 7:
					// 高级工具
					startActivity(new Intent(HomeActivity.this,
							AToolsActivity.class));
					break;

				case 8:
					// 设置中心
					startActivity(new Intent(HomeActivity.this,
							SettingActivity.class));
					break;

				default:
					break;
				}

			}
		});
	}

	/**
	 * 显示密码对话框
	 */
	protected void showPasswordDialog() {
		// 判断有没有设置过密码
		String savedpassword = mPref.getString("password", null);

		if (!TextUtils.isEmpty(savedpassword)) {
			showPasswordInputDailog();
		} else {
			showPasswordSetDailog();
		}
	}

	/**
	 * 输入密码框
	 */
	private void showPasswordInputDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dailog_set_inputpassword, null);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);

		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();

				if (!TextUtils.isEmpty(password)) {

					String savedPassword = mPref.getString("password", null);
					if (Md5Uils.encode(password).equals(savedPassword)) {

						dialog.dismiss();
						// 跳转防盗页面
						startActivity(new Intent(HomeActivity.this,
								LostFindActivity.class));
					} else {
						Toast.makeText(HomeActivity.this, "密码错误",
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(HomeActivity.this, "请输入密码",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		// dialog.setView(view);
		dialog.setView(view, 0, 0, 0, 0);

		dialog.show();

	}

	/**
	 * 设置密码弹窗
	 */
	private void showPasswordSetDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dailog_set_password, null);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);
		final EditText etPasswordConfirm = (EditText) view
				.findViewById(R.id.et_password_confirm);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				String passwordConfirm = etPasswordConfirm.getText().toString();

				if (!TextUtils.isEmpty(password) && !passwordConfirm.isEmpty()) {
					if (password.equals(passwordConfirm)) {

						// 跳转防盗页面
						startActivity(new Intent(HomeActivity.this,
								LostFindActivity.class));

						mPref.edit()
								.putString("password", Md5Uils.encode(password))
								.commit();
						dialog.dismiss();
					} else {
						Toast.makeText(HomeActivity.this, "两次密码不一致",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框不能为空",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		// dialog.setView(view);
		dialog.setView(view, 0, 0, 0, 0);

		dialog.show();

	}

	class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItems.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mItems[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this,
					R.layout.home_list_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

			tvItem.setText(mItems[position]);
			ivItem.setImageResource(mPics[position]);

			return view;
		}

	}

}
