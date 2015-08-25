package com.example.mobilesafe.activity;

import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AntivirusDao;
import com.example.mobilesafe.utils.Md5Uils;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class AntivirusActivity extends Activity {
	protected static final int BEGING = 0;

	protected static final int SCANING = 1;

	protected static final int FINISH = 2;

	private ImageView iv_scan;
	private LinearLayout ll_scaninfo;
	private ProgressBar pb_progress;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BEGING:
				tv_info.setText("初始化双核引擎");
				break;
			case SCANING:
				TextView child = new TextView(AntivirusActivity.this);
				
				ScanAppInfo appInfo = (ScanAppInfo) msg.obj;
				
				if(appInfo.desc){
					child.setTextColor(Color.RED);

					child.setText(appInfo.appName + "有病毒");
				}else {
					child.setTextColor(Color.BLACK);					
					child.setText(appInfo.appName + "扫描安全");
				}
				
				ll_scaninfo.addView(child);
				
				sv_list.post(new Runnable() {
					@Override
					public void run() {
						sv_list.fullScroll(sv_list.FOCUS_DOWN);
					}
				});
				
				break;
			case FINISH:
				iv_scan.clearAnimation();
				break;
			}
		};
	};

	private ScrollView sv_list;

	private TextView tv_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		// initData();
	}

	private void initUI() {
		setContentView(R.layout.activity_antivirus);
		
		tv_info = (TextView) findViewById(R.id.tv_info);

		iv_scan = (ImageView) findViewById(R.id.iv_scan);

		ll_scaninfo = (LinearLayout) findViewById(R.id.ll_scaninfo);

		pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
		
		sv_list = (ScrollView) findViewById(R.id.sv_list);
	}

	private void initData() {
		new Thread() {
			private Message message;

			public void run() {
				message = Message.obtain();

				message.what = BEGING;

				// 获取全部应用包名
				PackageManager manager = getPackageManager();

				List<PackageInfo> packages = manager.getInstalledPackages(0);

				int size = packages.size();

				pb_progress.setMax(size);

				int progress = 0;

				for (PackageInfo packageInfo : packages) {
					ScanAppInfo appInfo = new ScanAppInfo();
					// 获取到app的名字,其他信息
					appInfo.appName = packageInfo.applicationInfo.loadLabel(
							manager).toString();
					appInfo.packageName = packageInfo.packageName;

					String filePath = packageInfo.applicationInfo.sourceDir;

					String md5 = Md5Uils.GetFileMd5(filePath);

					String desc = AntivirusDao.isAntivirusDao(md5);

					if (desc == null) {
						appInfo.desc = false;
					} else {
						appInfo.desc = true;
					}

					progress++;

					SystemClock.sleep(100);

					pb_progress.setProgress(progress);

					message = Message.obtain();

					message.what = SCANING;

					message.obj = appInfo;

					handler.sendMessage(message);

				}

				message = Message.obtain();

				message.what = FINISH;

				handler.sendMessage(message);
			};

		}.start();

	}

	static class ScanAppInfo {
		boolean desc;
		String appName;
		String packageName;
	}

	public void start(View view) {
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 设置动画的时间
		rotateAnimation.setDuration(5000);
		// 设置动画无限循环
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		// 开始动画
		iv_scan.startAnimation(rotateAnimation);
		
		initData();
	}
}
