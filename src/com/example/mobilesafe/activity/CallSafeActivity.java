package com.example.mobilesafe.activity;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.adapter.MyBaseAdapter;
import com.example.mobilesafe.bean.BlackNumberInfo;
import com.example.mobilesafe.db.dao.BalckNumberDao;
import com.example.mobilesafe.utils.ToastUtils;

public class CallSafeActivity extends Activity {
	private ListView lv_blacknumber;
	private List<BlackNumberInfo> blackNumber;
	private BalckNumberDao dao;
	private Myadapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsafe);
		initUI();
		initDate();
	}

	private void initDate() {

		dao = new BalckNumberDao(this);
		blackNumber = dao.findAll();
		
		adapter = new Myadapter(blackNumber, CallSafeActivity.this);

		lv_blacknumber.setAdapter(adapter);

	}

	private void initUI() {
		lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);

	}

	private class Myadapter extends MyBaseAdapter<BlackNumberInfo> {

		private Myadapter(List lists, Context mContext) {
			super(lists, mContext);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(CallSafeActivity.this,
						R.layout.item_call_safe, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) convertView
						.findViewById(R.id.tv_number);
				holder.tv_mode = (TextView) convertView
						.findViewById(R.id.tv_mode);
				holder.iv_delete = (ImageView) convertView
						.findViewById(R.id.iv_delete);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_number.setText(lists.get(position).getNumber());
			String mode = lists.get(position).getMode();
			if (mode.equals("1")) {
				holder.tv_mode.setText("来电+短信拦截");
			} else if (mode.equals("2")) {
				holder.tv_mode.setText("来电拦截");
			} else if (mode.equals("3")) {
				holder.tv_mode.setText("短信拦截");
			}

			final BlackNumberInfo info = lists.get(position);

			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String number = info.getNumber();
					boolean result = dao.delete(number);
					if (result) {
						lists.remove(info);
						if (adapter == null) {
							adapter = new Myadapter(blackNumber, CallSafeActivity.this);
							lv_blacknumber.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						ToastUtils.showToast(CallSafeActivity.this, "删除成功!");
					} else {
						ToastUtils.showToast(CallSafeActivity.this, "删除失败!");
					}

				}
			});
			return convertView;
		}
	}

	static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}

	public void btn_addblacknumber(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_callsafe_add, null);
		dialog.setView(view);

		final EditText et_number = (EditText) view.findViewById(R.id.et_number);
		final CheckBox cb_phone = (CheckBox) view.findViewById(R.id.cb_phone);
		final CheckBox cb_sms = (CheckBox) view.findViewById(R.id.cb_sms);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = et_number.getText().toString().trim();

				BalckNumberDao dao = new BalckNumberDao(CallSafeActivity.this);
				String exist = dao.query(number);

				if (TextUtils.isEmpty(number)) {
					ToastUtils.showToast(CallSafeActivity.this, "请正确输入号码");
					return;
				}
				
				if (exist != null) {
					ToastUtils.showToast(CallSafeActivity.this, "该号码已在黑名单中");
					return;
				}
				
				String mode = "";				
				if(cb_phone.isChecked()&&cb_sms.isChecked()){
					mode = "1";
				}else if(cb_phone.isChecked()){
					mode = "2";
				}else if(cb_sms.isChecked()){
					mode = "3";
				}else{
					ToastUtils.showToast(CallSafeActivity.this, "请选择拦截模式");
					return;
				}
				
				//添加到listview的队列中
				BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
				blackNumberInfo.setNumber(number);
				blackNumberInfo.setMode(mode);
				blackNumber.add(blackNumberInfo);

				//添加到数据库中
				dao.add(number, mode);
				if (adapter == null) {
					adapter = new Myadapter(blackNumber, CallSafeActivity.this);
					lv_blacknumber.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				dialog.dismiss();
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		dialog.show();

	}
}
