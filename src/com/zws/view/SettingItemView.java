package com.zws.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zws.bean.MacRangeInfo;
import com.zws.bean.SettingItemInfo;
import com.zws.saveinterface.*;

public class SettingItemView extends LinearLayout {
	private TextView titleTextView;
	private EditText firstEditText, secondEditText;
	private String tag;
	private SaveSetDataInterface saveLis;
	
	public void setSaveDataLis(SaveSetDataInterface lis){
		saveLis = lis;
	}
	
	public SettingItemView(Context context,String tag) {
		super(context);
		this.tag = tag;
		setTag(tag);
		setOrientation(LinearLayout.HORIZONTAL);
		initView(context);
		addTextChangedListener();
	}

	private void initView(Context mContext) {
		titleTextView = new TextView(mContext);
		titleTextView.setTextSize(15);
		LinearLayout.LayoutParams params = new LinearLayout.
				LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.rightMargin = 30;
		this.addView(titleTextView,params);
		
		if("wifi_mac".equals(tag)||"blue_mac".equals(tag)){
			firstEditText = new EditText(mContext);
			firstEditText.setGravity(Gravity.CENTER);
			params = new LinearLayout.
					LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.rightMargin = 30;
			params.weight = 1;
			this.addView(firstEditText,params);
			
			View view = new View(mContext);
			view.setBackgroundColor(Color.WHITE);
			params = new LinearLayout.LayoutParams(200, 1);
			params.rightMargin = 30;
			params.gravity = Gravity.CENTER;
			this.addView(view, params);
			
			secondEditText = new EditText(mContext);
			secondEditText.setGravity(Gravity.CENTER);
			params = new LinearLayout.
					LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1;
			this.addView(secondEditText,params);
		}else if("wifi_colon".equals(tag)||"blue_colon".equals(tag)){
			firstEditText = new EditText(mContext);
			firstEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
			firstEditText.setGravity(Gravity.CENTER);
			params = new LinearLayout.
					LayoutParams(300, LayoutParams.WRAP_CONTENT);
			this.addView(firstEditText,params);
		}else{
			firstEditText = new EditText(mContext);
			firstEditText.setGravity(Gravity.CENTER);
			params = new LinearLayout.
					LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.rightMargin = 30;
			params.weight = 1;
			this.addView(firstEditText,params);

			secondEditText = new EditText(mContext);
			secondEditText.setGravity(Gravity.CENTER);
			params = new LinearLayout.
					LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.weight = 2;
			this.addView(secondEditText,params);
		}
	}
	
	public void setTitleTxt(String titleName){
		titleTextView.setText(titleName);
	}
	
	public String getSSidStr(){
		return firstEditText.getText().toString();
	}
	
	public String getSLevel(){
		return secondEditText.getText().toString();
	}
	
	public void setSharedData(SettingItemInfo info){
		firstEditText.setText(info.getName());
		secondEditText.setText(info.getLevel()+"");
	}
	
	public void setSharedData(MacRangeInfo info){
		firstEditText.setText(info.getStartMac());
		secondEditText.setText(info.getEndMac());
	}
	
	public void setSharedData(String data){
		firstEditText.setText(data);
	}
	
	public void addTextChangedListener(){
		if(firstEditText!=null){
			firstEditText.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				@Override
				public void afterTextChanged(Editable s) {
					if(saveLis!=null){
						saveLis.saveSetData(1, tag,s.toString());
					}
				}
			});
		}
		
		if(secondEditText!=null){
			secondEditText.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					if(saveLis!=null){
						saveLis.saveSetData(2, tag,s.toString());
					}
				}
			});
		}
	}
}
