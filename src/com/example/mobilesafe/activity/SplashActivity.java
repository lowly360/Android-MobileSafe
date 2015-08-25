package com.example.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.BlackNumberInfo;
import com.example.mobilesafe.db.dao.BalckNumberDao;
import com.example.mobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {

    protected static final int CODE_UPDATE_DIALOG = 0;


	protected static final int CODE_URL_ERROR = 1;


	protected static final int CODE_NET_ERROR = 2;


	protected static final int CODE_JSON_ERROR = 3;


	protected static final int CODE_ENTER_HOME = 4;


	private TextView tv_version;
	
	private TextView tv_progress;
    
    //服务器数据，json中数据
    private String mVersionName;//版本名
    private int mVersionCode;//版本号
    private String mDesc;//版本说明
    private String mDownloadUrl; //下载地址
    
    private Handler mHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this,"网络异常", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_UPDATE_DIALOG:
				showUpdateDailog();	
				
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this,"URL异常", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this,"数据异常", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				
				enterHome();
				break;
			default:
				break;
			}
    	};
    };


	private SharedPreferences mPref;


	private RelativeLayout rlRoot;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
   
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("版本号："+getVersionName());
        
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
		anim.setDuration(2000);
		
		rlRoot.startAnimation(anim);
		
		CopyDB("address.db");
		//把病毒数据库拷贝到files文件夹中
		CopyDB("antivirus.db");
        
        //判定是否需要更新
        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        
        if(autoUpdate){
        	checkVersion();
        }else{
        	mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);//延时两秒后发送消息
        }
    }

	/**
	 * 获取版本名
	 * @return
	 */
	private String getVersionName() {
		String versionName = null;
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			versionName = packageInfo.versionName;
			
			return versionName;	
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return " ";		
	}   
	
	/**
	 * 获取版本号
	 * @return
	 */
	private int getVersionCode() {
		String versionName = null;
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			versionName = packageInfo.versionName;
			
			return versionCode;	
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return -1;		
	}
		
/**
 * 检查本地应用的版本号与服务器端的版本号，判断是否要更新应用
 */
		private void checkVersion(){
			final long startTime = System.currentTimeMillis();
			new Thread(){
				
				@Override
				public void run() {
					Message msg = mHandler.obtainMessage();
					HttpURLConnection conn = null;
					super.run();
					try {
						URL url = new URL("http://192.168.0.107:8080/update.json");
						conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");
						conn.setReadTimeout(2000);
						conn.setReadTimeout(2000);
						conn.connect();
						
						if(conn.getResponseCode() == 200){
							InputStream inputStream = conn.getInputStream();
							String result = StreamUtils.readFromStream(inputStream);					
							
							//解析json				
							JSONObject jo = new JSONObject(result);
							mVersionName = jo.getString("versionName");
							mVersionCode = jo.getInt("versionCode");
							mDesc = jo.getString("description");
							mDownloadUrl = jo.getString("downloadUrl");
							
							//System.out.println("-----"+mDownloadUrl);
							
							if(mVersionCode > getVersionCode()){
								msg.what = CODE_UPDATE_DIALOG;
							}else{
								msg.what = CODE_ENTER_HOME;
							}			
						}		
					} catch (MalformedURLException e) {
						// url错误
						msg.what = CODE_URL_ERROR;
						e.printStackTrace();
					} catch (IOException e) {
						// 错误
						msg.what = CODE_NET_ERROR;
						e.printStackTrace();
					}
					catch (JSONException e) {
						//json
						msg.what = CODE_JSON_ERROR;
						e.printStackTrace();
					}finally{
						long endTime = System.currentTimeMillis();	
						
						long timeUsed = endTime - startTime;//计算网络请求的时间
						//固定等待时间
						if(timeUsed<2000){
							try {
								Thread.sleep(2000-timeUsed);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
						mHandler.sendMessage(msg);
						if(conn!=null){
							conn.disconnect();
						}
					}			
				}
			}.start();
	}
	
	/**
	 * 显示升级对话框
	 */
	protected void showUpdateDailog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("版本"+mVersionName);
		builder.setMessage(mDesc);
		//builder.setCancelable(false); //�����û���ȡ��Ի��򣬾�����Ҫ��
		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				download();
				
			}
		});
		builder.setNegativeButton("稍后再说˵", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();		
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});
		
		builder.show();
	}
	/**
	 * 下载apk
	 */
	protected void download() {		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			tv_progress.setVisibility(View.VISIBLE);
			
			String target = Environment.getExternalStorageDirectory()+"/update.apk";

			HttpUtils utils = new HttpUtils();
			System.out.println("------->"+target);
			utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
				
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					// TODO Auto-generated method stub
					super.onLoading(total, current, isUploading);
					System.out.println("下载进程"+current+"/"+total);
					
					tv_progress.setText("下载进程"+current*100/total+"%");
				}
				
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					Toast.makeText(SplashActivity.this, "下载成功", 0).show();
					//��ת��ϵͳ����ҳ��
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result), "application/vnd.android.package-archive");
					startActivityForResult(intent, 0);				//����û�ȡ��װ�Ļ����᷵�ؽ��ص�onActivityResult
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "下载失败", 0).show();			
				}
			});
		}else{
			Toast.makeText(SplashActivity.this, "找不到sd卡", 0).show();
		}	
	}
	
	//取消更新，关闭对话框回调此 onActivityResult
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	private void CopyDB(String dbName){
		File destFile = new File(getFilesDir(),dbName);
		FileOutputStream out = null;
		InputStream in = null;
		if(destFile.exists()){//如果已经存在就不再次复制了
			return ;
		}
		try {
			in = getAssets().open(dbName);
			out = new FileOutputStream(destFile);
			
			int len;
			byte[] buffer = new byte[1024];
			while((len = in.read(buffer))!=-1){	
				out.write(buffer, 0, len);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * 跳转主菜单
	 */
	private void enterHome() {
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
		finish();
	}
}
