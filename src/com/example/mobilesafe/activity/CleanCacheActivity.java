package com.example.mobilesafe.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 清理缓存
 * 
 * @author Administrator
 * 
 */
public class CleanCacheActivity extends Activity {
	private ListView lv_clear;
	private List<Cacheinfo> list;
	private Myadapter myadapter;
	private PackageManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			myadapter = new Myadapter();
			lv_clear.setAdapter(myadapter);

		};
	};
	private TextView tv_info;

	private void initData() {
		new Thread() {

			private long size;

			public void run() {

				// 获取所有app
				// List<AppInfos> appInfos = GetAppInfos
				// .getAppInfos(CleanCacheActivity.this);
				list = new ArrayList<CleanCacheActivity.Cacheinfo>();

				manager = getPackageManager();

				List<PackageInfo> installedPackages = manager
						.getInstalledPackages(0);

				for (PackageInfo packageInfo : installedPackages) {
					getCachesize(packageInfo);
				}
				System.out.println("!!!!!!!!!!!!!!!!!!! " + list.size());

				// 上面getCachesize 估计放在了子线程中执行,这里稍微等待
				handler.sendMessageDelayed(new Message(), 1000);
			}
		}.start();

	}

	private class MyIPackageStatsObserver extends IPackageStatsObserver.Stub {
		private PackageInfo packageInfo;
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			// 获取到应用缓存大小
			long cacheSize = pStats.cacheSize;
			if (cacheSize > 0) {
				System.out.println("当前应用的名字 "
						+ packageInfo.applicationInfo.loadLabel(manager)
						+ Formatter.formatFileSize(CleanCacheActivity.this,
								cacheSize));
				Cacheinfo cacheinfo = new Cacheinfo();
				cacheinfo.packageName = packageInfo.packageName;
				cacheinfo.name = packageInfo.applicationInfo.loadLabel(manager)
						.toString();
				cacheinfo.size = cacheSize;
				cacheinfo.icon = packageInfo.applicationInfo.loadIcon(manager);
				list.add(cacheinfo);
			}
		}
		
		

		public MyIPackageStatsObserver(PackageInfo packageInfo) {
			super();
			this.packageInfo = packageInfo;
		}
	}

	private void getCachesize(PackageInfo packageInfo) {
		try {
			Method method = PackageManager.class.getDeclaredMethod(
					"getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			/**
			 * 第一个参数:表示这个方法由谁来调用 第二个参数:表示包名
			 */
			method.invoke(manager, packageInfo.applicationInfo.packageName,
					new MyIPackageStatsObserver(packageInfo));

		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	
	

	static class Cacheinfo {
		String packageName;
		Drawable icon;
		String name;
		long size;
	}

	class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			tv_info.setText("缓存应用共: " + (list.size()) + "个");
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;
			if (convertView == null) {
				view = View.inflate(CleanCacheActivity.this,
						R.layout.item_clean_cache, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.iv_clear = (ImageView) view.findViewById(R.id.iv_clear);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.tv_size = (TextView) view.findViewById(R.id.tv_size);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) convertView.getTag();
			}

			holder.iv_icon.setImageDrawable(list.get(position).icon);

			holder.tv_name.setText(list.get(position).name);
			holder.tv_size.setText("缓存大小: "
					+ Formatter.formatFileSize(CleanCacheActivity.this,
							list.get(position).size));

			holder.iv_clear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent detail_intent = new Intent();
					detail_intent
							.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
					detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
					detail_intent.setData(Uri.parse("package:"
							+ list.get(position).packageName));

					startActivity(detail_intent);
				}
			});
			return view;
		}

	}

	@Override
	protected void onRestart() {
		list.clear();
		initData();
		super.onRestart();
	}


	static class ViewHolder {
		ImageView iv_icon;
		ImageView iv_clear;
		TextView tv_name;
		TextView tv_size;
	}

	private void initUI() {
		setContentView(R.layout.activity_clearcache);

		lv_clear = (ListView) findViewById(R.id.lv_clear);

		tv_info = (TextView) findViewById(R.id.tv_info);
	}

	private class MyIPackageDataObserver extends IPackageDataObserver.Stub {

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

	}

	public void startClearAll(View view) {
		if (list.size() == 0) {
			ToastUtils.showToast(this, "没有缓存数据可以清理!");
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("注意");
			builder.setMessage("是否清理所有应用缓存数据?");
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;

						}
					});

			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Method[] methods = PackageManager.class
									.getMethods();

							for (Method method : methods) {
								if (method.getName().equals(
										"freeStorageAndNotify")) {
									try {
										method.invoke(manager,
												Integer.MAX_VALUE,
												new MyIPackageDataObserver());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							ToastUtils.showToast(CleanCacheActivity.this,
									"全部清理完毕");
							list.clear();
							myadapter.notifyDataSetChanged();
							return;
						}
					});
			builder.show();
		}
	}
}
