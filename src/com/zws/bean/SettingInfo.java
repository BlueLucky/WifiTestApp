package com.zws.bean;

import java.io.Serializable;
import java.util.List;

public class SettingInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1625320698402671030L;
	private MacRangeInfo wifiMacRange;
	private MacRangeInfo blueMacRange;
	private List<SettingItemInfo> wifiSettings;
	private List<SettingItemInfo> bluetoothSettings;
	private String wifiColon;
	private String blueColon;

	public MacRangeInfo getWifiMacRange() {
		return wifiMacRange;
	}

	public void setWifiMacRange(MacRangeInfo wifiMacRange) {
		this.wifiMacRange = wifiMacRange;
	}

	public MacRangeInfo getBlueMacRange() {
		return blueMacRange;
	}

	public void setBlueMacRange(MacRangeInfo blueMacRange) {
		this.blueMacRange = blueMacRange;
	}

	public List<SettingItemInfo> getWifiSettings() {
		return wifiSettings;
	}

	public void setWifiSettings(List<SettingItemInfo> wifiSettings) {
		this.wifiSettings = wifiSettings;
	}

	public List<SettingItemInfo> getBluetoothSettings() {
		return bluetoothSettings;
	}

	public void setBluetoothSettings(List<SettingItemInfo> bluetoothSettings) {
		this.bluetoothSettings = bluetoothSettings;
	}

	public String getWifiColon() {
		return wifiColon;
	}

	public void setWifiColon(String wifiColon) {
		this.wifiColon = wifiColon;
	}

	public String getBlueColon() {
		return blueColon;
	}

	public void setBlueColon(String blueColon) {
		this.blueColon = blueColon;
	}

	public SettingInfo() {
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("wifilist:\n");
		for(SettingItemInfo item:wifiSettings){
			sb.append(item.toString());
		}
		sb.append("wifiMac:\n");
		sb.append(wifiMacRange.toString());
		sb.append("wifiColon:"+wifiColon).append("\n");
		
		sb.append("bluelist:\n");
		for(SettingItemInfo item:bluetoothSettings){
			sb.append(item.toString());
		}
		sb.append("blueMac:\n");
		sb.append(blueMacRange.toString());
		sb.append("blueColon:"+blueColon).append("\n");
		return sb.toString();
	}
}
