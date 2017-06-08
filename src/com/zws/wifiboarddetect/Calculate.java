package com.zws.wifiboarddetect;

import com.zws.bean.MacRangeInfo;

import android.util.Log;

public class Calculate {
	private static final String TAG = "zws";
	private static long wifiBegin = -1;
	private static long wifiEnd = -1;
	private static long blueBegin = -1;
	private static long blueEnd = -1;

	public Calculate(MacRangeInfo wifiInfo,MacRangeInfo blueInfo) {
		setWifiMacRange(wifiInfo);
		setBlueMacRange(blueInfo);
	}
	
	public void setWifiMacRange(MacRangeInfo info) {
		String startMac = "00:00:00:00:00:00";
		String endMac = "FF:FF:FF:FF:FF:FF";
		if (info != null && info.getStartMac().length() > 0
				&& info.getEndMac().length() > 0) {
			startMac = info.getStartMac();
			endMac = info.getEndMac();
		} else {
			wifiBegin = -1;
			wifiEnd = -1;
			return;
		}
		if (startMac != null && endMac != null) {
			wifiBegin = Long.parseLong(removeMacColon(startMac), 16);
			wifiEnd = Long.parseLong(removeMacColon(endMac), 16);
			Log.d(TAG, "begin:" + wifiBegin + " end:" + wifiEnd);
		}
	}
	
	public void setBlueMacRange(MacRangeInfo info) {
		String startMac = "00:00:00:00:00:00";
		String endMac = "FF:FF:FF:FF:FF:FF";
		if (info != null && info.getStartMac().length() > 0
				&& info.getEndMac().length() > 0) {
			startMac = info.getStartMac();
			endMac = info.getEndMac();
		} else {
			blueBegin = -1;
			blueEnd = -1;
			return;
		}
		if (startMac != null && endMac != null) {
			blueBegin = Long.parseLong(removeMacColon(startMac), 16);
			blueEnd = Long.parseLong(removeMacColon(endMac), 16);
			Log.d(TAG, "begin:" + blueBegin + " end:" + blueEnd);
		}
	}
	
	private String removeMacColon(String macStr) {
		if (macStr == null || macStr != null && macStr.length() == 0) {
			return "";
		}
		String macArray[] = macStr.split(":");
		StringBuffer sb = new StringBuffer();
		for (String sub : macArray) {
			sb.append(sub);
		}
		return sb.toString();
	}
	
	
	public boolean isWifiMacInRange(String macStr){
		if (macStr == null || macStr != null && macStr.length() == 0) {
			Log.d("lucky", "----macStr---is--null---");
			return false;
		}
		Long macLong = Long.parseLong(removeMacColon(macStr), 16);
		if (macLong >= wifiBegin && macLong <= wifiEnd) {
			return true;
		}
		return false;
	}
	
	public boolean isBlueMacInRange(String macStr) {
		if (macStr == null || macStr != null && macStr.length() == 0) {
			Log.d("lucky", "----macStr---is--null---");
			return false;
		}
		Long macLong = Long.parseLong(removeMacColon(macStr), 16);
		if (macLong >= blueBegin && macLong <= blueEnd) {
			return true;
		}
		return false;
	}
}
