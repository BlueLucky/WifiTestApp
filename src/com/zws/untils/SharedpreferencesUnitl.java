package com.zws.untils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedpreferencesUnitl {
	private SharedPreferences shared;
	private SharedPreferences.Editor editor;
	private static SharedpreferencesUnitl intance;

	public static SharedpreferencesUnitl getIntance(Context mContext) {
		if (intance == null) {
			intance = new SharedpreferencesUnitl(mContext);
		}
		return intance;
	}

	private SharedpreferencesUnitl(Context mContext) {
		shared = mContext.getSharedPreferences("detect_setting", 0);
		editor = shared.edit();
	}

	public void putData(String key, String data) {
		editor.putString(key, data);
	}

	public void putData(String key, int data) {
		editor.putInt(key, data);
	}

	public String getString(String key) {
		return shared.getString(key, "");
	}

	public int getInt(String key) {
		return shared.getInt(key, 0);
	}

	public void save() {
		editor.commit();
	}
}
