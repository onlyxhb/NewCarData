package com.xhb.onlystar.bean;

public class Login {
	private String UserId;
	private String Pwd;
	private String RequestTime;
	private String TimeMd5;

	public Login(String userId, String pwd, String requestTime, String timeMd5) {
		super();
		UserId = userId;
		Pwd = pwd;
		RequestTime = requestTime;
		TimeMd5 = timeMd5;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getPwd() {
		return Pwd;
	}

	public void setPwd(String pwd) {
		Pwd = pwd;
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
