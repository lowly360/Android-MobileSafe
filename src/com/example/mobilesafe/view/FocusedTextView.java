package com.example.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusedTextView extends TextView {
	//��style��ʽ��ʱ����ô˷���
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	//�����Ե�ʱ����ô˷���
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	//�ô���new���ô˷���
	public FocusedTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
	 * �����Ҫ���У����ȵ��ô˺����ж��Ƿ��н��㣬��true�Ļ�������ƲŻ���Ч��
	 * ���Բ���textview�Ƿ��н��㣬���Ƕ�Ҫǿ�Ʒ���true�����������Ϊ�н���
	 */
	@Override
	public boolean isFocused() {
		return true;
	}

	
}
