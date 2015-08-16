package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.ProcessInfo;
import com.example.mobilesafe.logic.GetProcessInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProcessMangerActivity extends Activity {
	@ViewInject(R.id.tv_processsum)
	private TextView tv_processsum;
	@ViewInject(R.id.tv_memoryinfo)
	private TextView tv_memoryinfo;
	@ViewInject(R.id.lv_process)
	private ListView lv_process;

	private long toalmem;
	private List<ProcessInfo> processInfos;
	private List<ProcessInfo> userprocessInfos;
	private List<ProcessInfo> sysprocessInfos;

	private Myadapter myadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		toalmem = GetProcessInfo.GetTotalMemory(this);
		initUI();
		initData();

	}

	private void initUI() {
		setContentView(R.layout.activity_processmanger);
		ViewUtils.inject(this);
		long avilmem = GetProcessInfo.GetFreeMemory(this);
		tv_processsum.setText("进程数: " + GetProcessInfo.GetProessNumber(this)
				+ " 个");
		tv_memoryinfo.setText("内存信息: "
				+ Formatter.formatFileSize(this, avilmem) + "/"
				+ Formatter.formatFileSize(this, toalmem));

		// lv_process.setAdapter(myadapter);
		lv_process.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				CheckBox cb_process_cheak = (CheckBox) view
						.findViewById(R.id.cb_process_cheak);
				if (position < userprocessInfos.size() + 1) {
					// Toast.makeText(ProcessMangerActivity.this,
					// "---->2"+userprocessInfos.get(position-1).isIscheck(),0).show();
					if (userprocessInfos.get(position - 1).isIscheck()) {
						userprocessInfos.get(position - 1).setIscheck(false);
						cb_process_cheak.setChecked(false);
					} else {
						userprocessInfos.get(position - 1).setIscheck(true);
						cb_process_cheak.setChecked(true);
					}
				} else if (position >= userprocessInfos.size() + 2) {
					// Toast.makeText(ProcessMangerActivity.this,
					// "---->2"+sysprocessInfos.get(position-userprocessInfos.size()-2).isIscheck(),0).show();
					if (sysprocessInfos.get(
							position - userprocessInfos.size() - 2).isIscheck()) {
						sysprocessInfos.get(
								position - userprocessInfos.size() - 2)
								.setIscheck(false);
						cb_process_cheak.setChecked(false);
					} else {
						sysprocessInfos.get(
								position - userprocessInfos.size() - 2)
								.setIscheck(true);
						cb_process_cheak.setChecked(true);
					}
				}
			}
			//
		});

	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			myadapter = new Myadapter();
			lv_process.setAdapter(myadapter);
		};
	};

	private void initData() {
		processInfos = new ArrayList<ProcessInfo>();
		userprocessInfos = new ArrayList<ProcessInfo>();
		sysprocessInfos = new ArrayList<ProcessInfo>();
		new Thread() {
			public void run() {
				processInfos = GetProcessInfo
						.GetProcessList(ProcessMangerActivity.this);

				for (ProcessInfo info : processInfos) {
					if (info.isUers()) {
						userprocessInfos.add(info);
					} else {
						sysprocessInfos.add(info);
					}
				}

				handler.sendEmptyMessage(0);
			};
		}.start();

	}

	class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userprocessInfos.size() + sysprocessInfos.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			if (position < userprocessInfos.size() + 1) {
				// Toast.makeText(ProcessMangerActivity.this,
				// "---->2"+userprocessInfos.get(position-1).isIscheck(),0).show();

				return userprocessInfos.get(position - 1);
			} else if (position >= userprocessInfos.size() + 2) {
				// Toast.makeText(ProcessMangerActivity.this,
				// "---->2"+sysprocessInfos.get(position-userprocessInfos.size()-2),0).show();
				return sysprocessInfos.get(position - userprocessInfos.size()
						- 2);
			}

			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view;
			ViewHolder holder;
			if (position == 0) {
				TextView tv = new TextView(ProcessMangerActivity.this);
				tv.setText("用户进程:" + userprocessInfos.size() + "个");
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}
			if (position == userprocessInfos.size() + 1) {
				TextView tv = new TextView(ProcessMangerActivity.this);
				tv.setText("系统进程:" + sysprocessInfos.size() + "个");
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}

			if (convertView != null && convertView instanceof LinearLayout) {
				view = convertView;
				holder = (ViewHolder) convertView.getTag();

			} else {

				view = View.inflate(ProcessMangerActivity.this,
						R.layout.process_list_item, null);
				holder = new ViewHolder();

				holder.iv_process_icon = (ImageView) view
						.findViewById(R.id.iv_process_icon);
				holder.tv_process_name = (TextView) view
						.findViewById(R.id.tv_process_name);
				holder.tv_process_size = (TextView) view
						.findViewById(R.id.tv_process_size);
				holder.cb_process_cheak = (CheckBox) view
						.findViewById(R.id.cb_process_cheak);
				view.setTag(holder);

			}

			if (position <= userprocessInfos.size() + 1) {
				holder.iv_process_icon.setBackground(userprocessInfos.get(
						position - 1).getIcon());

				holder.tv_process_name.setText(userprocessInfos.get(
						position - 1).getName());

				holder.tv_process_size.setText("内存占用: "
						+ Formatter.formatFileSize(ProcessMangerActivity.this,
								userprocessInfos.get(position - 1).getSize())
						+ "");

				if (userprocessInfos.get(position - 1).getPackageName()
						.equals("com.example.mobilesafe")) {
					holder.cb_process_cheak.setVisibility(View.GONE);
				}
				if (userprocessInfos.get(position - 1).isIscheck()) {
					holder.cb_process_cheak.setChecked(true);
				} else {
					holder.cb_process_cheak.setChecked(false);
				}
			} else if (position >= userprocessInfos.size() + 2) {
				holder.iv_process_icon.setBackground(sysprocessInfos.get(
						position - userprocessInfos.size() - 2).getIcon());

				holder.tv_process_name.setText(sysprocessInfos.get(
						position - userprocessInfos.size() - 2).getName());

				holder.tv_process_size.setText("内存占用: "
						+ Formatter.formatFileSize(
								ProcessMangerActivity.this,
								sysprocessInfos.get(
										position - userprocessInfos.size() - 2)
										.getSize()) + "");

				if (sysprocessInfos.get(position - userprocessInfos.size() - 2)
						.isIscheck()) {
					holder.cb_process_cheak.setChecked(true);
				} else {
					holder.cb_process_cheak.setChecked(false);
				}
			}

			return view;
		}

	}

	class ViewHolder {
		ImageView iv_process_icon;
		TextView tv_process_name;
		TextView tv_process_size;
		CheckBox cb_process_cheak;

	}

	public void selectAll(View v) {
		for (ProcessInfo infouser : userprocessInfos) {
			infouser.setIscheck(true);
			if (infouser.getPackageName().equals("com.example.mobilesafe")) {
				infouser.setIscheck(false);
			}
		}

		for (ProcessInfo infouser : sysprocessInfos) {
			infouser.setIscheck(true);
		}

		myadapter.notifyDataSetChanged();

		for (ProcessInfo info : userprocessInfos) {
			System.out.println("......" + info.getPackageName()
					+ info.isIscheck());
		}
	}

	public void selectReverse(View v) {
		for (ProcessInfo infouser : userprocessInfos) {

			if (infouser.isIscheck()) {
				infouser.setIscheck(false);
			} else {
				infouser.setIscheck(true);
			}

			if (infouser.getPackageName().equals("com.example.mobilesafe")) {
				infouser.setIscheck(false);
			}

		}

		for (ProcessInfo infouser : sysprocessInfos) {
			if (infouser.isIscheck()) {
				infouser.setIscheck(false);
			} else {
				infouser.setIscheck(true);
			}
		}

		myadapter.notifyDataSetChanged();
	}

	public void killProcess(View v) {
		List<ProcessInfo> kill_list = new ArrayList<ProcessInfo>();
		int availMen = 0;
		for (ProcessInfo info : userprocessInfos) {
			if (info.isIscheck()) {
				kill_list.add(info);
				availMen += info.getSize();
			}
		}
		


		for (ProcessInfo info : sysprocessInfos) {
			if (info.isIscheck()) {
				kill_list.add(info);
				availMen += info.getSize();
			}
		}

		for (ProcessInfo processInfo : kill_list) {
			if (processInfo.isUers()) {
				userprocessInfos.remove(processInfo);
			} else {
				sysprocessInfos.remove(processInfo);
			}
		}

		GetProcessInfo.KillProcess(this, kill_list);

		Toast.makeText(
				ProcessMangerActivity.this,
				"共清理:"
						+ Formatter.formatFileSize(ProcessMangerActivity.this,
								availMen), Toast.LENGTH_SHORT).show();

		myadapter.notifyDataSetChanged();
	}

}
