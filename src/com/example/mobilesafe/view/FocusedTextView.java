package com.example.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusedTextView extends TextView {
	//有style样式的时候调用此方法
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	//有属性的时候调用此方法
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	//用代码new调用此方法
	public FocusedTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 跑马灯要运行，首先调用此函数判断是否有焦点，是true的话，跑马灯才会有效果
	 * 所以不管textview是否有焦点，我们都要强制返回true，让跑马灯认为有焦点
	 */
	@Override
	public boolean isFocused() {
		return true;
	}

	
}
