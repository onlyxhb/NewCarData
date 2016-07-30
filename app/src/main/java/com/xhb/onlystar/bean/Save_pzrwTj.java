package com.xhb.onlystar.bean;

import java.util.List;

public class Save_pzrwTj {
	private Rwzb rwzbModel;
	private List<Rwmxb> rwmxbList;
	private String RequestTime;
	private String TimeMd5;

	public Save_pzrwTj(Rwzb rwzbModel, List<Rwmxb> rwmxbList, String requestTime, String timeMd5) {
		super();
		this.rwzbModel = rwzbModel;
		this.rwmxbList = rwmxbList;
		RequestTime = requestTime;
		TimeMd5 = timeMd5;
	}

	public Rwzb getRwzbModel() {
		return rwzbModel;
	}

	public void setRwzbModel(Rwzb rwzbModel) {
		this.rwzbModel = rwzbModel;
	}

	public List<Rwmxb> getRwmxbList() {
		return rwmxbList;
	}

	public void setRwmxbList(List<Rwmxb> rwmxbList) {
		this.rwmxbList = rwmxbList;
	}

	public String getRequestTime() {
		return RequestTime;
	}

	public void setRequestTime(String requestTime) {
		RequestTime = requestTime;
	}

	public String getTimeMd5() {
		return TimeMd5;
	}

	public void setTimeMd5(String timeMd5) {
		TimeMd5 = timeMd5;
	}

}
