package com.zws.untils;

public interface Contans {
	public final static String KEY_SSID = "ssid";
	public final static String KEY_WIFI_MAC_START = "wifi_mac_start";
	public final static String KEY_WIFI_MAC_END = "wifi_mac_end";
	public final static String KEY_WIFI_COLON = "wifi_colon";

	public final static String KEY_BLUETOOTH = "bluetooth";
	public final static String KEY_BLUE_MAC_START = "blue_mac_start";
	public final static String KEY_BLUE_MAC_END = "blue_mac_end";
	public final static String KEY_BLUE_COLON = "blue_colon";
	
	public final static int MSG_UPDATE_SETTING_DATA = 0x10;
	public final static int MSG_RELOAD_SETTING = 0x11;
	public final static int MSG_GET_SETTING = 0x12;
	public final static int MSG_GET_WIFI_SCAN_RESULT = 0x13;
	public final static int MSG_WIFI_UPDATE_RESULT = 0x14;
	public final static int MSG_WIFI_FAIL_RESULT = 0x15;
	public final static int MSG_WIFI_SCAN_COMPLETED = 0x16;
	
	public final static int MSG_UPDATE_BLUETOOTH_MAC = 0x17;
	public final static int MSG_ADD_REMOTE_BLUETOOTH = 0x18;
	public final static int MSG_BLUETOOTH_UPDATE_VIEW = 0x19;
	public final static int MSG_CLEAN_BLUETOOTH_DATA = 0x20;
	public final static int MSG_CLEAN_WIFI_DATA = 0x21;
	
	public final static int requestCode = 2;
	public final static int WIFI_ITEM_COUNT = 4;
	public final static int BLUE_ITEM_COUNT = 2;
}
