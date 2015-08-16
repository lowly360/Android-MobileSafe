package com.example.mobilesafe.view;

import com.example.mobilesafe.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 自定义组合控件
 */
public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.mobilesafe";
	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbStatus;
	private String mTitle;
	private String mDesc_on;
	private String mDesc_off;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTitle = attrs.getAttributeValue(NAMESPACE, "title");
		mDesc_on = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mDesc_off = attrs.getAttributeValue(NAMESPACE, "desc_off");
		initView();
		
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public void initView() {
		//将自定义好的布局文件设置给当前的SettingItemView
		View.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		
		setTitle(mTitle);
		
	}
	
	public void setTitle(String title){
		tvTitle.setText(title);
	}
	
	public void setDesc(String desc){
		tvDesc.setText(desc);
	}
	
	public boolean isChecked(){
		return cbStatus.isChecked();		
	}
	
	public void setChecked(boolean check){
		cbStatus.setChecked(check);
		if(check){
			setDesc(mDesc_on);
		}else{
			setDesc(mDesc_off);
		}
	}

}
