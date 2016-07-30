package com.xhb.onlystar.bean;

public class GetUploadFileUrl {
	private String rwdh;
	private String clsbm;
	private String RequestTime;
	private String TimeMd5;

	public GetUploadFileUrl(String rwdh, String clsbm, String requestTime, String timeMd5) {
		this.rwdh = rwdh;
		this.clsbm = clsbm;
		RequestTime = requestTime;
		TimeMd5 = timeMd5;
	}
	public String getRwdh() {
		return rwdh;
	}
	public void setRwdh(String rwdh) {
		this.rwdh = rwdh;
	}
	public String getClsbm() {
		return clsbm;
	}
	public void setClsbm(String clsbm) {
		this.clsbm = clsbm;
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
