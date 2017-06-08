package com.zws.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitileLayout extends LinearLayout {
	private TextView  leftTextView;
	private TextView rightTextView;
	
	public TitileLayout(Context context,String tag) {
		super(context);
		setOrientation(LinearLayout.HORIZONTAL);
		initView(context,tag);
	}
	
	private void initView(Context context,String tag) {
		leftTextView = new TextView(context);
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.leftMargin = 100;
		leftTextView.setGravity(Gravity.CENTER);
		params.weight = 1;
		if(tag.contains("title")||tag.contains("mac")||tag.contains("item")){
			leftTextView.setTextSize(20);
		}else{
			leftTextView.setTextSize(30);
		}
		addView(leftTextView, params);
		
		rightTextView = new TextView(context);
		params = new LayoutParams(
				LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		if(tag.contains("title")||tag.contains("item")){
			rightTextView.setTextSize(20);
			rightTextView.setGravity(Gravity.CENTER);
			params.weight = 2;
		}else if(tag.contains("mac")){
			rightTextView.setTextSize(20);
			rightTextView.setGravity(Gravity.LEFT);
			params.weight = 1;
		}else{
			rightTextView.setTextSize(30);
			rightTextView.setGravity(Gravity.CENTER);
			params.weight = 1;
		}
		addView(rightTextView, params);
	}
	
	public void setTitleTxt(int leftTxtId,int rightTxtId){
		leftTextView.setText(leftTxtId);
		rightTextView.setText(rightTxtId);
	}
	
	public void setLeftResId(int resID){
		leftTextView.setText(resID);
	}
	
	public void setRightResId(int resID){
		rightTextView.setText(resID);
	}
	
	public void setTitleName(String name){
		leftTextView.setText(name);
	}
	
	public void setResultValue(String value){
		rightTextView.setText(value);
	}
	
	public void setResultTxtColor(int color){
		rightTextView.setTextColor(color);
	}
}
