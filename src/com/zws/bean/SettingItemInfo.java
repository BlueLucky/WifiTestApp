package com.zws.bean;

import java.io.Serializable;

public class SettingItemInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8380678809353817295L;
	private String name;
	private String level;

	public SettingItemInfo(String name, String level) {
		this.name = name;
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("name:").append(name).append("level:").append(level);
		return sb.toString();
	}
}
