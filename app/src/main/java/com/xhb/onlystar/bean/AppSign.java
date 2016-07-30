package com.xhb.onlystar.bean;

public class AppSign {
	private SignInfo SignModel;
	private String RequestTime;
	private String TimeMd5;
	
	
	public AppSign(SignInfo signModel, String requestTime, String timeMd5) {
		SignModel = signModel;
		RequestTime = requestTime;
		TimeMd5 = timeMd5;
	}


	public SignInfo getSignModel() {
		return SignModel;
	}


	public void setSignModel(SignInfo signModel) {
		SignModel = signModel;
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
