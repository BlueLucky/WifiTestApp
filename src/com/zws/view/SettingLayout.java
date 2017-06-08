package com.zws.view;

import java.util.ArrayList;
import java.util.List;

import com.zws.bean.SettingInfo;
import com.zws.bean.SettingItemInfo;
import com.zws.saveinterface.SaveSetDataInterface;
import com.zws.untils.Contans;
import com.zws.wifiboarddetect.R;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Button;
import android.widget.LinearLayout;

public class SettingLayout extends LinearLayout {
	private int wifiSetItemsCounts = 4;
	private int blueSetItemsCounts = 2;
	private SettingItemView wifiMacItemView;
	private SettingItemView wifiColonItemView;
	private SettingItemView blueMacItemView;
	private SettingItemView blueColonItemView;
	private Resources mResources;
	List<SettingItemView> wifiViewList = new ArrayList<SettingItemView>();
	List<SettingItemView> blueViewList = new ArrayList<SettingItemView>();
	
	public SettingLayout(Context context,SaveSetDataInterface lis,
			OnClickListener onClickLis) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);
		mResources = context.getResources();
		LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.leftMargin=10;
		params.rightMargin=10;
		setLayoutParams(params);
		initView(context,lis,onClickLis);
	}
	
	private void addItemViews(Context context,String tag,List<SettingItemView>list,
			SaveSetDataInterface lis,int count){
		if(list==null){
			return;
		}
		list.clear();
		for(int i=0;i<count;i++){
			SettingItemView ssid1View = new SettingItemView(context,tag+(i+1));
			ssid1View.setSaveDataLis(lis);
			ssid1View.setTitleTxt(tag+(i+1));
			LayoutParams params  = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			params.topMargin = 5;
			list.add(ssid1View);
			this.addView(ssid1View, params);
		}
	}
	
	private void initView(Context context,
			SaveSetDataInterface lis,OnClickListener onClicklis) {
		TitileLayout wifiTitleLayout = new TitileLayout(context,"wifiTitle");
		LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		wifiTitleLayout.setTitleTxt(R.string.wifiName_text, R.string.wifiLevel_text);
		this.addView(wifiTitleLayout, params);
		
		addItemViews(context,"ssid",wifiViewList,lis,Contans.WIFI_ITEM_COUNT);
		//initWifiItem(context,lis);
		
		wifiMacItemView = new SettingItemView(context,"wifi_mac");
		wifiMacItemView.setSaveDataLis(lis);
		wifiMacItemView.setTitleTxt(mResources.getString(R.string.mac_range_text));
		params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		this.addView(wifiMacItemView, params);
		
		wifiColonItemView = new SettingItemView(context,"wifi_colon");
		wifiColonItemView.setSaveDataLis(lis);
		wifiColonItemView.setTitleTxt(mResources.getString(R.string.standPercent));
		params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		this.addView(wifiColonItemView, params);
		
		TitileLayout bluetoothTitleLayout = new TitileLayout(context,"bluetitle");
		params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		bluetoothTitleLayout.setTitleTxt(R.string.bluetoothName_text, R.string.bluetoothLevel_text);
		this.addView(bluetoothTitleLayout, params);
		
		addItemViews(context,"bluetooth",blueViewList,lis,Contans.BLUE_ITEM_COUNT);
		
		blueMacItemView = new SettingItemView(context,"blue_mac");
		blueMacItemView.setSaveDataLis(lis);
		blueMacItemView.setTitleTxt(mResources.getString(R.string.mac_range_text));
		params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		this.addView(blueMacItemView, params);
		
		blueColonItemView = new SettingItemView(context,"blue_colon");
		blueColonItemView.setSaveDataLis(lis);
		blueColonItemView.setTitleTxt(mResources.getString(R.string.standPercent));
		params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		this.addView(blueColonItemView, params);
		
		Button button = new Button(context);
		button.setOnClickListener(onClicklis);
		button.setText(R.string.save);
		button.setTextSize(20);
		params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.topMargin = 20;
		this.addView(button,params);
	}
	
	public void setDataFromShared(SettingInfo bean){
		if(bean==null){
			return;
		}
		List tempWifis = bean.getWifiSettings();
		for(int i=0;i<tempWifis.size();i++){
			wifiViewList.get(i).setSharedData((SettingItemInfo)tempWifis.get(i));
		}
		List tempBlues = bean.getBluetoothSettings();
		for(int i=0;i<tempBlues.size();i++){
			blueViewList.get(i).setSharedData((SettingItemInfo)tempBlues.get(i));
		}
		wifiMacItemView.setSharedData(bean.getWifiMacRange());
		wifiColonItemView.setSharedData(bean.getWifiColon()+"");
		
		blueMacItemView.setSharedData(bean.getBlueMacRange());
		blueColonItemView.setSharedData(bean.getBlueColon()+"");
	}
}
