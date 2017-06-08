package com.zws.bean;

import java.io.Serializable;

public class MacRangeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 210604206074565636L;
	private String startMac;
	private String endMac;

	public MacRangeInfo(String startMac, String endMac) {
		this.startMac = startMac;
		this.endMac = endMac;
	}

	public String getStartMac() {
		return startMac;
	}

	public void setStartMac(String startMac) {
		this.startMac = startMac;
	}

	public String getEndMac() {
		return endMac;
	}

	public void setEndMac(String endMac) {
		this.endMac = endMac;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("startMac:").append(startMac).append("endMac:")
				.append(endMac);
		return sb.toString();
	}
}
