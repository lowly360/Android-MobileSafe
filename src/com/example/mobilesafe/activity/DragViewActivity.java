package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;

public class DragViewActivity extends Activity {
	private TextView tvTop;
	private TextView tvBottom;
	private ImageView ivDrag;

	private int start_x;
	private int start_y;

	private int end_x;
	private int end_y;
	private SharedPreferences mPref;
	private int width;
	private int height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_view);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		tvTop = (TextView) findViewById(R.id.tv_top);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);

		ivDrag = (ImageView) findViewById(R.id.iv_drag);
				
		int dragX = mPref.getInt("drag_x", 0);
		int dragY = mPref.getInt("drag_y", 0);		

		width = getWindowManager().getDefaultDisplay().getWidth();
		height = getWindowManager().getDefaultDisplay().getHeight();
		
		if(dragY>height/2){
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		}
		else{
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		}

		RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) ivDrag
				.getLayoutParams();

		layoutParams.leftMargin = dragX;
		layoutParams.topMargin = dragY;

		ivDrag.setLayoutParams(layoutParams);

		// ivDrag.layout(dragX, dragY, dragX+ivDrag.getWidth(),
		// dragY+ivDrag.getHeight()); //此方法不行

		ivDrag.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					start_x = (int) event.getRawX();
					start_y = (int) event.getRawY();
					// System.out.println("------------------------>1");
					// 获取触摸坐标
					break;

				case MotionEvent.ACTION_MOVE:
					// System.out.println("------------------------>2");
					end_x = (int) event.getRawX();
					end_y = (int) event.getRawY();

					int dx = end_x - start_x;
					int dy = end_y - start_y;

					int l = ivDrag.getLeft() + dx;
					int r = ivDrag.getRight() + dx;

					int t = ivDrag.getTop() + dy;
					int b = ivDrag.getBottom() + dy;

					if (l < 0 || r > width || t < 0 || b > height - 20) {
						break;
					}
					
					if(t>height/2){
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					}
					else{
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}

					ivDrag.layout(l, t, r, b);

					start_x = (int) event.getRawX();
					start_y = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					mPref.edit().putInt("drag_x", ivDrag.getLeft()).commit();
					mPref.edit().putInt("drag_y", ivDrag.getTop()).commit();
					break;

				default:
					break;
				}
				return true;
			}
		});

	}
}
