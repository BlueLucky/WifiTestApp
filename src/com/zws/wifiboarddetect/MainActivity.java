package com.zws.wifiboarddetect;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.zws.bean.MacRangeInfo;
import com.zws.bean.SettingInfo;
import com.zws.bean.SettingItemInfo;
import com.zws.untils.Contans;
import com.zws.untils.MacAddressUtils;
import com.zws.untils.SharedpreferencesUnitl;
import com.zws.view.MainView;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
	private SharedpreferencesUnitl shared;
	private WifiManager mWifiManager;
	private Context mContext;
	private SettingInfo info;
	private ListAdapter wifiAdapter;
	private ListAdapter blueAdapter;
	private List<MyListItem> wifiListDatas ;
	private List<MyListItem> blueListDatas ;
	private List<String> saveWifiInfoName;
	private Map<String,String> saveWifiInfoMap;
	private List<String> saveBlueInfoName;
	private Map<String,String> saveBlueInfoMap;
	private BluetoothAdapter defaultAdapter;
	private MainView mainView;
	private boolean isScanWifi = true;
	private boolean isScanBluetooth = true;
	private boolean isWifiMacInRange = false;
	private boolean isBlueMacInRange = false;
	private Calculate wifiCalculate;
	private WifiScanThread scanThread;
	private BluetoothScanThread blueScanThread;
	private PowerManager.WakeLock wakeLock;
	
	private static int lowLevel = -120;
	private static int upLevel = -30;
	float step = ((float)(upLevel-lowLevel))/100;
	
	public float calcualteLevel(int crutterLevel){
		return (crutterLevel-lowLevel);
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case Contans.MSG_RELOAD_SETTING:
				getSharedData();
				break;
			case Contans.MSG_GET_SETTING:
				info = (SettingInfo)msg.obj;
				MacRangeInfo wifiMacInfo = info.getWifiMacRange();
				MacRangeInfo blueMacInfo = info.getBlueMacRange();
				if(wifiCalculate==null){
					wifiCalculate = new Calculate(wifiMacInfo,blueMacInfo);
				}else{
					wifiCalculate.setWifiMacRange(wifiMacInfo);
					wifiCalculate.setBlueMacRange(blueMacInfo);
				}
				Log.d("lucky", info.toString());
				updateMacView(1);
				updateMacView(2);
				break;
			case Contans.MSG_GET_WIFI_SCAN_RESULT:
				updateMacView(1);
				break;
			case Contans.MSG_UPDATE_SETTING_DATA:
				Log.d("lucky", "---MSG_UPDATE_SETTING_DATA---");
				wifiAdapter.notifyDataSetChanged();
				if (isWifiMacInRange) {
					if(checkPass(1)){
						if(wifiListDatas.size()==saveWifiInfoName.size()){
							mainView.setWifiResultTxtColor(Color.GREEN);
							mainView.setWifiResultValue(getResources().getString(R.string.test_result_pass));
						}else{
							mainView.setWifiResultTxtColor(Color.RED);
							mainView.setWifiResultValue(getFromatString(R.string.fail_scan_no_complete));
						}
					}else {
						mainView.setWifiResultTxtColor(Color.RED);
						mainView.setWifiResultValue(getFromatString(R.string.fail_type_low_level));
					}
				}
				break;
			case Contans.MSG_WIFI_SCAN_COMPLETED:
				List<ScanResult> list = mWifiManager.getScanResults();
				if(list.size()==0){
					list = mWifiManager.getScanResults();
					Log.d("lucky", "list："+list.size());
				}
				getResultWifiAp(list);
				break;
			case Contans.MSG_UPDATE_BLUETOOTH_MAC:
				updateMacView(2);
				break;
			case Contans.MSG_ADD_REMOTE_BLUETOOTH:
				Intent intent = (Intent)msg.obj;
				BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); 
				short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
				addBlueDevice(btDevice,rssi);
				break;
			case Contans.MSG_BLUETOOTH_UPDATE_VIEW:
				Log.d("lucky", "---MSG_UPDATE_SETTING_DATA---");
				blueAdapter.notifyDataSetChanged();
				if (isBlueMacInRange) {
					if (checkPass(2)) {
						if(blueListDatas.size()==saveBlueInfoName.size()){
							mainView.setBlueResultTxtColor(Color.GREEN);
							mainView.setBlueResultValue(getResources().getString(R.string.test_result_pass));
						}else{
							mainView.setBlueResultTxtColor(Color.RED);
							mainView.setBlueResultValue(getFromatString(R.string.fail_scan_no_complete));
						}
					} else {
						mainView.setBlueResultTxtColor(Color.RED);
						mainView.setBlueResultValue(getFromatString(R.string.fail_type_low_level));
					}
				}
				break;
			case Contans.MSG_CLEAN_BLUETOOTH_DATA:
				blueListDatas.clear();
				blueAdapter.notifyDataSetChanged();
				mainView.setBlueResultValue("");
				break;
			case Contans.MSG_CLEAN_WIFI_DATA:
				wifiListDatas.clear();
				wifiAdapter.notifyDataSetChanged();
				mainView.setWifiResultValue("");
				break;
			}
		};
	};
	
	protected void onStart() {
		super.onStart();
		acquire();
	};
	
	/**
	 * 判断mac地址范围
	 * @param type
	 */
	private void updateMacView(int type) {
		String mac = null;
		switch (type) {
		case 1:
			mac = MacAddressUtils.getMacAddress("wlan0");
			if (mac == null || mac != null && mac.length() == 0) {
				mainView.setWifiResultValue("");
				wifiListDatas.clear();
				wifiAdapter.notifyDataSetChanged();
			} else {
				mainView.setWifiMac(mac);
				if (wifiCalculate != null) {
					isWifiMacInRange = wifiCalculate.isWifiMacInRange(mac);
					if (!isWifiMacInRange) {
						mainView.setWifiResultTxtColor(Color.RED);
						mainView.setWifiResultValue(getFromatString(R.string.fail_type_noinrange));
					}
				}
			}
			break;
		case 2:
			if (defaultAdapter != null) {
				mac = defaultAdapter.getAddress();
			}
			if (mac == null || mac != null && mac.length() == 0) {
				mainView.setBlueResultValue("");
				blueListDatas.clear();
				blueAdapter.notifyDataSetChanged();
			} else {
				mainView.setBlueMac(mac);
				if (wifiCalculate != null) {
					isBlueMacInRange = wifiCalculate.isBlueMacInRange(mac);
					if (!isBlueMacInRange) {
						mainView.setBlueResultTxtColor(Color.RED);
						mainView.setBlueResultValue(getFromatString(R.string.fail_type_noinrange));
					}
				}
			}
			break;
		}
	}
	/**
	 * flag:1 check wifi;
	 * flag:2 check bluetooth
	 * @param flag
	 * @return
	 */
	private boolean checkPass(int flag) {
		Log.d("lucky", "checkPass");
		if (info == null) {
			return false;
		}
		Log.d("lucky", "checkPass_info:"+info.toString());
		switch (flag) {
		case 1:
			if(wifiListDatas.size()==0){
				return false;
			}else{
				for (MyListItem item : wifiListDatas) {
					if (item.level < Integer.parseInt(info.getWifiColon())) {
						return false;
					}
				}
				return true;
			}
		case 2:
			if (blueListDatas.size() == 0) {
				return false;
			} else {
				for (MyListItem item : blueListDatas) {
					if (item.level < Integer.parseInt(info.getBlueColon())) {
						return false;
					}
				}
				return true;
			}
		}
		return true;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (Build.VERSION.SDK_INT >= 23) {
			Settings.Secure.putInt(getContentResolver(),Settings.Secure.LOCATION_MODE, 1);
		}
		mContext = this;
		registerReceiver();
		getSharedData();
		mainView = new MainView(this, this);
		setContentView(mainView);
		setAdpater();
		startScanWifi();
		startBlueScanThead();
	}
	
	private String getFromatString(int failType){
		String failTypeStr = getResources().getString(failType);
		String failStr = getResources().getString(R.string.test_result_fail);
		String reslut = String.format(failStr, failTypeStr);
		Log.d("lucky", "reslut:"+reslut);
		return reslut;
	}
	
	class BluetoothScanThread extends Thread {
		@Override
		public void run() {
			try {
				while (isScanBluetooth) {
					if (defaultAdapter == null) {
						defaultAdapter = BluetoothAdapter.getDefaultAdapter();
					}
					if (defaultAdapter != null) {
						if (!defaultAdapter.isEnabled()) {
							defaultAdapter.enable();
							sleep(3000);
						} else {
							mHandler.sendEmptyMessage(Contans.MSG_UPDATE_BLUETOOTH_MAC);
							if (defaultAdapter.isDiscovering()) {
								defaultAdapter.cancelDiscovery();
							}
							defaultAdapter.startDiscovery();
							sleep(15000);
						}
					} else {
						sleep(3000);
					}
				}
			} catch (Exception e) {
				Log.d("lucky", "---e--->>"+e.getMessage());
			}
		}
	}
	
	private  void setAdpater(){
		wifiListDatas = new ArrayList<MyListItem>();
		wifiAdapter =  new ListAdapter(mContext, wifiListDatas);
		mainView.setWifiResultAdapter(wifiAdapter);
		
		blueListDatas = new ArrayList<MyListItem>();
		blueAdapter =  new ListAdapter(mContext, blueListDatas);
		mainView.setBlueResultAdapter(blueAdapter);
	}

	private void startBlueScanThead() {
		stopBlueScanThread();
		isScanBluetooth = true;
		if (blueScanThread == null) {
			blueScanThread = new BluetoothScanThread();
		}
		blueScanThread.start();
	}
	
	private void stopBlueScanThread(){
		isScanBluetooth = false;
		if (blueScanThread != null) {
			BluetoothScanThread tempThread = blueScanThread;
			blueScanThread = null;
			if (tempThread != null) {
				tempThread.interrupt();
			}
		}
	}
	
	private void startScanWifi(){
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		startScanThead();
	}
	
	private void startScanThead() {
		stopScanThread();
		isScanWifi = true;
		if (scanThread == null) {
			scanThread = new WifiScanThread();
		}
		scanThread.start();
	}
	
	private void stopScanThread(){
		isScanWifi = false;
		if (scanThread != null) {
			WifiScanThread tempThread = scanThread;
			scanThread = null;
			if (tempThread != null) {
				tempThread.interrupt();
			}
		}
	}
	
	class WifiScanThread extends Thread{
		@Override
		public void run() {
			try {
				while (isScanWifi) {
					if (!mWifiManager.isWifiEnabled()) {
						mWifiManager.setWifiEnabled(true);
					}
					if (mWifiManager.isWifiEnabled()) {
						mHandler.sendEmptyMessage(Contans.MSG_GET_WIFI_SCAN_RESULT);
						mWifiManager.startScan();
						sleep(10000);
					} else {
						sleep(3000);
					}
				}
			}catch(Exception e){
				Log.d("lucky", "e:"+e.getMessage());
			}
		}
	}
	
	private void getResultWifiAp(List<ScanResult> datas){
		if(datas==null||datas!=null&&datas.size()==0){
			Log.d("lucky", "--scan-results-null--");
			return;
		}
		wifiListDatas.clear();
		Log.d("lucky", "--namelist--size->"+saveWifiInfoName.size()+"--reslut-->"+datas.size());
		for(ScanResult result:datas){
			if(saveWifiInfoName.contains(result.SSID)){
				int resLevel = result.level;
				Log.d("lucky", "result.SSID:"+result.SSID);
				int setValue = Integer.parseInt(saveWifiInfoMap.get(result.SSID));
				if (setValue != 0) {
					float resultValue = calcualteLevel(resLevel)/calcualteLevel(setValue);
					BigDecimal bd = new BigDecimal(resultValue);
					bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					int pcentValue = (int) (bd.floatValue() * 100);
					Log.d("lucky", "pcentValue:" + pcentValue);
					wifiListDatas.add(new MyListItem(result.SSID, pcentValue));
				}
			}
		}	
		if(wifiListDatas.size()>0){
			mHandler.sendEmptyMessage(Contans.MSG_UPDATE_SETTING_DATA);
		}
	}
		
	private void registerReceiver(){
		IntentFilter intnetF = new IntentFilter();
		intnetF.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		intnetF.addAction(BluetoothDevice.ACTION_FOUND);
		intnetF.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		intnetF.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(receiver, intnetF);
	}
	
	private void unRegisterReceiver(){
		if(receiver!=null){
			unregisterReceiver(receiver);
		}
	}
	
	@Override
	public void onClick(View v) {
		startActivity();
	}
	
	private void startActivity(){
		Intent intent = new Intent();
		intent.setClassName("com.zws.wifiboarddetect","com.zws.wifiboarddetect.TestActivity");
		startActivityForResult(intent, Contans.requestCode);
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
				saveWifiInfoName = new ArrayList<String>();
				saveWifiInfoMap = new HashMap<String, String>();
				for (int i = 0; i < 3; i++) {
					String name = shared.getString(Contans.KEY_SSID + (i + 1));
					String level = shared.getString(Contans.KEY_SSID + (i + 1) + "_level");
					if (name != null && name.length() > 0) {
						saveWifiInfoName.add(name);
						saveWifiInfoMap.put(name, level);
					}
					SettingItemInfo itemWifiInfo = new SettingItemInfo(name,level);
					wifiList.add(itemWifiInfo);
				}
				info.setWifiSettings(wifiList);
				List<SettingItemInfo> blueList = new ArrayList<SettingItemInfo>();
				saveBlueInfoName = new ArrayList<String>();
				saveBlueInfoMap = new HashMap<String, String>();
				for(int i=0;i<3;i++){
					String name = shared.getString(Contans.KEY_BLUETOOTH+(i+1));
					String level = shared.getString(Contans.KEY_BLUETOOTH+(i+1)+"_level");
					if(name!=null&&name.length()>0){
						saveBlueInfoName.add(name);
						saveBlueInfoMap.put(name, level);
					}
					SettingItemInfo itemWifiInfo = new SettingItemInfo(name, level);
					blueList.add(itemWifiInfo);
				}
				info.setBluetoothSettings(blueList);
				Message message = mHandler.obtainMessage();
				message.what = Contans.MSG_GET_SETTING;
				message.obj = info;
				mHandler.sendMessage(message);
			}
		}).start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopScanThread();
		stopBlueScanThread();
		unRegisterReceiver();
		release();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		 case Contans.requestCode:
			 mHandler.sendEmptyMessage(Contans.MSG_RELOAD_SETTING);
			 break;
		}
	}
	
	private void addBlueDevice(BluetoothDevice device, short rssi) {
		if (device == null) {
			return;
		}
		String name = device.getName();
		Log.d("lucky", "--addBlueDevice--name->>" + name);
		Log.d("lucky", "--addBlueDevice---rssi->>" + rssi);
		if (name == null || name != null && name.length() == 0) {
			return;
		}
		if (saveBlueInfoName.contains(name)) {
			int resLevel = rssi;
			Log.d("lucky", "resLevel:" + resLevel);
			int setValue = Integer.parseInt(saveBlueInfoMap.get(name));
			Log.d("lucky", "setValue:" + setValue);
			if (setValue != 0) {
				float resultValue = calcualteLevel(resLevel)/calcualteLevel(setValue);
				BigDecimal bd = new BigDecimal(resultValue);
				bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
				int pcentValue = (int) (bd.floatValue() * 100);
				Log.d("lucky", "pcentValue:" + pcentValue);
				upBlueDevice(name,pcentValue);
			}
			if(blueListDatas.size()>0){
				mHandler.sendEmptyMessage(Contans.MSG_BLUETOOTH_UPDATE_VIEW);
			}
		}
	}
	
	private void upBlueDevice(String name, int pcentValue) {
		if (blueListDatas.size() == 0) {
			blueListDatas.add(new MyListItem(name, pcentValue));
		} else {
			for (int i = 0; i < blueListDatas.size(); i++) {
				MyListItem item = blueListDatas.get(i);
				if (item.ssid.equals(name)) {
					blueListDatas.remove(i);
					break;
				}
			}
			blueListDatas.add(new MyListItem(name, pcentValue));
		}
		blueAdapter.notifyDataSetChanged();
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
				Log.d("lucky", "SCAN_RESULTS_AVAILABLE_ACTION");
				mHandler.sendEmptyMessage(Contans.MSG_WIFI_SCAN_COMPLETED);
			}else if(BluetoothDevice.ACTION_FOUND.equals(action)){
				Message message = mHandler.obtainMessage();
				message.what = Contans.MSG_ADD_REMOTE_BLUETOOTH;
				message.obj = intent;
				mHandler.sendMessage(message);
			}else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
				Log.d("lucky", "---bluetooth--state->>"+state);
				if(state==BluetoothAdapter.STATE_OFF){
					mHandler.sendEmptyMessage(Contans.MSG_CLEAN_BLUETOOTH_DATA);
				}
			}else if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)){
				int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1); 
				Log.d("lucky", "---wiif--state->>"+wifiState);
				if(wifiState==WifiManager.WIFI_STATE_DISABLED){
					mHandler.sendEmptyMessage(Contans.MSG_CLEAN_WIFI_DATA);
				}
			}
		}
	};
	
	
	/**
	 * 屏蔽屏幕保护
	 */
	private void acquire() {
		if(null==wakeLock){
		PowerManager powerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
				|PowerManager.ON_AFTER_RELEASE, "BaseActivity");
		}
		if(wakeLock!=null){
			wakeLock.acquire();
		}
	}
	
	/**
	 *释放锁
	 */
	private void release() {
		if (wakeLock != null) {
			wakeLock.release();
			wakeLock = null;
		}
	}
}
