package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TrafficManager extends Activity {
	private TextView tv_traffic_info;
	private ListView ll_traffic_list;
	private static List<TrafficInfo> arrayList;
	private List<ApplicationInfo> applications;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}

	static class TrafficInfo {
		Drawable icon;
		String name;
		int uid;
		long up;
		long down;
		String packageName;
	}

	private void initData() {
		final TrafficStats manager = new TrafficStats();
		final PackageManager packageManager = getPackageManager();

		long rxBytes = manager.getTotalRxBytes();
		long txBytes = manager.getTotalTxBytes();

		tv_traffic_info.setText("下载: "
				+ Formatter.formatFileSize(TrafficManager.this, rxBytes)
				+ " 接受: "
				+ Formatter.formatFileSize(TrafficManager.this, txBytes));

		arrayList = new ArrayList<TrafficInfo>();

		applications = packageManager.getInstalledApplications(0);

		new Thread() {

			public void run() {
				for (ApplicationInfo applicationInfo : applications) {
					int uid = applicationInfo.uid;
					if (manager.getUidRxBytes(uid) != 0
							|| manager.getUidTxBytes(uid) != 0) {
						TrafficInfo trafficInfo = new TrafficInfo();
						trafficInfo.icon = applicationInfo
								.loadIcon(packageManager);
						trafficInfo.name = applicationInfo.loadLabel(
								packageManager).toString();
						trafficInfo.packageName = applicationInfo.packageName;
						trafficInfo.up = manager.getUidTxBytes(uid);
						trafficInfo.down = manager.getUidRxBytes(uid);
						trafficInfo.uid = uid;
						arrayList.add(trafficInfo);
					}
				}
				handler.sendEmptyMessage(0);

			};

		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			TrafficAdapter adapter = new TrafficAdapter();

			ll_traffic_list.setAdapter(adapter);

		};
	};

	public class TrafficAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				view = View.inflate(TrafficManager.this,
						R.layout.item_traffice_list, null);
				holder.imageView = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.tv_traffic = (TextView) view
						.findViewById(R.id.tv_traffic);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) convertView.getTag();
			}

			holder.imageView.setImageDrawable(arrayList.get(position).icon);
			holder.tv_name.setText(arrayList.get(position).name);

			long up = arrayList.get(position).up;
			long down = arrayList.get(position).down;

			holder.tv_traffic.setText("上传: "
					+ Formatter.formatFileSize(TrafficManager.this, up)
					+ " 下载: "
					+ Formatter.formatFileSize(TrafficManager.this, down));
			;

			return view;
		}

	}

	static class ViewHolder {
		ImageView imageView;
		TextView tv_name;
		TextView tv_traffic;

	}

	private void initUI() {
		setContentView(R.layout.activity_traffic);

		tv_traffic_info = (TextView) findViewById(R.id.tv_traffic_info);
		ll_traffic_list = (ListView) findViewById(R.id.ll_traffic_list);

	}
}
