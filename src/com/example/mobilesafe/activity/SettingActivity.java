package com.example.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AddressDao;
import com.example.mobilesafe.receiver.AdminReceiver;
import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.service.CallSafeService;
import com.example.mobilesafe.service.WatchDogService;
import com.example.mobilesafe.utils.ServiceStatusUtils;
import com.example.mobilesafe.utils.ToastUtils;
import com.example.mobilesafe.view.SettingClickView;
import com.example.mobilesafe.view.SettingItemView;

/**
 * 设置中心
 * 
 * @author Administrator
 * 
 */
public class SettingActivity extends Activity {
	private SettingItemView sivUpdate;
	private SettingItemView sivActiveAdmin;
	private SettingItemView sivCallSafeActive;
	private SettingClickView scvAddressStyle;
	private SettingClickView scvLocation;
	private SettingItemView siv_watch_dog;
	
	private SettingItemView sivAddress;
	private SharedPreferences mPref;

	private DevicePolicyManager mDPM;
	private ComponentName mComponentName;
	private boolean admin;
	private String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		initUpdateView();
		initAdminView();
		initAddressView();
		initAddressStyle();
		initAddressLocation();
		initCallSafeView();
		initActiveWatchDog();
		
		
	}
	/**
	 * 初始化看门狗
	 */
	private void initActiveWatchDog() {
		siv_watch_dog = (SettingItemView) findViewById(R.id.siv_watch_dog);

		

//		if (admin) {
//			sivActiveAdmin.setChecked(true);
//		} else {
//			sivActiveAdmin.setChecked(false);
//		}
		
		boolean serviceRuning = ServiceStatusUtils.isServiceRuning(this,
				"com.example.mobilesafe.service.WatchDogService"); // 判断服务是否在运行中

		if (serviceRuning) {
			siv_watch_dog.setChecked(true);
		} else {
			siv_watch_dog.setChecked(false);
		}

		siv_watch_dog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_watch_dog.isChecked()) {
					siv_watch_dog.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							WatchDogService.class));
				} else {
					siv_watch_dog.setChecked(true);
					startService(new Intent(SettingActivity.this,
							WatchDogService.class));
				}
			}
		});
		
	}


	public void Active(View v) {
		if (admin == false) {
			//ToastUtils.showToast(SettingActivity.this, "--------------");
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			ComponentName mComponentName = new ComponentName(
					SettingActivity.this, AdminReceiver.class);

			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
					mComponentName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "    ");
			// startActivity(intent);
			startActivityForResult(intent, 2);

		} else if (admin) {
			ToastUtils.showToast(this, "取消设备管理器权限");
			mDPM.removeActiveAdmin(mComponentName);// 取消激活
			sivActiveAdmin.setChecked(false);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2) {
			if (mDPM.isAdminActive(mComponentName)) {
				// ToastUtils.showToast(this, "激活成功");
				mPref.edit().putBoolean("admin", true).commit();
				sivActiveAdmin.setChecked(true);
			} else {
				ToastUtils.showToast(this, "激活激活失败");
				sivActiveAdmin.setChecked(false);
				mPref.edit().putBoolean("admin", false).commit();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initAdminView() {

		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mComponentName = new ComponentName(SettingActivity.this,
				AdminReceiver.class);

		sivActiveAdmin = (SettingItemView) findViewById(R.id.siv_activAdmin);

		admin = mPref.getBoolean("admin", false);

		// admin = mDPM.isAdminActive(mComponentName);

		if (admin) {
			sivActiveAdmin.setChecked(true);
		} else {
			sivActiveAdmin.setChecked(false);
		}

	}

	private void initUpdateView() {

		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		sivUpdate.setTitle("自动更新设置");

		boolean autoUpate = mPref.getBoolean("auto_update", true);

		if (autoUpate) {
			sivUpdate.setChecked(true);
		} else {
			sivUpdate.setChecked(false);
		}
		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivUpdate.isChecked()) {
					sivUpdate.setChecked(false);
					mPref.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);

					mPref.edit().putBoolean("auto_update", true).commit();
				}

			}
		});
	}

	private void initAddressStyle() {
		scvAddressStyle = (SettingClickView) findViewById(R.id.scv_style);
		
		int style = mPref.getInt("address_style", 0);

		scvAddressStyle.setTitle("归属地提示框风格");
		
		scvAddressStyle.setDesc(items[style]);
		
		scvAddressStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showsingleChooseDailog();

			}
		});
	}

	
	/**
	 * 初始化修改提示框位置
	 */
	private void initAddressLocation(){
		scvLocation = (SettingClickView) findViewById(R.id.scv_location);
		scvLocation.setTitle("归属地提示框显示位置");
		scvLocation.setDesc("设置归属提示框地显示位置");
		
		scvLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this,DragViewActivity.class));
				
			}
		});
	}
	
	
	/**
	 * 弹出选择风格的提示框
	 */
	protected void showsingleChooseDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("归属地提示框风格");

		

		builder.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mPref.edit().putInt("address_style", which).commit();
						dialog.dismiss();
						scvAddressStyle.setDesc(items[which]);
					}
				});
		builder.setNegativeButton("取消", null);
		builder.show();

	}
	
	
	
	
	

	private void initAddressView() {
		sivAddress = (SettingItemView) findViewById(R.id.siv_address);

		boolean serviceRuning = ServiceStatusUtils.isServiceRuning(this,
				"com.example.mobilesafe.service.AddressService"); // 判断服务是否在运行中

		if (serviceRuning) {
			sivAddress.setChecked(true);
		} else {
			sivAddress.setChecked(false);
		}

		sivAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivAddress.isChecked()) {
					sivAddress.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							AddressService.class));
				} else {
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this,
							AddressService.class));
				}
			}
		});
	}
		
	
	private void initCallSafeView() {
		sivCallSafeActive = (SettingItemView) findViewById(R.id.siv_callsafe);

		boolean serviceRuning = ServiceStatusUtils.isServiceRuning(this,
				"com.example.mobilesafe.service.CallSafeService"); // 判断服务是否在运行中

		if (serviceRuning) {
			sivCallSafeActive.setChecked(true);
		} else {
			sivCallSafeActive.setChecked(false);
		}

		sivCallSafeActive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivCallSafeActive.isChecked()) {
					sivCallSafeActive.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							CallSafeService.class));
				} else {
					sivCallSafeActive.setChecked(true);
					startService(new Intent(SettingActivity.this,
							CallSafeService.class));
				}
			}
		});
	}
}
