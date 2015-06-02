package com.dad.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PollutantsRtdData implements Serializable {

	private static final long serialVersionUID = -8390688555780539788L;

	private String code;
	
	private String name;

	private String deviceId;

	private Date dataTime;

	private Double rtd;
	
	private Double zsRtd;
	
	private String flag;
	
	private String dataUnit;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}

	public Double getRtd() {
		return rtd;
	}

	public void setRtd(Double rtd) {
		this.rtd = rtd;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Double getZsRtd() {
		return zsRtd;
	}

	public void setZsRtd(Double zsRtd) {
		this.zsRtd = zsRtd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataUnit() {
		return dataUnit;
	}

	public void setDataUnit(String dataUnit) {
		this.dataUnit = dataUnit;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
	}
	
	
}
