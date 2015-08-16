package com.example.mobilesafe.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

public class LocationService extends Service {

	private TextView tvShow;
	private LocationManager lm;
	private MyLocationListener listener;
	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		// List<String> allProviders = lm.getAllProviders();
		// System.out.println(allProviders);
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = lm.getBestProvider(criteria, true);

		listener = new MyLocationListener();
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);

		// tvShow = (TextView) findViewById(R.id.tv_show);
	}

	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			String j = "经度：" + location.getLongitude();
			String w = "纬度" + location.getLatitude();
			mPref.edit().putString("location", "j:"+j+"w:"+w).commit();
			
			stopSelf();//停止服务
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		lm.removeUpdates(listener);
		super.onDestroy();
	}

}
