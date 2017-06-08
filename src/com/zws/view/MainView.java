package com.zws.view;

import com.zws.wifiboarddetect.ListAdapter;
import com.zws.wifiboarddetect.R;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainView extends LinearLayout {
	private Button settingBtn;
	private Context mContext;
	private ShowTestResultView wifiShowResult;
	private ShowTestResultView  blueShowResult;
	
	public MainView(Context context,OnClickListener lis) {
		super(context);
		mContext = context;
		setOrientation(LinearLayout.VERTICAL);
		initView(lis);
		setDefalut();
	}
	
	private void initView(OnClickListener lis){
	  wifiShowResult = new ShowTestResultView(mContext);
	  LinearLayout.LayoutParams params = new LinearLayout.
			  LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	  params.weight = 1;
	  this.addView(wifiShowResult, params);
	  
	  blueShowResult = new ShowTestResultView(mContext);
	  params = new LinearLayout.
			  LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	  params.weight = 1;
	  params.topMargin = 30;
	  this.addView(blueShowResult, params);
	  
	  settingBtn = new Button(mContext);
	  settingBtn.setText("设置");
	  settingBtn.setTextSize(20);
	  settingBtn.setOnClickListener(lis);
	  params = new LinearLayout.
			  LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	  this.addView(settingBtn, params);
	}
	
	private void setDefalut(){
		wifiShowResult.setDefualtData(R.string.wifiName_text, 
				R.string.signalNum, R.string.wifiBoardMac, R.string.result_text);
		blueShowResult.setDefualtData(R.string.bluetoothName_text, 
				R.string.signalNum, R.string.wifiBoardMac, R.string.result_text);
	}
	
	public void setWifiResultAdapter(ListAdapter adapter){
		wifiShowResult.setListViewAdapter(adapter);
	}
	
	public void setBlueResultAdapter(ListAdapter adapter){
		blueShowResult.setListViewAdapter(adapter);
	}
	
	public void setWifiMac(String macStr){
		wifiShowResult.setMacValue(macStr);
	}
	
	public void setWifiResultValue(String resultStr){
		wifiShowResult.setResultValue(resultStr);
	}
	
	public void setWifiResultTxtColor(int color){
		wifiShowResult.setResultTxtColor(color);
	}
	
	public void setBlueMac(String macStr){
		blueShowResult.setMacValue(macStr);
	}
	
	public void setBlueResultValue(String resultStr){
		blueShowResult.setResultValue(resultStr);
	}
	
	public void setBlueResultTxtColor(int color){
		blueShowResult.setResultTxtColor(color);
	}
}
