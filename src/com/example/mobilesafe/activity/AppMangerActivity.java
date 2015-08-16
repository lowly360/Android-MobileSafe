package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInfos;
import com.example.mobilesafe.logic.GetAppInfos;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

public class AppMangerActivity extends Activity {
	@ViewInject(R.id.tv_interiorspace)
	private TextView tv_interiorspace; // 内部存储空间

	@ViewInject(R.id.tv_sdfreespace)
	private TextView tv_sdfreespace; // sd卡可用空间

	@ViewInject(R.id.lv_applist)
	private ListView lv_applist; //
	
	@ViewInject(R.id.pb_progress)
	private ProgressBar pb_progress;

	private List<AppInfos> app_info;
	private List<AppInfos> userApp;
	private List<AppInfos> systemApp;

	private AppMangerAdapter adapter;

	private AppInfos clickAppInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanger);
		initUI();
		initData();
	}

	private class AppMangerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return app_info.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			if (0 < position && position < userApp.size() + 1) {
				return userApp.get(position - 1);

			} else if (position > userApp.size() + 2) {
				return systemApp.get(position - userApp.size() - 2);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 0) {
				TextView tv = new TextView(AppMangerActivity.this);
				tv.setTextColor(Color.WHITE);
				tv.setTextSize(18);
				tv.setText("用户应用 (" + userApp.size() + ")");
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			} else if (position == userApp.size() + 1) {
				TextView tv = new TextView(AppMangerActivity.this);
				tv.setTextColor(Color.WHITE);
				tv.setTextSize(18);
				tv.setText("系统应用 (" + systemApp.size() + ")");
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}

			ViewHolder holder;
			View view;
			if (convertView != null && convertView instanceof LinearLayout) {
				view = convertView;
				holder = (ViewHolder) convertView.getTag();
			} else {
				holder = new ViewHolder();
				view = View.inflate(AppMangerActivity.this,
						R.layout.item_appmanger_list, null);
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_apkname = (TextView) view
						.findViewById(R.id.tv_apkname);
				holder.tv_isrom = (TextView) view.findViewById(R.id.tv_isrom);
				holder.tv_appsize = (TextView) view
						.findViewById(R.id.tv_appsize);
				holder.btn_delete = (TextView) view
						.findViewById(R.id.btn_delete);
				view.setTag(holder);
			}

			AppInfos appInfos = null;
			if (position < userApp.size() + 1) {
				appInfos = userApp.get(position - 1);
			} else {
				appInfos = systemApp.get(position - userApp.size() - 2);
			}

			holder.iv_icon.setBackground(appInfos.getApp_icon());
			holder.tv_apkname.setText(appInfos.getApp_name());
			if (appInfos.getIsrom()) {
				holder.tv_isrom.setText("手机内存");
			} else {
				holder.tv_isrom.setText("sd卡");
			}
			final AppInfos onClickApp = appInfos;

			holder.tv_appsize.setText(Formatter.formatFileSize(
					AppMangerActivity.this, appInfos.getApp_size()));
			holder.btn_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent uninstall_localIntent = new Intent(
							"android.intent.action.DELETE", Uri
									.parse("package:"
											+ onClickApp.getApkPackName()));
					startActivityForResult(uninstall_localIntent, 1);
					
				}

			});

			return view;
		}
	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_apkname;
		TextView tv_isrom;
		TextView tv_appsize;
		TextView btn_delete;
	}

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			adapter = new AppMangerAdapter();
			pb_progress.setVisibility(ProgressBar.INVISIBLE);
			lv_applist.setAdapter(adapter);
		};
	};

	private void initData() {
		systemApp = new ArrayList<AppInfos>();
		userApp = new ArrayList<AppInfos>();

		new Thread() {
			@Override
			public void run() {
				app_info = GetAppInfos.getAppInfos(AppMangerActivity.this);

				System.out.println("----->" + app_info.size());

				for (int i = 0; i < app_info.size(); i++) {
					if (app_info.get(i).isUserApp()) {
						userApp.add(app_info.get(i));
					} else {
						systemApp.add(app_info.get(i));
					}
				}

				handler.sendEmptyMessage(0);
				super.run();
			}
		}.start();

	}

	private void initUI() {
		ViewUtils.inject(this);

		long sd_space = Environment.getExternalStorageDirectory()
				.getFreeSpace();
		long rom_space = Environment.getDataDirectory().getFreeSpace();

		tv_interiorspace.setText("内存可用:"
				+ Formatter.formatFileSize(this, rom_space));
		tv_sdfreespace.setText("sd卡可用:"
				+ Formatter.formatFileSize(this, sd_space));

		lv_applist.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0 || position == userApp.size() + 1) {

				} else {

					clickAppInfo = (AppInfos) lv_applist
							.getItemAtPosition(position);
					showPopup(view);
				}
				return false;
			}
		});
	}

	@SuppressLint("NewApi")
	protected void showPopup(View view) {
		PopupMenu popupMenu = new PopupMenu(this, view);
		MenuInflater inflater = popupMenu.getMenuInflater();
		inflater.inflate(R.menu.main, popupMenu.getMenu());
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.start:
					Intent start_localIntent = AppMangerActivity.this
							.getPackageManager().getLaunchIntentForPackage(
									clickAppInfo.getApkPackName());
					AppMangerActivity.this.startActivity(start_localIntent);
					break;

				case R.id.detail:
					Intent detail_intent = new Intent();
					detail_intent
							.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
					detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
					detail_intent.setData(Uri.parse("package:"
							+ clickAppInfo.getApkPackName()));
					startActivity(detail_intent);

					break;

				default:
					break;
				}
				return false;
			}
		});

		popupMenu.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/**
		 * 响应卸载事件,删除后,更新listview条目
		 */
		if(requestCode == 1){
			initData();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
