package com.zws.wifiboarddetect;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.zws.bean.MacRangeInfo;
import com.zws.bean.SettingInfo;
import com.zws.bean.SettingItemInfo;
import com.zws.saveinterface.SaveSetDataInterface;
import com.zws.untils.Contans;
import com.zws.untils.SharedpreferencesUnitl;
import com.zws.view.SettingLayout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class TestActivity extends Activity implements SaveSetDataInterface,OnClickListener{
	private SharedpreferencesUnitl shared;
	private Context mContext;
	private SettingLayout layout;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case Contans.MSG_UPDATE_SETTING_DATA:
				layout.setDataFromShared((SettingInfo)msg.obj);
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = this;
		layout = new SettingLayout(this,this,this);
		setContentView(layout);
		getSharedData();
	}
	
	private void getSharedData() {
		shared = SharedpreferencesUnitl.getIntance(mContext);
		new Thread(new Runnable() {
			public void run() {
				SettingInfo info = new SettingInfo();
				info.setWifiMacRange( new MacRangeInfo(
						shared.getString(Contans.KEY_WIFI_MAC_START),
						shared.getString(Contans.KEY_WIFI_MAC_END)));
				info.setBlueMacRange(new MacRangeInfo(
						shared.getString(Contans.KEY_BLUE_MAC_START), 
						shared.getString(Contans.KEY_BLUE_MAC_END)));
				info.setBlueColon(shared.getString(Contans.KEY_BLUE_COLON));
				info.setWifiColon(shared.getString(Contans.KEY_WIFI_COLON));
				List<SettingItemInfo> wifiList = new ArrayList<SettingItemInfo>();
				for(int i=0;i<Contans.WIFI_ITEM_COUNT;i++){
					SettingItemInfo itemWifiInfo = new SettingItemInfo(
							shared.getString(Contans.KEY_SSID+(i+1)), 
							shared.getString(Contans.KEY_SSID+(i+1)+"_level"));
					wifiList.add(itemWifiInfo);
				}
				info.setWifiSettings(wifiList);
				List<SettingItemInfo> blueList = new ArrayList<SettingItemInfo>();
				for(int i=0;i<Contans.BLUE_ITEM_COUNT;i++){
					SettingItemInfo itemWifiInfo = new SettingItemInfo(
							shared.getString(Contans.KEY_BLUETOOTH+(i+1)), 
							shared.getString(Contans.KEY_BLUETOOTH+(i+1)+"_level"));
					blueList.add(itemWifiInfo);
				}
				info.setBluetoothSettings(blueList);
				Message message = mHandler.obtainMessage();
				message.what = Contans.MSG_UPDATE_SETTING_DATA;
				message.obj = info;
				mHandler.sendMessage(message);
			}
		}).start();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			setResult(Contans.requestCode);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void saveSetData(int index, String tag,String data) {
		Log.d("lucky", "--index-->>"+index+" tag:"+tag+" data:"+data);
		saveDataTimer(index,tag,data);
	}
	
	private Timer timer;
	/**
	 * 超时计时器
	 */
	private void saveDataTimer(int index, String tag,String data) {
		cancelTask();
		timer = new Timer();
		timer.schedule(new SaveDataTask(index, tag, data), 1000);
	}
	
	/**
	 * 取消计时器
	 */
	public void cancelTask() {
		if (timer != null) {
			timer.cancel();
		}
	}
	
	class SaveDataTask extends TimerTask {
		private int index;
		private String tag;
		private String data;
		public SaveDataTask(int index, String tag,String data){
			this.index=index;
			this.tag = tag;
			this.data = data;
		}
		@Override
		public void run() {
			StringBuffer  key = new StringBuffer();
			switch(index){
			case 1:
				if(tag.contains("mac")){
					key.append(tag).append("_start");
				}else{
					key.append(tag);
				}
				break;
			case 2:
				if(tag.contains("mac")){
					key.append(tag).append("_end");
				}else if(tag.contains("bluetooth")||
						tag.contains("ssid")){
					key.append(tag).append("_level");
				}
				break;
			}
			shared.putData(key.toString(), data);
			Log.d("lucky", "key:"+key.toString()+" data:"+data);
		}
	}

	@Override
	public void onClick(View v) {
		shared.save();
	}
}
