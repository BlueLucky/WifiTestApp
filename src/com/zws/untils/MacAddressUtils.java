package com.zws.untils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.util.Log;

public class MacAddressUtils {
	private static String loadFileAsString(String filePath) throws IOException {
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char buff[] = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buff)) != -1) {
			String readData = String.valueOf(buff, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}
	
	@SuppressLint("DefaultLocale")
	public static String getMacAddress(String inerfaceName){
		try {
			return loadFileAsString("/sys/class/net/"+inerfaceName+"/address").toUpperCase().substring(0,17);
		} catch (IOException e) {
			Log.d("lucky", "e:"+e.toString());
			return null;
		}
	}
}
