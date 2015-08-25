package com.example.mobilesafe.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInfos;
import com.example.mobilesafe.db.dao.AppLockDao;
import com.example.mobilesafe.logic.GetAppInfos;
import com.lidroid.xutils.DbUtils.DaoConfig;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LockFragment extends android.support.v4.app.Fragment {
	private List<AppInfos> lockappInfos;
	private TextView tv_lockApp;
	private ListView lv_lock;
	private AppLockDao dao;
	private LockAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.item_lock_fragment, null);

		lv_lock = (ListView) view.findViewById(R.id.lv_lock);

		tv_lockApp = (TextView) view.findViewById(R.id.tv_lockApp);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

		List<AppInfos> appInfos = GetAppInfos.getAppInfos(getActivity());

		lockappInfos = new ArrayList<AppInfos>();

		dao = new AppLockDao(getActivity());

		for (AppInfos appInfos2 : appInfos) {
			if (dao.find(appInfos2.getApkPackName())) {
				lockappInfos.add(appInfos2);
			}
		}

		adapter = new LockAdapter();
		lv_lock.setAdapter(adapter);
	}

	public class LockAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			tv_lockApp.setText("已加锁的软件 " + lockappInfos.size() + " 个");
			return lockappInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return lockappInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final View view ;
			ViewHolder holder = null;
			if (convertView == null) {
				view = View.inflate(getActivity(), R.layout.item_lock, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);

				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.iv_lock = (ImageView) view.findViewById(R.id.iv_lock);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			final AppInfos appInfos = lockappInfos.get(position);
			holder.iv_icon.setImageDrawable(appInfos.getApp_icon());
			holder.tv_name.setText(appInfos.getApp_name());
			holder.iv_lock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, -1.0f,
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0);
					
					animation.setDuration(500);
					
					view.startAnimation(animation);
					
					new Thread(){
						public void run() {
							SystemClock.sleep(500);
							getActivity().runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									dao.delete(appInfos.getApkPackName());
									lockappInfos.remove(position);
									adapter.notifyDataSetChanged();
								}
							});
							
							
						};
						
					}.start();
				}
			});

			return view;
		}

	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		ImageView iv_lock;

	}

}
