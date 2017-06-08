package com.zws.view;

import com.zws.wifiboarddetect.ListAdapter;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ShowTestResultView extends LinearLayout {
	private TitileLayout titileLayout;
	private ListView testContetResult;
	private TitileLayout macView;
	private TitileLayout resultView;
	
	public ShowTestResultView(Context context) {
		super(context);
		initView(context);
		setOrientation(LinearLayout.VERTICAL);
	}
	
	private void initView(Context context){
		titileLayout = new TitileLayout(context,"contenttitle");
		LinearLayout.LayoutParams params = new LinearLayout.
				  LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.addView(titileLayout, params);
		
		testContetResult = new ListView(context);
		params = new LinearLayout.
				  LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.topMargin = 15;
		params.weight = 1;
		this.addView(testContetResult, params);
		
		macView = new TitileLayout(context, "mac");
		params = new LinearLayout.
				  LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		this.addView(macView, params);
		
		resultView = new TitileLayout(context, "result");
		params = new LinearLayout.
				  LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		this.addView(resultView, params);
	}
	
	public void setDefualtData(int titleLeft,int titleRight,
			int macId,int resultId){
		Log.d("lucky", "--setDefualtData--");
		titileLayout.setTitleTxt(titleLeft, titleRight);
		macView.setLeftResId(macId);
		resultView.setLeftResId(resultId);
	}
	
	public void setListViewAdapter(ListAdapter adapter){
		testContetResult.setAdapter(adapter);
	}
	
	public void setMacValue(String value){
		macView.setResultValue(value);
	}
	
	public void setResultValue(String value){
		resultView.setResultValue(value);
	}
	
	public void setResultTxtColor(int color){
		resultView.setResultTxtColor(color);
	}
}
