package com.example.mobilesafe.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInfos;
import com.example.mobilesafe.db.dao.AppLockDao;
import com.example.mobilesafe.logic.GetAppInfos;
import com.lidroid.xutils.DbUtils.DaoConfig;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
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

public class UnLockFragment extends Fragment {
	private View view;
	private ListView lv_unlock;
	private TextView tv_unlock_info;
	private List<AppInfos> appInfos;
	private AppLockDao appLockDao;
	private List<AppInfos> unlocklist;
	private UnLockAdapter unLockAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.item_unlock_fragment, null);

		lv_unlock = (ListView) view.findViewById(R.id.lv_unlock);
		tv_unlock_info = (TextView) view.findViewById(R.id.tv_unlock_info);

		return view;
	}

	@Override
	public void onStart() {
		initData();
		super.onStart();
	}

	private void initData() {
		appInfos = GetAppInfos.getAppInfos(getActivity());

		appLockDao = new AppLockDao(getActivity());

		unlocklist = new ArrayList<AppInfos>();

		for (AppInfos info : appInfos) {

			if (appLockDao.find(info.getApkPackName())) {

			} else {
				unlocklist.add(info);
			}
		}

		unLockAdapter = new UnLockAdapter();

		lv_unlock.setAdapter(unLockAdapter);

	}

	class UnLockAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			tv_unlock_info.setText("未加锁的软件有 " + unlocklist.size() + " 个");
			return unlocklist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return unlocklist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final View view ;
			ViewHolder holder = null;
			final AppInfos appInfo;
			if (convertView == null) {
				view = View.inflate(getActivity(), R.layout.item_unlock, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.iv_unlock = (ImageView) view
						.findViewById(R.id.iv_unlock);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				view = convertView;
			}
			appInfo = unlocklist.get(position);

			holder.iv_icon.setImageDrawable(unlocklist.get(position)
					.getApp_icon());
			holder.tv_name.setText(unlocklist.get(position).getApp_name());

			holder.iv_unlock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 位移动画
					TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 1.0f,
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
									appLockDao.add(appInfo.getApkPackName());
									unlocklist.remove(position);
									unLockAdapter.notifyDataSetChanged();									
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
		ImageView iv_unlock;
		TextView tv_name;

	}
}
